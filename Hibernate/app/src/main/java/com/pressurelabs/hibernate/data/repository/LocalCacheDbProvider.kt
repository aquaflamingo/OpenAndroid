package com.pressurelabs.hibernate.data.repository

import android.content.Context
import com.pressurelabs.hibernate.data.repository.datasource.cache.LocalCacheDb
import com.pressurelabs.hibernate.data.repository.interfaces.ILocalCacheDb

object LocalCacheDbProvider {
    fun provide(c: Context): ILocalCacheDb {
        return LocalCacheDb(c)
    }
}