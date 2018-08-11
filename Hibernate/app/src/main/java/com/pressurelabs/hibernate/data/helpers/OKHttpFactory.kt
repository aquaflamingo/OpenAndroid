package com.pressurelabs.hibernate.data.helpers

import com.pressurelabs.hibernate.data.repository.datasource.FiveHundredPxSource
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit


object OKHttpFactory {
    fun CreateOkHttp():OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(
                        { chain ->
                    var request = chain.request()
                    val url = request.url().newBuilder()
                            .addQueryParameter("consumer_key", FiveHundredPxSource.CONSUMER_KEY).build()
                    request = request.newBuilder().url(url).build()
                    chain.proceed(request)
                })
                .build()
        return okHttpClient
    }

    fun CreateOkHttpLong(): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS) // connect timeout
                .addInterceptor({ chain ->
                    var request = chain.request()
                    val url = request.url().newBuilder()
                            .addQueryParameter("consumer_key", FiveHundredPxSource.CONSUMER_KEY).build()
                    request = request.newBuilder().url(url).build()
                    chain.proceed(request)
                })
                .build()

        return okHttpClient
    }

}