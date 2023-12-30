package agh.ics.oop.model.animal;
import agh.ics.oop.model.movement.Vector2d;
import agh.ics.oop.model.worldMaps.AnimalConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GenotypeTests {

    @Test
    public void testConstructor() {
        int genotypeLength = 10;
        int minMutations = 1;
        int maxMutations = 3;
        Genotype genotype = new Genotype(genotypeLength, minMutations, maxMutations);
        assertEquals(genotypeLength, genotype.toString().length());
    }

    @Test
    public void testSwitchGenes() {
        int genotypeLength = 10;
        int minMutations = 1;
        int maxMutations = 3;
        Genotype genotype = new Genotype(genotypeLength, minMutations, maxMutations);

        int initialFirstGene = genotype.getCurrentGene(0);
        int initialLastGene = genotype.getCurrentGene(genotypeLength - 1);

        genotype.switchGenes(0, genotypeLength - 1);

        assertEquals(initialLastGene, genotype.getCurrentGene(0));
        assertEquals(initialFirstGene, genotype.getCurrentGene(genotypeLength - 1));
    }

    @Test
    public void testRandomGene() {
        int genotypeLength = 10;
        int minMutations = 1;
        int maxMutations = 3;
        Genotype genotype = new Genotype(genotypeLength, minMutations, maxMutations);

        int initialGene = genotype.getCurrentGene(0);

        genotype.randomGene(0);

        assertNotEquals(initialGene, genotype.getCurrentGene(0));
    }

    @Test
    public void testCrossbreeding() {
        int minNumberOfMutations = 1;
        int maxNumberOfMutations = 3;
        int genotypeLength = 10;

        AnimalConfig animalConfig = new AnimalConfig(1, 100, 0, 1, minNumberOfMutations, maxNumberOfMutations, genotypeLength);

        Animal mother = new Animal(new Vector2d(0,1), animalConfig);
        Animal father = new Animal(new Vector2d(0,1), animalConfig);

        Genotype childGenotype = new Genotype(mother, father);

        assertNotNull(childGenotype);
        assertEquals(genotypeLength, childGenotype.toString().length());
    }
}