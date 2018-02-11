package com.example.edu.myapplication.di.application

import com.example.edu.myapplication.base.BaseApplication
import com.example.edu.myapplication.di.activity.ActivityModule
import com.example.edu.myapplication.di.modules.ConstModule
import com.example.edu.myapplication.di.modules.DatabaseModule
import com.example.edu.myapplication.di.modules.NetworkModule
import com.example.edu.myapplication.main.MainInteractor
import com.example.edu.myapplication.main.MainViewModel
import com.example.edu.myapplication.network.apixu.ApixuWeatherApiClient
import com.example.edu.myapplication.network.openweather.OpenWeatherApiClient
import com.example.edu.myapplication.weather.addlocation.AddLocationInteractor
import com.example.edu.myapplication.weather.addlocation.AddLocationViewModel
import com.example.edu.myapplication.weather.addlocation.apixu.ApixuAddLocationInteractor
import com.example.edu.myapplication.weather.addlocation.openweather.OpenWeatherAddLocationInteractor
import com.example.edu.myapplication.weather.addlocation.search.LocationAdapter
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
abstract class ApplicationComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun applicationContext(baseApplication: BaseApplication): Builder
        fun build(): ApplicationComponent
    }

    abstract fun inject(baseApplication: BaseApplication)
    abstract fun inject(weatherApiClient: ApixuWeatherApiClient)
    abstract fun inject(openWeatherApiClient: OpenWeatherApiClient)
    abstract fun inject(mainViewModel: MainViewModel)
    abstract fun inject(addLocationViewModel: AddLocationViewModel)
    abstract fun inject(mainInteractor: MainInteractor)
    abstract fun inject(addLocationInteractor: AddLocationInteractor)
    abstract fun inject(apixuAddLocationInteractor: ApixuAddLocationInteractor)
    abstract fun inject(openWeatherAddLocationInteractor: OpenWeatherAddLocationInteractor)
    abstract fun inject(locationAdapter: LocationAdapter)

}