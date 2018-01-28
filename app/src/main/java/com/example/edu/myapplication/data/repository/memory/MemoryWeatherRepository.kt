package com.example.edu.myapplication.data.repository.memory

import com.example.edu.myapplication.data.model.InternalLocation
import com.example.edu.myapplication.data.repository.WeatherRepository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by edu on 24/12/2017.
 */
@Singleton
class MemoryWeatherRepository @Inject constructor(): WeatherRepository {

    private val locations = mutableMapOf<String, InternalLocation>()

    override fun saveLocation(location: InternalLocation) {
        locations.put(location.name, location)
    }

    override fun saveLocationRx(location: InternalLocation): Completable {
        return Completable.create {
            locations.put(location.name, location)
            it.onComplete()
        }
    }

    override fun getLocation(location: InternalLocation): InternalLocation? {
        return locations[location.name]
    }

    override fun getLocationRx(location: InternalLocation): Single<WeatherRepository.GetLocationState> {

        return Single.create<WeatherRepository.GetLocationState> {
            val searchedLocation = locations[location.name]
            try {
                if (searchedLocation != null) {
                    it.onSuccess(WeatherRepository.GetLocationState.Exists(location))
                } else {
                    it.onSuccess(WeatherRepository.GetLocationState.NotFound(location))
                }
            } catch (e: Exception) {
                it.onSuccess(WeatherRepository.GetLocationState.Error(location, e))
            }
        }
    }
}