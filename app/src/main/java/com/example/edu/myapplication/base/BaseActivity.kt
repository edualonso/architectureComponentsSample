package com.example.edu.myapplication.base

import android.arch.lifecycle.ViewModel
import android.support.v7.app.AppCompatActivity


abstract class BaseActivity<in T: ViewModel> : AppCompatActivity() {

    abstract fun bindLiveData(viewModel: T)

}
