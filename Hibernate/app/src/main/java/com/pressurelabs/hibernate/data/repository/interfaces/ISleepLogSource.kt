package com.pressurelabs.hibernate.data.repository.interfaces

import android.database.sqlite.SQLiteDatabase
import com.pressurelabs.hibernate.data.models.SleepEntry

interface ISleepLogSource {
    fun getRecent(numberEntries:Int):List<SleepEntry>?
    fun insert(entry:SleepEntry)
    fun debug()
}