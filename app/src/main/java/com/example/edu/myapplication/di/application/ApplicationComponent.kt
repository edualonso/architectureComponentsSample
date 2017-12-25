package com.example.edu.myapplication.di.application

import com.example.edu.myapplication.base.BaseApplication
import com.example.edu.myapplication.di.activity.ActivityModule
import com.example.edu.myapplication.di.modules.DatabaseModule
import com.example.edu.myapplication.di.modules.NetworkModule
import com.example.edu.myapplication.weather.AddLocationInteractor
import com.example.edu.myapplication.weather.AddLocationViewModel
import com.example.edu.myapplication.weather.api.WeatherApiClient
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

/**
 * Created by edu on 24/12/2017.
 */
@Singleton
@Component(modules = arrayOf(
        AndroidInjectionModule::class,
        ApplicationModule::class,
        ActivityModule::class,
        DatabaseModule::class,
        NetworkModule::class
))
interface ApplicationComponent: AndroidInjector<BaseApplication> {

    fun inject(weatherApiClient: WeatherApiClient)
    fun inject(addLocationViewModel: AddLocationViewModel)      // TODO: can we do this more abstract?
    fun inject(addLocationInteractor: AddLocationInteractor)    // TODO: can we do this more abstract?

}