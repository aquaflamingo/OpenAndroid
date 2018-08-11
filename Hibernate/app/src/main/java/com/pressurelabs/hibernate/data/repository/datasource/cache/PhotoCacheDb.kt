package com.pressurelabs.hibernate.data.repository.datasource.cache

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.pressurelabs.hibernate.R
import com.pressurelabs.hibernate.data.repository.interfaces.CacheDb
import com.pressurelabs.hibernate.data.repository.interfaces.IPhotoCache
import java.io.FileOutputStream
import java.io.IOException

class PhotoCacheDb(con: Context): CacheDb(con), IPhotoCache {
    override fun getDefaultPhotographBitmap(): Bitmap {
//        var fileName: String? = (context.cacheDir.absoluteFile.toString() + IO.WALLPAPER_FILE)
        var bp:Bitmap= BitmapFactory.decodeResource(context.resources,R.drawable.default_wallpaper)
        return bp
    }

    override fun putPhotographBitmap(bp: Bitmap?) {
        var out: FileOutputStream? = null
        try {
            var fileName:String = context.cacheDir.absoluteFile.toString()+ IO.WALLPAPER_FILE
            out = FileOutputStream(fileName)
            bp?.compress(Bitmap.CompressFormat.PNG, 100, out) // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                if (out != null) {
                    out!!.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    override fun getPhotographBitmap(): Bitmap {
        var fileName: String? = (context.cacheDir.absoluteFile.toString() + IO.WALLPAPER_FILE)
        var bp:Bitmap= BitmapFactory.decodeFile(fileName)
        return bp
    }



    private object KEYS {
        val PHOTO_AUTHOR = "PHOTO_CACHE.PHOTO_AUTHOR"
        val PHOTO_NAME = "PHOTO_CACHE.PHOTO_NAME"
        val PHOTO_URL = "PHOTO_CACHE.PHOTO_URL"
    }

    private var context:Context = con

    private object IO {
        val WALLPAPER_FILE = "/wallpaper.png"
        val DEFAULT_WALLPAPER = "/default_wallpaper.png"
    }

    override fun getPhotographAuthor(): String {
        return instance.getString(KEYS.PHOTO_AUTHOR,"")
    }

    override fun getPhotographTitle(): String {
        return instance.getString(KEYS.PHOTO_NAME,"")
    }

    override fun getPhotographAuthorURL(): String {
        return instance.getString(KEYS.PHOTO_URL,"")
    }

    override fun putPhotographAuthor(author: String?) {
        instance.edit().putString(KEYS.PHOTO_AUTHOR,author).commit()
    }

    override fun putPhotographTitle(name: String?) {
        instance.edit().putString(KEYS.PHOTO_NAME,name).commit()
    }

    override fun putPhotographAuthorURL(url: String?) {
        instance.edit().putString(KEYS.PHOTO_URL,url).commit()
    }
}