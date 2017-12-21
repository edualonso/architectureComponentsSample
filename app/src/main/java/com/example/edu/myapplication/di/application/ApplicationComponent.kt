package com.example.edu.myapplication.di.application

import com.example.edu.myapplication.base.BaseApplication
import com.example.edu.myapplication.di.activity.ActivityModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

/**
 * Created by edu on 21/12/2017.
 */
@Singleton
@Component(modules = arrayOf(
        AndroidInjectionModule::class,
        ActivityModule::class,
        ApplicationModule::class
))
interface ApplicationComponent : AndroidInjector<BaseApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(context: BaseApplication): Builder

        fun build(): ApplicationComponent
    }

}