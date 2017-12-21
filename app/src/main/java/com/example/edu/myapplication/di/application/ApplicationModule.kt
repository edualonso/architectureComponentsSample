package com.example.edu.myapplication.di.application

import android.content.Context
import android.preference.PreferenceManager
import com.example.edu.myapplication.base.BaseApplication
import dagger.Module
import dagger.Provides

/**
 * Created by edu on 21/12/2017.
 */
@Module
class ApplicationModule {

    @Provides
    fun providesApplicationContext(applicationContext: BaseApplication): Context = applicationContext

    @Provides
    fun providesSharedPreferences(context: Context) = PreferenceManager.getDefaultSharedPreferences(context)

}