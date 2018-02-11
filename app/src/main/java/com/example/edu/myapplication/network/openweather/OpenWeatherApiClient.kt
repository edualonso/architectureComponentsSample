package com.example.edu.myapplication.network.openweather

import com.example.edu.myapplication.base.BaseApplication
import com.example.edu.myapplication.data.model.InternalLocation
import com.example.edu.myapplication.data.model.InternalWeather
import com.example.edu.myapplication.data.model.openweather.OpenWeatherLocation
import com.example.edu.myapplication.data.model.toInternalLocation
import com.example.edu.myapplication.data.model.toInternalWeather
import com.example.edu.myapplication.network.WeatherApiClient
import io.reactivex.Single
import io.realm.Case
import io.realm.Realm
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Edu on 28/01/2018.
 */
@Singleton
class OpenWeatherApiClient @Inject constructor() : WeatherApiClient {

    @Inject lateinit var openWeatherService: OpenWeatherService

    init {
        BaseApplication.applicationComponent.inject(this)
    }

    override fun searchForLocation(location: String): Single<List<InternalLocation>> {
        return Single.fromCallable {
            Realm.getDefaultInstance()
                    .where(OpenWeatherLocation::class.java)
                    .equalTo("name", location, Case.INSENSITIVE)
                    .findAll()
                    .map {
                        it.toInternalLocation()
                    }
        }
    }

    override fun getCurrentWeather(location: InternalLocation): Single<InternalWeather> {
        with(location) {
            return openWeatherService.getWeather(name)
                    .map {
                        it.toInternalWeather()
                    }
        }
    }
}