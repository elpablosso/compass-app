package com.example.test.compass

import com.example.test.compass.azimuth.AzimuthResolver
import com.example.test.compass.location.Location
import junit.framework.Assert.assertEquals
import org.junit.Test

class AzimuthResolverTest {

    private var azimuthResolver = AzimuthResolver()

    @Test
    fun angleCalculation_ShouldBe0() {
        val from = Location(0.0, 0.0)
        val to = Location(0.0, 10.0)
        assertEquals(azimuthResolver.calculateAzimuth(from, to).toInt(), 0)
    }

    @Test
    fun angleCalculation_ShouldBe90() {
        val from = Location(0.0, 0.0)
        val to = Location(10.0, 0.0)
        assertEquals(azimuthResolver.calculateAzimuth(from, to).toInt(), 90)
    }

    @Test
    fun angleCalculation_ShouldBe180() {
        val from = Location(0.0, 0.0)
        val toRight = Location(0.0, -10.0)
        assertEquals(azimuthResolver.calculateAzimuth(from, toRight).toInt(), 180)
    }

    @Test
    fun angleCalculation_ShouldBe270() {
        val from = Location(0.0, 0.0)
        val toRight = Location(-10.0, 0.0)
        assertEquals(azimuthResolver.calculateAzimuth(from, toRight).toInt(), 270)
    }

    @Test
    fun angleCalculation_ShouldBe45() {
        val from = Location(0.0, 0.0)
        val to = Location(10.0, 10.0)
        assertEquals(azimuthResolver.calculateAzimuth(from, to).toInt(), 45)
    }

    @Test
    fun angleCalculation_ShouldBe135() {
        val from = Location(0.0, 0.0)
        val to = Location(10.0, -10.0)
        assertEquals(azimuthResolver.calculateAzimuth(from, to).toInt(), 135)
    }

    @Test
    fun angleCalculation_ShouldBe225() {
        val from = Location(0.0, 0.0)
        val to = Location(-10.0, -10.0)
        assertEquals(azimuthResolver.calculateAzimuth(from, to).toInt(), 225)
    }

    @Test
    fun angleCalculation_ShouldBe315() {
        val from = Location(0.0, 0.0)
        val to = Location(-10.0, 10.0)
        assertEquals(azimuthResolver.calculateAzimuth(from, to).toInt(), 315)
    }
}