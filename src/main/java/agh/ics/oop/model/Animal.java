package agh.ics.oop.model;

import agh.ics.oop.model.movement.MapDirection;
import agh.ics.oop.model.movement.Vector2d;
import agh.ics.oop.model.util.AnimalTree;
import agh.ics.oop.model.util.RandomInteger;
import agh.ics.oop.model.worldMaps.Globe;

public class Animal {
    private final Globe globe;
    private Vector2d position;
    private MapDirection direction;
    private Genotype genotype;
    private int energy;
    private int daysLived=0;
    private int eatenPlants=0;
    private int dayOfDeath;
    private int currentGeneIndex;
    private AnimalTree animalTree;


    public Animal(Globe globe) {
        this.globe=globe;
        this.position=new Vector2d(RandomInteger.getRandomInt(globe.getWidth()) + 1, RandomInteger.getRandomInt(globe.getHeight()) + 1);
        this.direction=MapDirection.values()[RandomInteger.getRandomInt(8)];
        this.energy=globe.getAnimalsStartingEnergy();
        this.genotype=new Genotype(globe.getGenotypeLength());
        this.currentGeneIndex=RandomInteger.getRandomInt(globe.getGenotypeLength());
        this.animalTree=new AnimalTree(this);
    }

    public Animal(Animal mother, Animal father) {
        this.globe=mother.getGlobe();
        this.position=mother.getPosition();
        this.direction=MapDirection.values()[RandomInteger.getRandomInt(8)];
        this.energy=globe.getEnergyUsedToReproduce()*2;
        this.genotype=new Genotype(mother,father);
        this.currentGeneIndex=RandomInteger.getRandomInt(globe.getGenotypeLength());
        mother.useEnergy(globe.getEnergyUsedToReproduce());
        mother.animalTree.addChild(this.animalTree);
        father.useEnergy(globe.getEnergyUsedToReproduce());
        father.animalTree.addChild(this.animalTree);
    }

    private void useEnergy(int energyUsedToReproduce) {
        this.energy-=energyUsedToReproduce;
    }
    public void nextGene(){
        this.currentGeneIndex=(this.currentGeneIndex+1)%globe.getGenotypeLength();
    }

    public boolean canReproduce(){
        return this.energy>=globe.getMinEnergyToReproduce();
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
        return genotype;
    }

    public Globe getGlobe() {
        return globe;
    }


}
