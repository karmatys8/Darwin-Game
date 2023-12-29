package agh.ics.oop.model;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.Genotype;
import agh.ics.oop.model.movement.MapDirection;
import agh.ics.oop.model.movement.Vector2d;
import agh.ics.oop.model.worldMaps.Globe;
import agh.ics.oop.model.util.RandomInteger;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GenotypeTests {
    @Test
    public void testConstructor() {
        int genotypeLength = 10;
        Genotype genotype = new Genotype(genotypeLength);

        assertEquals(genotypeLength, genotype.toString().length());
    }

    @Test
    public void testCrossbreeding() {
        int energyMother = 100;
        int energyFather = 50;
        int minNumberOfMutations = 1;
        int maxNumberOfMutations = 3;
        int genotypeLength = 10;

        Animal mother = createTestAnimal(energyMother, genotypeLength, minNumberOfMutations, maxNumberOfMutations);
        Animal father = createTestAnimal(energyFather, genotypeLength, minNumberOfMutations, maxNumberOfMutations);

        Genotype childGenotype = new Genotype(mother, father);

        assertNotNull(childGenotype);
        assertEquals(genotypeLength, childGenotype.toString().length());
    }

    @Test
    public void testMutation() {
        int minNumberOfMutations = 1;
        int maxNumberOfMutations = 1;
        int genotypeLength = 10;

        Animal mother = createTestAnimal(100, genotypeLength, minNumberOfMutations, maxNumberOfMutations);
        Animal father = createTestAnimal(50, genotypeLength, minNumberOfMutations, maxNumberOfMutations);

        Genotype childGenotype = new Genotype(mother, father);

        assertNotNull(childGenotype);
        assertEquals(genotypeLength, childGenotype.toString().length());
    }

    @Test
    public void testSwitchGenes() {
        int genotypeLength = 10;
        Genotype genotype = new Genotype(genotypeLength);

        int initialFirstGene = genotype.getCurrentGene(0);
        int initialLastGene = genotype.getCurrentGene(genotypeLength - 1);

        genotype.switchGenes(0, genotypeLength - 1);

        assertEquals(initialLastGene, genotype.getCurrentGene(0));
        assertEquals(initialFirstGene, genotype.getCurrentGene(genotypeLength - 1));
    }

    @Test
    public void testRandomGene() {
        int genotypeLength = 10;
        Genotype genotype = new Genotype(genotypeLength);

        int initialGene = genotype.getCurrentGene(0);

        genotype.randomGene(0);

        assertNotEquals(initialGene, genotype.getCurrentGene(0));
    }

    private Animal createTestAnimal(int energy, int genotypeLength, int minMutations, int maxMutations) {
        Globe globe = new Globe(100, 100, 10, 10, 1, 10, 5, 5, minMutations, maxMutations, genotypeLength);
        return new Animal(globe);
    }
}