package com.example.edu.myapplication.data.repository

import com.example.edu.myapplication.data.model.InternalLocation
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Created by edu on 24/12/2017.
 */
interface WeatherRepository {

    fun saveLocation(location: InternalLocation)
    fun saveLocationRx(location: InternalLocation): Completable
    fun getLocation(location: InternalLocation): InternalLocation?
    fun getLocationRx(location: InternalLocation): Single<GetLocationState>

    /**
     * Models the state for searching locations in the repository.
     */
//    companion object {
//        class GetLocationState(
//                val locationExists: Boolean,
//                val notFound: Boolean,
//                val error: Boolean,
//                val location: InternalLocation,
//                val throwable: Throwable? = null
//        )
//
//        fun locationExists(location: InternalLocation) = GetLocationState(true, false, false, location)
//        fun notFound(location: InternalLocation) = GetLocationState(false, true, false, location)
//        fun error(location: InternalLocation, throwable: Throwable) = GetLocationState(false, false, true, location, throwable)
//    }

    sealed class GetLocationState(val location: InternalLocation) {
        class Exists(location: InternalLocation) : GetLocationState(location)
        class NotFound(location: InternalLocation) : GetLocationState(location)
        class Error(location: InternalLocation, val throwable: Throwable) : GetLocationState(location)
    }
}