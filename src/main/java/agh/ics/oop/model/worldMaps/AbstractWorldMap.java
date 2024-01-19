package agh.ics.oop.model.worldMaps;


import agh.ics.oop.model.movement.MapDirection;
import agh.ics.oop.model.worldElements.WorldElement;
import agh.ics.oop.model.worldElements.animal.Animal;
import agh.ics.oop.model.movement.Vector2d;
import agh.ics.oop.model.util.MapVisualizer;
import agh.ics.oop.model.util.configs.AnimalConfig;
import agh.ics.oop.model.util.configs.PlantConfig;
import javafx.util.Pair;

import java.util.*;

import static agh.ics.oop.model.util.CommonMethods.checkIfPositive;

abstract public class AbstractWorldMap {
    protected final int width;
    protected final int height;
    protected static final Vector2d lowerLeftBoundary = new Vector2d(1, 1);
    protected final Vector2d upperRightBoundary;

    protected final PlantConfig plantConfig;
    protected final AnimalConfig animalConfig;

    MapVisualizer mapVisualizer = new MapVisualizer(this);
    protected static final agh.ics.oop.model.worldElements.artificialElements.Plant dumbPlant = new agh.ics.oop.model.worldElements.artificialElements.Plant();
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
    }

    abstract public Pair<Vector2d, Integer> howToMove(Vector2d oldPosition, MapDirection direction);

    public void place(Animal animal) {
        Vector2d position = animal.getPosition();
        if (position.follows(lowerLeftBoundary) && position.precedes(upperRightBoundary)) {
            List<Animal> animalsAtThisPosition = animalsMap.getOrDefault(position, new ArrayList<>());
            animalsAtThisPosition.add(animal);

            animalsMap.put(position, animalsAtThisPosition);
        } else {
            throw new IllegalArgumentException("Animal is placed out of bounds!");
        }
    }
    public void remove(Animal animal) {
        Vector2d position = animal.getPosition();
        List<Animal> animalsAtThisPosition = animalsMap.get(position);
        animalsAtThisPosition.remove(animal);
        if(!animalsAtThisPosition.isEmpty()){
            animalsMap.put(position, animalsAtThisPosition);
        }
    }

    public String toString(){
        return mapVisualizer.draw(lowerLeftBoundary, upperRightBoundary);
    }

    abstract public WorldElement objectAt(Vector2d position);
}
