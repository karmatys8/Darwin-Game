package agh.ics.oop.model.worldMaps;

import agh.ics.oop.model.movement.Vector2d;
import agh.ics.oop.model.util.RandomInteger;

import java.util.*;


public class Plants {
    private final Set<Vector2d> emptyFieldsOnEquator = new LinkedHashSet<>();
    private final Set<Vector2d> emptyFieldsOutsideOfEquator = new LinkedHashSet<>();

    public Plants(int width, int height) {
        int equatorStart = (int) (0.4 * height) + 1;
        int equatorEnd = (int) (0.6 * height) + 1;

        for (int x = 1; x <= width; x++) {
            for (int y = 1; y <= height; y++) {
                Set<Vector2d> targetSet = (y > equatorStart && y < equatorEnd) ? emptyFieldsOnEquator : emptyFieldsOutsideOfEquator;
                targetSet.add(new Vector2d(x, y));
            }
        }
    }

    public void addPlants(int numberToAdd) {
        List<Vector2d> onEquator = new ArrayList<>(emptyFieldsOnEquator);
        Collections.shuffle(onEquator);
        List<Vector2d> outsideOfEquator = new ArrayList<>(emptyFieldsOutsideOfEquator);
        Collections.shuffle(outsideOfEquator);

        for (int i = 0; i < numberToAdd; i++) {
            if (! onEquator.isEmpty()  &&  RandomInteger.getRandomInt(4) == 0) {
                emptyFieldsOnEquator.remove(onEquator.remove(0));
            } else if (! outsideOfEquator.isEmpty()) {
                emptyFieldsOutsideOfEquator.remove(outsideOfEquator.remove(0));
            }
        }
    }

    public boolean wasEaten(Vector2d position) {
        return emptyFieldsOutsideOfEquator.remove(position) || emptyFieldsOnEquator.remove(position);
    }

    public boolean isFieldEmpty(Vector2d position) {
        return ! (emptyFieldsOutsideOfEquator.contains(position) || emptyFieldsOnEquator.contains(position));
    }
}
