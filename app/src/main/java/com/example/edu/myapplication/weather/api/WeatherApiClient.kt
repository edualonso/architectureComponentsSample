package com.example.edu.myapplication.weather.api

import com.example.edu.myapplication.weather.model.CurrentWeather
import com.example.edu.myapplication.weather.model.Location
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by edu on 19/12/2017.
 */
class WeatherApiClient {

    private val weatherService: WeatherService

    init {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC

        weatherService = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(OkHttpClient.Builder()
                        .addInterceptor(loggingInterceptor)
                        .addInterceptor(ApiKeyInterceptor())
                        .build())
                .build()
                .create(WeatherService::class.java)
    }

    fun searchForLocation(location: String): Single<List<Location>> {
        return weatherService.searchForLocation(location)
    }

    fun getCurrentWeather(location: Location): Single<CurrentWeather> {
        with(location) {
            return weatherService.getCurrentWeather(WeatherService.getNameAndCoordinatesForQuery(name.substringBefore(","), lat, lon))
        }
    }

    companion object {
        const val BASE_URL = "http://api.apixu.com/v1/"
    }
}