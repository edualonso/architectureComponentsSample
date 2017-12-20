package com.example.edu.myapplication.weather.api

import com.example.edu.myapplication.weather.model.CurrentWeather
import com.example.edu.myapplication.weather.model.Location
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by edu on 19/12/2017.
 */
interface WeatherService {

    @GET(AUTOCOMPLETION)
    fun searchForLocation(@Query(QUERY) location: String): Single<List<Location>>

    @GET(CURRENT_WEATHER)
    fun getCurrentWeather(@Query(QUERY) nameAndCoordinates: String): Single<CurrentWeather>

    companion object {
        const val AUTOCOMPLETION = "search.json"
        const val CURRENT_WEATHER = "current.json"
        const val QUERY = "q"

        fun getNameAndCoordinatesForQuery(name: String, lat: Float, lon: Float): String {
            return name + " " + lat.toString() + "," + lon.toString()
        }
    }
}