package com.example.edu.myapplication.weather.addlocation

import android.util.Log
import com.example.edu.myapplication.data.model.InternalLocation
import com.example.edu.myapplication.data.repository.WeatherRepository
import com.example.edu.myapplication.data.repository.memory.MemoryWeatherRepository
import com.example.edu.myapplication.network.apixu.ApixuWeatherApiClient
import com.example.edu.myapplication.weather.addlocation.state.SearchForCityState
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by edu on 25/12/2017.
 */
abstract class AddLocationInteractor {

    @Inject lateinit var weatherApiClient: ApixuWeatherApiClient
    @Inject lateinit var weatherRepository: MemoryWeatherRepository

    fun getSearchBoxStateObservable(cityTextChanges: Observable<CharSequence>): Observable<SearchForCityState> {
        return cityTextChanges
                .debounce(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                // check text length and emit appropriate state
                .map { city ->
                    when {
                        city.length in 0..2   -> SearchForCityState.Idle()
                        city.length >= 3      -> SearchForCityState.Ongoing(city.toString())
                        else                  -> SearchForCityState.Error(RuntimeException("WTF has just happened?"))
                    }
                }
                // handle errors gracefully
                .onErrorReturn { throwable ->
                    SearchForCityState.Error(throwable)
                }
    }

    fun getLocationWithState(searchForCityState: SearchForCityState): SearchForCityState {
        return weatherApiClient.searchForLocation(searchForCityState.city)
                .map { returnedLocations ->
                    SearchForCityState.Success(searchForCityState.city, returnedLocations) as SearchForCityState
                }
                .onErrorReturn { throwable ->
                    SearchForCityState.Error(throwable)
                }
                .blockingGet()
    }

    fun getLocation(location: InternalLocation) {
        weatherRepository.getLocationRx(location)
                .subscribe { getLocationState: WeatherRepository.GetLocationState ->
                    with(getLocationState) {
                        val locationName = this.location.name
                        when (this) {
                            is WeatherRepository.GetLocationState.Exists   -> Log.e("---", "-------------> LOCATION $locationName ALREADY EXISTS")
                            is WeatherRepository.GetLocationState.Error    -> Log.e("---", "-------------> THERE WAS AN ERROR SEARCHING FOR $locationName, > $throwable")
                            is WeatherRepository.GetLocationState.NotFound       -> {
                                Log.e("---", "-------------> LOCATION $locationName NOT FOUND IN REPOSITORY")
                                weatherRepository.saveLocationRx(location)
                                        .subscribe(
                                                { Log.e("---", "-------------> SUCCESS! LOCATION $locationName HAS BEEN SAVED") },
                                                { Log.e("---", "-------------> THERE WAS AN ERROR SAVING $locationName: ${it.message}") }
                                        )
                            }
                        }
                    }
                }
    }

    companion object {
        const val ROUTE_TO_CITY_LIST = "city_list.json"
    }
}