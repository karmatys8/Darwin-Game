package agh.ics.oop.model.util.configs;

import static agh.ics.oop.model.util.CommonMethods.checkIfNotNegative;
import static agh.ics.oop.model.util.CommonMethods.checkIfPositive;

public record AnimalConfig(int startingCount, int startingEnergy, int minEnergyToReproduce, int energyUsedToReproduce,
                           int minNumberOfMutations, int maxNumberOfMutations, int genomeLength, String mutationOption) {
    public AnimalConfig {
        checkIfNotNegative(startingCount);
        checkIfNotNegative(startingEnergy);
        checkIfNotNegative(minEnergyToReproduce);
        checkIfNotNegative(energyUsedToReproduce);

        checkIfNotNegative(minNumberOfMutations);
        if (maxNumberOfMutations < minNumberOfMutations) {
            throw new IllegalArgumentException("Maximum number of mutations must be greater than minimal");
        }

        checkIfPositive(genomeLength);

        if (mutationOption != "Swap" && mutationOption != "Full randomness") {
            throw new IllegalArgumentException("There in no such mutation option.");
        }
    }
}
