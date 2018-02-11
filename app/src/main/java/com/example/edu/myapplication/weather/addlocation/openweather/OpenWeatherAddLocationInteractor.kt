package com.example.edu.myapplication.weather.addlocation.openweather

import com.example.edu.myapplication.base.BaseApplication
import com.example.edu.myapplication.network.openweather.OpenWeatherApiClient
import com.example.edu.myapplication.weather.addlocation.AddLocationInteractor
import javax.inject.Inject

/**
 * Created by edu on 04/02/2018.
 */
class OpenWeatherAddLocationInteractor : AddLocationInteractor() {

    @Inject lateinit var _apiClient: OpenWeatherApiClient

    init {
        BaseApplication.applicationComponent.inject(this)
        weatherApiClient = _apiClient
    }

}