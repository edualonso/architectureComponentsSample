package com.example.edu.myapplication.weather.addlocation.apixu

import com.example.edu.myapplication.base.BaseApplication
import com.example.edu.myapplication.network.apixu.ApixuWeatherApiClient
import com.example.edu.myapplication.weather.addlocation.AddLocationInteractor
import javax.inject.Inject

/**
 * Created by edu on 04/02/2018.
 */
class ApixuAddLocationInteractor : AddLocationInteractor() {

    @Inject lateinit var _apiClient: ApixuWeatherApiClient

    init {
        BaseApplication.applicationComponent.inject(this)
        weatherApiClient = _apiClient
    }

}