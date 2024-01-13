package agh.ics.oop.model.simulation;

import agh.ics.oop.model.animal.Animal;
import agh.ics.oop.model.movement.Vector2d;
import agh.ics.oop.model.util.RandomInteger;
import agh.ics.oop.model.util.configs.AnimalConfig;
import agh.ics.oop.model.worldMaps.Globe;
import agh.ics.oop.model.worldMaps.Plant;
import agh.ics.oop.model.util.configs.PlantConfig;

import java.util.*;

public class Simulation implements Runnable {
    private final Globe globe;
    private final Map<Vector2d, List<Animal>> animals = new HashMap<>();
    private final Map<Vector2d, Plant> plants = new HashMap<>();
    private final AnimalConfig animalConfig;

    private int currentDay = 0;

    public Simulation(int width, int height, PlantConfig plantConfig, AnimalConfig animalConfig) {
        globe = new Globe(width, height, animalConfig, plantConfig, animals, plants);

        this.animalConfig = animalConfig;
        for (int i = 0; i < animalConfig.startingCount(); i++) {
            globe.place(new Animal(new Vector2d(RandomInteger.getRandomInt(1, width),
                    RandomInteger.getRandomInt(1, height)), animalConfig));
        }

        // initialize plants
    }

    private void killAnimals() {
        List<Animal> animalsToKill = new ArrayList<>();
        for (Map.Entry<Vector2d, List<Animal>> animalEntry : animals.entrySet()) {
            for (Animal animal : animalEntry.getValue()) {
                int animalEnergy = animal.getEnergy();

                if (animalEnergy <= 0) {
                    animalsToKill.add(animal);
                }
            }
        }

        for (Animal animal : animalsToKill) {
            globe.killAnimal(animal, currentDay);
        }
    }

    // copies both Map and Lists while keeping Animals the same to enable reference comparisons
    private Map<Vector2d, List<Animal>> deepCopyAnimals() {
        Map<Vector2d, List<Animal>> deepCopiedAnimals = new HashMap<>(animals.size());
        for (Map.Entry<Vector2d, List<Animal>> entry : animals.entrySet()) {
            List<Animal> animalList = entry.getValue();

            List<Animal> copiedList = new ArrayList<>(animalList.size());
            copiedList.addAll(animalList);

            deepCopiedAnimals.put(entry.getKey(), copiedList);
        }

        return deepCopiedAnimals;
    }

    private void moveAnimals() {
        for (Map.Entry<Vector2d, List<Animal>> animalEntry : deepCopyAnimals().entrySet()) {
            for (Animal animal : animalEntry.getValue()) {
                globe.moveAnimal(animal);
            }
        }
    }

    private void feedAndReproduceAnimals() {
        for (Vector2d position : animals.keySet()) {
            Plant plantToBeEaten = plants.get(position);

            if (plantToBeEaten != null) {
                globe.animalEats(position);
            }


            List<Animal> currAnimals = animals.get(position);
            if (currAnimals.size() >= 2) {
                Animal animal1 = currAnimals.get(0);
                Animal animal2 = currAnimals.get(1);
                if (animal1.canReproduce() && animal2.canReproduce()) {
                    currAnimals.add(new Animal(animal1, animal2, animalConfig));
                }
            }
        }
    }

    @Override
    public void run() {
        try{
            while (! animals.isEmpty()) {
                killAnimals();
                moveAnimals();
                feedAndReproduceAnimals();

                currentDay++;
                Thread.sleep(500);
            }
        } catch (InterruptedException ignored) {}
    }
}
