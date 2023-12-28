package agh.ics.oop.model.worldMaps;

import agh.ics.oop.model.movement.Vector2d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

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
    private final int genomeLength;

    private Map<Vector2d, List<Animal>> animals = new HashMap<Vector2d, ArrayList<Animal>>();
    private Map<Vector2d, Plant> plants = new HashMap<Vector2d, Plant>();

    public Globe(int width, int height, int energyPerPlant, int plantsPerDay, int numberOfStartingAnimals,
                 int animalsStartingEnergy,int minimalEnergyToReproduce, int energyUsedToReproduce,
                 int minNumberOfMutations, int maxNumberOfMutations, int genomeLength) {
        this.width = width;
        this.height = height;
        upperRightBoundary = new Vector2d(width, height);
        this.energyPerPlant = energyPerPlant;
        this.plantsPerDay = plantsPerDay;
        Random random = new Random();
        for (int i = 0; i < numberOfStartingAnimals; i++) {
            place(new Animal(new Vector2d(random.nextInt(width) + 1, random.nextInt(height) + 1)));
        }
        this.animalsStartingEnergy = animalsStartingEnergy;
        this.minimalEnergyToReproduce = minimalEnergyToReproduce;
        this.energyUsedToReproduce = energyUsedToReproduce;
        this.minNumberOfMutations = minNumberOfMutations;
        this.maxNumberOfMutations = maxNumberOfMutations;
        this.genomeLength = genomeLength;
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
    }
}
