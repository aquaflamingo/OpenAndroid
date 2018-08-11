package com.pressurelabs.hibernate.ui.services

import android.content.Context
import android.content.Intent
import android.support.v4.content.WakefulBroadcastReceiver
import android.widget.Toast
import com.pressurelabs.hibernate.data.models.SleepEntry
import com.pressurelabs.hibernate.data.repository.FiveHundredPxProvider
import com.pressurelabs.hibernate.data.repository.SleepLogProvider
import com.pressurelabs.hibernate.data.repository.datasource.cache.LocalCacheDb
import com.pressurelabs.hibernate.data.repository.interfaces.ILocalCacheDb
import com.pressurelabs.hibernate.ui.executors.EventCoordinator
import com.pressurelabs.hibernate.ui.executors.UIFactory
import timber.log.Timber
import java.sql.Timestamp
import java.util.*

class HibernateReceiver : WakefulBroadcastReceiver() {
    val TAG:String=javaClass.simpleName
    override fun onReceive(context: Context?, intent: Intent?) {

        when (intent?.action) {
            EventCoordinator.INTENT_DAILY_500_PX -> {
                Timber.d("Received Photo Cache Broadcast")
                val repo = FiveHundredPxProvider.provide()
                val cache: ILocalCacheDb = LocalCacheDb(context!!)
                repo.getPhoto({
                    photograph ->
                    if (photograph==null) {
                        Toast.makeText(context,"No image..", Toast.LENGTH_SHORT).show()
                    } else {
                        cache.cachePhotograph(photograph!!)
                    }
                })
            }

            EventCoordinator.INTENT_SLEEP_SOON_NOTIFICATION -> {
                Timber.d("\nReceived Sleep Soon Broadcast")
                var sleepIntent = Intent(context, HibernateService::class.java)
                sleepIntent.action= EventCoordinator.INTENT_SLEEP_SOON_NOTIFICATION
                startWakefulService(context,sleepIntent)
            }

            EventCoordinator.INTENT_BOOT_COMPLETE  -> {
                Timber.d("\nReceived Boot Complete")
                val cache: ILocalCacheDb = LocalCacheDb(context!!)
                var status = cache.getAlarmStatus()
                var alarm = cache.getAlarmTime()
                val alarmPlanner = EventCoordinator(context!!)

                if (status) {
                    alarmPlanner.setDailyWakeUpAlarm(alarm)
                } else {
                    alarmPlanner.cancelDailyWakeUpAlarm()
                }
            }

            EventCoordinator.INTENT_START_TRACKING -> {
                // Evenutally will track sleep
                Timber.d("\nReceived Start Track Request")
                var startTrackIntent = Intent(context!!,HibernateService::class.java)
                startTrackIntent.action = EventCoordinator.INTENT_START_TRACKING
                startWakefulService(context,startTrackIntent)
            }

            EventCoordinator.INTENT_END_TRACKING -> {
                // Evenutally will track sleep
                Timber.d("\nReceived Stop Track Request")
                var stopTrackIntent = Intent(context!!,HibernateService::class.java)
                stopTrackIntent.action = EventCoordinator.INTENT_END_TRACKING
                startWakefulService(context,stopTrackIntent)

            }
        }
    }

}