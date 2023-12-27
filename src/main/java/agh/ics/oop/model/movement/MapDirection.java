package agh.ics.oop.model.movement;

import java.util.List;

public enum MapDirection {
    N("N", new Vector2d(0, 1)),
    NE("NE", new Vector2d(1, 1)),
    E("E", new Vector2d(1, 0)),
    SE("SE", new Vector2d(1, -1)),
    S("S", new Vector2d(0, -1)),
    SW("SW", new Vector2d(-1, -1)),
    W("W", new Vector2d(-1, 0)),
    NW("NW", new Vector2d(-1, 1));

    private final String value; // we can change it later if necessary
    private final Vector2d unitVector;
    private static final List<MapDirection> valuesList = List.of(values());

    MapDirection(String value, Vector2d unitVector) {
        this.value = value;
        this.unitVector = unitVector;
    }

    public String toString() {
        return value;
    }

    public MapDirection turnRight(int step) {
        return valuesList.get((this.ordinal() + step) % valuesList.size());
    }
}
