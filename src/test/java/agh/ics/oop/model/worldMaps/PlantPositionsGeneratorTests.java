package agh.ics.oop.model.worldMaps;

import agh.ics.oop.model.movement.Vector2d;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PlantPositionsGeneratorTests {
    @Test
    public void testEquatorPositionsGenerator() {
        EquatorPositionsGenerator generator1 = new EquatorPositionsGenerator(100, 1, 1);
        Vector2d vector = new Vector2d(1, 1);

        for (Vector2d position : generator1) {
            Assertions.assertEquals(vector, position);
        }


        int numberOfPlantsToAdd = 100000;
        PlantPositionsGenerator generator2 = new PlantPositionsGenerator(numberOfPlantsToAdd, 1, 1000);
        Vector2d bottomOfEquator = new Vector2d(1, 400);
        Vector2d topOfEquator = new Vector2d(1, 599);

        int inEquatorCount = 0;

        for (Vector2d position : generator2) {
            if (position.follows(bottomOfEquator) && position.precedes(topOfEquator)) inEquatorCount++;
        }
        Assertions.assertTrue(inEquatorCount > 0.7 * numberOfPlantsToAdd  && inEquatorCount < 0.9 * numberOfPlantsToAdd);
    }
}
