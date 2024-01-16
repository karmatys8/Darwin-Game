package agh.ics.oop.model.worldElements.animal;

import agh.ics.oop.model.util.RandomInteger;

public class RandomGenotype extends Genotype {
    public RandomGenotype(int genotypeLength, int minNumberOfMutations, int maxNumberOfMutations) {
        super(genotypeLength, minNumberOfMutations, maxNumberOfMutations);
    }

    public RandomGenotype(Genotype other) {
        super(other);
    }

    public RandomGenotype(Animal mother, Animal father) {
        super(mother, father);
    }

    protected void mutate() {
        int numberOfMutations = RandomInteger.getRandomInt(minNumberOfMutations, maxNumberOfMutations);

        for (int i = 0; i < numberOfMutations; i++) {
            randomGene(RandomInteger.getRandomInt(genes.size()-1));
        }
    }
}
