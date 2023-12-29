package agh.ics.oop.model.worldMaps;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.Genotype;
import agh.ics.oop.model.movement.Vector2d;

import static agh.ics.oop.model.utils.CommonMethods.checkIfPositive;

public class Globe {
    private final int width;
    private final int height;

    private static final Vector2d lowerLeftBoundary = new Vector2d(1, 1);
    private final Vector2d upperRightBoundary;
    private int plantCount = 0;
    private int numberOfAnimals = 0;

    private Map<Vector2d, List<Animal>> animals = new HashMap<Vector2d, ArrayList<Animal>>();
    private Map<Vector2d, Plant> plants = new HashMap<Vector2d, Plant>();


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
            place(new Animal());
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

