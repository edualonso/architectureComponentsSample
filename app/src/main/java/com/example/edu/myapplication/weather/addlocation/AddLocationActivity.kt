package com.example.edu.myapplication.weather.addlocation

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.example.edu.myapplication.R
import com.example.edu.myapplication.base.BaseActivity
import com.example.edu.myapplication.databinding.ActivityAddLocationBinding
import com.example.edu.myapplication.weather.addlocation.apixu.ApixuAddLocationInteractor
import com.example.edu.myapplication.weather.addlocation.openweather.OpenWeatherAddLocationInteractor
import com.example.edu.myapplication.weather.addlocation.search.LocationAdapter
import com.example.edu.myapplication.weather.addlocation.state.SearchForCityState
import com.jakewharton.rxbinding2.widget.RxTextView
import kotlinx.android.synthetic.main.activity_add_location.*

class AddLocationActivity : BaseActivity() {

    private lateinit var binding: ActivityAddLocationBinding
    private lateinit var viewModel: AddLocationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_location)

        initViewModel()
        initRecyclerView()
    }

    @SuppressLint("SetTextI18n")
    private fun initViewModel() {
        val factory = when (intent.getIntExtra(EXTRA_PROVIDER, PROVIDER_OPENWEAHTER)) {
            PROVIDER_APIXU          -> AddLocationViewModel.Factory(ApixuAddLocationInteractor())
            PROVIDER_OPENWEAHTER    -> AddLocationViewModel.Factory(OpenWeatherAddLocationInteractor())
            else                    -> throw AssertionError("Invalid provider")
        }

        viewModel = ViewModelProviders
                .of(this, factory)
                .get(AddLocationViewModel::class.java)

        viewModel.searchForCityStateLiveData.observe(this, searchForLocationsStateObserver)
        viewModel.observeCityState(RxTextView.textChanges(cityField))
        binding.viewModel = viewModel
    }

    private fun initRecyclerView() {
        locationList.setHasFixedSize(true)
        locationList.layoutManager = LinearLayoutManager(this)
        LocationAdapter().apply {
            addLocationInteractor = viewModel.interactor
            locationList.adapter = this
        }
    }

    private val searchForLocationsStateObserver = Observer<SearchForCityState> {
        if (it != null) {
            when (it) {
                is SearchForCityState.Idle      -> message.text = "IDLE: ${it.city}"
                is SearchForCityState.Ongoing   -> message.text = "SEARCHING: ${it.city}"
                is SearchForCityState.Error     -> message.text = "ERROR SEARCHING FOR LOCATION ${it.city}: ${it.throwable!!.message}"
                is SearchForCityState.Success   -> with (it) {
                    message.text = "SUCCESS: ${city}"
                    (locationList.adapter as LocationAdapter).setLocations(locations)
                }
            }
        }
    }

    companion object {
        const val EXTRA_PROVIDER = "EXTRA_PROVIDER"
        const val PROVIDER_APIXU = 0
        const val PROVIDER_OPENWEAHTER = 1
    }
}

//            weatherApiClient.getCurrentWeather(location)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(
//                            { currentWeather: CurrentWeather ->
//                                Log.e("---------", "-----------> GOT CURRENT WEATHER, LAST UPDATED: ${currentWeather.current.lastUpdated}")
//                            },
//                            { throwable: Throwable ->
//                                Log.e("---------", "-----------> ERROR: ${throwable.message}")
//                            }
//                    )