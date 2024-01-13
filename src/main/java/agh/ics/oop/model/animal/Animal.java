package agh.ics.oop.model.animal;

import agh.ics.oop.model.movement.MapDirection;
import agh.ics.oop.model.movement.Vector2d;
import agh.ics.oop.model.util.RandomInteger;
import agh.ics.oop.model.util.configs.AnimalConfig;
import agh.ics.oop.model.worldMaps.Globe;
import agh.ics.oop.model.worldMaps.Plant;

import java.util.HashSet;
import java.util.Set;

public class Animal {
    private Vector2d position;
    private MapDirection direction;
    private Genotype genotype;
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
        this.position = position;
        this.direction = MapDirection.values()[RandomInteger.getRandomInt(7)];

        this.genotype = new RandomGenotype(animalConfig.genomeLength(), animalConfig.minNumberOfMutations(), animalConfig.maxNumberOfMutations());
        this.currentGeneIndex = RandomInteger.getRandomInt(animalConfig.genomeLength() - 1);

        this.energy = animalConfig.startingEnergy();
        this.minEnergyToReproduce = animalConfig.minEnergyToReproduce();

        this.mother = null;
        this.father = null;
    }

    public Animal(Animal mother, Animal father, AnimalConfig animalConfig) {
        this.position = mother.getPosition();
        this.direction = MapDirection.values()[RandomInteger.getRandomInt(7)];

        this.genotype = new RandomGenotype(mother,father);
        this.currentGeneIndex = RandomInteger.getRandomInt(animalConfig.genomeLength() - 1);

        this.energy = animalConfig.energyUsedToReproduce()*2;
        this.minEnergyToReproduce = animalConfig.minEnergyToReproduce();

        this.mother = mother;
        this.father = father;

        mother.useEnergy(animalConfig.energyUsedToReproduce());
        father.useEnergy(animalConfig.energyUsedToReproduce());

        mother.updateChildren();
        father.updateChildren();

        Set<Animal> ancestors = new HashSet<>();
        mother.updateDescendants(ancestors);
        father.updateDescendants(ancestors);
    }

    private void updateDescendants(Set<Animal> ancestors) {
        descendants++;
        if (father != null) {
            if (!ancestors.contains(father)) {
                ancestors.add(father);
                father.updateDescendants(ancestors);
            }
        }
        if (mother != null) {
            if(!ancestors.contains(mother)) {
                ancestors.add(mother);
                mother.updateDescendants(ancestors);
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

    public void move(Globe globe) { // I feel like animal should not receive globe
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
        energy += plant.getEnergy();
    }
}
