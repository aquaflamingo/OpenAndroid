package com.pressurelabs.hibernate.ui.presenter

import android.content.Context
import android.net.Uri

interface IAlarmReceiverPresenter {
    fun getAlarmURI(): Uri?
    fun play(alert: Uri?)
    fun beforeFinish()
}