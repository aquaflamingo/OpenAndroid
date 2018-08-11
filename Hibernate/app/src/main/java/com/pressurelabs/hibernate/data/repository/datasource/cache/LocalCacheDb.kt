package com.pressurelabs.hibernate.data.repository.datasource.cache

import com.pressurelabs.hibernate.data.repository.interfaces.CacheDb
import com.pressurelabs.hibernate.data.repository.interfaces.ILocalCacheDb
import com.pressurelabs.hibernate.data.repository.interfaces.IPhotoCache
import com.pressurelabs.hibernate.domain.Photograph
import com.pressurelabs.hibernate.domain.Time
import java.sql.Timestamp

class LocalCacheDb(c: android.content.Context) : CacheDb(c), ILocalCacheDb {
    override fun isTracking(): Boolean {
        return instance.getLong(START_TRACK,-1L)!=-1L
    }

    override fun getTrackStartTime(): Timestamp {
        // -1 = default is bad
        var time = instance.getLong(START_TRACK,-1)
        return Timestamp(time)
    }

    override fun getTrackEndTime(): Timestamp {
        var time = instance.getLong(END_TRACK,-1)
        return Timestamp(time)
    }

    override fun putStartTrackTime(start: Timestamp) {
        instance.edit().putLong(START_TRACK,start.time).commit()
    }

    override fun putEndTrackTime(end: Timestamp) {
        instance.edit().putLong(END_TRACK,end.time).commit()
    }

    override fun clearTrackLog() {
        instance.edit().putLong(START_TRACK,-1).apply()
        instance.edit().putLong(END_TRACK,-1).apply()
    }

    private object DefaultPhoto {
        val AUTHOR = "Daniel Fleischhacker"
        val URL = "https://500px.com/daniel-fleischhacker"
        val TITLE = "Enjoying Mother Nature"
    }
    override fun loadDefaultPhotograph(): Photograph {
        var author = DefaultPhoto.AUTHOR
        var url = DefaultPhoto.URL
        var title = DefaultPhoto.TITLE
        var bitmap = photoCache.getDefaultPhotographBitmap()

        var photograph:Photograph =
                Photograph.Builder()
                        .author(author)
                        .authorURL(url)
                        .bitmap(bitmap)
                        .title(title)
                        .build()
        return photograph
    }

    override fun isFirstInstall(): Boolean {
        return instance.getBoolean(FIRST_INSTALL,true)
    }

    override fun putFirstInstall(b: Boolean) {
        instance.edit().putBoolean(FIRST_INSTALL,b).commit()
    }

    override fun loadCachedPhotograph(): Photograph {
        var author = photoCache.getPhotographAuthor()
        var url = photoCache.getPhotographAuthorURL()
        var title = photoCache.getPhotographTitle()
        var bitmap = photoCache.getPhotographBitmap()

        var photograph:Photograph =
                Photograph.Builder()
                        .author(author)
                        .authorURL(url)
                        .bitmap(bitmap)
                        .title(title)
                        .build()
        return photograph
    }

    override fun cachePhotograph(photograph: Photograph) {
        photograph.authorURL.toString()
        photoCache.putPhotographAuthor(photograph.author)
        photoCache.putPhotographAuthorURL(photograph.authorURL.toString())
        photoCache.putPhotographTitle(photograph.title)
        photoCache.putPhotographBitmap(photograph.bitmap)
    }

    private var photoCache: IPhotoCache = PhotoCacheDb(c)
    private val default = Time(7, 0)

    override fun getClockType(): Int {
        return instance.getInt(CLOCK_DISPLAY, ILocalCacheDb.TWENTY_FOUR_HOUR)
    }

    override fun putClockDisplayType(display:Int) {
        instance.edit().putInt(CLOCK_DISPLAY,display).commit()
    }

    override fun getAlarmStatus(): Boolean {
        return instance.getBoolean(ALARM_STATUS,false)
    }

    override fun putAlarmStatus(status:Boolean) {
        instance.edit().putBoolean(ALARM_STATUS,status).commit()
    }


    override fun getDefaultAlarm(): Time {
        return default;
    }

    override fun putAlarmTime(newTime: Time) {
        instance.edit().putInt(ALARM_TIME_HOUR,newTime.hour).commit()
        instance.edit().putInt(ALARM_TIME_MINUTES,newTime.minutes).commit()
    }

    private companion object KEYS {
        private val ALARM_TIME_HOUR: String="PREFERENCE.ALARM_TIME_HOUR"
        private val ALARM_TIME_MINUTES: String="PREFERENCE.ALARM_TIME_MINUTES"
        private val ALARM_STATUS:String="PREFERENCE.ALARM_STATUS"
        private val CLOCK_DISPLAY:String="PREFERENCE.CLOCK_DISPLAY"
        private val FIRST_INSTALL:String="PREFERENCE.FIRST_INSTALL"
        private val START_TRACK:String="TEMP.START_TRACK_TIME"
        private val END_TRACK:String="TEMP.STOP_TRACK_TIME"
    }


    override fun getAlarmTime(): Time {
        var time: Time = default;
        time.hour = instance.getInt(ALARM_TIME_HOUR,default.hour)
        time.minutes= instance.getInt(ALARM_TIME_MINUTES,default.minutes)
        return time
    }


}
