package agh.ics.oop.model.animal;
import agh.ics.oop.model.movement.Vector2d;
import agh.ics.oop.model.util.configs.AnimalConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GenotypeTests {

    @Test
    public void testConstructor() {
        int genotypeLength = 10;
        int minMutations = 1;
        int maxMutations = 3;
        Genotype genotype = GenotypeFactory.getGenotype("Swap", genotypeLength, minMutations, maxMutations);
        assertEquals(genotypeLength, genotype.toString().length());

        Genotype genotype2 = GenotypeFactory.getGenotype("Full randomness", genotypeLength, minMutations, maxMutations);
        assertEquals(genotypeLength, genotype2.toString().length());
    }

    @Test
    public void testCrossbreeding() {
        int genotypeLength = 40;

        AnimalConfig animalConfig = new AnimalConfig(2, 100, 12,
                5, 0, 90, genotypeLength, "Full randomness");

        Vector2d vector = new Vector2d(0, 1);
        Animal mother = new Animal(vector, animalConfig);
        Animal father = new Animal(vector, animalConfig);

        for (int  i = 0; i < 4; i++) {
            Genotype childGenotype = GenotypeFactory.getGenotype(animalConfig.mutationOption(), mother, father);

            assertNotNull(childGenotype);
            assertEquals(genotypeLength, childGenotype.toString().length());
        }
    }
}