package com.example.edu.myapplication.data.model.apixu

/**
 * Created by edu on 19/12/2017.
 */
data class Location(
        val id: Long = 0,
        val name: String = "",
        val region: String = "",
        val country: String = "",
        val lat: Float = 0f,
        val lon: Float = 0f,
        val url: String = ""
)