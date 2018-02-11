package com.example.edu.myapplication.weather.addlocation.search

import android.annotation.SuppressLint
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.edu.myapplication.R
import com.example.edu.myapplication.base.BaseApplication
import com.example.edu.myapplication.data.model.InternalLocation
import com.example.edu.myapplication.databinding.ViewLocationItemBinding
import com.example.edu.myapplication.weather.addlocation.AddLocationInteractor
import dagger.Reusable
import javax.inject.Inject

/**
 * Created by edu on 20/12/2017.
 */
@Reusable
class LocationAdapter @Inject constructor() : RecyclerView.Adapter<LocationAdapter.LocationViewHolder>() {

    private var locations = mutableListOf<InternalLocation>()

    lateinit var addLocationInteractor: AddLocationInteractor

    init {
        BaseApplication.applicationComponent.inject(this)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        with(holder.locationItemBinding) {
            root.setOnClickListener {
                onLocationClicked(locations[position])
            }
            val text = locations[position].name + ", ${locations[position].country} with ID ${locations[position].id}"
            name.text = text
        }
    }

    override fun getItemCount(): Int {
        return locations.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val binding: ViewLocationItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.view_location_item, parent, false)
        return LocationViewHolder(binding)
    }

    fun setLocations(locations: List<InternalLocation>?) {
        locations?.apply {
            this@LocationAdapter.locations.clear()
            this@LocationAdapter.locations.addAll(locations)

            // TODO: use diffutil maybe?
            notifyDataSetChanged()
        }
    }

    private fun onLocationClicked(location: InternalLocation) {
        addLocationInteractor.getLocation(location)
    }

    class LocationViewHolder(val locationItemBinding: ViewLocationItemBinding) : RecyclerView.ViewHolder(locationItemBinding.root)
}
