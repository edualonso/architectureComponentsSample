package com.example.edu.myapplication.weather

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import com.example.edu.myapplication.R
import com.example.edu.myapplication.base.BaseActivity
import com.jakewharton.rxbinding2.widget.RxTextView
import kotlinx.android.synthetic.main.activity_main.*

class WeatherActivity : BaseActivity<WeatherViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        bindLiveData(ViewModelProviders.of(this).get(WeatherViewModel::class.java))
    }

    override fun bindLiveData(viewModel: WeatherViewModel) {
        viewModel.cityStateLiveData.observe(
                this,
                Observer {
                    it?.apply {
                        when {
                            it.idle         -> message.text = "IDLE         : ${it.city}"
                            it.searching    -> message.text = "SEARCHING    : ${it.city}"
                            it.success      -> message.text = "DONE         : ${it.city}"
                            it.error        -> message.text = "ERROR        : ${it.throwable!!.message}"
                            else            -> message.text = "WTF?!?!?!    : ${it.city}"
                        }
                    }
                }
        )

        viewModel.observeCityState(RxTextView.textChanges(cityField))
    }
}
