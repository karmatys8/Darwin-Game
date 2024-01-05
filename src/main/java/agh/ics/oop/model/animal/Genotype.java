package agh.ics.oop.model.animal;

import agh.ics.oop.model.util.RandomInteger;

import java.util.*;

public class Genotype {
    private final List<Integer> genes;
    private final int minNumberOfMutations;
    private final int maxNumberOfMutations;

    public Genotype(int genotypeLength, int minNumberOfMutations, int maxNumberOfMutations) {
        this.minNumberOfMutations=minNumberOfMutations;
        this.maxNumberOfMutations=maxNumberOfMutations;

        genes = new ArrayList<>();
        for (int i = 0; i < genotypeLength; i++) {
            this.genes.add(RandomInteger.getRandomInt(7));
        }
    }
    public Genotype(Genotype other) { //copying method
        this.genes = new ArrayList<>(other.genes);
        this.minNumberOfMutations = other.minNumberOfMutations;
        this.maxNumberOfMutations = other.maxNumberOfMutations;
    }

    public Genotype(Animal mother, Animal father) {
        validateParents(mother, father);

        this.minNumberOfMutations=mother.getGenotype().minNumberOfMutations;
        this.maxNumberOfMutations=mother.getGenotype().maxNumberOfMutations;

        int divisionPoint = (int) (((double) mother.getEnergy() / (father.getEnergy() + mother.getEnergy())) * mother.getGenotype().genes.size());
        boolean chooseLeftSide = RandomInteger.getRandomBoolean();

        int genotypeLength = mother.getGenotype().genes.size();
        genes = new ArrayList<>(genotypeLength);
        for (int i = 0; i < genotypeLength; i++) {
            if ((chooseLeftSide && i < divisionPoint) || (!chooseLeftSide && i >= divisionPoint)) {
                genes.add(mother.getGenotype().genes.get(i));
            } else {
                genes.add(father.getGenotype().genes.get(i));
            }
        }

        mutate();
    }

    private void validateParents(Animal mother, Animal father) {
        if (mother == null || father == null) {
            throw new IllegalArgumentException("Both mother and father must not be null");
        }
    }

    private void mutate() {
        int numberOfMutations = RandomInteger.getRandomInt(minNumberOfMutations, maxNumberOfMutations);

        for (int i = 0; i < numberOfMutations; i++) {
            switchGenes(RandomInteger.getRandomInt(genes.size()-1), RandomInteger.getRandomInt(genes.size()-1)); //opcja symulacji z podmianką
            randomGene(RandomInteger.getRandomInt(genes.size()-1)); //opcja symulacji pełna losowość
        }
    }

    //przekazujemy mutator

    public void switchGenes(int firstGeneIndex, int secondGeneIndex) {
        Collections.swap(genes, firstGeneIndex, secondGeneIndex);
    }
    //losowy inny
    public void randomGene(int geneIndex) {
        genes.set(geneIndex, RandomInteger.getRandomInt(7));
    }

    public String toString() {
        StringBuilder genotypeToString = new StringBuilder();
        for (Integer gene : genes) {
            genotypeToString.append(gene.toString());
        }
        return genotypeToString.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Genotype) {
            Genotype other = (Genotype) obj;
            return this.genes.equals(other.genes);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.genes.hashCode();
    }

    public int getCurrentGene(int currentGeneIndex) {
        return genes.get(currentGeneIndex);
    }

    public List<Integer> getGenes() {
        return new ArrayList<>(genes);
    }
}