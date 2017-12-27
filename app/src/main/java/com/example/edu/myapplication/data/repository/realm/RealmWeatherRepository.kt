package com.example.edu.myapplication.data.repository.realm

import com.example.edu.myapplication.data.model.InternalLocation
import com.example.edu.myapplication.data.repository.WeatherRepository
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Created by edu on 27/12/2017.
 */
class RealmWeatherRepository : WeatherRepository {
    override fun saveLocation(location: InternalLocation) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun saveLocationRx(location: InternalLocation): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLocation(location: InternalLocation): InternalLocation? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLocationRx(location: InternalLocation): Single<WeatherRepository.Companion.GetLocationState> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}