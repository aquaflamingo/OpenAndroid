package com.pressurelabs.hibernate.ui.view

import android.app.Dialog

interface IBaseView {
    fun toast(msg:String)
    fun showDialog(dialog:Dialog)
    fun dismissDialogs()
}