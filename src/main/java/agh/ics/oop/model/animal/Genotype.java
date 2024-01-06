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

        genes = new ArrayList<>(genotypeLength);
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
      
        Genotype mothersGenotype=mother.getGenotype();
        this.minNumberOfMutations=mothersGenotype.minNumberOfMutations;
        this.maxNumberOfMutations=mothersGenotype.maxNumberOfMutations;
      
        int divisionPoint = (int) (((double) mother.getEnergy() / (father.getEnergy() + mother.getEnergy())) * mothersGenotype.genes.size());
        boolean chooseLeftSide = RandomInteger.getRandomBoolean();

        genes = new ArrayList<>(mothersGenotype.genes.size());
        for (int i = 0; i < mothersGenotype.genes.size(); i++) {
            if ((chooseLeftSide && i < divisionPoint) || (!chooseLeftSide && i >= divisionPoint)) {
                genes.add(mothersGenotype.genes.get(i));
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Genotype genotype = (Genotype) o;
        return Objects.equals(genes, genotype.genes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(genes);
    }

    public int getCurrentGene(int currentGeneIndex) {
        return genes.get(currentGeneIndex);
    }

    public List<Integer> getGenes() {
        return new ArrayList<>(genes);
    }
}