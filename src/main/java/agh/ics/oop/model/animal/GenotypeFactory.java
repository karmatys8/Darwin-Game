package agh.ics.oop.model.animal;


public class GenotypeFactory {
    public static Genotype getGenotype(String type, int genotypeLength, int minNumberOfMutations, int maxNumberOfMutations) {
        return switch (type) {
            case "Full randomness" -> new RandomGenotype(genotypeLength, minNumberOfMutations, maxNumberOfMutations);
            case "Swap" -> new SwitchGenotype(genotypeLength, minNumberOfMutations, maxNumberOfMutations);
            default -> throw new IllegalArgumentException("There is no " + type + " genotype option.");
        };
    }

    public static Genotype getGenotype(String type, Animal mother, Animal father) {
        return switch (type) {
            case "Full randomness" -> new RandomGenotype(mother, father);
            case "Swap" -> new SwitchGenotype(mother, father);
            default -> throw new IllegalArgumentException("There is no " + type + " genotype option.");
        };
    }

    public static Genotype getGenotype(Genotype genotype) {
        if (genotype instanceof RandomGenotype) {
            return new RandomGenotype(genotype);
        } else{
            return new SwitchGenotype(genotype);
        }
    }
}
