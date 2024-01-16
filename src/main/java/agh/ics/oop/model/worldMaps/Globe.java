package agh.ics.oop.model.worldMaps;

import agh.ics.oop.model.movement.MapDirection;
import agh.ics.oop.model.worldElements.WorldElement;
import agh.ics.oop.model.worldElements.animal.Animal;
import agh.ics.oop.model.movement.Vector2d;
import agh.ics.oop.model.util.configs.AnimalConfig;
import agh.ics.oop.model.util.configs.PlantConfig;
import javafx.util.Pair;

import java.util.*;


public class Globe extends AbstractWorldMap {
    public Globe(int width, int height, AnimalConfig animalConfig, PlantConfig plantConfig,
                            Map<Vector2d, List<Animal>> animalsMap, Map<Vector2d, Plant> plants) {
        super(width, height, animalConfig, plantConfig, animalsMap, plants);
    }

    @Override
    public Pair<Vector2d, Integer> howToMove(Vector2d oldPosition, MapDirection direction) {
        Vector2d newPosition = oldPosition.add(direction.getUnitVector());
        if (newPosition.getY() < 1  ||  newPosition.getY() > height) return new Pair<>(oldPosition, 4);
        if (newPosition.getX() < 1) return new Pair<>(new Vector2d(newPosition.getX() + width, newPosition.getY()), 0);
        if (newPosition.getX() > width) return new Pair<>(new Vector2d(newPosition.getX() % width, newPosition.getY()), 0);
        return new Pair<>(newPosition, 0);
    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        List<Animal> animalsAtThisPosition = animalsMap.get(position);
        if (animalsAtThisPosition != null) return animalsAtThisPosition.get(0);
        if (plants.get(position) != null) return dumbPlant;
        else return null;
    }
}

