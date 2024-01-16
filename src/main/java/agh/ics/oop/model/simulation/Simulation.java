package agh.ics.oop.model.simulation;

import agh.ics.oop.model.animal.Animal;
import agh.ics.oop.model.movement.Vector2d;
import agh.ics.oop.model.util.MostCommonGenotype;
import agh.ics.oop.model.util.RandomInteger;
import agh.ics.oop.model.util.configs.AnimalConfig;
import agh.ics.oop.model.worldMaps.Globe;
import agh.ics.oop.model.util.configs.PlantConfig;
import agh.ics.oop.model.worldMaps.Plants;

import java.util.*;

public class Simulation implements Runnable {
    private final Globe globe;
    private final Map<Vector2d, List<Animal>> animalsMap = new HashMap<>();
    private final Set<Animal> aliveAnimals = new HashSet<>();
    private final MostCommonGenotype mostCommonGenotype = new MostCommonGenotype();
    private final Plants plants;
    private final AnimalConfig animalConfig;
    private final PlantConfig plantConfig;
    private int currentDay = 0;

    public Simulation(int width, int height, PlantConfig plantConfig, AnimalConfig animalConfig) {
        plants = new Plants(width, height);
        plants.addPlants(plantConfig.startingCount());

        globe = new Globe(width, height, animalConfig, plantConfig, animalsMap, plants);

        this.animalConfig = animalConfig;
        for (int i = 0; i < animalConfig.startingCount(); i++) {
            Animal animal = new Animal(new Vector2d(RandomInteger.getRandomInt(1, width),
                    RandomInteger.getRandomInt(1, height)), animalConfig);
            globe.place(animal);

            aliveAnimals.add(animal);
            mostCommonGenotype.insert(animal.getGenotype());
        }

        this.plantConfig = plantConfig;
    }

    private void killAnimal(Animal animal) {
        mostCommonGenotype.remove(animal.getGenotype());

        Vector2d position = animal.getPosition();

        List<Animal> prevAnimals = animalsMap.remove(position);
        prevAnimals.remove(animal);
        if (!prevAnimals.isEmpty()) animalsMap.put(position, prevAnimals);

        animal.kill(currentDay);
        aliveAnimals.remove(animal);
    }

    private void killAnimals() {
        List<Animal> animalsToKill = new ArrayList<>();
        for (Animal animal : aliveAnimals) {
            int animalEnergy = animal.getEnergy();

            if (animalEnergy <= 0) {
                animalsToKill.add(animal);
            }
        }

        for (Animal animal : animalsToKill) {
            killAnimal(animal);
        }
    }

    private void moveAnimal(Animal animal) {
        Vector2d oldPosition = animal.getPosition();
        animal.move(globe);

        Vector2d newPosition = animal.getPosition();
        if (oldPosition != newPosition) {
            List<Animal> prevAnimals = animalsMap.get(oldPosition);
            prevAnimals.remove(animal);

            if (prevAnimals.isEmpty()) animalsMap.remove(oldPosition);

            List<Animal> currAnimals = animalsMap.getOrDefault(newPosition, new ArrayList<>(1));
            currAnimals.add(animal);

            animalsMap.put(newPosition, currAnimals);
        }
    }

    private void moveAnimals() {
        for (Animal animal : aliveAnimals) {
            moveAnimal(animal);
        }
    }

    private void feedAndReproduceAnimals() {
        for (Vector2d position : animalsMap.keySet()) {
            if (plants.wasEaten(position)) {
                animalsMap.get(position).get(0).eat(plantConfig.energyPerPlant());
            }


            List<Animal> currAnimals = animalsMap.get(position);
            if (currAnimals.size() >= 2) {
                Animal animal1 = currAnimals.get(0);
                Animal animal2 = currAnimals.get(1);
                if (animal1.canReproduce() && animal2.canReproduce()) {
                    Animal newBorn = new Animal(animal1, animal2, animalConfig);
                    currAnimals.add(newBorn);

                    aliveAnimals.add(newBorn);
                    mostCommonGenotype.insert(newBorn.getGenotype());
                }
            }
        }
    }

    @Override
    public void run() {
        try{
            while (! aliveAnimals.isEmpty()) {
                killAnimals();
                moveAnimals();
                feedAndReproduceAnimals();
                plants.addPlants(plantConfig.plantsPerDay());

                currentDay++;
                Thread.sleep(500);
            }
        } catch (InterruptedException ignored) {}
    }
}
