package agh.ics.oop.model.worldMaps;

import agh.ics.oop.model.movement.Vector2d;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GlobeTests {
    @Test
    public void testGlobe() {
        Globe globe1 = new Globe(20, 30, new PlantConfig(0, 2, 5),
                new AnimalConfig(18, 1, 3, 2, 0, 4, 2));
        Assertions.assertInstanceOf(Globe.class, globe1);

        Globe globe2 = new Globe(1000, 1, new PlantConfig(0, 0, 0),
                new AnimalConfig(Integer.MAX_VALUE, 0, 1000, 0, 0, 0, Integer.MAX_VALUE));
        Assertions.assertInstanceOf(Globe.class, globe2);


        Assertions.assertThrows(IllegalArgumentException.class, () -> new Globe(-2, 10, new PlantConfig(0, 0, 0),
                new AnimalConfig(Integer.MAX_VALUE, 0, 1000, 0, 0, 0, Integer.MAX_VALUE)));

        Assertions.assertThrows(IllegalArgumentException.class, () -> new Globe(400, 0, new PlantConfig(0, 0, 0),
                new AnimalConfig(8, 0, 1000, 0, 0, 0, 9)));
    }

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
        Assertions.assertFalse(globe.canMoveTo(new Vector2d(201 ,36)));
    }
}
