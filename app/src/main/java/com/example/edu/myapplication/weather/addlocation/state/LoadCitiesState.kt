package com.example.edu.myapplication.weather.addlocation.state

/**
 * Class that models the different states of the initial setup process for loading OpenWeather cities.
 */
class LoadCitiesState(
        val ongoing: Boolean,
        val done: Boolean,
        val error: Boolean,
        val throwable: Throwable? = null
) {
    companion object {
        fun loadCitiesOngoing(): LoadCitiesState = LoadCitiesState(true, false, false)
        fun loadCitiesDone(): LoadCitiesState = LoadCitiesState(false, true, false)
        fun loadCitiesError(throwable: Throwable): LoadCitiesState = LoadCitiesState(false, false, true, throwable)
    }
}