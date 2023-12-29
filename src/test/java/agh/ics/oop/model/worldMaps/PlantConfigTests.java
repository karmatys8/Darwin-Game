package agh.ics.oop.model.worldMaps;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PlantConfigTests {
    @Test
    public void testPlantConfig() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new PlantConfig(-1, -6, 0));

        Assertions.assertThrows(IllegalArgumentException.class, () -> new PlantConfig(0, -20, 200000));

        Assertions.assertThrows(IllegalArgumentException.class, () -> new PlantConfig(0, 9, Integer.MIN_VALUE));
    }
}
