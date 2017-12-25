package com.example.edu.myapplication.weather

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.example.edu.myapplication.R
import com.example.edu.myapplication.base.BaseActivity
import com.example.edu.myapplication.weather.model.Location
import com.example.edu.myapplication.weather.repository.WeatherRepository
import com.example.edu.myapplication.weather.repository.memory.MemoryWeatherRepository
import com.jakewharton.rxbinding2.widget.RxTextView
import kotlinx.android.synthetic.main.activity_main.*

class WeatherActivity : BaseActivity<WeatherViewModel>() {

    lateinit var weatherRepository: WeatherRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        bindLiveData()
        setupRecyclerView()
        weatherRepository = MemoryWeatherRepository()
    }

    override fun bindLiveData() {
        viewModel = ViewModelProviders.of(this).get(WeatherViewModel::class.java)
        viewModel.cityStateLiveData.observe(
                this,
                Observer {
                    it?.apply {
                        when {
                            it.success      -> {
                                message.text = "SUCCESS: ${it.city}"
                                viewModel.setLocations(it.locations)
                            }
                            it.idle         -> message.text = "IDLE: ${it.city}"
                            it.searching    -> message.text = "SEARCHING: ${it.city}"
                            it.error        -> message.text = "ERROR: ${it.throwable!!.message}"
                            else            -> message.text = "WTF?!?!?!: ${it.city}"
                        }
                    }
                }
        )

        viewModel.observeCityState(RxTextView.textChanges(cityField))
    }

    private fun setupRecyclerView() {
        locationList.setHasFixedSize(true)
        locationList.layoutManager = LinearLayoutManager(this)
        locationList.adapter = viewModel.locationAdapter

        val goToLocation: (Location) -> Unit = { location: Location ->
            // TODO: extract to viewmodel or interactor
            if (weatherRepository.getLocation(location) == null) {
                Log.e("---", "-------------> LOCATION ${location.name} DID NOT EXIST")
                weatherRepository.saveLocation(location)
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
        }
        viewModel.locationAdapter.setGoToLocation(goToLocation)
    }
}
