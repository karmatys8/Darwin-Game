package agh.ics.oop.controllers;

import agh.ics.oop.model.simulation.Simulation;
import agh.ics.oop.model.worldElements.animal.Animal;
import agh.ics.oop.model.movement.Vector2d;
import agh.ics.oop.model.worldElements.artificialElements.Plant;
import agh.ics.oop.model.worldElements.artificialElements.Tunnel;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import static java.lang.Math.min;

public class TunnelDrawer extends MapDrawer {
    public TunnelDrawer(int width, int height, int startingEnergy, GridPane mapGrid, LineChart<String, Number> lineChart, Label[] simulationStats, Simulation simulation){
        super(width,  height, startingEnergy, mapGrid, lineChart, simulationStats, simulation);
    }

    protected void printCell(int column, int row, String backgroundColor) {
        Object object = super.worldMap.objectAt(new Vector2d(column, row));
        Label cellLabel = new Label(" ");
        cellLabel.setContentDisplay(ContentDisplay.CENTER);
        Node cellNode = cellLabel;

        if (object instanceof Animal) {
            Circle dot = createDot("#31081F", 3.5);
            Button animalButton = new Button();
            dot.setOpacity(min((((double)((Animal) object).getEnergy())/(double) super.startingEnergy), 1));
            animalButton.setGraphic(dot);
            animalButton.setOnAction(event -> super.handleAnimalButtonClick((Animal) object));
            cellNode = animalButton;
        } else if (object instanceof Plant) {
            Shape triangle = createTriangle("#F5FCE9");
            cellLabel.setGraphic(triangle);
            cellNode = cellLabel;
        } else if(object instanceof Tunnel){
            Circle dot = createDot("#00000000", 4.0);
            dot.setStroke(Color.web("#1E3F20"));
            cellLabel.setGraphic(dot);
            cellNode = cellLabel;
        } else {
            emptyCells++;
        }
        addCellNode(cellNode, column, super.height - row + 1, backgroundColor);
    }
}
