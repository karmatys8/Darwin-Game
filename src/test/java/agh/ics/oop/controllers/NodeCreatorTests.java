package agh.ics.oop.controllers;

import agh.ics.oop.model.animal.Animal;
import agh.ics.oop.model.movement.Vector2d;
import agh.ics.oop.model.util.RandomInteger;
import agh.ics.oop.model.util.configs.AnimalConfig;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class NodeCreatorTests {
    private static final NodeCreator nodeCreator = new NodeCreator(10, 10, 100);
    private static final Vector2d position = new Vector2d(1, 1);
    private static final AnimalConfig animalConfig = new AnimalConfig(1, 100, 10, 10, 0, 0, 4, "Swap");

    @BeforeAll
    public static void initJavaFX() {
        Platform.startup(() -> {});
    }

    @AfterAll
    public static void cleanupJavaFX() {
        Platform.exit();
    }

    @Test
    public void testAnimalsNode() {
        for (int i = 0; i < 100; i++) {
            Animal animal = new Animal(position, animalConfig);
            animal.eat(-RandomInteger.getRandomInt(100));
            Node result = nodeCreator.animalsNode(animal);

            Assertions.assertTrue(result instanceof Button);
            Button button = (Button) result;
            Assertions.assertTrue(button.getGraphic() instanceof Circle);
            Assertions.assertEquals((double) animal.getEnergy() / 100, button.getGraphic().getOpacity());
        }

        Animal animal1 = new Animal(position, animalConfig);
        animal1.eat(RandomInteger.getRandomInt(20));
        Node result1 = nodeCreator.animalsNode(animal1);
        Assertions.assertEquals(1, ((Button) result1).getGraphic().getOpacity());

        Animal animal2 = new Animal(position, animalConfig);
        animal2.eat(RandomInteger.getRandomInt(1));
        Node result2 = nodeCreator.animalsNode(animal2);
        Assertions.assertEquals(1, ((Button) result2).getGraphic().getOpacity());
    }

    @Test
    public void testPlantsNode() {
        for (int i = 0; i < 100; i++) {
            Node result = nodeCreator.plantsNode();

            Assertions.assertTrue(result instanceof Label);
            Label label = (Label) result;
            Assertions.assertTrue(label.getGraphic() instanceof Polygon);
        }
    }

    @Test
    public void testTunnelsNode() {
        for (int i = 0; i < 100; i++) {
            Node result = nodeCreator.tunnelsNode();

            Assertions.assertTrue(result instanceof Label);
            Label label = (Label) result;
            Assertions.assertTrue(label.getGraphic() instanceof Circle);
        }
    }
}
