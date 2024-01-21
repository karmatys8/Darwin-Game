package agh.ics.oop.model.animal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RandomGenotypeTests {
    @Test
    public void testMutate() {
        RandomGenotype randomGenotype = new RandomGenotype(16, 1, 1);

        for (int i = 0 ; i < 250; i++) {
            String prevState = randomGenotype.toString();
            randomGenotype.mutate();
            Assertions.assertNotEquals(prevState, randomGenotype.toString());
        }
    }
}
