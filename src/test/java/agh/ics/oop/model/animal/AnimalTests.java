package agh.ics.oop.model.animal;

import agh.ics.oop.model.animal.Animal;
import agh.ics.oop.model.movement.Vector2d;
import agh.ics.oop.model.worldMaps.AnimalConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static agh.ics.oop.model.util.RandomInteger.getRandomInt;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Random;

public class AnimalTests {

    private AnimalConfig animalConfig;

    @BeforeEach
    void setUp() {
        animalConfig = new AnimalConfig(1, 10, 9, 3, 3, 20, 10);
    }

    @Test
    void testAnimalCreationWithPosition() {
        for(int i=0;i<1000;i++) {
            Vector2d initialPosition = new Vector2d(getRandomInt(1000), getRandomInt(1000));
            Animal animal = new Animal(initialPosition, animalConfig);

            assertEquals(initialPosition, animal.getPosition());
            assertNotNull(animal.getGenotype());
            assertEquals(animalConfig.startingEnergy(), animal.getEnergy());
        }
    }


    @Test
    void testAnimalNextGene() {
        for(int i=0;i<1000;i++) {

            Animal animal = new Animal(new Vector2d(2, 2), animalConfig);
            int initialGeneIndex = animal.getCurrentGeneIndex();

            animal.nextGene();

            assertEquals((initialGeneIndex + 1) % animal.getGenotype().getGenes().size(), animal.getCurrentGeneIndex());
        }
    }

}