package com.raz.utils;

public class Utils {
    public static int generateRandomInt(int min, int max) {
        return min + (int)(Math.random() * ((max - min) + 1));
    }

    private static boolean DEBUG_ON = false;
    public static void debug(Object str) {
        if(DEBUG_ON) {
            System.out.println(str);
        }
    }
}
