package com.example.edu.myapplication.weather.addlocation.apixu

import com.example.edu.myapplication.base.BaseApplication
import com.example.edu.myapplication.weather.addlocation.AddLocationInteractor

/**
 * Created by edu on 04/02/2018.
 */
class ApixuAddLocationInteractor : AddLocationInteractor() {

    init {
        BaseApplication.applicationComponent.inject(this)
    }

}