package com.example.edu.myapplication.weather.api

import com.example.edu.myapplication.weather.model.Location
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by edu on 19/12/2017.
 */
interface WeatherService {

    @GET(AUTOCOMPLETION)
    fun searchForLocation(@Query(LOCATION) location: String): Single<List<Location>>

    companion object {
        const val AUTOCOMPLETION = "search.json"
        const val LOCATION = "q"
    }
}