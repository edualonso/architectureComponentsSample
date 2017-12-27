package com.example.edu.myapplication.data.model.openweather

/**
 * Created by edu on 27/12/2017.
 */
data class WeatherResult(
        var coord: Coord? = null,
        val weather: List<Weather> = arrayListOf(),
        var base: String? = null,
        var main: Main? = null,
        var wind: Wind? = null,
        var clouds: Clouds? = null,
        var dt: Long? = null,
        var sys: Sys? = null,
        var id: Long? = null,
        var name: String? = null,
        var cod: Long? = null
) {

    companion object {
        const val KEY_ID = "id"
    }

}