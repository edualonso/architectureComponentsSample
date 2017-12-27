package com.example.edu.myapplication.data.model.openweather

import com.google.gson.annotations.SerializedName

/**
 * Created by edu on 27/12/2017.
 */
data class Main(
        var temp: Float? = null,
        var pressure: Float? = null,
        var humidity: Long? = null,
        @SerializedName("temp_min") var tempMin: Float? = null,
        @SerializedName("temp_max") var tempMax: Float? = null,
        @SerializedName("sea_level") var seaLevel: Float? = null,
        @SerializedName("grnd_level") var grndLevel: Float? = null
)