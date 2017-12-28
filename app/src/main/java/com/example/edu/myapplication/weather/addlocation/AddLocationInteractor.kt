package com.example.edu.myapplication.weather.addlocation

import android.util.Log
import com.example.edu.myapplication.data.model.InternalLocation
import com.example.edu.myapplication.data.model.openweather.Location
import com.example.edu.myapplication.data.repository.WeatherRepository
import com.example.edu.myapplication.data.repository.memory.MemoryWeatherRepository
import com.example.edu.myapplication.network.apixu.ApixuWeatherApiClient
import com.example.edu.myapplication.weather.base.BaseApplication
import com.google.gson.GsonBuilder
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
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
    lateinit var cityListInputStream: InputStream

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
                            else -> Log.e("---", "-------------> WTF HAPPENED??? TRIED TO SAVE $locationName BUT FAILED: ${getLocationState.error}")
                        }
                    }
        }
    }

    fun getSearchBoxStateObservable(cityTextChanges: Observable<CharSequence>): Observable<AddLocationViewModel.Companion.SearchForCityState> {
        return cityTextChanges
                .debounce(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                // 1 - check text length and emit appropriate state
                .map { city ->
                    when {
                        city.length in 0..2   -> AddLocationViewModel.searchForCityIdle(city.toString())
                        city.length >= 3      -> AddLocationViewModel.searchForCityOngoing(city.toString())
                        else                  -> AddLocationViewModel.searchForCityError(RuntimeException("WTF has just happened?"))
                    }
                }
                // 2 - update UI via live data
                .map { state ->
                    addLocationViewModel.searchForLocationStateLiveData.value = state
                    state
                }
                // 3 - handle errors gracefully until here
                .onErrorReturn { throwable ->
                    AddLocationViewModel.searchForCityError(throwable)
                }
                // 4 - do not query server if we are "searchForCityIdle"
                .filter { state ->
                    !state.idle
                }
                // 5 - query server if everything went fine so far
                .observeOn(Schedulers.io())
                .map { state ->
                    weatherApiClient.searchForLocation(state.city)
                            .map { returnedLocations ->
                                AddLocationViewModel.searchForCitySuccess(state.city, returnedLocations)
                            }
                            .onErrorReturn { throwable ->
                                AddLocationViewModel.searchForCityError(throwable)
                            }
                            .blockingGet()
                }
    }

    fun parseCities() {
        addLocationViewModel.loadCitiesStateLiveData.value = AddLocationViewModel.loadCitiesOngoing()

        Single
                // 1 - read file
                .fromCallable {
                    Log.e("---------------", "===========> ${Thread.currentThread().name}: FILE READING JOB STARTED")
                    val buffer = StringBuilder()
                    val bufferedReader = BufferedReader(InputStreamReader(cityListInputStream, "UTF-8"))
                    var jsonString: String

                    while (true) {
                        jsonString = bufferedReader.readLine() ?: break
                        buffer.append(jsonString)
                    }
                    cityListInputStream.close()
                    Log.e("---------------", "===========> ${Thread.currentThread().name}: FILE READING JOB FINISHED")

                    return@fromCallable buffer.toString()
                }
                // 2 - parse JSON
                .map { jsonString: String ->
                    Log.e("---------------", "===========> ${Thread.currentThread().name}: GSON JOB STARTED")
                    val gson = GsonBuilder().create()
                    val cities: List<Location> = gson.fromJson(jsonString, Array<Location>::class.java).toList()
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
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn { throwable ->
                    addLocationViewModel.loadCitiesStateLiveData.value = AddLocationViewModel.loadCitiesError(throwable)
                }
                .toCompletable()
                .subscribe {
                    addLocationViewModel.loadCitiesStateLiveData.value = AddLocationViewModel.loadCitiesDone()
                }
    }

    fun countCities(): Long {
        val numCities = Realm.getDefaultInstance().where(Location::class.java).count()
        Log.e("---------------", "===========> THERE ARE $numCities CITIES STORED IN THE DATABASE")
        return numCities
    }
}