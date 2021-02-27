package com.example.test.compass.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test.compass.azimuth.AzimuthResolver
import com.example.test.compass.common.update
import com.example.test.compass.common.updateValue
import com.example.test.compass.location.DistanceEvaluator
import com.example.test.compass.location.GpsLocationProvider
import com.example.test.compass.location.Location
import com.example.test.compass.sensor.PhoneRotationSensor
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

class CompassViewModel @Inject constructor(
    private val sensor: PhoneRotationSensor,
    private val locationProvider: GpsLocationProvider,
    private val azimuthResolver: AzimuthResolver,
    private val distanceEvaluator: DistanceEvaluator
) : ViewModel() {

    val viewState: LiveData<CompassViewState> = MutableLiveData(CompassViewState())
    val checkPermissionsEvent: LiveData<Boolean> = MutableLiveData()

    fun init() {
        sensor.onAngleUpdated = { angleUpdated(it) }
        locationProvider.onLocationUpdate = { currentLocationUpdated(it) }
        locationProvider.startUpdatingLocation()
    }

    fun setDesiredLocation(location: Location) {
        viewState.update {
            it.copy(locationViewEntity = it.locationViewEntity.copy(destinationLocation = location))
        }
        recalculateDistance()
    }

    fun switchChanged(active: Boolean) {
        viewState.update {
            it.copy(
                locationViewEntity = it.locationViewEntity.copy(
                    locationViewType = if (active) LocationViewType.CURRENT else LocationViewType.DESTINATION
                )
            )
        }
    }

    fun checkGpsStatus() {
        if (checkPermissionsEvent.value == null) {
            checkPermissionsEvent.updateValue(true)
        } else checkPermissionsEvent.update { !it }
    }

    private fun angleUpdated(angle: Int) {
        viewState.value?.let { compassViewState ->
            if (compassViewState.isCurrentLocationSet) {
                val destinationAzimuth = azimuthResolver.calculateAzimuth(compassViewState.currentLocation, compassViewState.destinationLocation).toInt()
                val angleDifference = destinationAzimuth - angle
                val compassAngle = when {
                    angleDifference == 0 -> {
                        compassViewState.compassAngle
                    }
                    angleDifference > 0 -> {
                        (0 + angleDifference).toFloat()
                    }
                    else -> {
                        (360 + angleDifference).toFloat()
                    }
                }
                viewState.update {
                    it.copy(compassAngle = compassAngle)
                }
            } else {
                viewState.update {
                    it.copy(compassAngle = 360 - angle.toFloat())
                }
            }
        }
    }

    private fun currentLocationUpdated(location: Location) {
        viewState.update {
            it.copy(
                locationViewEntity = it.locationViewEntity.copy(currentLocation = location)
            )
        }
        recalculateDistance()
    }

    private fun recalculateDistance() {
        viewState.update {
            it.copy(
                distance = distanceEvaluator.calculateDistance(it.currentLocation, it.destinationLocation)
            )
        }
    }
}

data class CompassViewState(
    val distance: BigDecimal = BigDecimal.ZERO,
    val compassAngle: Float = 0F,
    val locationViewEntity: LocationViewEntity = LocationViewEntity()
) {
    val currentLocation = locationViewEntity.currentLocation
    val destinationLocation = locationViewEntity.destinationLocation

    val isCurrentLocationSet: Boolean
        get() = currentLocation.latitude != BigDecimal.ZERO || currentLocation.longitude != BigDecimal.ZERO

    val isTurnRightVisible: Boolean
        get() = compassAngle < 180 && compassAngle > 20 && isCurrentLocationSet

    val isTurnLeftVisible: Boolean
        get() = compassAngle > 180 && compassAngle < 340 && isCurrentLocationSet

}