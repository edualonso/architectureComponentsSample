package com.example.edu.myapplication.weather.addlocation

import android.arch.lifecycle.MutableLiveData
import com.example.edu.myapplication.weather.addlocation.state.LoadCitiesState
import com.example.edu.myapplication.weather.addlocation.state.LoadCitiesState.Companion.loadCitiesDone
import com.example.edu.myapplication.weather.addlocation.state.SearchForCityState
import com.example.edu.myapplication.weather.addlocation.state.SearchForCityState.Companion.searchForCityIdle
import com.example.edu.myapplication.weather.base.BaseApplication
import com.example.edu.myapplication.weather.base.BaseViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
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

    fun parseCities() {
        interactor.parseCities()
    }

    fun countCities() {
        interactor.countCities()
    }
}