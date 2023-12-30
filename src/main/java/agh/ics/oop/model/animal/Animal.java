package agh.ics.oop.model.animal;

import agh.ics.oop.model.movement.MapDirection;
import agh.ics.oop.model.movement.Vector2d;
import agh.ics.oop.model.util.RandomInteger;
import agh.ics.oop.model.worldMaps.AnimalConfig;

public class Animal {
    private Vector2d position;
    private MapDirection direction;
    private Genotype genotype;
    private int energy;
    private int daysLived=0;
    private int eatenPlants=0;
    private int dayOfDeath;
    private int currentGeneIndex;
    private AnimalTree animalTree;
    private int minEnergyToReproduce;


    public Animal(Vector2d position, AnimalConfig animalConfig) {
        this.position=position;
        this.direction=MapDirection.values()[RandomInteger.getRandomInt(7)];
        this.energy=animalConfig.startingEnergy();
        this.genotype=new Genotype(animalConfig.genomeLength(), animalConfig.minNumberOfMutations(), animalConfig.maxNumberOfMutations());
        this.currentGeneIndex=RandomInteger.getRandomInt(animalConfig.genomeLength());
        this.animalTree=new AnimalTree(this);
        this.minEnergyToReproduce=animalConfig.minEnergyToReproduce();
    }

    public Animal(Animal mother, Animal father, AnimalConfig animalConfig) {
        this.position=mother.getPosition();
        this.direction=MapDirection.values()[RandomInteger.getRandomInt(7)];
        this.energy= animalConfig.energyUsedToReproduce()*2;
        this.genotype=new Genotype(mother,father);
        this.currentGeneIndex=RandomInteger.getRandomInt(animalConfig.genomeLength());
        this.minEnergyToReproduce=animalConfig.minEnergyToReproduce();
        mother.useEnergy(animalConfig.energyUsedToReproduce());
        mother.animalTree.addChild(this.animalTree);
        father.useEnergy(animalConfig.energyUsedToReproduce());
        father.animalTree.addChild(this.animalTree);
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

    public String toShortString() {
        return (direction.toString());
    }
}
