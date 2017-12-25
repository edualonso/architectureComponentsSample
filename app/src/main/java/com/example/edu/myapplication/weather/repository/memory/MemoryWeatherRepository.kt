package com.example.edu.myapplication.weather.repository.memory

import com.example.edu.myapplication.weather.model.Location
import com.example.edu.myapplication.weather.repository.WeatherRepository

/**
 * Created by edu on 24/12/2017.
 */
class MemoryWeatherRepository: WeatherRepository {

    private val locations = mutableMapOf<String, Location>()

    override fun saveLocation(location: Location) {
        locations.put(location.name, location)
    }

    override fun getLocation(location: Location): Location? {
        return locations.get(location.name)
    }
}