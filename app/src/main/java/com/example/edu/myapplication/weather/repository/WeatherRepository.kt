package com.example.edu.myapplication.weather.repository

import com.example.edu.myapplication.weather.model.Location
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Created by edu on 24/12/2017.
 */
interface WeatherRepository {

    fun saveLocation(location: Location)
    fun saveLocationRx(location: Location): Completable
    fun getLocation(location: Location): Location?
    fun getLocationRx(location: Location): Single<GetLocationState>

    /**
     * Models de state for searching locations in the repository.
     */
    companion object {
        class GetLocationState(
                val locationExists: Boolean,
                val notFound: Boolean,
                val error: Boolean,
                val location: Location,
                val throwable: Throwable? = null
        )

        fun locationExists(location: Location) = GetLocationState(true, false, false, location)
        fun notFound(location: Location) = GetLocationState(false, true, false, location)
        fun error(location: Location, throwable: Throwable) = GetLocationState(false, false, true, location, throwable)
    }
}