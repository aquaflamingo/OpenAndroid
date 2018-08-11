package com.pressurelabs.hibernate.ui.activities

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.pressurelabs.hibernate.R
import com.pressurelabs.hibernate.data.models.SleepEntry
import com.pressurelabs.hibernate.data.repository.SleepLogProvider
import com.pressurelabs.hibernate.ui.executors.EventCoordinator
import com.pressurelabs.hibernate.ui.services.HibernateReceiver
import timber.log.Timber
import java.util.*

class DebugActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debug)
    }

    fun sendAlarmIntent(v:View) {
        var calendar = Calendar.getInstance()
        var wakeUpAlarmIntent: Intent = Intent(this, WakeUpActivity::class.java)
        var manager: AlarmManager? = getSystemService(Context.ALARM_SERVICE) as AlarmManager?
        var pendingWakeUpIntent: PendingIntent= PendingIntent.getActivity(this, EventCoordinator.RC_WAKE_UP_ALARM, wakeUpAlarmIntent, PendingIntent.FLAG_CANCEL_CURRENT)



        Timber.d("Alarm Intent Sent!")
        manager?.set(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis+2000,
                pendingWakeUpIntent);
    }


    fun sleepSoon(v:View) {
        Timber.d("Sleep soon sent!")
        sendBroadcast(Intent(EventCoordinator.EventCommunications.INTENT_SLEEP_SOON_NOTIFICATION))
    }

    fun sendWallpaper(v:View) {
        Timber.d("Wallpaper Intent sent!")
        sendBroadcast(Intent(EventCoordinator.EventCommunications.INTENT_DAILY_500_PX))
    }

    fun readDb(v:View) {
        val rep = SleepLogProvider.provide(this)
        var myList:List<SleepEntry>? = rep.getRecent(30)
        Timber.d("Reading List,\n ")

        myList?.forEach {
            entry ->
            Log.d("TAG","$entry")
        }

    }

    fun seedDb(v:View) {
        val rep = SleepLogProvider.provide(this)
        rep.debug()
    }

}
