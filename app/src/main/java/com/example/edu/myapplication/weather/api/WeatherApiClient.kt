package com.example.edu.myapplication.weather.api

import com.example.edu.myapplication.base.BaseApplication
import com.example.edu.myapplication.weather.model.CurrentWeather
import com.example.edu.myapplication.weather.model.Location
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by edu on 19/12/2017.
 */
@Singleton
class WeatherApiClient @Inject constructor() {

    init {
        BaseApplication.applicationComponent.inject(this)
    }

    @Inject
    lateinit var weatherService: WeatherService

    fun searchForLocation(location: String): Single<List<Location>> {
        return weatherService.searchForLocation(location)
    }

    fun getCurrentWeather(location: Location): Single<CurrentWeather> {
        with(location) {
            return weatherService.getCurrentWeather(WeatherService.getNameAndCoordinatesForQuery(name.substringBefore(","), lat, lon))
        }
    }
}