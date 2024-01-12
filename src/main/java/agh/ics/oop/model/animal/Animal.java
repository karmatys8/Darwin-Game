package agh.ics.oop.model.animal;

import agh.ics.oop.model.movement.MapDirection;
import agh.ics.oop.model.movement.Vector2d;
import agh.ics.oop.model.util.RandomInteger;
import agh.ics.oop.model.util.configs.AnimalConfig;

import java.util.HashSet;
import java.util.Set;

public class Animal {
    private Vector2d position;
    private MapDirection direction;
    private final Genotype genotype;
    private int energy;
    private int daysLived=0;
    private int plantsEaten=0;
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
        this.genotype=new RandomGenotype(animalConfig.genomeLength(), animalConfig.minNumberOfMutations(), animalConfig.maxNumberOfMutations());
        this.currentGeneIndex=RandomInteger.getRandomInt(animalConfig.genomeLength() - 1);
        this.minEnergyToReproduce=animalConfig.minEnergyToReproduce();
        this.mother=null;
        this.father=null;
    }

    public Animal(Animal mother, Animal father, AnimalConfig animalConfig) {
        this.position=mother.getPosition();
        this.direction=MapDirection.values()[RandomInteger.getRandomInt(7)];
        this.energy= animalConfig.energyUsedToReproduce()*2;
        this.genotype=new RandomGenotype(mother,father);
        this.currentGeneIndex=RandomInteger.getRandomInt(animalConfig.genomeLength() - 1);
        this.minEnergyToReproduce=animalConfig.minEnergyToReproduce();
        mother.useEnergy(animalConfig.energyUsedToReproduce());
        father.useEnergy(animalConfig.energyUsedToReproduce());
        Set<Animal> ancestors = new HashSet<>();
        this.mother=mother;
        this.father=father;
        mother.updateChildren();
        mother.updateDescendants(ancestors);
        father.updateChildren();
        father.updateDescendants(ancestors);
    }

    private void updateDescendants(Set<Animal> ancestors) {
        this.descendants ++;
        if (this.father != null) {
            if (!ancestors.contains(father)) {
                ancestors.add(father);
                this.father.updateDescendants(ancestors);
            }
        }
        if (this.mother != null) {
            if(!ancestors.contains(mother)) {
                ancestors.add(mother);
                this.mother.updateDescendants(ancestors);
            }
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
        return new RandomGenotype(genotype);
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

    public String toShortString() {
        return (direction.toString());

    }
}
