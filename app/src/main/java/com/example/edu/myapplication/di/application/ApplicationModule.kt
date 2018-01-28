package com.example.edu.myapplication.di.application

import android.content.Context
import android.content.res.AssetManager
import com.example.edu.myapplication.base.BaseApplication
import dagger.Module
import dagger.Provides

/**
 * Created by edu on 24/12/2017.
 */
@Module
class ApplicationModule {
    @Provides
    fun providesContext(baseApplication: BaseApplication): Context {
        return baseApplication.applicationContext
    }

    @Provides
    fun providesAssets(context: Context): AssetManager {
        return context.assets
    }
}