package agh.ics.oop.model.animal;

import agh.ics.oop.model.util.RandomInteger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

abstract public class Genotype {
    protected final List<Integer> genes;
    protected final int minNumberOfMutations;
    protected final int maxNumberOfMutations;

    public Genotype(int genotypeLength, int minNumberOfMutations, int maxNumberOfMutations) {
        this.minNumberOfMutations = minNumberOfMutations;
        this.maxNumberOfMutations = maxNumberOfMutations;

        genes = new ArrayList<>(genotypeLength);
        for (int i = 0; i < genotypeLength; i++) {
            genes.add(RandomInteger.getRandomInt(7));
        }
    }
    public Genotype(Genotype other) { //copying method
        this.genes = new ArrayList<>(other.genes);
        this.minNumberOfMutations = other.minNumberOfMutations;
        this.maxNumberOfMutations = other.maxNumberOfMutations;
    }

    public Genotype(Animal mother, Animal father) {
        validateParents(mother, father);

        Genotype mothersGenotype = mother.getGenotype();
        this.minNumberOfMutations = mothersGenotype.minNumberOfMutations;
        this.maxNumberOfMutations = mothersGenotype.maxNumberOfMutations;
      
        int divisionPoint = (int) (((double) mother.getEnergy() / (father.getEnergy() + mother.getEnergy())) * mothersGenotype.genes.size());
        boolean chooseLeftSide = RandomInteger.getRandomBoolean();

        int genotypeLength = mothersGenotype.genes.size();
        genes = new ArrayList<>(genotypeLength);
        for (int i = 0; i < genotypeLength; i++) {
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

    protected abstract void mutate();

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