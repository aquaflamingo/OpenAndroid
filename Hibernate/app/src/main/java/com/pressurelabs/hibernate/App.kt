package com.pressurelabs.hibernate

import android.app.Application
import android.util.Log
import timber.log.Timber


class App : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(App.CrashReportingTree())
        }
    }

    /** A tree which logs important information for crash reporting.  */
    class CrashReportingTree : Timber.Tree() {
        override fun log(priority: Int, tag: String, message: String, t: Throwable?) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return
            }

            var mTag = "TAG"

            if (t != null) {
                if (priority == Log.ERROR) {

                }
            }
        }
    }
}