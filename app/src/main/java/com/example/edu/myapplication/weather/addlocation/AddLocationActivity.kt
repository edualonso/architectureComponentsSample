package com.example.edu.myapplication.weather.addlocation

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.example.edu.myapplication.R
import com.example.edu.myapplication.base.BaseActivity
import com.example.edu.myapplication.databinding.ActivityAddLocationBinding
import com.example.edu.myapplication.weather.addlocation.search.LocationAdapter
import com.example.edu.myapplication.weather.addlocation.state.LoadCitiesState
import com.example.edu.myapplication.weather.addlocation.state.SearchForCityState
import com.jakewharton.rxbinding2.widget.RxTextView
import kotlinx.android.synthetic.main.activity_add_location.*
import javax.inject.Inject

class AddLocationActivity : BaseActivity() {

    @Inject lateinit var locationAdapter: LocationAdapter

    private lateinit var binding: ActivityAddLocationBinding
    private lateinit var viewModel: AddLocationViewModel        // TODO: inject this too

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_location)
        initViewModel()
        initRecyclerView()
    }

    @SuppressLint("SetTextI18n")
    private fun initViewModel() {
        viewModel = ViewModelProviders
                .of(this)
                .get(AddLocationViewModel::class.java)
        viewModel.searchForCityStateLiveData.observe(this, searchForLocationsStateObserver)
        viewModel.observeCityState(RxTextView.textChanges(cityField))
        binding.viewModel = viewModel
    }

    private fun initRecyclerView() {
        locationList.setHasFixedSize(true)
        locationList.layoutManager = LinearLayoutManager(this)
        locationList.adapter = locationAdapter
    }

    private val searchForLocationsStateObserver = Observer<SearchForCityState> {
        it?.apply {
            when {
                it.success  -> {
                    message.text = "SUCCESS: ${it.city}"
                    locationAdapter.setLocations(it.locations)
                }
                it.idle     -> message.text = "IDLE: ${it.city}"
                it.ongoing  -> message.text = "SEARCHING: ${it.city}"
                it.error    -> message.text = "ERROR SEARCHINF FOR LOCATION ${it.city}: ${it.throwable!!.message}"
                else        -> message.text = "WTF?!?!?! (SEARCHING FOR LOCATION): ${it.city}"
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