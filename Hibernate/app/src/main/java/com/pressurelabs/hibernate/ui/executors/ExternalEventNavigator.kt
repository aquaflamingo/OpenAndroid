package com.pressurelabs.hibernate.ui.executors

import android.content.Intent
import android.net.Uri

object ExternalEventNavigator {
    object URIs {
        val FIRST_DESTINATION = "http://bitcoin.com"
        val SECOND_DESTINATION = "http://duckduckgo.com"
    }

    fun CreateExternalLinkIntent(url:String):Intent {
        val intentVal = url
        val myItent: Intent = Intent(Intent.ACTION_VIEW, Uri.parse(intentVal))
        return myItent
    }
}