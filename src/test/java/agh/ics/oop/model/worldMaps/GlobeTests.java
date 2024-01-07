package agh.ics.oop.model.worldMaps;

import agh.ics.oop.model.movement.Vector2d;
import agh.ics.oop.model.util.configs.AnimalConfig;
import agh.ics.oop.model.util.configs.PlantConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GlobeTests {
    @Test
    public void testCanMoveTo() {
        Globe globe = new Globe(36, 200, new PlantConfig(0, 22, 51),
                new AnimalConfig(8, 9, 93, 0, 0, 0, 2));
        Assertions.assertTrue(globe.canMoveTo(new Vector2d(10, 19)));
        Assertions.assertTrue(globe.canMoveTo(new Vector2d(15, 15)));

        Assertions.assertTrue(globe.canMoveTo(new Vector2d(1, 1)));
        Assertions.assertTrue(globe.canMoveTo(new Vector2d(1, 200)));
        Assertions.assertTrue(globe.canMoveTo(new Vector2d(36, 200)));
        Assertions.assertTrue(globe.canMoveTo(new Vector2d(36, 1)));

        Assertions.assertFalse(globe.canMoveTo(new Vector2d(0 ,0)));
        Assertions.assertFalse(globe.canMoveTo(new Vector2d(10 ,-1)));
        Assertions.assertFalse(globe.canMoveTo(new Vector2d(10 ,Integer.MAX_VALUE)));
        Assertions.assertFalse(globe.canMoveTo(new Vector2d(10 ,Integer.MIN_VALUE)));
        Assertions.assertFalse(globe.canMoveTo(new Vector2d(Integer.MAX_VALUE ,Integer.MIN_VALUE)));
        Assertions.assertFalse(globe.canMoveTo(new Vector2d(200 ,36)));
    }
}
