package com.example.test.compass.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.example.test.compass.R
import com.example.test.compass.databinding.LayoutLocationBinding
import com.example.test.compass.location.Location
import com.google.android.material.card.MaterialCardView

class LocationView(context: Context, attrs: AttributeSet) : MaterialCardView(context, attrs) {

    private val binding = LayoutLocationBinding.inflate(LayoutInflater.from(context), this)

    init {
        cardElevation = 10F
        setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryBackground))
    }
}

@BindingAdapter("locationViewEntity")
fun setLocationViewType(view: LocationView, entity: LocationViewEntity) {
    LayoutLocationBinding.bind(view).run {
        if (entity.locationViewType == LocationViewType.CURRENT) {
            txtLatitude.text = entity.currentLocation.latitude.toString()
            txtLongitude.text = entity.currentLocation.longitude.toString()
            txtLabel.setText(R.string.l_current_location)
        } else {
            txtLatitude.text = entity.destinationLocation.latitude.toString()
            txtLongitude.text = entity.destinationLocation.longitude.toString()
            txtLabel.setText(R.string.l_destination_location)
        }
    }
}

data class LocationViewEntity(
    val currentLocation: Location = Location(),
    val destinationLocation: Location = Location(),
    val locationViewType: LocationViewType = LocationViewType.DESTINATION
)

enum class LocationViewType {
    CURRENT,
    DESTINATION
}