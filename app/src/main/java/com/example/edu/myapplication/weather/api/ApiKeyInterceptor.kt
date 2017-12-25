package com.example.edu.myapplication.weather.api

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * Created by edu on 19/12/2017.
 */
class ApiKeyInterceptor @Inject constructor(): Interceptor {

    private val KEY = "KEY"
    private val VALUE = "11682c59698444f6b59160534171912"

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalHttpUrl = original.url()

        val url = originalHttpUrl.newBuilder()
                .addQueryParameter(KEY, VALUE)
                .build()

        // Request customization: add request headers
        val requestBuilder = original.newBuilder()
                .url(url)

        val request = requestBuilder.build()
        return chain.proceed(request)
    }

}