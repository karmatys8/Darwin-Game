package agh.ics.oop.model.animal.animal;

import agh.ics.oop.model.util.RandomInteger;

public class SwitchGenotype extends Genotype {
    public SwitchGenotype(int genotypeLength, int minNumberOfMutations, int maxNumberOfMutations) {
        super(genotypeLength, minNumberOfMutations, maxNumberOfMutations);
    }

    public SwitchGenotype(Genotype other) {
        super(other);
    }

    public SwitchGenotype(Animal mother, Animal father) {
        super(mother, father);
    }

    protected void mutate() {
        int numberOfMutations = RandomInteger.getRandomInt(minNumberOfMutations, maxNumberOfMutations);

        for (int i = 0; i < numberOfMutations; i++) {
            switchGenes(RandomInteger.getRandomInt(genes.size() - 1), RandomInteger.getRandomInt(genes.size() - 1));
        }
    }
}
