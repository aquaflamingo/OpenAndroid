package com.pressurelabs.hibernate.ui.presenter

import android.content.Context
import com.pressurelabs.hibernate.domain.Photograph

interface IMainAlarmPresenter {
    fun onAlarmButtonClick(c: Context);
    fun refresh()
    fun debug()
    fun onAlarmEnabledChange()
    fun onClockDisplayTypeChange()
    fun setPhotograph(photograph: Photograph)
    fun RefreshImage()
    fun introduceIfNeeded()
}