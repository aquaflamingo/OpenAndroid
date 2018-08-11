package com.pressurelabs.hibernate.ui.executors

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.pressurelabs.hibernate.domain.Time
import com.pressurelabs.hibernate.ui.activities.WakeUpActivity
import com.pressurelabs.hibernate.ui.services.HibernateReceiver
import timber.log.Timber
import java.util.*

class EventCoordinator(c: Context) {
    companion object EventCommunications {
        const val RC_WAKE_UP_ALARM:Int = 454
        const val RC_SLEEP_SOON:Int = 453
        const val RC_END_TRACK:Int=455
        const val INTENT_SLEEP_SOON_NOTIFICATION: String = "INTENT_SLEEP_SOON_NOTIFICATION"
        const val INTENT_DAILY_500_PX: String = "INTENT_DAILY_500_PX"
        const val INTENT_START_TRACKING:String ="INTENT_START_TRACKING"
        const val INTENT_BOOT_COMPLETE: String = "android.intent.action.BOOT_COMPLETED"
        const val INTENT_END_TRACKING: String = "INTENT_END_TRACKING"
    }
    private var wakeUpAlarmIntent: Intent = Intent(c, WakeUpActivity::class.java)
    private var sleepSoonIntent: Intent = Intent(c, HibernateReceiver::class.java)

    init {
        sleepSoonIntent.action=INTENT_SLEEP_SOON_NOTIFICATION
    }
    private var manager: AlarmManager? = c.getSystemService(Context.ALARM_SERVICE) as AlarmManager?

    private var pendingSleepSoonIntent: PendingIntent= PendingIntent.getBroadcast(c, RC_SLEEP_SOON, sleepSoonIntent,  PendingIntent.FLAG_CANCEL_CURRENT)
    private var pendingWakeUpIntent: PendingIntent= PendingIntent.getActivity(c, RC_WAKE_UP_ALARM, wakeUpAlarmIntent, PendingIntent.FLAG_CANCEL_CURRENT)


    fun setDailySleepSoonNotification(time: Time) {

        val calendar: Calendar = Calendar.getInstance();
        calendar.timeInMillis = System.currentTimeMillis();
        if (time.hour-1<0) {
            time.hour=23
        } else {
            time.hour=time.hour-1
        }

        Timber.d("Setting sleep soon notification to ${time.hour}:${time.minutes}")
        calendar.set(Calendar.HOUR_OF_DAY, time.hour);
        calendar.set(Calendar.MINUTE, time.minutes);


        manager?.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingSleepSoonIntent);
    }

    fun cancelDailySleepSoonNotification() {
        manager?.cancel(pendingSleepSoonIntent)
        pendingSleepSoonIntent.cancel()
    }

    fun setDailyWakeUpAlarm(time: Time) {

        val calendar: Calendar = Calendar.getInstance();
        calendar.timeInMillis = System.currentTimeMillis();

        if (HibernateAlgorithm.isFutureAlarm(time)) {
            calendar.add(Calendar.DATE,1)
        }

        calendar.set(Calendar.HOUR_OF_DAY, time.hour);
        calendar.set(Calendar.MINUTE, time.minutes);

        manager?.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingWakeUpIntent);

    }


    fun setDaily500PXWallpaper(c:Context) {
        var fivehundred: Intent = Intent(c, HibernateReceiver::class.java)
        fivehundred.action=INTENT_DAILY_500_PX
        var pending = PendingIntent.getBroadcast(c, RC_WAKE_UP_ALARM, fivehundred,  PendingIntent.FLAG_NO_CREATE)

        val calendar: Calendar = Calendar.getInstance();
        calendar.timeInMillis = System.currentTimeMillis();
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);

        manager?.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY, pending);

    }

    fun cancelDailyWakeUpAlarm() {
        manager?.cancel(pendingWakeUpIntent)
        pendingWakeUpIntent.cancel()
    }


}