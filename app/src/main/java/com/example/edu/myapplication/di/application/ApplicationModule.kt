package com.example.edu.myapplication.di.application

import com.example.edu.myapplication.weather.WeatherActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by edu on 24/12/2017.
 */
@Module
abstract class ApplicationModule {
    @ContributesAndroidInjector
    internal abstract fun contributesWeatherActivityInjector(): WeatherActivity
}