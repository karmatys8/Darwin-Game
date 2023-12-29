package agh.ics.oop.model.worldMaps;

import agh.ics.oop.model.movement.Vector2d;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

public class EquatorPositionsGenerator implements Iterable<Vector2d> {
    private int numberOfPlantsToAdd;
    private final int mapWidth;
    private final int mapHeight;

    public EquatorPositionsGenerator(int numberOfPlantsToAdd, int mapWidth, int mapHeight) {
        this.numberOfPlantsToAdd = numberOfPlantsToAdd;
        this.mapWidth = mapWidth; // not sure whether to check if width and height are positive
        this.mapHeight = mapHeight;
    }

    @Override
    public Iterator<Vector2d> iterator() {
        return new Iterator<>() {
            private final Random random = new Random();

            @Override
            public boolean hasNext() {
                return numberOfPlantsToAdd > 0;
            }

            @Override
            public Vector2d next() {
                if (! hasNext()) {
                    throw new NoSuchElementException();
                }
                numberOfPlantsToAdd--;

                int x = random.nextInt(mapWidth);
                boolean isInEquator = random.nextInt(5) != 0; // ~80% that plants are in the equator
                int y;
                if (isInEquator) {
                    y = (int) (0.4 * mapHeight + random.nextInt((int) (0.2 * mapHeight) + 1));// + 1 to prevent randInt(0)
                } else {
                    y = random.nextInt((int) (0.4 * mapHeight) + 1) // both parts outside the equator are 40% of the map height
                        + random.nextInt(2) * (int)(0.6 * mapHeight); // you either keep the previous value and get position from below the equator
                }                                                                // or add 0.6 height of map and get position from above the equator

                return new Vector2d(x, y);
            }
        };
    }
}
