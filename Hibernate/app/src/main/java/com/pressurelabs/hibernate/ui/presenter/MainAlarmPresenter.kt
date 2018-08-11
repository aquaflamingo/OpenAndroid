package com.pressurelabs.hibernate.ui.presenter

import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.pressurelabs.hibernate.data.repository.FiveHundredPxProvider
import com.pressurelabs.hibernate.data.repository.LocalCacheDbProvider
import com.pressurelabs.hibernate.data.repository.interfaces.ILocalCacheDb
import com.pressurelabs.hibernate.domain.Photograph
import com.pressurelabs.hibernate.domain.Time
import com.pressurelabs.hibernate.ui.executors.EventCoordinator
import com.pressurelabs.hibernate.ui.executors.SleepOptimizer
import com.pressurelabs.hibernate.ui.executors.UIFactory
import com.pressurelabs.hibernate.ui.view.IMainAlarmView
import timber.log.Timber
import java.lang.Exception

class MainAlarmPresenter(view: IMainAlarmView, c: Context): IMainAlarmPresenter {
    override fun introduceIfNeeded() {
        if (cache.isFirstInstall()) {
            cache.putFirstInstall(false)
            mView.onIntroduction()
        }
    }

    override fun RefreshImage() {
        val api = FiveHundredPxProvider.provide()
        api.getPhoto {
            photo ->
            if (photo == null) {
                var p = cache.loadDefaultPhotograph()
                setPhotograph(p)
            } else {
                cache.cachePhotograph(photo!!)
                setPhotograph(photo!!)
            }

        }
    }

    override fun onClockDisplayTypeChange() {
        var clockPref: Int = cache.getClockType()
        when (clockPref) {
            ILocalCacheDb.TWELVE_HOUR -> {
                cache.putClockDisplayType(ILocalCacheDb.TWENTY_FOUR_HOUR)
                mView.toast("Clock preferences saved!")

            }

            ILocalCacheDb.TWENTY_FOUR_HOUR -> {
                cache.putClockDisplayType(ILocalCacheDb.TWELVE_HOUR)
                mView.toast("Clock preferences saved!")
            }
        }
        this.refresh()

    }

    override fun onAlarmEnabledChange() {
        var status = cache.getAlarmStatus()
        var alarm = cache.getAlarmTime()

        if (status) {
            cache.putAlarmStatus(false)
            mView.toast("Alarm turned off!")
            alarmPlanner.cancelDailyWakeUpAlarm()
        } else {
            cache.putAlarmStatus(true)
            mView.toast("Alarm turned on!")
            alarmPlanner.setDailyWakeUpAlarm(alarm)
        }

        this.refresh()
    }

    override fun debug() {
//
    }

    private val alarmPlanner: EventCoordinator = EventCoordinator(c)
    private var mView: IMainAlarmView = view;
    private var cache: ILocalCacheDb = LocalCacheDbProvider.provide(c)

    init {
        this.refresh()

        alarmPlanner.setDaily500PXWallpaper(c)
    }

    override fun setPhotograph(photograph: Photograph) {
        mView.setWallpaperAuthor(photograph.author)
        mView.setWallpaperTitle(photograph.title)
        mView.setWallpaperImage(photograph.bitmap)
        var intentVal = if (photograph.authorURL!!.equals(""))
            Uri.parse("http://500px.com") else photograph.authorURL
        mView.set500PXDetailsViewIntent(
                Intent(Intent.ACTION_VIEW, intentVal))

    }

    override fun refresh() {
        Timber.d("Refreshing...")
        Timber.d("\tSetting wake time.")
        var alarm: Time = cache.getAlarmTime()


        Timber.d("\tOptimizing cycle count.")
        var optimizer = findIdealBedTime(alarm)
        mView.setCycleCount(optimizer.calculatedFullCycles)


        var clockType = cache.getClockType()
        if (clockType == ILocalCacheDb.Const.TWELVE_HOUR) {
            mView.setTargetSleep(optimizer.idealTimeToSleep.toTweleveHour())
            Timber.d("\tSetting Target Sleep: ${optimizer.idealTimeToSleep.toTweleveHour()}.")
            mView.setAlarmWakeTime(alarm.toTweleveHour())
            mView.setClockType24Hour(false)
        } else {
            mView.setTargetSleep(optimizer.idealTimeToSleep.toString())
            Timber.d("\tSetting Target Sleep: ${optimizer.idealTimeToSleep}.")
            mView.setClockType24Hour(true)
            mView.setAlarmWakeTime(alarm.toString())
        }


        var status = cache.getAlarmStatus()

        Timber.d("\tAlarm status: $status, setting view.")
        if (status) {
            mView.setAlarmStatus(true)
        } else {
            mView.setAlarmStatus(false)

        }


        alarmPlanner.setDailySleepSoonNotification(optimizer.idealTimeToSleep)

        Timber.d("\tLoading cached wallpaper.")
        try {
            var photo = cache.loadCachedPhotograph()
            setPhotograph(photo)
        } catch (t: Exception) {
            Timber.e("Caught Java Exception  ${t.localizedMessage}")
            RefreshImage()
        }


    }

    private fun findIdealBedTime(alarm: Time): SleepOptimizer.Optimal {
        var optimizer = SleepOptimizer(alarm).getOptimized()
        return optimizer
    }

    override fun onAlarmButtonClick(c: Context) {
        var dialog: TimePickerDialog = UIFactory.CreateTimePickerDialog(c, cache.getAlarmTime(),
                {
                    time ->
                    val alarm = time
                    val currentAlarm = cache.getAlarmTime()
                    if (alarm.toMinutes().equals(currentAlarm.toMinutes())) {
                        mView.toast("Alarm is too close for a full sleep cycle!")
                    } else {
                        mView.toast("Alarm Saved!")
                        cache.putAlarmTime(alarm)
                        var optimizer = findIdealBedTime(alarm)
                        alarmPlanner.setDailyWakeUpAlarm(alarm)

                        alarmPlanner.cancelDailySleepSoonNotification()
                        alarmPlanner.setDailySleepSoonNotification(optimizer.idealTimeToSleep)
                        mView.toast("Alarm Time is ${cache.getAlarmTime()}!")
                    }

                    refresh()
                })
        mView.showTimePicker(dialog)
    }
}