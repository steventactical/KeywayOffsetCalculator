package com.example;

public class KeywayCalculator {

    // Method 1: From bottom of diameter to bottom of keyway
    public static double fromBottomOfDiameter(double diameter, double distanceFromBottom) {
        return (diameter / 2.0) - distanceFromBottom;
    }

    // Method 2: From flat across top of keyway to bottom of keyway
    public static double fromTopFlat(double depth, double width, double diameter) {
        double d = (width * width) / (4.0 * diameter);  // Step 1
        d = d + depth;                                  // Step 2
        return (diameter / 2.0) - d;                    // Step 3
    }

    // Method 3: From top of theoretical diameter (before cut) to bottom of keyway
    public static double fromTopOfDiameter(double diameter, double depth) {
        return (diameter / 2.0) - depth;
    }

}