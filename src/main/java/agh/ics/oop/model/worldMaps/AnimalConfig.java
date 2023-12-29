package agh.ics.oop.model.worldMaps;

import static agh.ics.oop.model.utils.CommonMethods.checkIfNotNegative;
import static agh.ics.oop.model.utils.CommonMethods.checkIfPositive;

public record AnimalConfig(int startingCount, int startingEnergy, int minEnergyToReproduce, int energyUsedToReproduce,
                           int minNumberOfMutations, int maxNumberOfMutations, int genomeLength) {
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
    }
}