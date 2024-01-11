package agh.ics.oop.model.animal;

import agh.ics.oop.model.movement.MapDirection;
import agh.ics.oop.model.movement.Vector2d;
import agh.ics.oop.model.util.RandomInteger;
import agh.ics.oop.model.worldMaps.AnimalConfig;
import agh.ics.oop.model.worldMaps.Globe;
import agh.ics.oop.model.worldMaps.Plant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Animal {
    private Vector2d position;
    private MapDirection direction;
    private Genotype genotype;
    private int energy;
    private int daysLived=0;
    private int plantsEaten=0;
    private int dayOfDeath;
    private int currentGeneIndex;
    private AnimalTree animalTree;
    private int minEnergyToReproduce;


    public Animal(Vector2d position, AnimalConfig animalConfig) {
        this.position=position;
        this.direction=MapDirection.values()[RandomInteger.getRandomInt(7)];
        this.energy=animalConfig.startingEnergy();
        this.genotype=new Genotype(animalConfig.genomeLength(), animalConfig.minNumberOfMutations(), animalConfig.maxNumberOfMutations());
        this.currentGeneIndex=RandomInteger.getRandomInt(animalConfig.genomeLength() - 1);
        this.animalTree=new AnimalTree(this);
        this.minEnergyToReproduce=animalConfig.minEnergyToReproduce();
    }

    public Animal(Animal mother, Animal father, AnimalConfig animalConfig) {
        this.position=mother.getPosition();
        this.direction=MapDirection.values()[RandomInteger.getRandomInt(7)];
        this.energy= animalConfig.energyUsedToReproduce()*2;
        this.genotype=new Genotype(mother,father);
        this.currentGeneIndex=RandomInteger.getRandomInt(animalConfig.genomeLength() - 1);
        this.minEnergyToReproduce=animalConfig.minEnergyToReproduce();
        mother.useEnergy(animalConfig.energyUsedToReproduce());
        mother.animalTree.addChild(this.animalTree);
        father.useEnergy(animalConfig.energyUsedToReproduce());
        father.animalTree.addChild(this.animalTree);
    }

    public Animal(Animal animal) {
        this.position = animal.position;
        this.direction = animal.direction;
        this.energy = animal.energy;
        this.genotype = animal.getGenotype();
        this.currentGeneIndex = animal.currentGeneIndex;
        this.animalTree = animal.animalTree;
        this.minEnergyToReproduce = animal.minEnergyToReproduce;
        this.daysLived = animal.daysLived;
        this.plantsEaten = animal.plantsEaten;
        this.dayOfDeath = animal.dayOfDeath;
    }

    private void useEnergy(int energyUsedToReproduce) {
        this.energy-=energyUsedToReproduce;
    }
    public void nextGene(){
        this.currentGeneIndex=(this.currentGeneIndex+1)%genotype.getGenes().size();
    }

    public boolean canReproduce(){
        return this.energy>=this.minEnergyToReproduce;
    }

    public int getNumberOfChildren(){
        return animalTree.getChildrenCount();
    }
    public int getNumberOfDescendants(){
        return animalTree.getDescendantsCount();
    }

    public Vector2d getPosition() {
        return position;
    }

    public int getEnergy() {
        return energy;
    }

    public Genotype getGenotype() {
        return new Genotype(genotype);
    }

    public void move(Globe globe) {
        energy--;
        daysLived++;

        direction = direction.turnRight(genotype.getCurrentGene(this.currentGeneIndex));
        this.nextGene();

        Vector2d newPosition = position.add(direction.getUnitVector());
        if (globe.canMoveTo(newPosition)) {
            position = newPosition;
        }
    }

    public void kill(int dayOfDeath) {
        this.dayOfDeath = dayOfDeath;
        this.position = null;
        this.direction = null;
        this.genotype = null;
    }

    public void eat(Plant plant) {
        plantsEaten++;
        energy+=plant.getEnergy();
    }
}
