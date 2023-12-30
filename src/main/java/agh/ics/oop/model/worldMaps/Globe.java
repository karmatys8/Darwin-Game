package agh.ics.oop.model.worldMaps;

import agh.ics.oop.model.animal.Animal;
import agh.ics.oop.model.animal.Genotype;
import agh.ics.oop.model.movement.Vector2d;
import agh.ics.oop.model.util.RandomInteger;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;


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
    private Map<Genotype, Integer> genotypeCount = new HashMap<>();
    private PriorityQueue<Map.Entry<Genotype, Integer>> maxHeap = new PriorityQueue<>(Comparator.comparingInt(entry -> ((Map.Entry<Genotype, Integer>) entry).getValue()).reversed());
    private final PlantConfig plantConfig;
    private final AnimalConfig animalConfig;

    public Globe(int width, int height, PlantConfig plantConfig, AnimalConfig animalConfig) {
        checkIfPositive(width);
        this.width = width;

        checkIfPositive(height);
        this.height = height;

        upperRightBoundary = new Vector2d(width - 1, height - 1);

        this.plantConfig = plantConfig;
        this.animalConfig = animalConfig;


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
        animalsAtThisPosition.add(animal);

        animals.put(position, animalsAtThisPosition);
        updateGenotypeCount(animal.getGenotype());
    }
    public void remove(Animal animal) {
        try {
            Vector2d position = animal.getPosition();
            List<Animal> animalsAtThisPosition = animals.get(position);

            animalsAtThisPosition.remove(animal);

            if (animalsAtThisPosition.isEmpty()) {
                animals.remove(position);
            }
            removeGenotype(animal.getGenotype());

        } catch (NullPointerException e) {
            System.err.println("NullPointerException: Position in animal is not kept correctly.");
        }
    }
    public void updateGenotypeCount(Genotype genotype) {
        int count = genotypeCount.getOrDefault(genotype, 0) + 1;
        genotypeCount.put(genotype, count);

        maxHeap.removeIf(entry -> entry.getKey().equals(genotype));
        maxHeap.add(Map.entry(genotype, count));
    }
    private void removeGenotype(Genotype genotype) {
        int count=genotypeCount.get(genotype)-1;
        genotypeCount.put(genotype, count);

        maxHeap.removeIf(entry -> entry.getKey().equals(genotype));
        maxHeap.add(Map.entry(genotype, count));
    }
    public Genotype findMostCommonGenotype() {
        return maxHeap.isEmpty() ? null : maxHeap.peek().getKey();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}

