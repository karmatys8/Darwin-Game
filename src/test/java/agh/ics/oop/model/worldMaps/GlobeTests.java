package agh.ics.oop.model.worldMaps;

import agh.ics.oop.model.animal.Animal;
import agh.ics.oop.model.movement.Vector2d;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GlobeTests {
    private static final Map<Vector2d, List<Animal>> animals = new HashMap<>();

    @Test
    public void testGlobe() {
        Globe globe1 = new Globe(20, 30, animals);
        Assertions.assertInstanceOf(Globe.class, globe1);

        Globe globe2 = new Globe(1000, 1, animals);
        Assertions.assertInstanceOf(Globe.class, globe2);


        Assertions.assertThrows(IllegalArgumentException.class, () -> new Globe(-2, 10, animals));

        Assertions.assertThrows(IllegalArgumentException.class, () -> new Globe(400, 0, animals));
    }

    @Test
    public void testCanMoveTo() {
        Globe globe = new Globe(36, 200, animals);

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
