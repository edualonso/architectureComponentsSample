package com.example.edu.myapplication.data.model.openweather

import io.realm.RealmObject

/**
 * Created by edu on 27/12/2017.
 */
//data class Location(
//        val id: Long,
//        val name: String,
//        val country: String,
//        val coord: Coord
//)

open class OpenWeatherLocation() : RealmObject() {

    var id: Long = 0
    var name: String = ""
    var country: String = ""
    var coord: Coord? = null

    constructor(id: Long, name: String, country: String, coord: Coord) : this() {
        this.id = id
        this.name = name
        this.country = country
        this.coord = coord
    }
}