package agh.ics.oop.model.animal;

import agh.ics.oop.model.movement.MapDirection;
import agh.ics.oop.model.movement.Vector2d;
import agh.ics.oop.model.util.RandomInteger;
import agh.ics.oop.model.worldMaps.AnimalConfig;

public class Animal {
    private Vector2d position;
    private MapDirection direction;
    private final Genotype genotype;
    private int energy;
    private int daysLived=0;
    private int eatenPlants=0;
    private int dayOfDeath;
    private int currentGeneIndex;
    private final Animal mother;
    private final Animal father;
    private int descendants=0;
    private int children=0;
    private final int minEnergyToReproduce;


    public Animal(Vector2d position, AnimalConfig animalConfig) {
        this.position=position;
        this.direction=MapDirection.values()[RandomInteger.getRandomInt(7)];
        this.energy=animalConfig.startingEnergy();
        this.genotype=new Genotype(animalConfig.genomeLength(), animalConfig.minNumberOfMutations(), animalConfig.maxNumberOfMutations());
        this.currentGeneIndex=RandomInteger.getRandomInt(animalConfig.genomeLength() - 1);
        this.minEnergyToReproduce=animalConfig.minEnergyToReproduce();
        this.mother=null;
        this.father=null;
    }

    public Animal(Animal mother, Animal father, AnimalConfig animalConfig) {
        this.position=mother.getPosition();
        this.direction=MapDirection.values()[RandomInteger.getRandomInt(7)];
        this.energy= animalConfig.energyUsedToReproduce()*2;
        this.genotype=new Genotype(mother,father);
        this.currentGeneIndex=RandomInteger.getRandomInt(animalConfig.genomeLength() - 1);
        this.minEnergyToReproduce=animalConfig.minEnergyToReproduce();
        mother.useEnergy(animalConfig.energyUsedToReproduce());
        father.useEnergy(animalConfig.energyUsedToReproduce());
        this.mother=mother;
        this.father=father;
        mother.updateChildren();
        mother.updateDescendants();
        father.updateChildren();
        father.updateDescendants();
    }

    private void updateDescendants() {
        this.descendants ++;
        if (this.father != null) {
            this.father.updateDescendants();
        }
        if (this.mother != null) {
            this.mother.updateDescendants();
        }
    }

    private void updateChildren() {
        this.children++;
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
    public int getCurrentGeneIndex(){ return currentGeneIndex;}

    private void useEnergy(int energyUsedToReproduce) {
        this.energy-=energyUsedToReproduce;
    }
    public void nextGene(){
        this.currentGeneIndex=(this.currentGeneIndex+1)%genotype.getGenes().size();
    }

    public boolean canReproduce(){
        return this.energy>=this.minEnergyToReproduce;
    }

    public int getNumberOfDescendants() {
        return descendants;
    }

    public int getNumberOfChildren() {
        return children;
    }
}
