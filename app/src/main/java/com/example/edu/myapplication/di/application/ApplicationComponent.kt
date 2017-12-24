package com.example.edu.myapplication.di.application

import com.example.edu.myapplication.base.BaseApplication
import com.example.edu.myapplication.di.activity.ActivityModule
import com.example.edu.myapplication.di.modules.DatabaseModule
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector

/**
 * Created by edu on 24/12/2017.
 */
@Component(modules = arrayOf(
        AndroidInjectionModule::class,
        ApplicationModule::class,
        ActivityModule::class,
        DatabaseModule::class
))
interface ApplicationComponent: AndroidInjector<BaseApplication>