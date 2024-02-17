package agh.ics.oop.model.worldMaps;

import agh.ics.oop.model.movement.MapDirection;
import agh.ics.oop.model.movement.Vector2d;
import javafx.util.Pair;

public interface PositionCalculator {
    Pair<Vector2d, Integer> calculateNextPosition(Vector2d oldPosition, MapDirection direction);
}
