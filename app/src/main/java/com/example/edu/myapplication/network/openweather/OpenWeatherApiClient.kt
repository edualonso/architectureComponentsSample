package com.example.edu.myapplication.network.openweather

import com.example.edu.myapplication.base.BaseApplication
import com.example.edu.myapplication.data.model.InternalLocation
import com.example.edu.myapplication.data.model.InternalWeather
import com.example.edu.myapplication.data.model.toInternalWeather
import com.example.edu.myapplication.data.repository.realm.RealmWeatherRepository
import com.example.edu.myapplication.network.WeatherApiClient
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Edu on 28/01/2018.
 */
@Singleton
class OpenWeatherApiClient @Inject constructor() : WeatherApiClient {

    @Inject lateinit var openWeatherService: OpenWeatherService
    @Inject lateinit var weatherRepository: RealmWeatherRepository

    init {
        BaseApplication.applicationComponent.inject(this)
    }

    override fun searchForLocation(location: String): Single<List<InternalLocation>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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