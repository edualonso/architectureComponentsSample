package com.example.edu.myapplication.di.application

import com.example.edu.myapplication.weather.addlocation.AddLocationActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by edu on 10/01/2018.
 */
@Module
abstract class ApplicationBuildersModule {
    @ContributesAndroidInjector
    internal abstract fun contributesAddLocationActivityInjector(): AddLocationActivity
}