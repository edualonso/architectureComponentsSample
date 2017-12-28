package com.example.edu.myapplication.weather.addlocation

import android.arch.lifecycle.MutableLiveData
import com.example.edu.myapplication.data.model.InternalLocation
import com.example.edu.myapplication.weather.base.BaseApplication
import com.example.edu.myapplication.weather.base.BaseViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.io.InputStream
import javax.inject.Inject

/**
 * Created by edu on 19/12/2017.
 */
class AddLocationViewModel : BaseViewModel() {

    @Inject
    lateinit var interactor: AddLocationInteractor

    var searchForLocationStateLiveData: MutableLiveData<SearchForCityState> = MutableLiveData()
    var loadCitiesStateLiveData: MutableLiveData<LoadCitiesState> = MutableLiveData()

    init {
        BaseApplication.applicationComponent.inject(this)

        searchForLocationStateLiveData.value = searchForCityIdle()
        loadCitiesStateLiveData.value = loadCitiesDone()

        // TODO: figure out how to inject viewmodels using dagger so that we can inject them in the interactor
        interactor.addLocationViewModel = this
    }

    fun observeCityState(cityTextChangesObservable: Observable<CharSequence>) {
        if (disposables.size() == 0) {
            disposables.add(interactor
                    .getSearchBoxStateObservable(cityTextChangesObservable)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { state ->
                        searchForLocationStateLiveData.value = state
                    }
            )
        }
    }

    fun setCityListInputStream(inputStream: InputStream) {
        interactor.cityListInputStream = inputStream
    }

    fun parseCities() {
        interactor.parseCities()
    }

    fun countCities() {
        interactor.countCities()
    }


    /**
     * State models for the UI.
     */
    companion object {

        class SearchForCityState(
                val idle: Boolean,
                val ongoing: Boolean,
                val success: Boolean,
                val error: Boolean,
                val city: String,
                val locations: List<InternalLocation> = ArrayList(),
                val throwable: Throwable? = null
        )

        fun searchForCityIdle(city: String = ""): SearchForCityState = SearchForCityState(true, false, false, false, city)
        fun searchForCityOngoing(city: String): SearchForCityState = SearchForCityState(false, true, false, false, city)
        fun searchForCitySuccess(city: String, locations: List<InternalLocation>): SearchForCityState = SearchForCityState(false, false, true, false, city, locations)
        fun searchForCityError(throwable: Throwable): SearchForCityState = SearchForCityState(false, false, false, true, "", arrayListOf(), throwable)

        class LoadCitiesState(
                val ongoing: Boolean,
                val done: Boolean,
                val error: Boolean,
                val throwable: Throwable? = null
        )

        fun loadCitiesOngoing(): LoadCitiesState = LoadCitiesState(true, false, false)
        fun loadCitiesDone(): LoadCitiesState = LoadCitiesState(false, true, false)
        fun loadCitiesError(throwable: Throwable): LoadCitiesState = LoadCitiesState(false, false, true, throwable)
    }
}