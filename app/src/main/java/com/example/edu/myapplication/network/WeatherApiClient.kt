package com.example.edu.myapplication.network

import com.example.edu.myapplication.data.model.InternalLocation
import com.example.edu.myapplication.data.model.InternalWeather
import io.reactivex.Single

/**
 * Created by edu on 19/12/2017.
 */
interface WeatherApiClient {
    fun searchForLocation(location: String): Single<List<InternalLocation>>
    fun getCurrentWeather(location: InternalLocation): Single<InternalWeather>
}