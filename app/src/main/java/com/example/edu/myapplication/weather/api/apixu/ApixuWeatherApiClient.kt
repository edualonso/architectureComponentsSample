package com.example.edu.myapplication.weather.api.apixu

import com.example.edu.myapplication.base.BaseApplication
import com.example.edu.myapplication.weather.api.WeatherApiClient
import com.example.edu.myapplication.weather.model.InternalLocation
import com.example.edu.myapplication.weather.model.InternalWeather
import com.example.edu.myapplication.weather.model.apixu.Location
import com.example.edu.myapplication.weather.model.toInternalLocation
import com.example.edu.myapplication.weather.model.toInternalWeather
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by edu on 19/12/2017.
 */
@Singleton
class ApixuWeatherApiClient @Inject constructor() : WeatherApiClient {

    init {
        BaseApplication.applicationComponent.inject(this)
    }

    @Inject
    lateinit var apixuWeatherService: ApixuWeatherService

    override fun searchForLocation(location: String): Single<List<InternalLocation>> {
        return apixuWeatherService.searchForLocation(location).map { locations: List<Location> ->
            val internalLocations = arrayListOf<InternalLocation>()
            locations.mapTo(internalLocations) {
                it.toInternalLocation()
            }
            return@map internalLocations
        }
    }

    override fun getCurrentWeather(location: InternalLocation): Single<InternalWeather> {
        with(location) {
            return apixuWeatherService.getCurrentWeather(ApixuWeatherService.getNameAndCoordinatesForQuery(name.substringBefore(","), lat, lon))
                    .map {
                        it.toInternalWeather()
                    }
        }
    }
}