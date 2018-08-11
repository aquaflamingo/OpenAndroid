package com.pressurelabs.hibernate.ui.presenter

import android.content.Context
import android.content.Intent
import com.pressurelabs.hibernate.ui.executors.ExternalEventNavigator
import com.pressurelabs.hibernate.ui.view.IUpgradesView

class UpgradesPresenter(mView:IUpgradesView,c:Context):IUpgradesPresenter {
    override fun onSecondButtonClicked(): Intent {
        return ExternalEventNavigator.CreateExternalLinkIntent(ExternalEventNavigator.URIs.SECOND_DESTINATION)
    }

    private var mView:IUpgradesView = mView


    override fun onFirstButtonClicked():Intent {
        return ExternalEventNavigator.CreateExternalLinkIntent(ExternalEventNavigator.URIs.FIRST_DESTINATION)
    }
}