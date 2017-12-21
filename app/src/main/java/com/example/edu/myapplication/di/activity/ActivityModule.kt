package com.example.edu.myapplication.di.activity

import android.app.Activity
import com.example.edu.myapplication.weather.WeatherActivity
import dagger.Binds
import dagger.Module
import dagger.android.ActivityKey
import dagger.android.AndroidInjector
import dagger.multibindings.IntoMap



/**
 * Created by edu on 21/12/2017.
 */
@Module(subcomponents = arrayOf(
        WeatherActivitySubcomponent::class
))
abstract class ActivityModule {

    @Binds
    @IntoMap
    @ActivityKey(WeatherActivity::class)
    abstract fun bindWeatherActivityInjectorFactory(builder: WeatherActivitySubcomponent.Builder): AndroidInjector.Factory<out Activity>
}