package com.pressurelabs.hibernate.domain

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri


class Photograph private constructor() {

    var imageUri: Uri? = null
    var author: String? =null
    var title: String? = null
    var viewIntent: Intent? = null
    var bitmap: Bitmap?=null
    var authorURL: Uri?=null

    class Builder(private val mPhoto: Photograph = Photograph()) {
        fun author(author:String?): Builder {
            mPhoto.author =author
            return this;
        }
        fun imageUri(imageUri: Uri): Builder {
            mPhoto.imageUri = imageUri
            return this
        }

        fun title(title: String?): Builder {
            mPhoto.title = title
            return this
        }

        fun viewIntent(viewIntent: Intent): Builder {
            mPhoto.viewIntent = viewIntent
            return this
        }
        fun build(): Photograph {
            return mPhoto
        }

        fun bitmap(bitmap: Bitmap):Builder {
            mPhoto.bitmap=bitmap;
            return this
        }

        fun authorURL(url: String): Builder {
            mPhoto.authorURL=Uri.parse(url)
            return this
        }
    }
}