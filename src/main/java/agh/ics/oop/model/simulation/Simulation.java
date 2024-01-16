package agh.ics.oop.model.simulation;

import agh.ics.oop.model.worldElements.animal.Animal;
import agh.ics.oop.model.movement.Vector2d;
import agh.ics.oop.model.util.RandomInteger;
import agh.ics.oop.model.util.configs.AnimalConfig;
import agh.ics.oop.model.worldMaps.Globe;
import agh.ics.oop.model.worldMaps.Plant;
import agh.ics.oop.model.util.configs.PlantConfig;

import java.util.*;

public class Simulation implements Runnable {
    private final Globe globe;
    private final Map<Vector2d, List<Animal>> animalsMap = new HashMap<>();
    private final Set<Animal> aliveAnimals = new HashSet<>();
    private final Map<Vector2d, Plant> plants = new HashMap<>();
    private final AnimalConfig animalConfig;
    private int currentDay = 0;

    public Simulation(int width, int height, PlantConfig plantConfig, AnimalConfig animalConfig) {
        globe = new Globe(width, height, animalConfig, plantConfig, animalsMap, plants);

        this.animalConfig = animalConfig;
        for (int i = 0; i < animalConfig.startingCount(); i++) {
            Animal animal = new Animal(new Vector2d(RandomInteger.getRandomInt(1, width),
                    RandomInteger.getRandomInt(1, height)), animalConfig);
            globe.place(animal);
            aliveAnimals.add(animal);
        }

        // initialize plants
    }

    private void killAnimal(Animal animal) {
        aliveAnimals.remove(animal);

        Vector2d position = animal.getPosition();
        animal.kill(currentDay);

        List<Animal> prevAnimals = animalsMap.remove(position);
        prevAnimals.remove(animal);

        if (!prevAnimals.isEmpty()) animalsMap.put(position, prevAnimals);

        // change genotype Heap here
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
            Plant plantToBeEaten = plants.remove(position);

            if (plantToBeEaten != null) {
                animalsMap.get(position).get(0).eat(plantToBeEaten);
            }


            List<Animal> currAnimals = animalsMap.get(position);
            if (currAnimals.size() >= 2) {
                Animal animal1 = currAnimals.get(0);
                Animal animal2 = currAnimals.get(1);
                if (animal1.canReproduce() && animal2.canReproduce()) {
                    Animal newBorn = new Animal(animal1, animal2, animalConfig);
                    currAnimals.add(newBorn);
                    aliveAnimals.add(newBorn);
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

                currentDay++;
                Thread.sleep(500);
            }
        } catch (InterruptedException ignored) {}
    }
}
