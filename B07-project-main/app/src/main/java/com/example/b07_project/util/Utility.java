package com.example.b07_project.util;

import android.annotation.SuppressLint;

public class Utility {
    /**
     * Returns string representation of price given its integer representation
     * @param price Integer representation of price
     * @return String representation of price
     */
    @SuppressLint("DefaultLocale")
    public static String priceIntToString(int price) {
        return String.format("$%d.%02d", price / 100, price % 100);
    }
}
