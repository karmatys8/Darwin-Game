package agh.ics.oop.controllers;

import agh.ics.oop.model.animal.Animal;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

import static java.lang.Math.min;
import static java.lang.StrictMath.max;

public class NodeCreator {
    private final double cellWidth, cellHeight, startingEnergy;

    public NodeCreator(int width, int height, int startingEnergy) {
        this.cellWidth = 500.0/max(width, height);
        this.cellHeight =  500.0/max(width, height);
        this.startingEnergy = startingEnergy;
    }

    public Node createAnimalsNode(Animal animal) {
        Circle dot = createDot("#31081F", 3.5);
        Button animalButton = new Button();
        dot.setOpacity(min((((double) animal.getEnergy())/ startingEnergy), 1));
        animalButton.setGraphic(dot);

        return animalButton;
    }

    public Node createPlantsNode() {
        Label cellLabel = createLabeledNode(createTriangle("#F5FCE9"));
        return cellLabel;
    }

    public Node createTunnelsNode() {
        Label cellLabel = createLabeledNode(createDot("#00000000", 4.0));
        Circle dot = (Circle) cellLabel.getGraphic();
        dot.setStroke(Color.web("#1E3F20"));
        return cellLabel;
    }

    private Label createLabeledNode(Shape graphic) {
        Label cellLabel = new Label(" ");
        cellLabel.setContentDisplay(ContentDisplay.CENTER);
        cellLabel.setGraphic(graphic);
        return cellLabel;
    }

    protected Circle createDot(String color, Double radiusFraction) {
        double radius = Math.min(cellWidth, cellHeight) / radiusFraction;
        return new Circle(radius, Color.web(color));
    }

    protected Shape createTriangle(String color) {
        double sideLength = Math.min(cellWidth, cellHeight) / 2.0;
        double height = sideLength * Math.sqrt(3) / 2;

        Polygon triangle = new Polygon(
                0.0, 0.0,
                -sideLength / 2, height,
                sideLength / 2, height
        );
        triangle.setFill(Color.web(color));
        return triangle;
    }
}
