package agh.ics.oop.model.worldMaps;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.Genotype;
import agh.ics.oop.model.movement.Vector2d;

import java.util.*;

public class Globe {
    private final int width;
    private final int height;

    private static final Vector2d lowerLeftBoundary = new Vector2d(1, 1);
    private final Vector2d upperRightBoundary;;
    private int plantCount;
    private final int energyPerPlant;
    private final int plantsPerDay;
    private int numberOfAnimals = 0;
    private final int animalsStartingEnergy;
    private final int minimalEnergyToReproduce;
    private final int energyUsedToReproduce;
    private final int minNumberOfMutations;
    private final int maxNumberOfMutations;
    private final int genotypeLength;
    private final Map<Genotype, Integer> genotypeCount = new HashMap<>();


    private Map<Vector2d, List<Animal>> animals = new HashMap<Vector2d, ArrayList<Animal>>();
    private Map<Vector2d, Plant> plants = new HashMap<Vector2d, Plant>();

    private void checkIfNotNegative(int value) throws IllegalArgumentException{
        if (value < 0) {
            throw new IllegalArgumentException();
        };
    }

    public Globe(int width, int height, int energyPerPlant, int plantsPerDay, int numberOfStartingAnimals,
                 int animalsStartingEnergy,int minEnergyToReproduce, int energyUsedToReproduce,
                 int minNumberOfMutations, int maxNumberOfMutations, int genotypeLength) throws IllegalArgumentException {

        if (width < 1) throw new IllegalArgumentException("Map's width must be positive");
        this.width = width;

        if (height < 1) throw new IllegalArgumentException("Map's height must be positive");
        this.height = height;

        upperRightBoundary = new Vector2d(width, height);


        checkIfNotNegative(energyPerPlant);
        this.energyPerPlant = energyPerPlant;

        checkIfNotNegative(plantsPerDay);
        this.plantsPerDay = plantsPerDay;


        checkIfNotNegative(animalsStartingEnergy);
        this.animalsStartingEnergy = animalsStartingEnergy;

        checkIfNotNegative(minEnergyToReproduce);
        this.minimalEnergyToReproduce = minEnergyToReproduce;
        checkIfNotNegative(energyUsedToReproduce);
        this.energyUsedToReproduce = energyUsedToReproduce;


        checkIfNotNegative(minNumberOfMutations);
        this.minNumberOfMutations = minNumberOfMutations;
        if (maxNumberOfMutations < minNumberOfMutations) {
            throw new IllegalArgumentException("Maximum number of mutations must be greater than minimal");
        }
        this.maxNumberOfMutations = maxNumberOfMutations;
        if (genotypeLength < 1) throw new IllegalArgumentException("Genotype length must be positive");
        this.genotypeLength = genotypeLength;

        checkIfNotNegative(numberOfStartingAnimals);
        for (int i = 0; i < numberOfStartingAnimals; i++) {
            place(new Animal(this));
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

        animals.put(animalsAtThisPosition);
        updateGenotypeCount(animal);
    }
    public void remove(Animal animal) {
        Vector2d position = animal.getPosition();
        List<Animal> animalsAtThisPosition = animals.get(position);

        if (animalsAtThisPosition != null) {
            animalsAtThisPosition.remove(animal);
            if (animalsAtThisPosition.isEmpty()) {
                animals.remove(position);
            }
        }

        removeGenotype(animal);
    }
    private void updateGenotypeCount(Animal animal) {
        Genotype animalGenotype = animal.getGenotype();

        genotypeCount.put(animalGenotype, genotypeCount.getOrDefault(animalGenotype, 0) + 1);
    }
    private void removeGenotype(Animal animal) {
        Genotype animalGenotype = animal.getGenotype();

        genotypeCount.put(animalGenotype, genotypeCount.getOrDefault(animalGenotype, 1) - 1);

        if (genotypeCount.get(animalGenotype) == 0) {
            genotypeCount.remove(animalGenotype);
        }
    }
    public Genotype findMostCommonGenotype() { //niedynamiczne- do poprawy
        Genotype mostCommonGenotype = null;
        int maxCount = 0;

        for (Map.Entry<Genotype, Integer> entry : genotypeCount.entrySet()) {
            if (entry.getValue() > maxCount) {
                mostCommonGenotype = entry.getKey();
                maxCount = entry.getValue();
            }
        }

        return mostCommonGenotype;
    }
    public int getAnimalsStartingEnergy() {
        return animalsStartingEnergy;
    }

    public int getGenotypeLength() {
        return genotypeLength;
    }

    public int getEnergyUsedToReproduce() {
        return energyUsedToReproduce;
    }

    public int getMinNumberOfMutations() {
        return minNumberOfMutations;
    }

    public int getMaxNumberOfMutations() {
        return maxNumberOfMutations;
    }

    public int getMinEnergyToReproduce() {
        return minimalEnergyToReproduce;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}

