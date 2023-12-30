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

    private void switchTester(Genotype genotype, int idx1, int idx2) {
        int gene1 = genotype.getCurrentGene(idx1);
        int gene2 = genotype.getCurrentGene(idx2);

        genotype.switchGenes(idx1, idx2);

        assertEquals(gene1, genotype.getCurrentGene(idx2));
        assertEquals(gene2, genotype.getCurrentGene(idx1));
    }

    @Test
    public void testSwitchGenes() {
        int genotypeLength = 10;
        Genotype genotype = new Genotype(genotypeLength, 1, 3);

        switchTester(genotype, 0, genotypeLength - 1);
        switchTester(genotype, 3, 4);
        switchTester(genotype, 2, 2);
        switchTester(genotype, 0, 3);
    }

    @Test
    public void testRandomGene() {
        int genotypeLength = 30;
        Genotype genotype = new Genotype(genotypeLength, 1, 3);

        String initialGenotype = genotype.toString();

        for(int i = 0; i < genotypeLength; i++) {
            genotype.randomGene(i);
        }

        assertNotEquals(initialGenotype, genotype.toString());
    }

    @Test
    public void testCrossbreeding() {
        int genotypeLength = 40;

        AnimalConfig animalConfig = new AnimalConfig(2, 100, 12,
                5, 0, 90, genotypeLength);

        Vector2d vector = new Vector2d(0, 1);
        Animal mother = new Animal(vector, animalConfig);
        Animal father = new Animal(vector, animalConfig);

        for (int  i = 0; i < 4; i++) {
            Genotype childGenotype = new Genotype(mother, father);

            assertNotNull(childGenotype);
            assertEquals(genotypeLength, childGenotype.toString().length());
        }
    }
}