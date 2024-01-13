package agh.ics.oop.model.worldMaps;

import agh.ics.oop.model.animal.Animal;
import agh.ics.oop.model.movement.Vector2d;
import agh.ics.oop.model.util.configs.AnimalConfig;
import agh.ics.oop.model.util.configs.PlantConfig;

import java.util.*;


public class Globe extends AbstractWorldMap {
    public Globe(int width, int height, AnimalConfig animalConfig, PlantConfig plantConfig,
                            Map<Vector2d, List<Animal>> animalsMap, Map<Vector2d, Plant> plants) {
        super(width, height, animalConfig, plantConfig, animalsMap, plants);
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.precedes(upperRightBoundary) && position.follows(lowerLeftBoundary);
    }

    @Override
    public Object objectAt(Vector2d position) {
        List<Animal> animalsAtThisPosition = animalsMap.get(position);
        if (animalsAtThisPosition != null) return animalsAtThisPosition;
        return plants.get(position);
    }
}

