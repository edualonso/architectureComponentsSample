package com.example.edu.myapplication.weather.addlocation

import android.content.res.AssetManager
import android.util.Log
import com.example.edu.myapplication.data.model.InternalLocation
import com.example.edu.myapplication.data.model.openweather.Location
import com.example.edu.myapplication.data.repository.WeatherRepository
import com.example.edu.myapplication.data.repository.memory.MemoryWeatherRepository
import com.example.edu.myapplication.network.apixu.ApixuWeatherApiClient
import com.example.edu.myapplication.util.JsonParsingUtil
import com.example.edu.myapplication.weather.addlocation.state.SearchForCityState
import com.example.edu.myapplication.weather.addlocation.state.SearchForCityState.Companion.searchForCityError
import com.example.edu.myapplication.weather.addlocation.state.SearchForCityState.Companion.searchForCityIdle
import com.example.edu.myapplication.weather.addlocation.state.SearchForCityState.Companion.searchForCityOngoing
import com.example.edu.myapplication.weather.addlocation.state.SearchForCityState.Companion.searchForCitySuccess
import com.example.edu.myapplication.base.BaseApplication
import com.google.gson.GsonBuilder
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.realm.Realm
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by edu on 25/12/2017.
 */
@Singleton
class AddLocationInteractor @Inject constructor() {

    @Inject lateinit var weatherApiClient: ApixuWeatherApiClient
    @Inject lateinit var weatherRepository: MemoryWeatherRepository
    @Inject lateinit var assetManager: AssetManager

    init {
        BaseApplication.applicationComponent.inject(this)
    }

    fun getSearchBoxStateObservable(cityTextChanges: Observable<CharSequence>): Observable<SearchForCityState> {
        return cityTextChanges
                .debounce(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                // check text length and emit appropriate state
                .map { city ->
                    when {
                        city.length in 0..2   -> searchForCityIdle(city.toString())
                        city.length >= 3      -> searchForCityOngoing(city.toString())
                        else                  -> searchForCityError(RuntimeException("WTF has just happened?"))
                    }
                }
                // handle errors gracefully until here
                .onErrorReturn { throwable ->
                    searchForCityError(throwable)
                }
    }

    fun getLocationWithState(searchForCityState: SearchForCityState): SearchForCityState {
        return weatherApiClient.searchForLocation(searchForCityState.city)
                .map { returnedLocations ->
                    searchForCitySuccess(searchForCityState.city, returnedLocations)
                }
                .onErrorReturn { throwable ->
                    searchForCityError(throwable)
                }
                .blockingGet()
    }

    fun parseCities(): Completable {
        return Single
                // 1 - read file
                .fromCallable {
                    Log.e("---------------", "===========> ${Thread.currentThread().name}: FILE READING JOB STARTED")
                    val buffer = JsonParsingUtil.readFile(assetManager.open(ROUTE_TO_CITY_LIST))
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

    fun getLocation(location: InternalLocation) {
        weatherRepository.getLocationRx(location)
                .subscribe { getLocationState: WeatherRepository.Companion.GetLocationState ->
                    val locationName = getLocationState.location.name
                    when {
                        getLocationState.notFound       -> {
                            Log.e("---", "-------------> LOCATION $locationName NOT FOUND IN REPOSITORY")
                            weatherRepository.saveLocationRx(location)
                                    .subscribe(
                                            { Log.e("---", "-------------> SUCCESS! LOCATION $locationName HAS BEEN SAVED") },
                                            { Log.e("---", "-------------> THERE WAS AN ERROR SAVING $locationName: ${it.message}") }
                                    )
                        }
                        getLocationState.locationExists -> Log.e("---", "-------------> LOCATION $locationName ALREADY EXISTS")
                        getLocationState.error          -> Log.e("---", "-------------> THERE WAS AN ERROR SEARCHING FOR $locationName")
                        else -> Log.e("---", "-------------> WTF HAPPENED??? TRIED TO SAVE $locationName BUT FAILED: ${getLocationState.error}")
                    }
                }
    }

    companion object {
        const val ROUTE_TO_CITY_LIST = "city_list.json"
    }
}