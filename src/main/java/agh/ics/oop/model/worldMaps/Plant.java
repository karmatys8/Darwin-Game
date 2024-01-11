package agh.ics.oop.model.worldMaps;

import agh.ics.oop.model.movement.Vector2d;

import java.util.Objects;

public class Plant {
    private final Vector2d position;

    private final PlantConfig plantConfig;

    public Plant(Vector2d position, PlantConfig plantConfig) {
        this.position = position;
        this.plantConfig = plantConfig;
    }

    public Vector2d getPosition() {
        return position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Plant plant = (Plant) o;
        return Objects.equals(position, plant.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position);
    }

    public int getEnergy() {
        return plantConfig.energyPerPlant();
    }
}
