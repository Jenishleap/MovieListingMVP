package com.example.leapfrog.movielistingmvp.helper;


public class RatingCalculator {

    public static int NUM_OF_STARS = 5;

    public static Float calculateRatingNum(String rateStr) {
        Float rate = Float.parseFloat(rateStr);
        //rating num in 5
        Float finalRate = (rate / 10) * NUM_OF_STARS;
        String formatDecimal = String.format("%.1f", finalRate);
        Float finalFloat = Float.parseFloat(formatDecimal);
        return finalFloat;
    }

}
