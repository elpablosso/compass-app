package com.example.test.compass.location

import java.io.Serializable
import java.math.BigDecimal
import java.math.RoundingMode

private const val DEFAULT_LOCATION_SCALE = 4

class Location(
    val longitude: BigDecimal = BigDecimal.ZERO,
    val latitude: BigDecimal = BigDecimal.ZERO
) : Serializable {

    constructor(longitude: Double, latitude: Double) : this(
        BigDecimal.valueOf(longitude).setScale(DEFAULT_LOCATION_SCALE, RoundingMode.HALF_DOWN),
        BigDecimal.valueOf(latitude).setScale(DEFAULT_LOCATION_SCALE, RoundingMode.HALF_DOWN)
    )
}

