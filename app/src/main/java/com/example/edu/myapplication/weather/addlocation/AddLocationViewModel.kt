package com.example.edu.myapplication.weather.addlocation

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.edu.myapplication.base.BaseApplication
import com.example.edu.myapplication.base.BaseViewModel
import com.example.edu.myapplication.weather.addlocation.state.SearchForCityState
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by edu on 19/12/2017.
 */
class AddLocationViewModel(
        val interactor: AddLocationInteractor
) : BaseViewModel() {

    var searchForCityStateLiveData: MutableLiveData<SearchForCityState> = MutableLiveData()

    init {
        BaseApplication.applicationComponent.inject(this)

        searchForCityStateLiveData.value = SearchForCityState.Idle()
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
                                state !is SearchForCityState.Idle
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

    /**
     * Viewmodel factory. Needed to pass the interactor.
     */
    class Factory<I: AddLocationInteractor>(
            private val interactor: I
    ) : ViewModelProvider.Factory {
        override fun <V : ViewModel?> create(modelClass: Class<V>): V {
            return AddLocationViewModel(interactor) as V
        }
    }
}