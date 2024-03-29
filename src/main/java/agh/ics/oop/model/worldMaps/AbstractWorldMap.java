package agh.ics.oop.model.worldMaps;


import agh.ics.oop.controllers.NodeCreator;
import agh.ics.oop.model.animal.AnimalComparator;
import agh.ics.oop.model.animal.Animal;
import agh.ics.oop.model.movement.Vector2d;
import agh.ics.oop.model.util.configs.AnimalConfig;
import agh.ics.oop.model.util.configs.PlantConfig;
import javafx.scene.Node;
import javafx.util.Pair;

import java.util.*;

import static agh.ics.oop.model.util.CommonMethods.checkIfPositive;

abstract public class AbstractWorldMap implements PositionCalculator {
    protected final int width;
    protected final int height;
    protected static final Vector2d lowerLeftBoundary = new Vector2d(1, 1);
    protected final Vector2d upperRightBoundary;

    protected final PlantConfig plantConfig;
    protected final AnimalConfig animalConfig;

    protected NodeCreator nodeCreator;
    protected static final AnimalComparator animalComparator = new AnimalComparator();
    protected Map<Vector2d, List<Animal>> animalsMap;
    protected Plants plants;

    public AbstractWorldMap(int width, int height, AnimalConfig animalConfig, PlantConfig plantConfig,
                            Map<Vector2d, List<Animal>> animalsMap, Plants plants) {
        checkIfPositive(width);
        this.width = width;

        checkIfPositive(height);
        this.height = height;

        upperRightBoundary = new Vector2d(width, height);

        this.animalConfig = animalConfig;
        this.plantConfig = plantConfig;
        this.animalsMap = animalsMap;
        this.plants = plants;

        nodeCreator = new NodeCreator(width, height, animalConfig.startingEnergy());
    }

    public void place(Animal animal) {
        Vector2d position = animal.getPosition();
        if (isWithinBounds(position)) {
            List<Animal> animalsAtThisPosition = animalsMap.computeIfAbsent(position, k -> new ArrayList<>());
            animalsAtThisPosition.add(animal);
        } else {
            throw new IllegalArgumentException("Animal is placed out of bounds!");
        }
    }

    private boolean isWithinBounds(Vector2d position) {
        return position.follows(lowerLeftBoundary) && position.precedes(upperRightBoundary);
    }

    public void remove(Animal animal) {
        Vector2d position = animal.getPosition();
        List<Animal> animalsAtThisPosition = animalsMap.remove(position);
        animalsAtThisPosition.remove(animal);
        if(!animalsAtThisPosition.isEmpty()){
            animalsMap.put(position, animalsAtThisPosition);
        }
    }

    abstract public Pair<Node, Optional<Animal>> getNodeAt(Vector2d position);

    public void moveAnimal(Animal animal) {
        Vector2d oldPosition = animal.getPosition();
        animal.move(this);

        Vector2d newPosition = animal.getPosition();
        if (oldPosition != newPosition) {
            List<Animal> prevAnimals = animalsMap.get(oldPosition);
            prevAnimals.remove(animal);

            if (prevAnimals.isEmpty()) animalsMap.remove(oldPosition);

            List<Animal> currAnimals = animalsMap.getOrDefault(newPosition, new ArrayList<>(1));
            currAnimals.add(animal);

            animalsMap.put(newPosition, currAnimals);
        }
    }
}
