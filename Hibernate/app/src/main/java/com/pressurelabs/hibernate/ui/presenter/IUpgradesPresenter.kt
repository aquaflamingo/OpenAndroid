package com.pressurelabs.hibernate.ui.presenter

import android.content.Intent

interface IUpgradesPresenter {
    fun onFirstButtonClicked(): Intent
    fun onSecondButtonClicked(): Intent
}