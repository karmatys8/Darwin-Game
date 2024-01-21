package agh.ics.oop.model.worldMaps;

import agh.ics.oop.model.movement.Vector2d;
import agh.ics.oop.model.util.configs.AnimalConfig;
import agh.ics.oop.model.util.configs.PlantConfig;
import agh.ics.oop.model.animal.Animal;

import java.util.List;
import java.util.Map;

public class WorldMapFactory {
    public static AbstractWorldMap createWorldMap(String mapOption, int width, int height, AnimalConfig animalConfig, PlantConfig plantConfig, Map<Vector2d, List<Animal>> animalsMap, Plants plants){
        switch (mapOption){
            case "Globe":
                return new Globe(width, height, animalConfig, plantConfig, animalsMap, plants);
            case "Underground tunnels":
                return new Tunnels(width, height, animalConfig, plantConfig, animalsMap, plants);
            default:
                throw new IllegalArgumentException("There is no " + mapOption + " map option.");
        }
    }
}
