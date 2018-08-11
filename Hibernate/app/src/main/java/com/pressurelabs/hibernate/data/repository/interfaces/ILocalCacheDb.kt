package com.pressurelabs.hibernate.data.repository.interfaces

import com.pressurelabs.hibernate.domain.Photograph
import com.pressurelabs.hibernate.domain.Time
import java.sql.Timestamp

interface ILocalCacheDb {
    companion object Const {
        val TWENTY_FOUR_HOUR:Int = 1
        val TWELVE_HOUR:Int = 2
    }
    fun getAlarmTime(): Time
    fun putAlarmTime(newTime: Time)
    fun getDefaultAlarm() : Time
    fun getAlarmStatus(): Boolean
    fun putAlarmStatus(status:Boolean)
    fun getClockType(): Int
    fun putClockDisplayType(display:Int)

    fun loadCachedPhotograph(): Photograph
    fun cachePhotograph(photograph: Photograph)
    fun isFirstInstall():Boolean
    fun putFirstInstall(b: Boolean)
    fun loadDefaultPhotograph():Photograph

    fun putStartTrackTime(start:Timestamp)
    fun putEndTrackTime(end:Timestamp)
    fun getTrackStartTime():Timestamp
    fun getTrackEndTime():Timestamp
    fun isTracking():Boolean
    fun clearTrackLog()
}
