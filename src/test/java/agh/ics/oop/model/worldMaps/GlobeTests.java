package agh.ics.oop.model.worldMaps;

import agh.ics.oop.model.movement.Vector2d;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GlobeTests {
    @Test
    public void constructorTests() {
        Globe globe1 = new Globe(20, 30, 2, 5, 18, 1,
                3, 2, 0, 4, 2);
        Assertions.assertInstanceOf(Globe.class, globe1);

        Globe globe2 = new Globe(1000, 1, 0, 0, Integer.MAX_VALUE, 0,
                1000, 0, 0, 0, Integer.MAX_VALUE);
        Assertions.assertInstanceOf(Globe.class, globe2);


        Assertions.assertThrows(IllegalArgumentException.class, () -> new Globe(-2, 10, 0,
                0, Integer.MAX_VALUE, 0, 1000, 0,
                0, 0, Integer.MAX_VALUE));

        Assertions.assertThrows(IllegalArgumentException.class, () -> new Globe(400, 0, 0,
                0, 8, 0, 1000, 0,
                0, 0, 9));

        Assertions.assertThrows(IllegalArgumentException.class, () -> new Globe(45, 100, -6,
                0, 13, 0, 1000, 0,
                0, 0, 11));

        Assertions.assertThrows(IllegalArgumentException.class, () -> new Globe(2, 120, 20,
                200000, Integer.MIN_VALUE, 20, 10, 70,
                70, 70, 1));

        Assertions.assertThrows(IllegalArgumentException.class, () -> new Globe(25, 20, 9,
                0, 1, Integer.MAX_VALUE, 9, 3,
                3, 2, 10));

        Assertions.assertThrows(IllegalArgumentException.class, () -> new Globe(25, 20, 9,
                0, 1, Integer.MAX_VALUE, 9, 3,
                3, 200, 0));
    }

    @Test
    public void canMoveToTests() {
        Globe globe = new Globe(36, 200, 22, 51, 8, 9,
                93, 0, 0, 0, 2);

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
