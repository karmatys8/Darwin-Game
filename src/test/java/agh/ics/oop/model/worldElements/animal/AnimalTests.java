package agh.ics.oop.model.worldElements.animal;

import agh.ics.oop.model.movement.Vector2d;
import agh.ics.oop.model.util.configs.AnimalConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static agh.ics.oop.model.util.RandomInteger.getRandomInt;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
            int initialGeneIndex = animal.getCurrentGene();

            animal.nextGene();

            assertEquals((initialGeneIndex + 1) % animal.getGenotype().getGenes().size(), animal.getCurrentGene());
        }
    }

    @Test
    void testCanReproduce() {
        int startingEnergy = 20;
        int minEnergyToReproduce = 6;
        AnimalConfig animalConfig = new AnimalConfig(2, startingEnergy, minEnergyToReproduce, 1, 0 ,10, 6);
        Vector2d position = new Vector2d(2, 2);
        Animal mother = new Animal(position, animalConfig);
        Animal father = new Animal(position, animalConfig);

        for (int i = 0; i < startingEnergy - minEnergyToReproduce; i++) {
            Assertions.assertTrue(mother.canReproduce());
            Assertions.assertTrue(father.canReproduce());

            new Animal(mother, father, animalConfig);
        }

        new Animal(mother, new Animal(position, animalConfig), animalConfig);

        for (int i = 0; i < startingEnergy; i++) {
            Assertions.assertFalse(mother.canReproduce());
            Assertions.assertTrue(father.canReproduce());
        }
    }

    @Test
    void testAnimalReproduction() {
        int startingEnergy = 50;
        AnimalConfig animalConfig = new AnimalConfig(2, startingEnergy, 1, 1, 0 ,10, 6);
        Vector2d position = new Vector2d(2, 2);
        Animal mother = new Animal(position, animalConfig);
        Animal father = new Animal(position, animalConfig);

        for (int i = 1; i <= startingEnergy; i++) {
            if (mother.canReproduce() && father.canReproduce()) {
                Animal child = new Animal(mother, father, animalConfig);

                assertEquals(0, child.getNumberOfDescendants());
                assertEquals(position, child.getPosition());
            }

            assertEquals(i, mother.getNumberOfChildren());
            assertEquals(i, father.getNumberOfChildren());

            assertEquals(50 - i * animalConfig.energyUsedToReproduce(), mother.getEnergy());
            assertEquals(50 - i * animalConfig.energyUsedToReproduce(), father.getEnergy());
        }

        if (mother.canReproduce() && father.canReproduce()) new Animal(mother, father, animalConfig);

        assertEquals(startingEnergy, mother.getNumberOfChildren());
        assertEquals(startingEnergy, father.getNumberOfChildren());

        assertEquals(0, mother.getEnergy());
        assertEquals(0, father.getEnergy());
    }
    @Test
    void testCountingDescendants() {
        AnimalConfig animalConfig = new AnimalConfig(4, 100, 10, 2, 0, 0, 8);
        Animal motherA = new Animal(new Vector2d(2, 2), animalConfig);
        Animal fatherA = new Animal(new Vector2d(3, 3), animalConfig);
        Animal motherB = new Animal(new Vector2d(12, 12), animalConfig);
        Animal fatherB = new Animal(new Vector2d(31, 31), animalConfig);
        Animal childAA = new Animal(motherA, fatherA, animalConfig);
        Animal childBB = new Animal(motherB, fatherB, animalConfig);
        Animal childAB = new Animal(motherA, fatherB, animalConfig);
        Animal childAABB = new Animal(childAA, childBB, animalConfig);
        Animal childAABBAB = new Animal(childAABB, childAB, animalConfig);
        Animal child = new Animal(fatherA, fatherB, animalConfig);

        assertEquals(4, motherA.getNumberOfDescendants());
        assertEquals(3, motherB.getNumberOfDescendants());
        assertEquals(4, fatherA.getNumberOfDescendants());
        assertEquals(5, fatherB.getNumberOfDescendants());

        assertEquals(2, motherA.getNumberOfChildren());
        assertEquals(1, motherB.getNumberOfChildren());
        assertEquals(2, fatherA.getNumberOfChildren());
        assertEquals(3, fatherB.getNumberOfChildren());

        assertEquals(0, childAABBAB.getNumberOfChildren());
        assertEquals(0, child.getNumberOfDescendants());

        assertEquals(2, childAA.getNumberOfDescendants());
        assertEquals(1, childAB.getNumberOfDescendants());
    }
}