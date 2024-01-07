package agh.ics.oop.model.util;

public class CommonMethods {
    public static void checkIfNotNegative(int value) throws IllegalArgumentException{
        if (value < 0) {
            throw new IllegalArgumentException();
        };
    }

    public static void checkIfPositive(int value) throws IllegalArgumentException{
        if (value < 1) {
            throw new IllegalArgumentException();
        };
    }
}
