package agh.ics.oop.model.worldMaps;

import agh.ics.oop.model.animal.Animal;
import agh.ics.oop.model.animal.Genotype;
import agh.ics.oop.model.movement.Vector2d;
import agh.ics.oop.model.util.MapVisualizer;
import agh.ics.oop.model.util.RandomInteger;
import agh.ics.oop.model.util.configs.AnimalConfig;
import agh.ics.oop.model.util.configs.PlantConfig;

import java.util.*;

import static agh.ics.oop.model.util.CommonMethods.checkIfPositive;

abstract public class AbstractWorldMap {
    protected final int width;
    protected final int height;
    protected static final Vector2d lowerLeftBoundary = new Vector2d(1, 1);
    protected final Vector2d upperRightBoundary;

    protected int plantCount = 0;
    protected int numberOfAnimals = 0;

    MapVisualizer map = new MapVisualizer(this);

    protected Map<Vector2d, List<Animal>> animals = new HashMap<>();
    protected Map<Vector2d, Plant> plants = new HashMap<Vector2d, Plant>();

    protected Map<Genotype, Integer> genotypeCount = new HashMap<>();
    protected PriorityQueue<Map.Entry<Genotype, Integer>> maxHeap = new PriorityQueue<>(Comparator.comparingInt(entry -> ((Map.Entry<Genotype, Integer>) entry).getValue()).reversed());

    protected final PlantConfig plantConfig;
    protected final AnimalConfig animalConfig;

    public AbstractWorldMap(int width, int height, PlantConfig plantConfig, AnimalConfig animalConfig) {
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

    abstract public boolean canMoveTo(Vector2d position);

    public void place(Animal animal) {
        Vector2d position = animal.getPosition();
        if (position.follows(lowerLeftBoundary) && position.precedes(upperRightBoundary)) {
            numberOfAnimals++;

            List<Animal> animalsAtThisPosition = animals.remove(position);
            if (animalsAtThisPosition == null) animalsAtThisPosition = new ArrayList<>();
            animalsAtThisPosition.add(animal);

            animals.put(position, animalsAtThisPosition);
            updateGenotypeCount(animal.getGenotype());
        } else {
            throw new IllegalArgumentException("Animal is placed out of bounds!");
        }
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

    public String toString(){
        return map.draw(this.lowerLeftBoundary, this.upperRightBoundary);
    }

    abstract public Object objectAt(Vector2d position);

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
