package agh.ics.oop.model.worldMaps;

import agh.ics.oop.model.movement.Vector2d;
import agh.ics.oop.model.util.RandomInteger;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class PlantPositionsGenerator implements Iterable<Vector2d> {
    private int numberOfPlantsToAdd;
    private final int mapWidth;
    private final int mapHeight;

    public PlantPositionsGenerator(int numberOfPlantsToAdd, int mapWidth, int mapHeight) {
        this.numberOfPlantsToAdd = numberOfPlantsToAdd;
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
    }

    @Override
    public Iterator<Vector2d> iterator() {
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return numberOfPlantsToAdd > 0;
            }

            @Override
            public Vector2d next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                numberOfPlantsToAdd--;

                int x = RandomInteger.getRandomInt(1, mapWidth);
                boolean isInEquator = RandomInteger.getRandomInt(4) != 0; // ~80% that plants are in the equator
                int y;
                if (isInEquator) {
                    y = RandomInteger.getRandomInt((int) (0.4 * mapHeight), (int) (0.6 * mapHeight));
                } else {
                    if (RandomInteger.getRandomBoolean()) {
                        y = RandomInteger.getRandomInt(1, (int) (0.4 * mapHeight));
                    } else {
                        y = RandomInteger.getRandomInt((int) (0.6 * mapHeight), mapHeight);
                    }
                }

                return new Vector2d(x, y);
            }
        };
    }
}
