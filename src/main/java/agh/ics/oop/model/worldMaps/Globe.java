package agh.ics.oop.model.worldMaps;

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

        upperRightBoundary = new Vector2d(width, height);


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
    }
}
