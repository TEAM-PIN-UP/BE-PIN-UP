package com.pinup.global.common;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class Formatter {

    public static double formatStarRating(double starRating) {
        return BigDecimal.valueOf(starRating).setScale(1, RoundingMode.HALF_UP).doubleValue();
    }

    public static String formatDistance(double distance) {
        return distance < 1
                ? Math.round(distance * 1000) + "m"
                : Math.round(distance) + "km";
    }
}
