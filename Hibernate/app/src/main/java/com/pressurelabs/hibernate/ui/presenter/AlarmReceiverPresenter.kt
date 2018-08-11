package com.pressurelabs.hibernate.ui.presenter

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.view.View
import com.pressurelabs.hibernate.ui.executors.EventCoordinator
import com.pressurelabs.hibernate.ui.services.HibernateReceiver
import com.pressurelabs.hibernate.ui.view.IAlarmReceiverView
import timber.log.Timber

class AlarmReceiverPresenter(view: IAlarmReceiverView, c: Context): IAlarmReceiverPresenter {
    override fun beforeFinish() {
        mMediaPlayer.stop()
        mView.onFinish()
    }

    val mMediaPlayer:MediaPlayer = MediaPlayer()
    val audio:AudioManager? =c.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    val TAG:String? = javaClass.simpleName
    val mView: IAlarmReceiverView = view

    init {

        mMediaPlayer.setDataSource(c,getAlarmURI())
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM)

        // Set Alarm Button Dismiss
      mView.setDismissButtonListener(View.OnTouchListener { arg0, arg1 ->
          mMediaPlayer.stop()
          var recordIntent = Intent(EventCoordinator.INTENT_END_TRACKING)
          c.sendBroadcast(recordIntent)
          mView.onFinish()
          false
      })
        mMediaPlayer.prepareAsync()
        mMediaPlayer.setOnPreparedListener({
                    play(getAlarmURI())
                })

    }



    override fun getAlarmURI(): Uri? {
            var alert: Uri? = RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_ALARM)
            if (alert == null) {
                alert = RingtoneManager
                        .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                if (alert == null) {
                    alert = RingtoneManager
                            .getDefaultUri(RingtoneManager.TYPE_RINGTONE)
                }
            }
            return alert
    }

    override fun play(alert: Uri?) {
        try {
            var volume:Int? = audio?.getStreamVolume(AudioManager.STREAM_ALARM);
            if (volume!= 0) {

                mMediaPlayer?.start()

            }
        } catch (e:Exception) {
            Timber.e(TAG, "Error playing alert. " + e.message)
        }
    }


    /*
        Volume off = important ?
     */
}