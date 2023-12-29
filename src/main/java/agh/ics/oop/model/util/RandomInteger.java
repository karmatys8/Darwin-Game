package agh.ics.oop.model.util;

import java.util.Random;

public class RandomInteger {
    private static final Random random = new Random();

    public static int getRandomInt(int bound) {
        return random.nextInt(bound);
    }

    public static int getRandomInt(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("Min value must be less than max value");
        }
        return random.nextInt(max - min + 1) + min;
    }

    public static boolean getRandomBoolean() {
        return random.nextBoolean();
    }
}