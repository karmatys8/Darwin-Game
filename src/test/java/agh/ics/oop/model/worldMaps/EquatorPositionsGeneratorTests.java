package agh.ics.oop.model.worldMaps;

import agh.ics.oop.model.movement.Vector2d;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EquatorPositionsGeneratorTests {
    @Test
    public void testEquatorPositionsGenerator() {
        EquatorPositionsGenerator generator1 = new EquatorPositionsGenerator(100, 1, 1);
        Vector2d vector = new Vector2d(0, 0);

        for (Vector2d position : generator1) {
            Assertions.assertEquals(vector, position);
        }


        int numberOfPlantsToAdd = 100000;
        EquatorPositionsGenerator generator2 = new EquatorPositionsGenerator(numberOfPlantsToAdd, 1, 1000);

        Vector2d bottomOfEquator = new Vector2d(0, 400);
        Vector2d topOfEquator = new Vector2d(0, 599);

        int inEquatorCount = 0;

        for (Vector2d position : generator2) {
            if (position.follows(bottomOfEquator) && position.precedes(topOfEquator)) inEquatorCount++;
        }

        // this test may fail but chance for that is extremely low
        Assertions.assertTrue(inEquatorCount > 0.7 * numberOfPlantsToAdd  && inEquatorCount < 0.9 * numberOfPlantsToAdd);
    }
}
