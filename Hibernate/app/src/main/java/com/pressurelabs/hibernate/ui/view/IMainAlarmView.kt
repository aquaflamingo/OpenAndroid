package com.pressurelabs.hibernate.ui.view

import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import com.pressurelabs.hibernate.domain.Time
import com.pressurelabs.hibernate.domain.UIAttributeBundle

interface IMainAlarmView :IBaseView{
    fun setWallpaperAuthor(author:String?)
    fun setWallpaperTitle(title:String?)
    fun setWallpaperImage(img: Bitmap?)
    fun set500PXDetailsViewIntent(intent: Intent?)
    fun setTargetSleep(timeAsString: String)
    fun setCycleCount(cycles:Int)
    fun setAlarmWakeTime(time: String)
    fun showTimePicker(dialog: TimePickerDialog)
    fun setAlarmStatus(alarmIsOn:Boolean)
    fun setClockType24Hour(hour24:Boolean)
    fun onIntroduction()
}