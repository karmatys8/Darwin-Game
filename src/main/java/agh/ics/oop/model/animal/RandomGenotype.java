package agh.ics.oop.model.animal;

import agh.ics.oop.model.util.RandomInteger;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RandomGenotype extends Genotype {
    private final static List<Integer> randomGenes = IntStream.range(0, 8).boxed().collect(Collectors.toList());

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
            randomGene(RandomInteger.getRandomInt(genes.size() - 1));
        }
    }

    private void randomGene(int geneIndex) {
        Collections.shuffle(randomGenes);
        int randomGene = randomGenes.get(0);

        if (randomGene != genes.get(geneIndex)) {
            genes.set(geneIndex, randomGene);
        } else genes.set(geneIndex, randomGenes.get(1));
    }
}
