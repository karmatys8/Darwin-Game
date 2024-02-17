package agh.ics.oop.model.animal;

import agh.ics.oop.model.util.RandomInteger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

abstract public class Genotype {
    protected final List<Integer> genes;
    private int currentGeneIndex;

    protected final int minNumberOfMutations;
    protected final int maxNumberOfMutations;

    public Genotype(int genotypeLength, int minNumberOfMutations, int maxNumberOfMutations) {
        this.minNumberOfMutations = minNumberOfMutations;
        this.maxNumberOfMutations = maxNumberOfMutations;

        genes = IntStream.range(0, genotypeLength)
                .mapToObj(i -> RandomInteger.getRandomInt(7))
                .collect(Collectors.toList());
        this.currentGeneIndex = RandomInteger.getRandomInt(genotypeLength - 1);
    }

    public Genotype(Genotype other) {
        this.genes = new ArrayList<>(other.genes);
        this.minNumberOfMutations = other.minNumberOfMutations;
        this.maxNumberOfMutations = other.maxNumberOfMutations;
        this.currentGeneIndex = other.currentGeneIndex;
    }

    public Genotype(Animal mother, Animal father) {
        if (mother == null || father == null) {
            throw new IllegalArgumentException("Neither parent can be null");
        }

        Genotype mothersGenotype = mother.getGenotype();
        this.minNumberOfMutations = mothersGenotype.minNumberOfMutations;
        this.maxNumberOfMutations = mothersGenotype.maxNumberOfMutations;
      
        int divisionPoint = (int) (((double) mother.getEnergy() / (father.getEnergy() + mother.getEnergy())) * mothersGenotype.genes.size());
        boolean chooseLeftSide = RandomInteger.getRandomBoolean();

        int genotypeLength = mothersGenotype.genes.size();
        genes = IntStream.range(0, genotypeLength)
                .mapToObj(i -> (chooseLeftSide && i < divisionPoint) || (!chooseLeftSide && i >= divisionPoint)
                        ? mothersGenotype.genes.get(i) : father.getGenotype().genes.get(i))
                .collect(Collectors.toList());
        this.currentGeneIndex = RandomInteger.getRandomInt(genotypeLength - 1);

        mutate();
    }

    protected abstract void mutate();

    public String toString() {
        return genes.stream()
                .map(Object::toString)
                .collect(Collectors.joining());
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

    public int getCurrentGene() {
        return genes.get(currentGeneIndex);
    }

    public List<Integer> getGenes() {
        return new ArrayList<>(genes);
    }

    public void nextGene() {
        currentGeneIndex=(currentGeneIndex+1)% genes.size();
    }
}