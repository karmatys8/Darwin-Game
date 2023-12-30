package agh.ics.oop.model.util;

public class CommonMethods { // idk if they are necessary and if so should they be merged to one method: valueBiggerThan(value, min) ?
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
