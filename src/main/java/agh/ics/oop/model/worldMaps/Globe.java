package agh.ics.oop.model.worldMaps;

import agh.ics.oop.model.animal.Animal;
import agh.ics.oop.model.animal.Genotype;
import agh.ics.oop.model.movement.Vector2d;
import agh.ics.oop.model.util.RandomInteger;

import java.util.*;


import static agh.ics.oop.model.util.CommonMethods.checkIfPositive;

public class Globe {
    private final int width;
    private final int height;

    private static final Vector2d lowerLeftBoundary = new Vector2d(1, 1);
    private final Vector2d upperRightBoundary;

    private Map<Vector2d, List<Animal>> animals;
    private Map<Vector2d, Plant> plants = new HashMap<Vector2d, Plant>();
    private Map<Genotype, Integer> genotypeCount = new HashMap<>();
    private PriorityQueue<Map.Entry<Genotype, Integer>> maxHeap = new PriorityQueue<>(Comparator.comparingInt(entry -> ((Map.Entry<Genotype, Integer>) entry).getValue()).reversed());

    public Globe(int width, int height, Map<Vector2d, List<Animal>> animals) {
        checkIfPositive(width);
        this.width = width;

        checkIfPositive(height);
        this.height = height;

        this.animals = animals;

        upperRightBoundary = new Vector2d(width, height);
    }

    public boolean canMoveTo(Vector2d position) {
        return position.precedes(upperRightBoundary) && position.follows(lowerLeftBoundary);
    }



    public void place(Animal animal) {
        Vector2d position = animal.getPosition();

        List<Animal> animalsAtThisPosition = animals.remove(position);
        if (animalsAtThisPosition == null) animalsAtThisPosition = new ArrayList<>();
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
        maxHeap.removeIf(entry -> entry.getKey().equals(genotype));

        if(count>0){
            maxHeap.add(Map.entry(genotype, count));
            genotypeCount.put(genotype, count);
        }
        else {
            genotypeCount.remove(genotype);
        }

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

    public void moveAnimal(Animal animal) {
        Vector2d oldPosition = animal.getPosition();
        animal.move(this);

        Vector2d newPosition = animal.getPosition();
        if (oldPosition != newPosition) {
            List<Animal> prevAnimals = animals.remove(oldPosition);
            prevAnimals.remove(animal);

            if (!prevAnimals.isEmpty()) animals.put(oldPosition, prevAnimals);

            List<Animal> currAnimals = animals.getOrDefault(newPosition, new ArrayList<>(1));
            currAnimals.add(animal);

            animals.put(newPosition, currAnimals);
        }
    }

    public void killAnimal(Animal animal, int currentDay) {
        Vector2d position = animal.getPosition();
        animal.kill(currentDay);

        List<Animal> prevAnimals = animals.remove(position);
        prevAnimals.remove(animal);

        if (!prevAnimals.isEmpty()) animals.put(position, prevAnimals);

        // change genotype Heap here
    }
}

