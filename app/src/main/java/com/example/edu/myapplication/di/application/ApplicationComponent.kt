package com.example.edu.myapplication.di.application

import com.example.edu.myapplication.di.activity.ActivityModule
import com.example.edu.myapplication.di.modules.ConstModule
import com.example.edu.myapplication.di.modules.DatabaseModule
import com.example.edu.myapplication.di.modules.NetworkModule
import com.example.edu.myapplication.network.apixu.ApixuWeatherApiClient
import com.example.edu.myapplication.weather.addlocation.AddLocationInteractor
import com.example.edu.myapplication.weather.addlocation.AddLocationViewModel
import com.example.edu.myapplication.weather.addlocation.search.LocationAdapter
import com.example.edu.myapplication.weather.base.BaseApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton



/**
 * Created by edu on 24/12/2017.
 */
@Singleton
@Component(modules = arrayOf(
        AndroidInjectionModule::class,
        ApplicationBuildersModule::class,
        ApplicationModule::class,
        ActivityModule::class,
        DatabaseModule::class,
        NetworkModule::class,
        ConstModule::class
))
interface ApplicationComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun applicationContext(baseApplication: BaseApplication): Builder
        fun build(): ApplicationComponent
    }

    fun inject(baseApplication: BaseApplication)
    fun inject(weatherApiClient: ApixuWeatherApiClient)
    fun inject(addLocationViewModel: AddLocationViewModel)      // TODO: can we do this more abstract?
    fun inject(addLocationInteractor: AddLocationInteractor)    // TODO: can we do this more abstract?
    fun inject(locationAdapter: LocationAdapter)                // TODO: can we do this more abstract?

}