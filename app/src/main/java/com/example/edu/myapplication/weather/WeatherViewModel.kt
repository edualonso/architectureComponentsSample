package com.example.edu.myapplication.weather

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.edu.myapplication.weather.api.WeatherApiClient
import com.example.edu.myapplication.weather.model.Location
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * Created by edu on 19/12/2017.
 */
class WeatherViewModel : ViewModel() {

    var weatherApiClient: WeatherApiClient = WeatherApiClient()
    var cityStateLiveData: MutableLiveData<SearchForCityState> = MutableLiveData()

    init {
        cityStateLiveData.value = idle("")
    }

    fun observeCityState(cityTextChanges: Observable<CharSequence>) {
        cityTextChanges
                .debounce(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                // 1 - check text length and emit appropriate state
                .map { city ->
                    when {
                        city.length in 0..2     -> WeatherViewModel.idle(city.toString())
                        city.length >= 3        -> WeatherViewModel.searching(city.toString())
                        else                    -> WeatherViewModel.error(RuntimeException("WTF has just happened?"))
                    }
                }
                // 2 - update UI via live data
                .map { state ->
                    cityStateLiveData.value = state
                    state
                }
                // 3 - handle errors gracefully until here
                .onErrorReturn { throwable ->
                    error(throwable)
                }
                // 4 - do not query server if we are "idle"
                .filter { state ->
                    !state.idle
                }
                // 5 - query server if everything went fine so far
                .map { state ->
                    weatherApiClient.searchForLocation(state.city)
                            .subscribeOn(Schedulers.io())
                            .map { locations ->
                                success(state.city, locations)
                            }
                            .onErrorReturn { throwable ->
                                error(throwable)
                            }
                            .blockingGet()
                }
                // 6 - process results from state
                .subscribe { state ->
                    cityStateLiveData.value = state
                }
    }

    /**
     * Models the UI state for the search box
     */
    companion object {

        class SearchForCityState(
                val idle: Boolean,
                val searching: Boolean,
                val success: Boolean,
                val error: Boolean,
                val city: String,
                val locations: List<Location>? = ArrayList(),
                val throwable: Throwable? = null
        )

        fun idle(city: String): SearchForCityState = SearchForCityState(true, false, false, false, city)
        fun searching(city: String): SearchForCityState = SearchForCityState(false, true, false, false, city)
        fun success(city: String, locations: List<Location>): SearchForCityState = SearchForCityState(false, false, true, false, city, locations)
        fun error(throwable: Throwable): SearchForCityState = SearchForCityState(false, false, false, true, "", null, throwable)
    }
}