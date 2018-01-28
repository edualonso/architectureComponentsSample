package com.example.edu.myapplication.main

import android.content.res.AssetManager
import android.util.Log
import com.example.edu.myapplication.base.BaseApplication
import com.example.edu.myapplication.data.model.openweather.Location
import com.example.edu.myapplication.util.JsonParsingUtil
import com.example.edu.myapplication.weather.addlocation.AddLocationInteractor
import com.google.gson.GsonBuilder
import io.reactivex.Completable
import io.reactivex.Single
import io.realm.Realm
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Edu on 28/01/2018.
 */
@Singleton
class MainInteractor @Inject constructor() {

    @Inject lateinit var assetManager: AssetManager

    init {
        BaseApplication.applicationComponent.inject(this)
    }

    fun parseCities(): Completable {
        return Single
                // 1 - read file
                .fromCallable {
                    Log.e("---------------", "===========> ${Thread.currentThread().name}: FILE READING JOB STARTED")
                    val buffer = JsonParsingUtil.readFile(assetManager.open(AddLocationInteractor.ROUTE_TO_CITY_LIST))
                    Log.e("---------------", "===========> ${Thread.currentThread().name}: FILE READING JOB FINISHED")
                    return@fromCallable buffer
                }
                // 2 - parse JSON
                .map { jsonString: String ->
                    Log.e("---------------", "===========> ${Thread.currentThread().name}: GSON JOB STARTED")
                    val cities: List<Location> = GsonBuilder().create().fromJson(jsonString, Array<Location>::class.java).toList()
                    Log.e("---------------", "===========> ${Thread.currentThread().name}: GSON JOB FINISHED")
                    return@map cities
                }
                // 3 - store cities
                .map { cities: List<Location> ->
                    Realm.getDefaultInstance().executeTransaction {
                        Log.e("---------------", "===========> ${Thread.currentThread().name}: STORING CITIES...")
                        it.copyToRealmOrUpdate(cities)
                        Log.e("---------------", "===========> ${Thread.currentThread().name}: STORING CITIES DONE!!!")
                    }
                }
                .toCompletable()
    }

    fun countCities(): Long {
        val numCities = Realm.getDefaultInstance().where(Location::class.java).count()
        Log.e("---------------", "===========> THERE ARE $numCities CITIES STORED IN THE DATABASE")
        return numCities
    }
}