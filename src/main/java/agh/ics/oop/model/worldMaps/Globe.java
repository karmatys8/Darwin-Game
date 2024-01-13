package agh.ics.oop.model.worldMaps;

import agh.ics.oop.model.animal.Animal;
import agh.ics.oop.model.movement.Vector2d;
import agh.ics.oop.model.util.MostCommonGenotype;
import agh.ics.oop.model.util.MapVisualizer;
import agh.ics.oop.model.util.RandomInteger;
import agh.ics.oop.model.util.configs.AnimalConfig;
import agh.ics.oop.model.util.configs.PlantConfig;

import java.util.*;

import static agh.ics.oop.model.util.CommonMethods.checkIfPositive;


public class Globe extends AbstractWorldMap {
    public Globe(int width, int height, AnimalConfig animalConfig, PlantConfig plantConfig,
                            Map<Vector2d, List<Animal>> animals, Map<Vector2d, Plant> plants) {
        super(width, height, animalConfig, plantConfig, animals, plants);
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.precedes(upperRightBoundary) && position.follows(lowerLeftBoundary);
    }

    @Override
    public Object objectAt(Vector2d position) {
        List<Animal> animalsAtThisPosition = animals.get(position);
        if (animalsAtThisPosition!=null) return animalsAtThisPosition;
        return plants.get(position);
    }

    public void moveAnimal(Animal animal) {
        Vector2d oldPosition = animal.getPosition();
        animal.move(this);

        Vector2d newPosition = animal.getPosition();
        if (oldPosition != newPosition) {
            List<Animal> prevAnimals = animals.get(oldPosition);
            prevAnimals.remove(animal);

            if (prevAnimals.isEmpty()) animals.remove(oldPosition);

            List<Animal> currAnimals = animals.getOrDefault(newPosition, new ArrayList<>(1));
            currAnimals.add(animal);

            animals.put(newPosition, currAnimals);
        }
    }

    public void killAnimal(Animal animal, int currentDay) {
        Vector2d position = animal.getPosition();
        animal.kill(currentDay);

        List<Animal> prevAnimals = animals.remove(position);
        prevAnimals.remove(animal);

        if (!prevAnimals.isEmpty()) animals.put(position, prevAnimals);

        // change genotype Heap here
    }

    public void animalEats(Vector2d position) {
        animals.get(position).get(0).eat(plants.remove(position));
    }
}

