package agh.ics.oop.model.util.configs;

import agh.ics.oop.model.util.configs.AnimalConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AnimalConfigTests {
    @Test
    public void testAnimalConfig() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new AnimalConfig(Integer.MIN_VALUE, 20, 10, 70, 70, 70, 1));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new AnimalConfig(13, -4, 1000, 0, 0, 0, 11));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new AnimalConfig(1, 80, 4, -5, 0, 10, 98));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new AnimalConfig(1, Integer.MAX_VALUE, 9, 3, 3, 2, 10));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new AnimalConfig(1, Integer.MAX_VALUE, 9, 3, 3, 200, 0));
    }
}
