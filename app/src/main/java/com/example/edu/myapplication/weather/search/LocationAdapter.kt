package com.example.edu.myapplication.weather.search

import android.annotation.SuppressLint
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.edu.myapplication.R
import com.example.edu.myapplication.databinding.ViewLocationItemBinding
import com.example.edu.myapplication.weather.model.Location
import javax.inject.Inject

/**
 * Created by edu on 20/12/2017.
 */
class LocationAdapter @Inject constructor() : RecyclerView.Adapter<LocationAdapter.LocationViewHolder>() {

    private var locationClickedLambda: ((location: Location) -> Unit) = {}
    private var locations = mutableListOf<Location>()

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        with(holder.locationItemBinding) {
            root.setOnClickListener {
                locationClickedLambda(locations[position])
            }
            name.text = locations[position].name + " with ID ${locations[position].id}"
        }
    }

    override fun getItemCount(): Int {
        return locations.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val binding: ViewLocationItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.view_location_item, parent, false)
        return LocationViewHolder(binding)
    }

    fun setLocations(locations: List<Location>?) {
        locations?.apply {
            this@LocationAdapter.locations.clear()
            this@LocationAdapter.locations.addAll(locations)

            // TODO: use diffutil maybe?
            notifyDataSetChanged()
        }
    }

    fun setLocationClickedLambda(locationClickedLambda: ((location: Location) -> Unit)) {
        this.locationClickedLambda = locationClickedLambda
    }

    class LocationViewHolder(val locationItemBinding: ViewLocationItemBinding) : RecyclerView.ViewHolder(locationItemBinding.root)
}