package com.example.test.compass.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.example.test.compass.R
import com.example.test.compass.databinding.LayoutDistanceLocationBinding
import com.google.android.material.card.MaterialCardView
import java.math.BigDecimal

class DistanceView(context: Context, attributeSet: AttributeSet) : MaterialCardView(context, attributeSet) {

    var onSwitchChange: (Boolean) -> Unit = {}

    private val binding = LayoutDistanceLocationBinding.inflate(LayoutInflater.from(context), this, true)

    var distance: BigDecimal = BigDecimal.ZERO
        set(value) {
            field = value
            binding.txtKmLabel.text = context.getString(R.string.l_distance_km, value)
        }

    init {
        cardElevation = 10F
        setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryBackground))
        binding.switchUnits.setOnCheckedChangeListener { _, isChecked ->
            onSwitchChange(isChecked)
        }
    }

}
