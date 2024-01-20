package agh.ics.oop.model.worldMaps;

import agh.ics.oop.model.movement.MapDirection;
import agh.ics.oop.model.worldElements.animal.Animal;
import agh.ics.oop.model.movement.Vector2d;
import agh.ics.oop.model.util.configs.AnimalConfig;
import agh.ics.oop.model.util.configs.PlantConfig;
import javafx.util.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GlobeTests {
    private static final Map<Vector2d, List<Animal>> animals = new HashMap<>();
    private static final Plants plants = new Plants(1, 1);

    @Test
    public void testHowToMove() {
        Globe globe = new Globe(36, 200,
                new AnimalConfig(8, 9, 93, 0, 0, 0, 2, "Swap"),
                new PlantConfig(0, 22, 51), animals, plants);
        Vector2d lowerLeft = new Vector2d(1, 1);
        Vector2d upperRight = new Vector2d(36, 200);

        Assertions.assertEquals(new Pair<>(new Vector2d(10, 19), 0),
                globe.howToMove(new Vector2d(10, 18), MapDirection.N));
        Assertions.assertEquals(new Pair<>(new Vector2d(15, 15), 0),
                globe.howToMove(new Vector2d(14, 16), MapDirection.SE));

        Assertions.assertEquals(new Pair<>(lowerLeft, 0),
                globe.howToMove(new Vector2d(1, 2), MapDirection.S));
        Assertions.assertEquals(new Pair<>(new Vector2d(1, 200), 0),
                globe.howToMove(new Vector2d(2, 199), MapDirection.NW));
        Assertions.assertEquals(new Pair<>(upperRight, 0),
                globe.howToMove(new Vector2d(35, 199), MapDirection.NE));
        Assertions.assertEquals(new Pair<>(new Vector2d(36, 1), 0),
                globe.howToMove(new Vector2d(35, 1), MapDirection.E));

        Assertions.assertEquals(new Pair<>(new Vector2d(10, 1), 4),
                globe.howToMove(new Vector2d(10, 1), MapDirection.S));
        Assertions.assertEquals(new Pair<>(lowerLeft, 4),
                globe.howToMove(lowerLeft, MapDirection.SW));
        Assertions.assertEquals(new Pair<>(upperRight, 4),
                globe.howToMove(upperRight, MapDirection.NE));

        Assertions.assertEquals(new Pair<>(new Vector2d(1, 10), 0),
                globe.howToMove(new Vector2d(36, 9), MapDirection.NE));
        Assertions.assertEquals(new Pair<>(new Vector2d(1, 52), 0),
                globe.howToMove(new Vector2d(36, 52), MapDirection.E));

        Assertions.assertEquals(new Pair<>(new Vector2d(36, 9), 0),
                globe.howToMove(new Vector2d(1, 8), MapDirection.NW));
        Assertions.assertEquals(new Pair<>(new Vector2d(36, 2), 0),
                globe.howToMove(lowerLeft, MapDirection.NW));
    }
}
