package com.pressurelabs.hibernate.ui.executors

import android.app.*
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Build
import com.pressurelabs.hibernate.R
import com.pressurelabs.hibernate.domain.Time
import com.pressurelabs.hibernate.ui.activities.MainActivity

object UIFactory {
    private fun getPendingMainIntent(c:Context): PendingIntent {
        var mainIntent:Intent =Intent(c, MainActivity::class.java)
        mainIntent.flags=Intent.FLAG_ACTIVITY_SINGLE_TOP
        var pendingToMain:PendingIntent = PendingIntent.getActivity(c,0,mainIntent,0)
        return pendingToMain
    }
    fun CreateSleepSoonNotification(c: Context, time:Time):Notification {
       var pendingToMain = getPendingMainIntent(c)
        var b: Notification.Builder
            b = Notification.Builder(c)
                           .setSmallIcon(R.drawable.ic_launcher_inverse)
                           .setSubText("Heads up!")
                           .setContentIntent(pendingToMain)
                           .setContentText("Start getting ready to fall asleep by " +time.toString())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    b.setColor(Color.parseColor("#1a237e"))
        }
        return b.build();
    }

    fun CreateSleepTrackingNotification(c:Context):Notification {
        var pendingToMain = getPendingMainIntent(c)
        var pendingEndTrack = getPendingEndSleep(c)
        var b: Notification.Builder
            b = Notification.Builder(c)
                    .setSmallIcon(R.drawable.ic_half_moon)
                    .setSubText("Sleeping.")
                    .setContentIntent(pendingToMain)
                    .setContentText("Currently in sleep mode.")
                    .setOngoing(true)
                    .setPriority(Notification.PRIORITY_MIN)
                    .addAction(0,"END",pendingEndTrack)

        return b.build();
    }

    private fun getPendingEndSleep(c: Context): PendingIntent {
        var endIntent:Intent =Intent(EventCoordinator.INTENT_END_TRACKING)
        var pendingToEnd:PendingIntent = PendingIntent.getBroadcast(c,EventCoordinator.RC_END_TRACK,endIntent,0)
        return pendingToEnd
    }

    fun CreateTimePickerDialog(c: Context, alarmTimeToSet: Time, callback: (mTime:Time)->Unit): TimePickerDialog {

        var dialog: TimePickerDialog = TimePickerDialog(
                c,
                TimePickerDialog.OnTimeSetListener {
                    view, hourOfDay, minute ->
                    callback(Time(hourOfDay,minute))
                },
                alarmTimeToSet.hour,
                alarmTimeToSet.minutes,
                true)
        return dialog;
    }

    fun CreateBasicPromptDialog(c: Context, title: String, msg: String): Dialog {
        var dialog = AlertDialog.Builder(c)
                .setIcon(R.mipmap.ic_launcher)
                .setMessage(msg)
                .setTitle(title)
                .setPositiveButton("Join",
                        DialogInterface.OnClickListener {
                            dialog, which ->
                    c.startActivity(ExternalEventNavigator.CreateExternalLinkIntent(ExternalEventNavigator.URIs.SECOND_DESTINATION))
                })

        return dialog.create()
    }
}