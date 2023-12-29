package agh.ics.oop.model;

import agh.ics.oop.model.util.RandomInteger;

import java.util.ArrayList;
import java.util.List;

public class Genotype {
    private List<Integer> genes = new ArrayList<>();

    public Genotype(int genotypeLength) {
        for (int i = 0; i < genotypeLength; i++) {
            this.genes.add(RandomInteger.getRandomInt(8));
        }
    }

    public Genotype(Animal mother, Animal father) {
        validateParents(mother, father);

        int divisionPoint = (int) (((double) mother.getEnergy() / (father.getEnergy() + mother.getEnergy())) * mother.getGenotype().genes.size());
        boolean chooseLeftSide = RandomInteger.getRandomBoolean();

        for (int i = 0; i < mother.getGlobe().getGenotypeLength(); i++) {
            if ((chooseLeftSide && i < divisionPoint) || (!chooseLeftSide && i >= divisionPoint)) {
                genes.add(mother.getGenotype().genes.get(i));
            } else {
                genes.add(father.getGenotype().genes.get(i));
            }
        }

        mutate(mother.getGlobe().getMinNumberOfMutations(), mother.getGlobe().getMaxNumberOfMutations());
    }

    private void validateParents(Animal mother, Animal father) {
        if (mother == null || father == null) {
            throw new IllegalArgumentException("Both mother and father must not be null");
        }
    }

    private void mutate(int minNumberOfMutation, int maxNumberOfMutation) {
        int numberOfMutations = RandomInteger.getRandomInt(minNumberOfMutation, maxNumberOfMutation);

        for (int i = 0; i < numberOfMutations; i++) {
            switchGenes(RandomInteger.getRandomInt(genes.size()), RandomInteger.getRandomInt(genes.size())); //opcja symulacji z podmianką
            randomGene(RandomInteger.getRandomInt(genes.size())); //opcja symulacji pełna losowość
        }
    }

    public void switchGenes(int firstGeneIndex, int secondGeneIndex) {
        int temp = this.genes.get(firstGeneIndex);
        this.genes.set(firstGeneIndex, this.genes.get(secondGeneIndex));
        this.genes.set(secondGeneIndex, temp);
    }
    public void randomGene(int geneIndex) {
        genes.set(geneIndex, RandomInteger.getRandomInt(8));
    }

    public String toString() {
        StringBuilder genotypeToString = new StringBuilder();
        for (Integer gene : genes) {
            genotypeToString.append(gene.toString());
        }
        return genotypeToString.toString();
    }

    public Integer getCurrentGene(int currentGeneIndex) {
        return genes.get(currentGeneIndex);
    }
}