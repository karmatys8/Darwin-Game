package agh.ics.oop.model.worldMaps;

import agh.ics.oop.model.animal.Animal;
import agh.ics.oop.model.movement.Vector2d;
import agh.ics.oop.model.util.MostCommonGenotype;
import agh.ics.oop.model.util.MapVisualizer;
import agh.ics.oop.model.util.RandomInteger;

import java.util.*;


import static agh.ics.oop.model.util.CommonMethods.checkIfPositive;

public class Globe {
    private final int width;
    private final int height;

    private static final Vector2d lowerLeftBoundary = new Vector2d(1, 1);
    private final Vector2d upperRightBoundary;
    private int plantCount = 0;
    private int numberOfAnimals = 0;
    MapVisualizer map = new MapVisualizer(this);
    private Map<Vector2d, List<Animal>> animals = new HashMap<>();
    private Map<Vector2d, Plant> plants = new HashMap<Vector2d, Plant>();
    private final PlantConfig plantConfig;
    private final AnimalConfig animalConfig;
    private final MostCommonGenotype genotypeHeapCounter = new MostCommonGenotype();

    public Globe(int width, int height, PlantConfig plantConfig, AnimalConfig animalConfig) {
        checkIfPositive(width);
        this.width = width;

        checkIfPositive(height);
        this.height = height;

        upperRightBoundary = new Vector2d(width, height);

        this.plantConfig = plantConfig;
        this.animalConfig = animalConfig;

        for (int i = 0; i < animalConfig.startingCount(); i++) {
            place(new Animal(new Vector2d(RandomInteger.getRandomInt(1, width), RandomInteger.getRandomInt(1, height)), animalConfig));        }
    }

    public boolean canMoveTo(Vector2d position) {
        return position.precedes(upperRightBoundary) && position.follows(lowerLeftBoundary);
    }

    public void place(Animal animal) {
        Vector2d position = animal.getPosition();
        numberOfAnimals++;

        List<Animal> animalsAtThisPosition = animals.remove(position);
        if (animalsAtThisPosition == null) animalsAtThisPosition = new ArrayList<>();
        animalsAtThisPosition.add(animal);

        animals.put(position, animalsAtThisPosition);
        genotypeHeapCounter.insert(animal.getGenotype());

    }
    public void remove(Animal animal) {
        try {
            Vector2d position = animal.getPosition();
            List<Animal> animalsAtThisPosition = animals.get(position);

            animalsAtThisPosition.remove(animal);
            genotypeHeapCounter.remove(animal.getGenotype());

            if (animalsAtThisPosition.isEmpty()) {
                animals.remove(position);
            }
        } catch (NullPointerException e) {
            System.err.println("NullPointerException: Position in animal is not kept correctly.");
        }
    }

    public String toString(){
        return map.draw(this.lowerLeftBoundary, this.upperRightBoundary);
    }

    public Object objectAt(Vector2d position) {
        List<Animal> animalsAtThisPosition = animals.get(position);
        if (animalsAtThisPosition!=null) return animalsAtThisPosition;
        return plants.get(position);
    }
  
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}

