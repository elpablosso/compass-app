package com.example.test.compass.azimuth

import com.example.test.compass.location.Location
import kotlin.math.abs
import kotlin.math.atan

class AzimuthResolver {

    fun calculateAzimuth(currentLocation: Location, destinationLocation: Location): Double {
        val deltaLat = currentLocation.latitude.toDouble() - destinationLocation.latitude.toDouble()
        val deltaLong = currentLocation.longitude.toDouble() - destinationLocation.longitude.toDouble()

        if (deltaLat == 0.0 && deltaLong == 0.0) return 0.0

        if (deltaLat == 0.0) {
            if (deltaLong < 0) return 90.0
            if (deltaLong > 0) return 270.0
        }

        if (deltaLong == 0.0) {
            if (deltaLat < 0) return 0.0
            if (deltaLat > 0) return 180.0
        }

        val degreesQuarterAngle = Math.toDegrees(atan(abs(deltaLat) / abs(deltaLong)))
        findCoordinateSystemQuarter(deltaLong, deltaLat).also { quarter ->
            return when (quarter) {
                CartesianCoordinateQuarter.I -> degreesQuarterAngle
                CartesianCoordinateQuarter.II -> degreesQuarterAngle + 90
                CartesianCoordinateQuarter.III -> degreesQuarterAngle + 180
                CartesianCoordinateQuarter.IV -> degreesQuarterAngle + 270
            }
        }
    }

    private fun findCoordinateSystemQuarter(deltaLong: Double, deltaLat: Double): CartesianCoordinateQuarter = when {
        (deltaLong < 0 && deltaLat > 0) -> CartesianCoordinateQuarter.II
        (deltaLong > 0 && deltaLat > 0) -> CartesianCoordinateQuarter.III
        (deltaLong > 0 && deltaLat < 0) -> CartesianCoordinateQuarter.IV
        else -> CartesianCoordinateQuarter.I
    }
}

enum class CartesianCoordinateQuarter {
    I,
    II,
    III,
    IV
}