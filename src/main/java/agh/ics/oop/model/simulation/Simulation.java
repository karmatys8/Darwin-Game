package agh.ics.oop.model.simulation;


import agh.ics.oop.controllers.SimulationController;
import agh.ics.oop.model.util.Average;
import agh.ics.oop.model.worldElements.animal.Animal;
import agh.ics.oop.model.movement.Vector2d;
import agh.ics.oop.model.util.MostCommonGenotype;
import agh.ics.oop.model.util.RandomInteger;
import agh.ics.oop.model.util.configs.AnimalConfig;
import agh.ics.oop.model.worldElements.animal.Genotype;
import agh.ics.oop.model.worldMaps.AbstractWorldMap;
import agh.ics.oop.model.util.configs.PlantConfig;
import agh.ics.oop.model.worldMaps.Plants;
import agh.ics.oop.model.worldMaps.Tunnels;

import java.util.*;

public class Simulation implements Runnable {
    private final AbstractWorldMap globe;
    private final Map<Vector2d, List<Animal>> animalsMap = new HashMap<>();
    private final Set<Animal> aliveAnimals = new HashSet<>();
    private final MostCommonGenotype mostCommonGenotype = new MostCommonGenotype();
    private final Plants plants;
    private final AnimalConfig animalConfig;
    private final PlantConfig plantConfig;
    private final int updateInterval;
    private int currentDay = 0;
    private final SimulationController controller;
    private boolean running = true;
    private Average animalsEnergy = new Average();
    private Average aliveAnimalsAge = new Average();
    private Average deadAnimalsAge = new Average();
    public Simulation(int width, int height, PlantConfig plantConfig, AnimalConfig animalConfig, int updateInterval, SimulationController controller) {
        plants = new Plants(width, height);
        plants.addPlants(plantConfig.startingCount());

        globe = new Tunnels(width, height, animalConfig, plantConfig, animalsMap, plants);
        this.controller = controller;
        this.animalConfig = animalConfig;
        for (int i = 0; i < animalConfig.startingCount(); i++) {
            Animal animal = new Animal(new Vector2d(RandomInteger.getRandomInt(1, width),
                    RandomInteger.getRandomInt(1, height)), animalConfig);
            globe.place(animal);
            aliveAnimals.add(animal);
            mostCommonGenotype.insert(animal.getGenotype());
        }
        this.plantConfig = plantConfig;
        this.updateInterval = updateInterval;
    }

    private void killAnimal(Animal animal) {
        mostCommonGenotype.remove(animal.getGenotype());
        deadAnimalsAge.add(animal.getDaysLived());
        globe.remove(animal);
        aliveAnimals.remove(animal);
        animal.kill(currentDay);
    }

    private void killAnimals() {
        List<Animal> animalsToKill = new ArrayList<>();
        for (Animal animal : aliveAnimals) {
            if (animal.getEnergy() <= 0) {
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
        animalsEnergy.add(animal.getEnergy());
        aliveAnimalsAge.add(animal.getDaysLived());
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
                    globe.place(newBorn);
                    aliveAnimals.add(newBorn);
                    mostCommonGenotype.insert(newBorn.getGenotype());
                }
            }
        }
    }

    public boolean isRunning() {
        return running;
    }

    public void pauseSimulation() {
        running = false;
    }

    public void resumeSimulation() {
        running = true;
    }

    public int getCurrentDay(){
        return currentDay;
    }

    public int getNumberOfAnimals(){
        return aliveAnimals.size();
    }

    public int getNumberOfPlants(){
        return plants.getNumberOfPlants();
    }

    public Genotype getMostCommonGenotype(){ return mostCommonGenotype.getMostCommonGenotype(); }

    public Average[] getSimulationStats(){
        Average[] simulationStats = {animalsEnergy, aliveAnimalsAge, deadAnimalsAge};
        return simulationStats;
    }

    public void run() {
        try {
            if (!aliveAnimals.isEmpty() && running) {
                setUpAverages();
                killAnimals();
                moveAnimals();
                feedAndReproduceAnimals();
                plants.addPlants(plantConfig.plantsPerDay());

                currentDay++;
                Thread.sleep(updateInterval);
            } else {
                running = false;
            }
        } catch (InterruptedException ignored) {}
    }

    public AbstractWorldMap getMap() {
        return globe;
    }
    private void setUpAverages(){
        animalsEnergy = new Average();
        aliveAnimalsAge = new Average();
    }
}
