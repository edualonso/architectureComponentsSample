package com.example.edu.myapplication.weather

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.example.edu.myapplication.R
import com.example.edu.myapplication.base.BaseActivity
import com.example.edu.myapplication.weather.search.LocationAdapter
import com.jakewharton.rxbinding2.widget.RxTextView
import kotlinx.android.synthetic.main.activity_main.*

class WeatherActivity : BaseActivity<WeatherViewModel>() {

    lateinit var adapter: LocationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        bindLiveData(ViewModelProviders.of(this).get(WeatherViewModel::class.java))
        setupRecyclerView()
    }

    override fun bindLiveData(viewModel: WeatherViewModel) {
        viewModel.cityStateLiveData.observe(
                this,
                Observer {
                    it?.apply {
                        when {
                            it.success      -> {
                                message.text = "SUCCESS: ${it.city}"
                                adapter.setLocations(it.locations)
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
        adapter = LocationAdapter()
        locationList.setHasFixedSize(true)
        locationList.layoutManager = LinearLayoutManager(this@WeatherActivity)
        locationList.adapter = adapter
    }
}
