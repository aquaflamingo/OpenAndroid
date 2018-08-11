package com.pressurelabs.hibernate.ui.services

import android.app.IntentService
import android.content.Intent
import android.app.NotificationManager
import android.content.Context
import com.pressurelabs.hibernate.data.models.SleepEntry
import com.pressurelabs.hibernate.data.repository.LocalCacheDbProvider
import com.pressurelabs.hibernate.data.repository.SleepLogProvider
import com.pressurelabs.hibernate.data.repository.datasource.cache.LocalCacheDb
import com.pressurelabs.hibernate.data.repository.interfaces.ILocalCacheDb
import com.pressurelabs.hibernate.ui.executors.EventCoordinator
import com.pressurelabs.hibernate.ui.executors.UIFactory
import timber.log.Timber
import java.sql.Timestamp
import java.util.*


class HibernateService : IntentService("Alarm Service") {

    override fun onCreate() {
        super.onCreate()
        Timber.d("Hibernate Service is online")
    }

    public override fun onHandleIntent(intent: Intent?) {
        var nm:NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val NOTIF_SLEEP_SOON = 1
        val NOTIF_TRACKING=2

        Timber.d("Handling Intent..")
        when (intent?.action) {
            EventCoordinator.INTENT_SLEEP_SOON_NOTIFICATION -> {
                Timber.d("Notifying Sleep Soon")
                var prefs = LocalCacheDbProvider.provide(this)
                var sleepTime = prefs.getAlarmTime()
                var notification = UIFactory.CreateSleepSoonNotification(this,sleepTime)
                nm.notify(NOTIF_SLEEP_SOON, notification)
            }

            EventCoordinator.INTENT_START_TRACKING -> {
                Timber.d("Started Tracking")
                LogSleepStart(this)
                var notification = UIFactory.CreateSleepTrackingNotification(this)
                nm.notify(NOTIF_TRACKING, notification)
            }

            EventCoordinator.INTENT_END_TRACKING -> {
                Timber.d("Stopped Tracking")
                LogSleepEnd(this)
                nm.cancelAll()
            }
        }

    }

    companion object TaskMonkey {
        fun LogSleepStart(context: Context) {
            var cal = Calendar.getInstance()
            val cache:ILocalCacheDb = LocalCacheDb(context)
            cache.putStartTrackTime(Timestamp(cal.timeInMillis))
        }
        fun LogSleepEnd(context: Context) {
            var cal = Calendar.getInstance()
            val cache: ILocalCacheDb = LocalCacheDb(context!!)
            if (cache.isTracking()) {
                Timber.d("Logging Sleep Record.")
                cache.putEndTrackTime(Timestamp(cal.timeInMillis))

                var start = cache.getTrackStartTime()
                var end = cache.getTrackEndTime()
                val repo = SleepLogProvider.provide(context!!)
                repo.insert(SleepEntry(start, end))
                cache.clearTrackLog()
            }

        }

    }

}