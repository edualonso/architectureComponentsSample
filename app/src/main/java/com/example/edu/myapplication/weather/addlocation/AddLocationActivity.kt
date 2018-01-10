package com.example.edu.myapplication.weather.addlocation

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.example.edu.myapplication.R
import com.example.edu.myapplication.databinding.ActivityMainBinding
import com.example.edu.myapplication.weather.addlocation.search.LocationAdapter
import com.example.edu.myapplication.weather.addlocation.state.LoadCitiesState
import com.example.edu.myapplication.weather.addlocation.state.SearchForCityState
import com.example.edu.myapplication.weather.base.BaseActivity
import com.jakewharton.rxbinding2.widget.RxTextView
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class AddLocationActivity : BaseActivity() {

    @Inject
    lateinit var locationAdapter: LocationAdapter

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: AddLocationViewModel        // TODO: inject this too

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        bindLiveData()
        setupRecyclerView()

        if (viewModel.interactor.countCities() == 0L) {
            viewModel.interactor.parseCities()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun bindLiveData() {
        viewModel = ViewModelProviders.of(this).get(AddLocationViewModel::class.java)

        viewModel.searchForLocationStateLiveData.observe(this, searchForLocationsStateObserver)
        viewModel.loadCitiesStateLiveData.observe(this, loadCitiesStateObserver)

        viewModel.observeCityState(RxTextView.textChanges(cityField))
        binding.viewModel = viewModel
    }

    private fun setupRecyclerView() {
        locationAdapter.setOnLocationClickedLambda(viewModel.interactor.getLocationClickedLambda())

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

    private val loadCitiesStateObserver = Observer<LoadCitiesState> {
        it?.apply {
            when {
                it.ongoing  -> progressContainer.visibility = View.VISIBLE
                it.done     -> progressContainer.visibility = View.GONE
                it.error    -> {
                    progressContainer.visibility = View.GONE
                    message.text = "ERROR LOADING CITIES: (${it.throwable?.message})"
                }
                else        -> {
                    progressContainer.visibility = View.GONE
                    message.text = "WTF?!?!?! (LOADING CITIES)"
                }
            }
        }
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