package com.pressurelabs.hibernate.data.repository.datasource

import android.graphics.BitmapFactory
import android.net.Uri
import com.pressurelabs.hibernate.data.models.PhotoDTO
import com.pressurelabs.hibernate.data.models.PhotosResponse
import com.pressurelabs.hibernate.data.network.FiveHundredPxService
import com.pressurelabs.hibernate.domain.Photograph
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.net.HttpURLConnection
import java.util.Random

class FiveHundredPxSource(val service: FiveHundredPxService) {
    fun getPhoto(callback: (m:Photograph?) -> Unit) {
        service.getPopularPhotos()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            response: PhotosResponse? ->
                            if(response?.photos?.size==0 || response?.photos==null) {
                                Timber.w(TAG, "No photos returned from API.")
                                callback(null)
                            } else {
                               Timber.d("Photos found from API response")
                                val random = Random()
                                val size = response?.photos?.size;

                                var photo: PhotoDTO
                                photo = response?.photos?.get(random.nextInt(size!!))!!

                                downloadPhoto(photo)
                                        .subscribeOn(Schedulers.newThread())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe({
                                            photograph ->
                                            callback(photograph)
                                        })
                            }

                        }, {
                            e->
                                Timber.e("No photos returned from API \n${e.localizedMessage}\n${e.printStackTrace().toString()}")
                                callback(null)
                            }
                )
    }

    private fun downloadPhoto(photo: PhotoDTO): Observable<Photograph> {
        return Observable.create {
            sub ->
                val url = java.net.URL(photo.image_url.toString())
                val connection = url
                        .openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                val input = connection. inputStream
                val myBitmap = BitmapFactory.decodeStream(input)

                var photograph: Photograph = Photograph.Builder()
                        .author(photo.user?.fullname)
                        .authorURL("https://www.500px.com/"+photo.user?.username)
                        .title(photo.name)
                        .imageUri(Uri.parse(photo.image_url))
                        .bitmap(myBitmap)
                        .build()

            sub.onNext(photograph)
            sub.onComplete()
        }
    }

     companion object {
        private val TAG = "500px"
        private val SOURCE_NAME = "500pxSource"
        val CONSUMER_KEY = "INSERT_KEY_HERE"
        val BASE_URL = "https://api.500px.com/"
    }


}
