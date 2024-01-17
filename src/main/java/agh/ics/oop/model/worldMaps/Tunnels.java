package agh.ics.oop.model.worldMaps;

import agh.ics.oop.model.movement.MapDirection;
import agh.ics.oop.model.worldElements.WorldElement;
import agh.ics.oop.model.worldElements.animal.Animal;
import agh.ics.oop.model.movement.Vector2d;
import agh.ics.oop.model.util.RandomInteger;
import agh.ics.oop.model.util.configs.AnimalConfig;
import agh.ics.oop.model.util.configs.PlantConfig;
import agh.ics.oop.model.worldElements.artificialElements.Tunnel;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tunnels extends AbstractWorldMap {
    private final Map<Vector2d, Vector2d> tunnels = new HashMap<>();
    private final static Tunnel dumbTunnel = new Tunnel();
    private final static Vector2d placeholder = new Vector2d(-1, -1);

    private Vector2d generateTunnelEntry() {
        Vector2d tunnelEntry;
        do {
            tunnelEntry = new Vector2d(RandomInteger.getRandomInt(1, width), RandomInteger.getRandomInt(1, height));
        } while (tunnels.containsKey(tunnelEntry));
        return tunnelEntry;
    }

    public Tunnels(int width, int height, AnimalConfig animalConfig, PlantConfig plantConfig, Map<Vector2d, List<Animal>> animalsMap, Plants plants) {
        super(width, height, animalConfig, plantConfig, animalsMap, plants);

        int numberOfTunnels = width * height / 50;
        for (int i = 0; i < numberOfTunnels; i++) {
            Vector2d start = generateTunnelEntry();
            tunnels.put(start, placeholder);
            Vector2d end = generateTunnelEntry();

            tunnels.put(start, end);
            tunnels.put(end, start);
        }
    }

    @Override
    public Pair<Vector2d, Integer> howToMove(Vector2d oldPosition, MapDirection direction) {
        Vector2d newPosition = oldPosition.add(direction.getUnitVector());
        if (tunnels.containsKey(newPosition)) return new Pair<>(tunnels.get(newPosition), 0);
        if (newPosition.y() < 1  ||  newPosition.y() > height
                ||  newPosition.x() < 1  ||  newPosition.x() > width) return new Pair<>(oldPosition, 0);
        return new Pair<>(newPosition, 0);
    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        List<Animal> animalsAtThisPosition = animalsMap.get(position);
        if (animalsAtThisPosition != null) return animalsAtThisPosition.get(0);
        if (tunnels.containsKey(position)) return dumbTunnel;
        if (! plants.isFieldEmpty(position)) return dumbPlant;
        else return null;
    }
}