package agh.ics.oop.model.worldMaps;

import agh.ics.oop.model.animal.Animal;
import agh.ics.oop.model.movement.Vector2d;
import agh.ics.oop.model.util.MostCommonGenotype;
import agh.ics.oop.model.util.MapVisualizer;
import agh.ics.oop.model.util.RandomInteger;
import agh.ics.oop.model.util.configs.AnimalConfig;
import agh.ics.oop.model.util.configs.PlantConfig;

import java.util.*;

import static agh.ics.oop.model.util.CommonMethods.checkIfPositive;

public class Globe extends AbstractWorldMap {
    public Globe(int width, int height, PlantConfig plantConfig, AnimalConfig animalConfig) {
        super(width, height, plantConfig, animalConfig);
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.precedes(upperRightBoundary) && position.follows(lowerLeftBoundary);
    }

    @Override
    public Object objectAt(Vector2d position) {
        List<Animal> animalsAtThisPosition = animals.get(position);
        if (animalsAtThisPosition!=null) return animalsAtThisPosition;
        return plants.get(position);
    }
}

