package com.example.edu.myapplication.data.model.openweather

/**
 * Created by edu on 27/12/2017.
 */
data class WeatherResult(
//        var coord: Coord? = null,
        var coord: Coord,
        val weather: List<Weather> = arrayListOf(),
        var base: String? = null,
        var main: Main? = null,
        var wind: Wind? = null,
        var clouds: Clouds? = null,
        var dt: Long? = null,
//        var sys: Sys? = null,
        var sys: Sys,
//        var id: Long? = null,
        var id: Long,
//        var name: String? = null,
        var name: String,
        var cod: Long? = null
)