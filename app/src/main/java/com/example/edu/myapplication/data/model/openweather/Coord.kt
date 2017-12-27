package com.example.edu.myapplication.data.model.openweather

import io.realm.RealmObject

/**
 * Created by edu on 27/12/2017.
 */
//data class Coord(
//        var lon: Float? = null,
//        var lat: Float? = null
//)
open class Coord() : RealmObject() {

    var lon: Float? = null
    var lat: Float? = null

    constructor(lon: Float? = null, lat: Float? = null) : this() {
        this.lon = lon
        this.lat = lat
    }
}