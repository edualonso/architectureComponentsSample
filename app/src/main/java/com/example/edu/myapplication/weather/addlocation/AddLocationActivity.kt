package com.example.edu.myapplication.weather.addlocation

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.example.edu.myapplication.R
import com.example.edu.myapplication.databinding.ActivityMainBinding
import com.example.edu.myapplication.weather.addlocation.search.LocationAdapter
import com.example.edu.myapplication.weather.base.BaseActivity
import com.jakewharton.rxbinding2.widget.RxTextView
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class AddLocationActivity : BaseActivity() {

    @Inject
    lateinit var locationAdapter: LocationAdapter
    @Inject
    lateinit var interactor: AddLocationInteractor

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: AddLocationViewModel        // TODO: inject this too

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        bindLiveData()
        setupRecyclerView()

        if (interactor.countCities() == 0L) {
            interactor.parseCities()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun bindLiveData() {
        viewModel = ViewModelProviders.of(this).get(AddLocationViewModel::class.java)
        viewModel.searchForLocationStateLiveData.observe(
                this,
                Observer {
                    it?.apply {
                        when {
                            it.success      -> {
                                message.text = "SUCCESS: ${it.city}"
                                locationAdapter.setLocations(it.locations)
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
        viewModel.setCityListInputStream(assets.open("city_list.json"))
        binding.viewModel = viewModel
    }

    private fun setupRecyclerView() {
        locationAdapter.setLocationClickedLambda(viewModel.interactor.getLocationClickedLambda())

        locationList.setHasFixedSize(true)
        locationList.layoutManager = LinearLayoutManager(this)
        locationList.adapter = locationAdapter
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