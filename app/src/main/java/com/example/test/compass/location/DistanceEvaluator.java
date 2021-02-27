package com.example.test.compass.location;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.inject.Inject;


public class DistanceEvaluator {

    private final double APPROXIMATE_DEGREE_TO_KM_RATIO = 111;

    @Inject
    public DistanceEvaluator() {
    }

    public BigDecimal calculateDistance(Location from, Location to) {
        final double deltaX = Math.abs(from.getLatitude().doubleValue()) - to.getLatitude().doubleValue();
        final double deltaY = Math.abs(from.getLatitude().doubleValue()) - to.getLatitude().doubleValue();

        final double distance = Math.sqrt((deltaX * deltaX) + (deltaY * deltaY)) * APPROXIMATE_DEGREE_TO_KM_RATIO;

        return BigDecimal.valueOf(distance).setScale(4, RoundingMode.HALF_DOWN);
    }
}
