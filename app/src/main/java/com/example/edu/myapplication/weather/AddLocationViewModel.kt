package com.example.edu.myapplication.weather

import android.arch.lifecycle.MutableLiveData
import com.example.edu.myapplication.base.BaseApplication
import com.example.edu.myapplication.base.BaseViewModel
import com.example.edu.myapplication.weather.model.Location
import io.reactivex.Observable
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
                val locations: List<Location> = ArrayList(),
                val throwable: Throwable? = null
        )

        fun idle(city: String = ""): SearchForCityState = SearchForCityState(true, false, false, false, city)
        fun searching(city: String): SearchForCityState = SearchForCityState(false, true, false, false, city)
        fun success(city: String, locations: List<Location>): SearchForCityState = SearchForCityState(false, false, true, false, city, locations)
        fun error(throwable: Throwable): SearchForCityState = SearchForCityState(false, false, false, true, "", arrayListOf(), throwable)
    }
}