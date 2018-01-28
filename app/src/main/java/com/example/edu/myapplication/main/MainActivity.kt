package com.example.edu.myapplication.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import com.example.edu.myapplication.R
import com.example.edu.myapplication.base.BaseActivity
import com.example.edu.myapplication.databinding.ActivityMainBinding
import com.example.edu.myapplication.weather.addlocation.state.LoadCitiesState
import kotlinx.android.synthetic.main.activity_add_location.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initViewModel()
        if (viewModel.interactor.countCities() == 0L) {
            viewModel.parseCities()
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders
                .of(this, MainViewModel.Factory(MainRouter(this)))
                .get(MainViewModel::class.java)
        viewModel.loadCitiesStateLiveData.observe(this, loadCitiesStateObserver)
        binding.viewModel = viewModel
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