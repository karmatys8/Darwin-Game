package agh.ics.oop.model.worldMaps;

import agh.ics.oop.model.worldElements.animal.Animal;
import agh.ics.oop.model.movement.Vector2d;
import agh.ics.oop.model.util.configs.AnimalConfig;
import agh.ics.oop.model.util.configs.PlantConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AbstractWorldMapTests {
    private final AnimalConfig animalConfig = new AnimalConfig(0, 3, 54, 2323, 17, 454, 2);
    private final PlantConfig plantConfig = new PlantConfig(1, 1, 1);
    private static final Map<Vector2d, List<Animal>> animals = new HashMap<>();
    private static final Plants plants = new Plants(1, 1);
    private final Globe globe = new Globe(10, 10, animalConfig, plantConfig, animals, plants);

    @Test
    public void testAbstractWorldMap() {
        Globe globe1 = new Globe(20, 30,
                new AnimalConfig(18, 1, 3, 2, 0, 4, 2),
                new PlantConfig(0, 2, 5), animals, plants);
        Assertions.assertInstanceOf(Globe.class, globe1);

        Globe globe2 = new Globe(1000, 1,
                new AnimalConfig(2000, 0, 1000, 0, 0, 0, 1000),
                new PlantConfig(0, 0, 0), animals, plants);
        Assertions.assertInstanceOf(Globe.class, globe2);


        Assertions.assertThrows(IllegalArgumentException.class, () -> new Globe(-2, 10,
                new AnimalConfig(100, 0, 1000, 0, 0, 0, Integer.MAX_VALUE),
                new PlantConfig(0, 0, 0), animals, plants));

        Assertions.assertThrows(IllegalArgumentException.class, () -> new Globe(400, 0,
                new AnimalConfig(8, 0, 1000, 0, 0, 0, 9),
                new PlantConfig(0, 0, 0), animals, plants));
    }

    @Test
    public void testPlace() {
        int width = 9;
        int height = 11;
        Globe globe = new Globe(width, height, animalConfig, plantConfig, animals, plants);

        for (int x = 1; x <= width; x++) {
            for (int y = 1; y <= height; y++) {
                Vector2d position = new Vector2d(x, y);
                Animal animal = new Animal(position, animalConfig);
                globe.place(animal);

                Assertions.assertTrue(globe.animalsMap.get(position).contains(animal));
            }
        }

        for (int x = 1; x <= width; x++) {
            Animal animal = new Animal(new Vector2d(1, 0), animalConfig);

            Assertions.assertThrows(IllegalArgumentException.class, () -> globe.place(animal));
        }
    }
}
