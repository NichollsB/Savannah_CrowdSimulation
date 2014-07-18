package com.UKC_AICS.simulation.utils;

import java.util.Random;

/**
 * Created by james on 30/06/2014.
 */
public class MathsUtils {

        public static final float nanoToSec = 1 / 1000000000f;

    public static final Random rand = new Random();

    public static int randomNumber(int min, int max) {

        return rand.nextInt((max - min) + 1) + min;
    }

    public static float randomNumber(float min, float max) {

        return (float)rand.nextInt(((int)max - (int)min) + 1) + min;
    }
}
