package agh.ics.oop.model.animal;


public class GenotypeFactory {
    public static Genotype getGenotype(String mutationOption, int genotypeLength, int minNumberOfMutations, int maxNumberOfMutations) {
        switch (mutationOption) {
            case "Full randomness":
                return new RandomGenotype(genotypeLength, minNumberOfMutations, maxNumberOfMutations);
            case "Swap":
                return new SwitchGenotype(genotypeLength, minNumberOfMutations, maxNumberOfMutations);
            default:
                throw new IllegalArgumentException("There is no " + mutationOption + " genotype option.");
        }
    }

    public static Genotype getGenotype(String type, Animal mother, Animal father) {
        switch (type) {
            case "Full randomness":
                return new RandomGenotype(mother, father);
            case "Swap":
                return new SwitchGenotype(mother, father);
            default:
                throw new IllegalArgumentException("There is no " + type + " genotype option.");
        }
    }

    public static Genotype getGenotype(Genotype genotype) {
        if (genotype instanceof RandomGenotype) {
            return new RandomGenotype(genotype);
        } else{
            return new SwitchGenotype(genotype);
        }
    }
}
