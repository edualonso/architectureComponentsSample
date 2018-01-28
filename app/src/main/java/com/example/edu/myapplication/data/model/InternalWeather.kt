package com.example.edu.myapplication.data.model

import com.example.edu.myapplication.data.model.InternalWeather.Companion.NO_COORDS_VALUE
import com.example.edu.myapplication.data.model.apixu.CurrentWeather
import com.example.edu.myapplication.data.model.openweather.WeatherResult

/**
 * Created by edu on 26/12/2017.
 */
data class InternalWeather(
        val lastUpdated: Long,
        val location: InternalLocation
) {
    companion object {
        const val NO_COORDS_VALUE = -1F
    }
}


/**
 * Extension functions for converting weather forecasts from different APIs to our own data model.
 */
fun CurrentWeather.toInternalWeather(): InternalWeather {
    return InternalWeather(
            this.current.lastUpdatedEpoch ?: System.currentTimeMillis(),
            this.location.toInternalLocation()
    )
}

fun WeatherResult.toInternalWeather(): InternalWeather {
    return InternalWeather(
            this.dt ?: System.currentTimeMillis(),
            this.sys.toInternalLocation(
                    this.id,
                    this.name,
                    coord.lat ?: NO_COORDS_VALUE,
                    coord.lon ?: NO_COORDS_VALUE
            )
    )
}