package com.example.edu.myapplication.weather.model

import com.example.edu.myapplication.weather.model.apixu.CurrentWeather

/**
 * Created by edu on 26/12/2017.
 */
data class InternalWeather(
        val lastUpdated: Long,
        val location: InternalLocation
)

/**
 * Extension functions for converting weather forecasts from different APIs to our own data model.
 */
fun CurrentWeather.toInternalWeather(): InternalWeather {
    return InternalWeather(
            this.current.lastUpdatedEpoch ?: System.currentTimeMillis(),
            this.location.toInternalLocation()
    )
}