package com.example.edu.myapplication.main

import android.content.Intent
import com.example.edu.myapplication.weather.addlocation.AddLocationActivity

/**
 * Created by Edu on 28/01/2018.
 */
class MainRouter(
        private val mainActivity: MainActivity
) {

    fun openOpenWeather() {
        openAddLocationActivity(AddLocationActivity.PROVIDER_OPENWEAHTER)
    }

    fun openApixu() {
        openAddLocationActivity(AddLocationActivity.PROVIDER_APIXU)
    }

    private fun openAddLocationActivity(provider: Int) {
        val intent = Intent(mainActivity, AddLocationActivity::class.java).apply {
            putExtra(AddLocationActivity.EXTRA_PROVIDER, provider)
        }
        mainActivity.startActivity(intent)
    }
}