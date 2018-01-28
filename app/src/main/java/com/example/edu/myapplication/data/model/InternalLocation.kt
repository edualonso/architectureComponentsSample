package com.example.edu.myapplication.data.model

import com.example.edu.myapplication.data.model.apixu.Location
import com.example.edu.myapplication.data.model.openweather.Sys

/**
 * Created by edu on 26/12/2017.
 */
data class InternalLocation(
        val id: Long = 0,
        val name: String = "",
        val country: String = "",
        val lat: Float = 0f,
        val lon: Float = 0f
)


/**
 * Extension functions for converting locations from different APIs to our own data model.
 */
fun Location.toInternalLocation(): InternalLocation {
    return InternalLocation(
            id,
            name,
            country,
            lat,
            lon
    )
}

fun Sys.toInternalLocation(id: Long, name: String, lat: Float, lon: Float): InternalLocation {
    return InternalLocation(
            id,
            name,
            country,
            lat,
            lon
    )
}