package com.pressurelabs.hibernate.ui.activities

import android.os.Bundle

import com.pressurelabs.hibernate.R
import android.view.View.OnTouchListener
import android.view.WindowManager
import android.widget.Toast
import com.pressurelabs.hibernate.BaseActivity
import com.pressurelabs.hibernate.ui.presenter.AlarmReceiverPresenter
import com.pressurelabs.hibernate.ui.presenter.IAlarmReceiverPresenter
import com.pressurelabs.hibernate.ui.view.IAlarmReceiverView
import kotlinx.android.synthetic.main.activity_alarm_receiver.*

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class WakeUpActivity : BaseActivity(), IAlarmReceiverView {

    override fun onFinish() {
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        presenter?.beforeFinish()
    }

    override fun setDismissButtonListener(listener: OnTouchListener) {
        button_alarm_dismiss.setOnTouchListener(listener)
    }

    var presenter:IAlarmReceiverPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_alarm_receiver)
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        presenter = AlarmReceiverPresenter(this,this)
    }


}
