package com.example.edu.myapplication.weather.addlocation.state

import com.example.edu.myapplication.data.model.InternalLocation

/**
 * Class that models the different states of a city search.
 */
sealed class SearchForCityState(
        val city: String = ""
) {
    class Idle: SearchForCityState()
    class Ongoing(city: String): SearchForCityState(city)
    class Success(city: String, val locations: List<InternalLocation>): SearchForCityState(city)
    class Error(val throwable: Throwable): SearchForCityState()
}