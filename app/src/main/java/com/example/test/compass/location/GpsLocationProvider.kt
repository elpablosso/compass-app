package com.example.test.compass.location
import android.content.Context
import android.location.Location as AndroidLocation
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

private const val LOCATION_UPDATE_INTERVAL_MS = 500L

class GpsLocationProvider @Inject constructor(private val context: Context) {

    var onLocationUpdate: (Location) -> Unit = {}

    private val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: AndroidLocation?) {
            location?.also {
                onLocationUpdate.invoke(
                    Location(
                        longitude = location.longitude,
                        latitude = location.latitude
                    )
                )
            }
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        }

        override fun onProviderEnabled(provider: String?) {
        }

        override fun onProviderDisabled(provider: String?) {

        }
    }

    fun startUpdatingLocation() {
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_UPDATE_INTERVAL_MS, 0F, locationListener)
        } catch (ex: SecurityException) {
            Log.e("Error", "Security error:${ex.message}")
        }
    }
}