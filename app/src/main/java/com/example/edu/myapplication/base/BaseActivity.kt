package com.example.edu.myapplication.base

import android.arch.lifecycle.ViewModel
import android.support.v7.app.AppCompatActivity


abstract class BaseActivity<T: ViewModel> : AppCompatActivity() {

    protected lateinit var viewModel: T

    abstract fun bindLiveData()

}
