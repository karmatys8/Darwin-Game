package agh.ics.oop.model.worldMaps;

import static agh.ics.oop.model.utils.CommonMethods.checkIfNotNegative;

public record PlantConfig(int startingCount, int energyPerPlant, int plantsPerDay) {
    public PlantConfig {
        checkIfNotNegative(startingCount);
        checkIfNotNegative(energyPerPlant);
        checkIfNotNegative(plantsPerDay);
    }
}
