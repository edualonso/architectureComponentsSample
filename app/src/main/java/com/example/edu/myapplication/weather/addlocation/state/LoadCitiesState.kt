package com.example.edu.myapplication.weather.addlocation.state

/**
 * Created by edu on 10/01/2018.
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