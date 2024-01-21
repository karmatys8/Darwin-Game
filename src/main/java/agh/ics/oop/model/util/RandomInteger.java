package agh.ics.oop.model.util;

import java.util.Random;

public class RandomInteger {
    private static final Random random = new Random();

    public static int getRandomInt(int upperBound) {
        return random.nextInt(upperBound + 1);
    }

    public static int getRandomInt(int lowerBound, int upperBound) {
        if (lowerBound > upperBound) {
            throw new IllegalArgumentException("Min value must not be greater than upperBound value");
        }
        return random.nextInt(upperBound - lowerBound + 1) + lowerBound;
    }

    public static boolean getRandomBoolean() {
        return random.nextBoolean();
    }
}
