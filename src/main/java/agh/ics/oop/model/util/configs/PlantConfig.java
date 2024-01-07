package agh.ics.oop.model.util.configs;

import static agh.ics.oop.model.util.CommonMethods.checkIfNotNegative;

public record PlantConfig(int startingCount, int energyPerPlant, int plantsPerDay) {
    public PlantConfig {
        checkIfNotNegative(startingCount);
        checkIfNotNegative(energyPerPlant);
        checkIfNotNegative(plantsPerDay);
    }
}
