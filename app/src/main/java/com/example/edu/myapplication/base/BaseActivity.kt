package com.example.edu.myapplication.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import dagger.android.AndroidInjection

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)

        super.onCreate(savedInstanceState)
    }

    /**
     * Use this method to bind any view observers to live data objects inside view models.
     */
    abstract fun bindLiveData()

}
