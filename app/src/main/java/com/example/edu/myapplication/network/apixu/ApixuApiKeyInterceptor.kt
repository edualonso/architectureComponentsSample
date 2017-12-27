package com.example.edu.myapplication.network.apixu

import com.example.edu.myapplication.di.modules.NetworkModule
import com.example.edu.myapplication.network.ApiKeyInterceptor
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by edu on 19/12/2017.
 */
class ApixuApiKeyInterceptor @Inject constructor(
        @Named(NetworkModule.APIXU_API_KEY) val apiKey: String
) : ApiKeyInterceptor(
        NetworkModule.APIXU_API_KEY,
        apiKey
)