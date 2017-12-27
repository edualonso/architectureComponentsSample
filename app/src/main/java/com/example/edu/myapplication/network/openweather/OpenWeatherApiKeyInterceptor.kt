package com.example.edu.myapplication.network.openweather

import com.example.edu.myapplication.di.modules.NetworkModule
import com.example.edu.myapplication.network.ApiKeyInterceptor
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by edu on 19/12/2017.
 */
class OpenWeatherApiKeyInterceptor @Inject constructor(
        @Named(NetworkModule.OPENWEATHER_API_KEY) val apiKey: String
) : ApiKeyInterceptor(
        NetworkModule.OPENWEATHER_API_KEY,
        apiKey
)