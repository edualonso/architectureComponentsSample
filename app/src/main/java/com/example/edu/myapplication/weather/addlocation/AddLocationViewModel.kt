package com.example.edu.myapplication.weather.addlocation

import android.arch.lifecycle.MutableLiveData
import com.example.edu.myapplication.base.BaseApplication
import com.example.edu.myapplication.base.BaseViewModel
import com.example.edu.myapplication.weather.addlocation.state.SearchForCityState
import com.example.edu.myapplication.weather.addlocation.state.SearchForCityState.Companion.searchForCityIdle
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by edu on 19/12/2017.
 */
class AddLocationViewModel : BaseViewModel() {

    @Inject lateinit var interactor: AddLocationInteractor

    var searchForCityStateLiveData: MutableLiveData<SearchForCityState> = MutableLiveData()

    init {
        BaseApplication.applicationComponent.inject(this)

        searchForCityStateLiveData.value = searchForCityIdle()
    }

    fun observeCityState(cityTextChangesObservable: Observable<CharSequence>) {
        if (disposables.size() == 0) {
            disposables.add(
                    interactor.getSearchBoxStateObservable(cityTextChangesObservable)
                            .doOnNext { state ->
                                searchForCityStateLiveData.value = state
                            }
                            // do not query server if we are "searchForCityIdle"
                            .filter { state ->
                                !state.idle
                            }
                            // query server if everything went fine so far
                            .observeOn(Schedulers.io())
                            .map { state ->
                                interactor.getLocationWithState(state)
                            }
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe { state ->
                                searchForCityStateLiveData.value = state
                            }
            )
        }
    }
}