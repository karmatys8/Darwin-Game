package agh.ics.oop.model.worldMaps;

import agh.ics.oop.model.movement.Vector2d;

import java.util.Objects;

public record Plant(Vector2d position) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Plant plant = (Plant) o;
        return Objects.equals(position, plant.position);
    }
}
