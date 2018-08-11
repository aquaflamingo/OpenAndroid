package com.pressurelabs.hibernate.data.network

import com.pressurelabs.hibernate.data.helpers.OKHttpFactory
import com.pressurelabs.hibernate.data.models.PhotosResponse
import com.pressurelabs.hibernate.data.repository.datasource.FiveHundredPxSource
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


interface FiveHundredPxService {
    @GET("v1/photos?feature=popular&sort=rating&image_size=6&&license=4&only=Landscapes,Nature,Travel")
    fun getPopularPhotos(): Observable<PhotosResponse>


    // 500px User of photo

    /**
     * Factory method for convenient creation of the Api Service interface
     */
    object Factory {

        fun create() : FiveHundredPxService {

            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(FiveHundredPxSource.BASE_URL)
                    .client(OKHttpFactory.CreateOkHttp())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

            return retrofit.create(FiveHundredPxService::class.java)
        }
    }
}