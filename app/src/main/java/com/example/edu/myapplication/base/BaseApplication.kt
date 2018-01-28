package com.example.edu.myapplication.base

import android.app.Activity
import android.support.multidex.MultiDexApplication
import com.example.edu.myapplication.di.application.ApplicationComponent
import com.example.edu.myapplication.di.application.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import io.realm.Realm
import io.realm.RealmConfiguration
import javax.inject.Inject

/**
 * Created by edu on 21/12/2017.
 */
class BaseApplication : MultiDexApplication(), HasActivityInjector {

    @Inject lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>
    @Inject lateinit var realmConfiguration: RealmConfiguration

    override fun onCreate() {
        super.onCreate()

        // initialise Realm before Dagger (or else generating a RealmConfiguration will make Realm crash
        Realm.init(this);

        applicationComponent = DaggerApplicationComponent
                .builder()
                .applicationContext(this)
                .build()
        applicationComponent.inject(this)

//        Realm.setDefaultConfiguration(realmConfiguration)
    }

    override fun activityInjector(): AndroidInjector<Activity> {
        return dispatchingActivityInjector
    }

    companion object {
        internal lateinit var applicationComponent: ApplicationComponent
    }
}