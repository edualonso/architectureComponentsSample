package com.example.edu.myapplication.weather.addlocation

import android.arch.lifecycle.MutableLiveData
import com.example.edu.myapplication.data.model.InternalLocation
import com.example.edu.myapplication.weather.base.BaseApplication
import com.example.edu.myapplication.weather.base.BaseViewModel
import io.reactivex.Observable
import java.io.InputStream
import javax.inject.Inject

/**
 * Created by edu on 19/12/2017.
 */
class AddLocationViewModel : BaseViewModel() {

    @Inject
    lateinit var interactor: AddLocationInteractor

    var searchForLocationStateLiveData: MutableLiveData<SearchForCityState> = MutableLiveData()

    init {
        BaseApplication.applicationComponent.inject(this)

        searchForLocationStateLiveData.value = idle()

        // TODO: figure out how to inject viewmodels using dagger so that we can inject them in the interactor
        interactor.addLocationViewModel = this
    }

    fun observeCityState(cityTextChangesObservable: Observable<CharSequence>) {
        if (disposables.size() == 0) {
            disposables.add(interactor
                    .getStateObservable(cityTextChangesObservable)
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
     * Models the UI state for the search box.
     */
    companion object {

        class SearchForCityState(
                val idle: Boolean,
                val searching: Boolean,
                val success: Boolean,
                val error: Boolean,
                val city: String,
                val locations: List<InternalLocation> = ArrayList(),
                val throwable: Throwable? = null
        )

        fun idle(city: String = ""): SearchForCityState = SearchForCityState(true, false, false, false, city)
        fun searching(city: String): SearchForCityState = SearchForCityState(false, true, false, false, city)
        fun success(city: String, locations: List<InternalLocation>): SearchForCityState = SearchForCityState(false, false, true, false, city, locations)
        fun error(throwable: Throwable): SearchForCityState = SearchForCityState(false, false, false, true, "", arrayListOf(), throwable)
    }
}