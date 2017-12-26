package com.example.edu.myapplication.di.application

import com.example.edu.myapplication.di.activity.ActivityModule
import com.example.edu.myapplication.di.modules.DatabaseModule
import com.example.edu.myapplication.di.modules.NetworkModule
import com.example.edu.myapplication.network.apixu.ApixuWeatherApiClient
import com.example.edu.myapplication.weather.addlocation.AddLocationInteractor
import com.example.edu.myapplication.weather.addlocation.AddLocationViewModel
import com.example.edu.myapplication.weather.base.BaseApplication
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

    fun inject(weatherApiClient: ApixuWeatherApiClient)
    fun inject(addLocationViewModel: AddLocationViewModel)      // TODO: can we do this more abstract?
    fun inject(addLocationInteractor: AddLocationInteractor)    // TODO: can we do this more abstract?

}