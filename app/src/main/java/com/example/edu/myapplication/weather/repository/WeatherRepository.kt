package com.example.edu.myapplication.weather.repository

import com.example.edu.myapplication.weather.model.Location

/**
 * Created by edu on 24/12/2017.
 */
interface WeatherRepository {
    fun saveLocation(location: Location)
    fun getLocation(location: Location): Location?
}