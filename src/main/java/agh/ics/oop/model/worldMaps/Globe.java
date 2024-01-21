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
    public Pair<Vector2d, Integer> howToMove(Vector2d oldPosition, MapDirection direction) {
        Vector2d newPosition = oldPosition.add(direction.getUnitVector());
        if (newPosition.y() < 1  ||  newPosition.y() > height) return new Pair<>(oldPosition, 4);
        if (newPosition.x() < 1) return new Pair<>(new Vector2d(newPosition.x() + width, newPosition.y()), 0);
        if (newPosition.x() > width) return new Pair<>(new Vector2d(newPosition.x() % width, newPosition.y()), 0);
        return new Pair<>(newPosition, 0);
    }

    @Override
    public Pair<Node, Optional<Animal>> nodeAt(Vector2d position) {
        List<Animal> animalsAtThisPosition = animalsMap.get(position);
        if (animalsAtThisPosition != null) {
            Animal animal = animalsAtThisPosition.get(0);
            return new Pair<>(nodeCreator.animalsNode(animal), Optional.of(animal));
        }
        if (! plants.isFieldEmpty(position)) return new Pair<>(nodeCreator.plantsNode(), Optional.empty());
        return new Pair<>(null, Optional.empty());
    }
}

