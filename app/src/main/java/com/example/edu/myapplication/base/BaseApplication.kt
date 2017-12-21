package com.example.edu.myapplication.base

import android.app.Activity
import android.app.Application
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import io.realm.Realm
import io.realm.RealmConfiguration
import javax.inject.Inject



/**
 * Created by edu on 21/12/2017.
 */
class BaseApplication : Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var realmConfiguration: RealmConfiguration

    override fun onCreate() {
        super.onCreate()

        DaggerApplicationComponent.create()
                .inject(this)

        Realm.init(this);
        Realm.setDefaultConfiguration(realmConfiguration)
    }

    override fun activityInjector(): AndroidInjector<Activity> {
        return dispatchingActivityInjector
    }
}