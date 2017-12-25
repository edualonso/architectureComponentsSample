package com.example.edu.myapplication.di.modules

import com.example.edu.myapplication.weather.api.ApiKeyInterceptor
import com.example.edu.myapplication.weather.api.WeatherService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named

/**
 * Created by edu on 24/12/2017.
 */
@Module
class NetworkModule {

    @Provides
    @Named("BASE_URL")
    fun providesBaseUrl(): String = "http://api.apixu.com/v1/"

    @Provides
    fun providesHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
        return loggingInterceptor
    }

    @Provides
    fun providesWeatherService(
            loggingInterceptor: HttpLoggingInterceptor,
            @Named("BASE_URL") baseUrl: String
    ): WeatherService {
        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(OkHttpClient.Builder()
                        .addInterceptor(loggingInterceptor)
                        .addInterceptor(ApiKeyInterceptor())
                        .build())
                .build()
                .create(WeatherService::class.java)
    }
}