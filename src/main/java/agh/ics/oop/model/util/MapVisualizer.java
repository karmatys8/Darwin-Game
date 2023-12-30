package agh.ics.oop.model.util;

import agh.ics.oop.model.animal.Animal;
import agh.ics.oop.model.worldMaps.Globe;
import agh.ics.oop.model.movement.Vector2d;
import agh.ics.oop.model.worldMaps.Plant;

import java.util.List;

public class MapVisualizer {
    private static final String EMPTY_CELL = " ";
    private static final String FRAME_SEGMENT = "-";
    private static final String CELL_SEGMENT = "|";
    private final Globe globe;

    /**
     * Initializes the MapVisualizer with an instance of map to visualize.
     *
     * @param globe
     */
    public MapVisualizer(Globe globe) {
        this.globe = globe;
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
        Object object = this.globe.objectAt(currentPosition);
        if (object instanceof List<?>) {
            StringBuilder result = new StringBuilder();
            for (Object item : (List<?>) object) {
                result.append(((Animal) item).toShortString()); //many animals in the same position will cause problems with display
            }
            return result.toString();
        } else if (object instanceof Plant) {
            return object.toString();
        }
        return EMPTY_CELL;
    }
}
