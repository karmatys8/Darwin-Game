package agh.ics.oop.model.simulation;

import agh.ics.oop.model.animal.Animal;
import agh.ics.oop.model.movement.Vector2d;
import agh.ics.oop.model.util.RandomInteger;
import agh.ics.oop.model.worldMaps.AnimalConfig;
import agh.ics.oop.model.worldMaps.Globe;
import agh.ics.oop.model.worldMaps.PlantConfig;

import java.util.*;

public class Simulation implements Runnable {
    private final Globe globe;
    private Map<Vector2d, List<Animal>> animals = new HashMap<>();

    private int currentDay = 0;

    public Simulation(int width, int height, PlantConfig plantConfig, AnimalConfig animalConfig) {
        globe = new Globe(width, height, animals);

        for (int i = 0; i < animalConfig.startingCount(); i++) {
            globe.place(new Animal(new Vector2d(RandomInteger.getRandomInt(1, width),
                    RandomInteger.getRandomInt(1, height)), animalConfig));
        }
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


    @Override
    public void run() {
        try{
            while (! animals.isEmpty()) {
                killAnimals();

                Map<Vector2d, List<Animal>> deepCopiedAnimals = new HashMap<>(animals.size());
                for (Map.Entry<Vector2d, List<Animal>> entry : animals.entrySet()) {
                    List<Animal> animalList = entry.getValue();

                    List<Animal> copiedList = new ArrayList<>(animalList.size());
                    copiedList.addAll(animalList);

                    deepCopiedAnimals.put(entry.getKey(), copiedList);
                }

                for (Map.Entry<Vector2d, List<Animal>> animalEntry : deepCopiedAnimals.entrySet()) {
                    for (Animal animal : animalEntry.getValue()) {
                        globe.moveAnimal(animal);
                    }
                }

                currentDay++;
                Thread.sleep(500);
            }
        } catch (InterruptedException ignored) {}
    }
}
