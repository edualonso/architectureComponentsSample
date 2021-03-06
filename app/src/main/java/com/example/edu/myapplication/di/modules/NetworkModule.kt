package com.example.edu.myapplication.di.modules

import com.example.edu.myapplication.BuildConfig
import com.example.edu.myapplication.network.ApiKeyInterceptor
import com.example.edu.myapplication.network.apixu.ApixuApiKeyInterceptor
import com.example.edu.myapplication.network.apixu.ApixuWeatherService
import com.example.edu.myapplication.network.openweather.OpenWeatherApiKeyInterceptor
import com.example.edu.myapplication.network.openweather.OpenWeatherService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by edu on 24/12/2017.
 */
@Module
class NetworkModule {

    //--------------------------------------------------------------------------------
    // Interceptors
    //--------------------------------------------------------------------------------

    @Provides
    @Singleton
    fun providesHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        when {
            BuildConfig.DEBUG -> loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            else -> loggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
        }
        return loggingInterceptor
    }

    //--------------------------------------------------------------------------------
    // Services
    //--------------------------------------------------------------------------------

    @Provides
    @Singleton
    fun providesApixuWeatherService(
            @Named(APIXU_BASE_URL) apixuBaseUrl: String,
            loggingInterceptor: HttpLoggingInterceptor,
            apixuApiKeyInterceptor: ApixuApiKeyInterceptor
    ): ApixuWeatherService {
        return getRetrofit(apixuBaseUrl, loggingInterceptor, apixuApiKeyInterceptor)
                .create(ApixuWeatherService::class.java)
    }

    @Provides
    @Singleton
    fun providesOpenWeatherService(
            @Named(APIXU_BASE_URL) openWeatherBaseUrl: String,
            loggingInterceptor: HttpLoggingInterceptor,
            openWeatherApiKeyInterceptor: OpenWeatherApiKeyInterceptor
    ): OpenWeatherService {
        return getRetrofit(openWeatherBaseUrl, loggingInterceptor, openWeatherApiKeyInterceptor)
                .create(OpenWeatherService::class.java)
    }

    private fun <T : ApiKeyInterceptor> getRetrofit(
            baseUrl: String,
            loggingInterceptor: HttpLoggingInterceptor,
            apiKeyInterceptor: T
    ): Retrofit {
        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(OkHttpClient.Builder()
                        .addInterceptor(loggingInterceptor)
                        .addInterceptor(apiKeyInterceptor)
                        .build())
                .build()
    }

    //--------------------------------------------------------------------------------
    // Other
    //--------------------------------------------------------------------------------

    @Provides
    @Named(APIXU_BASE_URL)
    fun providesApixuBaseUrl(): String = "http://api.apixu.com/v1/"

    @Provides
    @Named(APIXU_API_KEY)
    fun providesApixuApiKey(): String = "11682c59698444f6b59160534171912"

    @Provides
    @Named(OPENWEATHER_BASE_URL)
    fun providesOpenweatherBaseUrl(): String = "http://api.openweathermap.org/data/2.5/"

    @Provides
    @Named(OPENWEATHER_API_KEY)
    fun providesOpenWeatherApiKey(): String = "75805b09ea06260c9eb71391b785f444"

    companion object {
        const val APIXU_BASE_URL = "APIXU_BASE_URL"
        const val APIXU_API_KEY = "key"

        const val OPENWEATHER_BASE_URL = "OPENWEATHER_BASE_URL"
        const val OPENWEATHER_API_KEY = "appid"
    }
}