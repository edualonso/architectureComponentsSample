package com.example.edu.myapplication.weather

import android.util.Log
import com.example.edu.myapplication.base.BaseApplication
import com.example.edu.myapplication.weather.api.apixu.ApixuWeatherApiClient
import com.example.edu.myapplication.weather.model.InternalLocation
import com.example.edu.myapplication.weather.repository.WeatherRepository
import com.example.edu.myapplication.weather.repository.memory.MemoryWeatherRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by edu on 25/12/2017.
 */
@Singleton
class AddLocationInteractor @Inject constructor() {

    @Inject
    lateinit var weatherApiClient: ApixuWeatherApiClient
    @Inject
    lateinit var weatherRepository: MemoryWeatherRepository

    lateinit var addLocationViewModel: AddLocationViewModel

    init {
        BaseApplication.applicationComponent.inject(this)
    }

    fun getLocationClickedLambda(): (InternalLocation) -> Unit {
        return { location: InternalLocation ->
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
                            else                            -> Log.e("---", "-------------> WTF HAPPENED??? TRIED TO SAVE $locationName BUT FAILED: ${getLocationState.error}")
                        }
                    }
        }
    }

    fun getStateObservable(cityTextChanges: Observable<CharSequence>): Observable<AddLocationViewModel.Companion.SearchForCityState> {
        return cityTextChanges
                .debounce(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                // 1 - check text length and emit appropriate state
                .map { city ->
                    when {
                        city.length in 0..2 -> AddLocationViewModel.idle(city.toString())
                        city.length >= 3    -> AddLocationViewModel.searching(city.toString())
                        else                -> AddLocationViewModel.error(RuntimeException("WTF has just happened?"))
                    }
                }
                // 2 - update UI via live data
                .map { state ->
                    addLocationViewModel.searchForLocationStateLiveData.value = state
                    state
                }
                // 3 - handle errors gracefully until here
                .onErrorReturn { throwable ->
                    AddLocationViewModel.error(throwable)
                }
                // 4 - do not query server if we are "idle"
                .filter { state ->
                    !state.idle
                }
                // 5 - query server if everything went fine so far
                .map { state ->
                    weatherApiClient.searchForLocation(state.city)
                            .subscribeOn(Schedulers.io())
                            .map { returnedLocations ->
                                AddLocationViewModel.success(state.city, returnedLocations)
                            }
                            .onErrorReturn { throwable ->
                                AddLocationViewModel.error(throwable)
                            }
                            .blockingGet()
                }
    }

}