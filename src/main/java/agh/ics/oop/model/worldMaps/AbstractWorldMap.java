package agh.ics.oop.model.worldMaps;

import agh.ics.oop.model.movement.MapDirection;
import agh.ics.oop.model.worldElements.WorldElement;
import agh.ics.oop.model.worldElements.animal.Animal;
import agh.ics.oop.model.worldElements.animal.Genotype;
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

    protected int plantCount = 0;
    protected int numberOfAnimals = 0;
    protected final PlantConfig plantConfig;
    protected final AnimalConfig animalConfig;

    MapVisualizer mapVisualizer = new MapVisualizer(this);
    protected static final agh.ics.oop.model.worldElements.artificialElements.Plant dumbPlant = new agh.ics.oop.model.worldElements.artificialElements.Plant();
    protected Map<Vector2d, List<Animal>> animalsMap;
    protected Map<Vector2d, Plant> plants;

    protected Map<Genotype, Integer> genotypeCount = new HashMap<>();
    protected PriorityQueue<Map.Entry<Genotype, Integer>> maxHeap = new PriorityQueue<>(Comparator.comparingInt(entry -> ((Map.Entry<Genotype, Integer>) entry).getValue()).reversed());

    public AbstractWorldMap(int width, int height, AnimalConfig animalConfig, PlantConfig plantConfig,
                            Map<Vector2d, List<Animal>> animalsMap, Map<Vector2d, Plant> plants) {
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
            numberOfAnimals++;

            List<Animal> animalsAtThisPosition = animalsMap.getOrDefault(position, new ArrayList<>());
            animalsAtThisPosition.add(animal);

            animalsMap.put(position, animalsAtThisPosition);
            updateGenotypeCount(animal.getGenotype());
        } else {
            throw new IllegalArgumentException("Animal is placed out of bounds!");
        }
    }

    public void remove(Animal animal) {
        try {
            Vector2d position = animal.getPosition();
            List<Animal> animalsAtThisPosition = animalsMap.get(position);

            animalsAtThisPosition.remove(animal);

            if (animalsAtThisPosition.isEmpty()) {
                animalsMap.remove(position);
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
        return mapVisualizer.draw(lowerLeftBoundary, upperRightBoundary);
    }

    abstract public WorldElement objectAt(Vector2d position);
}
