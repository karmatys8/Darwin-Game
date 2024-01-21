package agh.ics.oop.model.worldMaps;

import agh.ics.oop.model.movement.MapDirection;
import agh.ics.oop.model.movement.Vector2d;
import agh.ics.oop.model.util.configs.AnimalConfig;
import agh.ics.oop.model.util.configs.PlantConfig;
import agh.ics.oop.model.animal.Animal;
import javafx.util.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TunnelsTests {
    private static final Map<Vector2d, List<Animal>> animals = new HashMap<>();
    private static final Plants plants = new Plants(8, 6);
    private static final AnimalConfig animalConfig = new AnimalConfig(0, 70, 40, 5, 2, 6, 9, "Full randomness");
    private static final PlantConfig plantConfig = new PlantConfig(0, 2, 0);

    private void countTunnels(int width, int height) {
        Tunnels tunnels = new Tunnels(width, height, animalConfig, plantConfig, animals, plants);

        int tunnelTravels = 0;
        for (int x = 1; x <= width; x++) {
            for (int y = 1; y <= height - 1; y++) {
                if (! Objects.equals(new Pair<>(new Vector2d(x, y + 1), 0),
                        tunnels.calculateNextPosition(new Vector2d(x, y), MapDirection.N))
                ) tunnelTravels++;
            }
        }

        for (int x = 1; x <= width; x++) {
            if (! Objects.equals(new Pair<>(new Vector2d(x, 1), 0),
                    tunnels.calculateNextPosition(new Vector2d(x, 2), MapDirection.S))
            ) tunnelTravels++;
        }

        Assertions.assertEquals((int)(width * height / 50) * 2, tunnelTravels);
    }

    @Test
    public void testTunnels() {
        for (int i = 0; i < 20; i++) {
            countTunnels(10, 10);
            countTunnels(7, 7);
            countTunnels(50, 50);
            countTunnels(1, 1);
            countTunnels(1, 201);
        }
    }

    @Test
    public void testHowToMove() {
        Tunnels tunnels = new Tunnels(4, 12, animalConfig, plantConfig, animals, plants);
        Vector2d lowerLeft = new Vector2d(1, 1);
        Vector2d upperRight = new Vector2d(4, 12);

        Assertions.assertEquals(new Pair<>(new Vector2d(2, 2), 0),
                tunnels.calculateNextPosition(new Vector2d(3, 1), MapDirection.NW));
        Assertions.assertEquals(new Pair<>(new Vector2d(3, 10), 0),
                tunnels.calculateNextPosition(new Vector2d(2, 10), MapDirection.E));

        Assertions.assertEquals(new Pair<>(lowerLeft, 0),
                tunnels.calculateNextPosition(new Vector2d(1, 2), MapDirection.S));
        Assertions.assertEquals(new Pair<>(new Vector2d(1, 12), 0),
                tunnels.calculateNextPosition(new Vector2d(2, 11), MapDirection.NW));
        Assertions.assertEquals(new Pair<>(upperRight, 0),
                tunnels.calculateNextPosition(new Vector2d(3, 11), MapDirection.NE));
        Assertions.assertEquals(new Pair<>(new Vector2d(4, 1), 0),
                tunnels.calculateNextPosition(new Vector2d(3, 1), MapDirection.E));

        Assertions.assertEquals(new Pair<>(new Vector2d(2, 1), 0),
                tunnels.calculateNextPosition(new Vector2d(2, 1), MapDirection.S));
        Assertions.assertEquals(new Pair<>(lowerLeft, 0),
                tunnels.calculateNextPosition(lowerLeft, MapDirection.SW));
        Assertions.assertEquals(new Pair<>(new Vector2d(4, 6), 0),
                tunnels.calculateNextPosition(new Vector2d(4, 6), MapDirection.NE));
        Assertions.assertEquals(new Pair<>(upperRight, 0),
                tunnels.calculateNextPosition(upperRight, MapDirection.NE));

        Assertions.assertEquals(new Pair<>(new Vector2d(1, 8), 0),
                tunnels.calculateNextPosition(new Vector2d(1, 8), MapDirection.SW));
        Assertions.assertEquals(new Pair<>(new Vector2d(1, 5), 0),
                tunnels.calculateNextPosition(new Vector2d(1, 5), MapDirection.W));
        Assertions.assertEquals(new Pair<>(new Vector2d(4, 7), 0),
                tunnels.calculateNextPosition(new Vector2d(4, 7), MapDirection.NE));
        Assertions.assertEquals(new Pair<>(new Vector2d(4, 4), 0),
                tunnels.calculateNextPosition(new Vector2d(4, 4), MapDirection.SE));
    }
}
