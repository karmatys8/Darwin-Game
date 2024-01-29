package agh.ics.oop.model.worldMaps;

import agh.ics.oop.model.movement.MapDirection;
import agh.ics.oop.model.animal.Animal;
import agh.ics.oop.model.movement.Vector2d;
import agh.ics.oop.model.util.configs.AnimalConfig;
import agh.ics.oop.model.util.configs.PlantConfig;
import javafx.scene.Node;
import javafx.util.Pair;

import java.util.*;


public class Globe extends AbstractWorldMap {
    public Globe(int width, int height, AnimalConfig animalConfig, PlantConfig plantConfig,
                            Map<Vector2d, List<Animal>> animalsMap, Plants plants) {
        super(width, height, animalConfig, plantConfig, animalsMap, plants);
    }

    @Override
    public Pair<Vector2d, Integer> calculateNextPosition(Vector2d oldPosition, MapDirection direction) {
        Vector2d newPosition = oldPosition.add(direction.getUnitVector());
        if (newPosition.y() < 1  ||  newPosition.y() > height) return new Pair<>(oldPosition, 4);
        if (newPosition.x() < 1) return new Pair<>(new Vector2d(newPosition.x() + width, newPosition.y()), 0);
        if (newPosition.x() > width) return new Pair<>(new Vector2d(newPosition.x() % width, newPosition.y()), 0);
        return new Pair<>(newPosition, 0);
    }

    @Override
    public Pair<Node, Optional<Animal>> getNodeAt(Vector2d position) { // czy to jest zadanie mapy, żeby zwrócić Node?
        List<Animal> animalsAtThisPosition = animalsMap.get(position);
        if (animalsAtThisPosition != null) {
            Collections.sort(animalsAtThisPosition, animalComparator);
            Animal dominantAnimal = animalsAtThisPosition.get(0);
            return new Pair<>(nodeCreator.createAnimalsNode(dominantAnimal), Optional.of(dominantAnimal));
        }
        if (! plants.isFieldEmpty(position)) return new Pair<>(nodeCreator.createPlantsNode(), Optional.empty());
        return new Pair<>(null, Optional.empty());
    }
}

