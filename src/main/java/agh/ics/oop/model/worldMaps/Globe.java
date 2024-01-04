package agh.ics.oop.model.worldMaps;

import agh.ics.oop.model.animal.Animal;
import agh.ics.oop.model.animal.Genotype;
import agh.ics.oop.model.movement.Vector2d;
import agh.ics.oop.model.util.MaxHeap;
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

    private Map<Vector2d, List<Animal>> animals = new HashMap<>();
    private Map<Vector2d, Plant> plants = new HashMap<Vector2d, Plant>();
    private final PlantConfig plantConfig;
    private final AnimalConfig animalConfig;
    private MaxHeap genotypeHeapCounter;

    public Globe(int width, int height, PlantConfig plantConfig, AnimalConfig animalConfig) {
        checkIfPositive(width);
        this.width = width;

        checkIfPositive(height);
        this.height = height;

        upperRightBoundary = new Vector2d(width, height);

        this.plantConfig = plantConfig;
        this.animalConfig = animalConfig;

        this.genotypeHeapCounter=new MaxHeap();


        for (int i = 0; i < animalConfig.startingCount(); i++) {
            place(new Animal(new Vector2d(RandomInteger.getRandomInt(width), RandomInteger.getRandomInt(height)), animalConfig));
        }
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
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}

