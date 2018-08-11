package com.pressurelabs.hibernate.data.repository

import android.content.Context
import com.pressurelabs.hibernate.data.repository.datasource.LocalSleepLogSource
import com.pressurelabs.hibernate.data.repository.interfaces.ISleepLogSource

object SleepLogProvider {
    fun provide(c: Context): ISleepLogSource {
        return LocalSleepLogSource.getInstance(c.applicationContext)
    }
}

