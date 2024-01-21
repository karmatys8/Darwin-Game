package agh.ics.oop.model.simulation;


import agh.ics.oop.controllers.SimulationController;
import agh.ics.oop.model.util.Average;
import agh.ics.oop.model.animal.AnimalComparator;

import agh.ics.oop.model.animal.Animal;
import agh.ics.oop.model.movement.Vector2d;
import agh.ics.oop.model.util.MostCommonGenotype;
import agh.ics.oop.model.util.RandomInteger;
import agh.ics.oop.model.util.configs.AnimalConfig;
import agh.ics.oop.model.animal.Genotype;
import agh.ics.oop.model.worldMaps.AbstractWorldMap;
import agh.ics.oop.model.util.configs.PlantConfig;
import agh.ics.oop.model.worldMaps.Plants;
import agh.ics.oop.model.worldMaps.WorldMapFactory;

import java.util.*;

public class Simulation implements Runnable {
    private final AbstractWorldMap worldMap;
    private final Map<Vector2d, List<Animal>> animalsMap = new HashMap<>();
    private final Set<Animal> aliveAnimals = new HashSet<>();
    private final MostCommonGenotype mostCommonGenotype = new MostCommonGenotype();
    private final Plants plants;
    private final AnimalConfig animalConfig;
    private final PlantConfig plantConfig;
    private final int updateInterval;
    private final String mapOption;
    private int currentDay = 0;
    private final SimulationController controller;
    private boolean running = true;
    private Average animalsEnergy = new Average();
    private Average aliveAnimalsAge = new Average();
    private Average deadAnimalsAge = new Average();
    private static final AnimalComparator animalComparator = new AnimalComparator();
    private final UUID id = UUID.randomUUID();
    public Simulation(int width, int height, PlantConfig plantConfig, AnimalConfig animalConfig, int updateInterval, String mapOption, SimulationController controller) {
        plants = new Plants(width, height);
        plants.addPlants(plantConfig.startingCount());

        worldMap = WorldMapFactory.getWorldMap(mapOption,width, height, animalConfig, plantConfig, animalsMap, plants);
        this.controller = controller;
        this.animalConfig = animalConfig;

        initializeAnimals(width, height, animalConfig);

        this.plantConfig = plantConfig;
        this.updateInterval = updateInterval;
        this.mapOption = mapOption;
    }

    private void initializeAnimals(int width, int height, AnimalConfig animalConfig) {
        for (int i = 0; i < animalConfig.startingCount(); i++) {
            Animal animal = new Animal(new Vector2d(RandomInteger.getRandomInt(1, width),
                    RandomInteger.getRandomInt(1, height)), animalConfig);
            worldMap.place(animal);
            aliveAnimals.add(animal);
            mostCommonGenotype.insert(animal.getGenotype());
        }
    }

    private void killAnimal(Animal animal) {
        mostCommonGenotype.remove(animal.getGenotype());
        deadAnimalsAge.add(animal.getDaysLived());
        worldMap.remove(animal);
        aliveAnimals.remove(animal);
        animal.kill(currentDay);
    }

    private void killAnimals() {
        Iterator<Animal> iterator = aliveAnimals.iterator();
        while (iterator.hasNext()) {
            Animal animal = iterator.next();
            if (animal.getEnergy() <= 0) {
                iterator.remove();
                killAnimal(animal);
            }
        }
    }

    private void moveAnimal(Animal animal) {
        Vector2d oldPosition = animal.getPosition();
        animal.move(worldMap);

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

    private void feedAndReproduceAnimals() {
        for (Vector2d position : animalsMap.keySet()) {
            List<Animal> currAnimals = animalsMap.get(position);
            currAnimals.sort(animalComparator);

            if (plants.wasEaten(position)) {
               currAnimals.get(0).eat(plantConfig.energyPerPlant());
            }

            if (currAnimals.size() >= 2) {
                Animal animal1 = currAnimals.get(0);
                Animal animal2 = currAnimals.get(1);
                if (animal1.canReproduce() && animal2.canReproduce()) {
                    Animal newBorn = new Animal(animal1, animal2, animalConfig);
                    worldMap.place(newBorn);
                    aliveAnimals.add(newBorn);
                    mostCommonGenotype.insert(newBorn.getGenotype());
                }
            }
        }
    }

    public boolean isNotRunning() {
        return !running;
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
        return new Average[]{animalsEnergy, aliveAnimalsAge, deadAnimalsAge};
    }

    public void run() {
        try {
            if (!aliveAnimals.isEmpty() && running) {
                setUpAverages();
                killAnimals();
                aliveAnimals.forEach(this::moveAnimal);
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
        return worldMap;
    }

    private void setUpAverages(){
        animalsEnergy = new Average();
        aliveAnimalsAge = new Average();
    }

    public Set<Animal> getAliveAnimals() {
        return new HashSet<>(aliveAnimals);
    }

    public UUID getId() {
        return id;
    }
}
