package com.example.edu.myapplication.di.activity

import com.example.edu.myapplication.weather.WeatherActivity
import dagger.Subcomponent
import dagger.android.AndroidInjector

/**
 * Created by edu on 21/12/2017.
 */
@Subcomponent(modules = arrayOf(
        // TODO
))
interface WeatherActivitySubcomponent {

    @Subcomponent.Builder
    abstract class Builder: AndroidInjector<WeatherActivity>

}