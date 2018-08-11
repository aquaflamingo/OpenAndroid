package com.pressurelabs.hibernate.ui.view

import android.content.Intent
import android.net.Uri
import android.view.View

interface IAlarmReceiverView :IBaseView {
    fun setDismissButtonListener(listener:View.OnTouchListener)
    fun onFinish()

}