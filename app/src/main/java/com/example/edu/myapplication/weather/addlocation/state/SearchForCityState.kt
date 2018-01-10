package com.example.edu.myapplication.weather.addlocation.state

import com.example.edu.myapplication.data.model.InternalLocation

/**
 * Created by edu on 10/01/2018.
 */
class SearchForCityState(
        val idle: Boolean,
        val ongoing: Boolean,
        val success: Boolean,
        val error: Boolean,
        val city: String,
        val locations: List<InternalLocation> = ArrayList(),
        val throwable: Throwable? = null
) {
    companion object {
        fun searchForCityIdle(city: String = ""): SearchForCityState = SearchForCityState(true, false, false, false, city)
        fun searchForCityOngoing(city: String): SearchForCityState = SearchForCityState(false, true, false, false, city)
        fun searchForCitySuccess(city: String, locations: List<InternalLocation>): SearchForCityState = SearchForCityState(false, false, true, false, city, locations)
        fun searchForCityError(throwable: Throwable): SearchForCityState = SearchForCityState(false, false, false, true, "", arrayListOf(), throwable)
    }
}