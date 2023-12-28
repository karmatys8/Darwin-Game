package agh.ics.oop.model.movement;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class MapDirectionTests {
    @Test
    public void getUnitVectorTests() {
        Assertions.assertEquals(new Vector2d(1, 1), MapDirection.NE.getUnitVector());
        Assertions.assertEquals(new Vector2d(1, 0), MapDirection.E.getUnitVector());
        Assertions.assertEquals(new Vector2d(1, -1), MapDirection.SE.getUnitVector());
        Assertions.assertEquals(new Vector2d(-1, 0), MapDirection.W.getUnitVector());
        Assertions.assertEquals(new Vector2d(-1, 1), MapDirection.NW.getUnitVector());
    }

    @Test
    public void toStringTests() {
        Assertions.assertEquals("N", MapDirection.N.toString());
        Assertions.assertEquals("E", MapDirection.E.toString());
        Assertions.assertEquals("SE", MapDirection.SE.toString());
        Assertions.assertEquals("SW", MapDirection.SW.toString());
        Assertions.assertEquals("W", MapDirection.W.toString());
    }

    @Test
    public void turnRightTests() {
        Assertions.assertEquals(MapDirection.NE, MapDirection.N.turnRight(1));
        Assertions.assertEquals(MapDirection.S, MapDirection.SE.turnRight(1));
        Assertions.assertEquals(MapDirection.N, MapDirection.NW.turnRight(1));
        Assertions.assertEquals(MapDirection.SW, MapDirection.E.turnRight(3));
        Assertions.assertEquals(MapDirection.N, MapDirection.W.turnRight(2));
        Assertions.assertEquals(MapDirection.S, MapDirection.SW.turnRight(7));
    }
}
