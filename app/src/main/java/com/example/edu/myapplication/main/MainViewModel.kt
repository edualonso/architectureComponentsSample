package com.example.edu.myapplication.main

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.edu.myapplication.base.BaseApplication
import com.example.edu.myapplication.base.BaseViewModel
import com.example.edu.myapplication.weather.addlocation.state.LoadCitiesState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Edu on 28/01/2018.
 */
class MainViewModel constructor(
        private val router: MainRouter
) : BaseViewModel() {

    @Inject lateinit var interactor: MainInteractor

    var loadCitiesStateLiveData: MutableLiveData<LoadCitiesState> = MutableLiveData()

    init {
        BaseApplication.applicationComponent.inject(this)

        // default to having parsed the list of cities from OpenWeather
        loadCitiesStateLiveData.value = LoadCitiesState.loadCitiesDone()
    }

    fun openOpenWeather() {
        router.openOpenWeather()
    }

    fun openApixu() {
        router.openApixu()
    }

    fun parseCities() {
        loadCitiesStateLiveData.value = LoadCitiesState.loadCitiesOngoing()

        interactor.parseCities()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { throwable ->
                    loadCitiesStateLiveData.value = LoadCitiesState.loadCitiesError(throwable)
                }
                .onErrorComplete()
                .subscribe {
                    loadCitiesStateLiveData.value = LoadCitiesState.loadCitiesDone()
                }
    }

    /**
     * Viewmodel factory. Needed to pass the router.
     */
    class Factory(
            private val mainRouter: MainRouter
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MainViewModel(mainRouter) as T
        }
    }
}