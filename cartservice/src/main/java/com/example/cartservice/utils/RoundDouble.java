package com.example.cartservice.utils;

public class RoundDouble {
    
    public static double roundDouble(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long temp = Math.round(value);
        return (double) temp / factor;
    }
}
