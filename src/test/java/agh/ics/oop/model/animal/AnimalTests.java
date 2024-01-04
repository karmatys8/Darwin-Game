package agh.ics.oop.model.animal;

import agh.ics.oop.model.animal.Animal;
import agh.ics.oop.model.movement.Vector2d;
import agh.ics.oop.model.worldMaps.AnimalConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AnimalTests {

    private AnimalConfig animalConfig;

    @BeforeEach
    void setUp() {
        animalConfig = new AnimalConfig(1, 10, 9, 3, 3, 20, 10);
    }

    @Test
    void testAnimalCreationWithPosition() {
        Vector2d initialPosition = new Vector2d(2, 2);
        Animal animal = new Animal(initialPosition, animalConfig);

        assertEquals(initialPosition, animal.getPosition());
        assertNotNull(animal.getGenotype());
        assertEquals(animalConfig.startingEnergy(), animal.getEnergy());
    }

    @Test
    void testAnimalReproduction() {
        Animal mother = new Animal(new Vector2d(2, 2), animalConfig);
        Animal father = new Animal(new Vector2d(3, 3), animalConfig);
        int initialEnergyMother = mother.getEnergy();
        int initialEnergyFather = father.getEnergy();

        Animal child = new Animal(mother, father, animalConfig);

        assertEquals(mother.getNumberOfChildren(),1);
        assertEquals(father.getNumberOfChildren(),1);
        assertEquals(child.getNumberOfDescendants(),0);
        assertEquals(initialEnergyMother - animalConfig.energyUsedToReproduce(), mother.getEnergy());
        assertEquals(initialEnergyFather - animalConfig.energyUsedToReproduce(), father.getEnergy());
    }

    @Test
    void testAnimalNextGene() {
        Animal animal = new Animal(new Vector2d(2, 2), animalConfig);
        int initialGeneIndex = animal.getCurrentGeneIndex();

        animal.nextGene();

        assertEquals((initialGeneIndex + 1) % animal.getGenotype().getGenes().size(), animal.getCurrentGeneIndex());
    }
    @Test
    void testCountingDescendants() {
        Animal motherA = new Animal(new Vector2d(2, 2), animalConfig);
        Animal fatherA = new Animal(new Vector2d(3, 3), animalConfig);
        Animal motherB = new Animal(new Vector2d(12, 12), animalConfig);
        Animal fatherB = new Animal(new Vector2d(31, 31), animalConfig);
        Animal childAA = new Animal(motherA, fatherA, animalConfig);
        Animal childBB= new Animal(motherB, fatherB, animalConfig);
        Animal childAB= new Animal(motherA, fatherB, animalConfig);
        Animal childBA= new Animal(motherB, fatherA, animalConfig);
        Animal childAABB= new Animal(childAA, childBB, animalConfig);
        Animal childAABBAB = new Animal(childAABB, childAB, animalConfig);

        assertEquals(motherA.getNumberOfDescendants(), 4);
        assertEquals(motherB.getNumberOfDescendants(), 4);
        assertEquals(fatherA.getNumberOfDescendants(), 4);
        assertEquals(fatherB.getNumberOfDescendants(), 4);
        assertEquals(motherA.getNumberOfChildren(), 2);
        assertEquals(motherB.getNumberOfChildren(), 2);
        assertEquals(fatherA.getNumberOfChildren(), 2);
        assertEquals(fatherB.getNumberOfChildren(), 2);
    }
}