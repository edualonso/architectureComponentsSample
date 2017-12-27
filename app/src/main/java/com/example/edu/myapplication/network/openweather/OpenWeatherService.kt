package com.example.edu.myapplication.network.openweather

import com.example.edu.myapplication.data.model.openweather.WeatherResult
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query


/**
 * Created by edu on 27/12/2017.
 */
interface OpenWeatherService {

    @GET(WEATHER)
    fun getWeather(@Query(QUERY) query: String): Single<WeatherResult>

    companion object {
        const val WEATHER = "weather"
        const val QUERY = "q"
    }
}