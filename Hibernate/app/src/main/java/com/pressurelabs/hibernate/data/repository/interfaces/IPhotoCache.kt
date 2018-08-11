package com.pressurelabs.hibernate.data.repository.interfaces

import android.graphics.Bitmap

interface IPhotoCache {
    fun getPhotographAuthor():String
    fun getPhotographTitle():String
    fun getPhotographAuthorURL():String
    fun putPhotographAuthor(author:String?)
    fun putPhotographTitle(name:String?)
    fun putPhotographAuthorURL(url:String?)

    fun putPhotographBitmap(bp: Bitmap?)
    fun getPhotographBitmap():Bitmap
    fun getDefaultPhotographBitmap():Bitmap

}