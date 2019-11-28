package com.chepiv.utils;

import com.chepiv.model.City;

/**
 * Created by chepiv on 11/10/2019.
 * Contact: chepurin.ivan@gmail.com
 * Github:chepiv
 */
public class DistanceCalculator {

    public static double calculateDistance(City start, City end) {
//        return Math.hypot(start.getX()-end.getX(), start.getY()-end.getY());
        return Math.sqrt((start.getX()-end.getX()) * (start.getX()-end.getX()) + (start.getY()-end.getY()) * (start.getY()-end.getY()));
    }
}
