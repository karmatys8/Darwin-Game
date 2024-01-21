package agh.ics.oop.model.worldMaps;

import agh.ics.oop.model.movement.Vector2d;
import agh.ics.oop.model.util.RandomInteger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PlantsTests {
    private void wereConstructed(int width, int height) {
        Plants plants = new Plants(width, height);

        for (int x = 1; x <= width; x++) {
            for (int y = 1; y <= height; y++) {
                Vector2d position = new Vector2d(x, y);
                Assertions.assertTrue(plants.isFieldEmpty(position));
            }
        }
    }

    @Test
    public void testIsFieldEmpty() {
        wereConstructed(10, 10);
        wereConstructed(3, 5);
        wereConstructed(83, 1);
    }


    private void wereAdded(int width, int height, int numberToAdd) {
        Plants plants = new Plants(width, height);
        plants.addPlants(numberToAdd);

        int numberOfPlants = 0;
        for (int x = 1; x <= width; x++) {
            for (int y = 1; y <= height; y++) {
                if (! plants.isFieldEmpty(new Vector2d(x, y))) numberOfPlants++;
            }
        }

        Assertions.assertEquals(numberToAdd, numberOfPlants);
    }

    @Test
    public void testAddPlants() {
        for (int i = 0; i < 25; i++) {
            wereAdded(4, 3, 5);
            wereAdded(8, 4, 32);
            wereAdded(50, 2, 99);
            wereAdded(11, 3, 0);
        }
    }


    private void wereEaten(int width, int height) {
        Plants plants = new Plants(width, height);
        plants.addPlants(width * height);

        for (int x = 1; x <= width; x++) {
            for (int y = 1; y <= height; y++) {
                Vector2d position = new Vector2d(x, y);
                Assertions.assertTrue(plants.wasEaten(position));
                Assertions.assertTrue(plants.isFieldEmpty(position));
            }
        }
    }

    @Test
    public void testWasEaten() {
        wereEaten(8, 5);
        wereEaten(12, 7);
        wereEaten(1, 101);
    }

    @Test
    public void testGetNumberOfPlants() {
        for (int i = 0; i < 20; i++) {
            int width = RandomInteger.getRandomInt(1, 25);
            int height = RandomInteger.getRandomInt(1, 25);
            int tilesCount = width * height;
            Plants plants = new Plants(width, height);

            int plantCount = RandomInteger.getRandomInt(tilesCount);
            plants.addPlants(plantCount);
            Assertions.assertEquals(plantCount, plants.getNumberOfPlants());

            for (int j = 0; j < RandomInteger.getRandomInt(30, 300); j++) {
                if (plants.wasEaten(new Vector2d(RandomInteger.getRandomInt(1, width),
                        RandomInteger.getRandomInt(1, height)))) plantCount--;

                Assertions.assertEquals(plantCount, plants.getNumberOfPlants());
            }

            int toAdd = RandomInteger.getRandomInt(tilesCount - plantCount);
            plants.addPlants(toAdd);
            plantCount+=toAdd;
            Assertions.assertEquals(plantCount, plants.getNumberOfPlants());

            plants.addPlants(tilesCount);
            Assertions.assertEquals(tilesCount, plants.getNumberOfPlants());
        }
    }
}
