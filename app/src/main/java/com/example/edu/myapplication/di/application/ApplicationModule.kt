package com.example.edu.myapplication.di.application

import com.example.edu.myapplication.weather.addlocation.AddLocationActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by edu on 24/12/2017.
 */
@Module
abstract class ApplicationModule {
    @ContributesAndroidInjector
    internal abstract fun contributesAddLocationActivityInjector(): AddLocationActivity
}