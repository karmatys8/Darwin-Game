package agh.ics.oop.model.animal;

import agh.ics.oop.model.movement.MapDirection;
import agh.ics.oop.model.movement.Vector2d;
import agh.ics.oop.model.util.RandomInteger;
import agh.ics.oop.model.util.configs.AnimalConfig;
import agh.ics.oop.model.worldMaps.AbstractWorldMap;

import javafx.util.Pair;

import java.util.HashSet;
import java.util.Set;

public class Animal {
    private Vector2d position;
    private MapDirection direction;
    private Genotype genotype;
    private int energy, daysLived = 0, plantsEaten = 0;
    private Integer dayOfDeath;
    private int currentGeneIndex; // czemu to jest w zwierzęciu, a nie w genotypie?
    private final Animal mother;
    private final Animal father;
    private int descendants=0;
    private int children=0;
    private final int minEnergyToReproduce;


    public Animal(Vector2d position, AnimalConfig animalConfig) {
        initializeCommonProperties(position, animalConfig);
        this.genotype = GenotypeFactory.getGenotype(animalConfig.mutationOption(), animalConfig.genomeLength(), animalConfig.minNumberOfMutations(), animalConfig.maxNumberOfMutations());

        this.energy = animalConfig.startingEnergy();
        this.minEnergyToReproduce = animalConfig.minEnergyToReproduce();

        this.mother = null;
        this.father = null;
    }

    public Animal(Animal mother, Animal father, AnimalConfig animalConfig) {
        initializeCommonProperties(mother.getPosition(), animalConfig);
        this.genotype = GenotypeFactory.getGenotype(animalConfig.mutationOption(), mother, father);
        this.currentGeneIndex = RandomInteger.getRandomInt(animalConfig.genomeLength() - 1);

        this.energy = animalConfig.energyUsedToReproduce() * 2;
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

    private void initializeCommonProperties(Vector2d mother, AnimalConfig animalConfig) { // wektor jest matką?
        this.position = mother;
        this.direction = MapDirection.values()[RandomInteger.getRandomInt(7)];
        this.currentGeneIndex = RandomInteger.getRandomInt(animalConfig.genomeLength() - 1);
    }

    public Vector2d getPosition() {
        return position;
    }

    public int getEnergy() {
        return energy;
    }

    public Genotype getGenotype() { return GenotypeFactory.getGenotype(genotype);}

    public int getCurrentGene(){ return genotype.getCurrentGene(currentGeneIndex);}

    private void useEnergy(int energyUsedToReproduce) {
        energy-=energyUsedToReproduce;
    }

    public void nextGene(){
        currentGeneIndex=(currentGeneIndex+1)%genotype.getGenes().size();
    }

    public boolean canReproduce(){
        return energy >= minEnergyToReproduce;
    }

    public int getNumberOfDescendants() {
        return descendants;
    }

    public int getNumberOfChildren() {
        return children;
    }
    public int getPlantsEaten(){ return plantsEaten;}
    public int getDaysLived(){ return daysLived;}
    public Integer getDayOfDeath(){ return dayOfDeath;}

    public void move(AbstractWorldMap globe) {
        energy--;
        daysLived++;

        direction = direction.turnRight(genotype.getCurrentGene(currentGeneIndex));
        this.nextGene();

        Pair<Vector2d, Integer> instructions = globe.calculateNextPosition(position, direction);
        position = instructions.getKey();
        direction.turnRight(instructions.getValue());
    }

    public void kill(int dayOfDeath) {
        this.dayOfDeath = dayOfDeath;
        this.position = null;
        this.direction = null;
    }

    public void eat(int energy) {
        plantsEaten++;
        this.energy += energy;
    }

    private void updateDescendants(Set<Animal> ancestors) {
        descendants++;
        if (father != null  &&  !ancestors.contains(father)) {
            ancestors.add(father);
            father.updateDescendants(ancestors);
        }
        if (mother != null  &&  !ancestors.contains(mother)) {
            ancestors.add(mother);
            mother.updateDescendants(ancestors);
        }
    }

    private void updateChildren() {
        this.children++;
    }
}
