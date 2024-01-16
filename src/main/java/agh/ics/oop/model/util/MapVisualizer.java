package agh.ics.oop.model.util;

import agh.ics.oop.model.animal.Animal;
import agh.ics.oop.model.worldMaps.AbstractWorldMap;
import agh.ics.oop.model.movement.Vector2d;
import javafx.util.Pair;

import java.util.List;
import java.util.Optional;

public class MapVisualizer {
    private static final String EMPTY_CELL = " ";
    private static final String FRAME_SEGMENT = "-";
    private static final String CELL_SEGMENT = "|";
    private static final String PLANT_VIEW = "*";
    private final AbstractWorldMap worldMap;

    /**
     * Initializes the MapVisualizer with an instance of map to visualize.
     *
     * @param worldMap
     */
    public MapVisualizer(AbstractWorldMap worldMap) {
        this.worldMap = worldMap;
    }

    /**
     * Convert selected region of the map into a string. It is assumed that the
     * indices of the map will have no more than two characters (including the
     * sign).
     *
     * @param lowerLeft  The lower left corner of the region that is drawn.
     * @param upperRight The upper right corner of the region that is drawn.
     * @return String representation of the selected region of the map.
     */
    public String draw(Vector2d lowerLeft, Vector2d upperRight) {
        StringBuilder builder = new StringBuilder();
        for (int i = upperRight.getY() + 1; i >= lowerLeft.getY() - 1; i--) {
            if (i == upperRight.getY() + 1) {
                builder.append(drawHeader(lowerLeft, upperRight));
            }
            builder.append(String.format("%3d: ", i));
            for (int j = lowerLeft.getX(); j <= upperRight.getX() + 1; j++) {
                if (i < lowerLeft.getY() || i > upperRight.getY()) {
                    builder.append(drawFrame(j <= upperRight.getX()));
                } else {
                    builder.append(CELL_SEGMENT);
                    if (j <= upperRight.getX()) {
                        builder.append(drawObject(new Vector2d(j, i)));
                    }
                }
            }
            builder.append(System.lineSeparator());
        }
        return builder.toString();
    }

    private String drawFrame(boolean innerSegment) {
        if (innerSegment) {
            return FRAME_SEGMENT + FRAME_SEGMENT;
        } else {
            return FRAME_SEGMENT;
        }
    }

    private String drawHeader(Vector2d lowerLeft, Vector2d upperRight) {
        StringBuilder builder = new StringBuilder();
        builder.append(" y\\x ");
        for (int j = lowerLeft.getX(); j < upperRight.getX() + 1; j++) {
            builder.append(String.format("%2d", j));
        }
        builder.append(System.lineSeparator());
        return builder.toString();
    }

    private String drawObject(Vector2d currentPosition) {
        Pair<Optional<List<Animal>>, Optional<Boolean>> objects = worldMap.objectAt(currentPosition);
        if (objects.getKey().isPresent()) {
            List<Animal> animalList = objects.getKey().get();
            return animalList.get(0).toShortString();
        } else if (objects.getValue().isPresent()) {
            return PLANT_VIEW;
        }
        return EMPTY_CELL;
    }
}
