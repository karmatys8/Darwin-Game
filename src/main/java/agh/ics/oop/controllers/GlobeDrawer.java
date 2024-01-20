package agh.ics.oop.controllers;

import agh.ics.oop.model.simulation.Simulation;
import agh.ics.oop.model.worldElements.animal.Animal;
import agh.ics.oop.model.movement.Vector2d;
import agh.ics.oop.model.worldElements.artificialElements.Plant;
import agh.ics.oop.model.worldMaps.AbstractWorldMap;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import static java.lang.Math.min;

public class GlobeDrawer extends MapDrawer {
    public GlobeDrawer(int width, int height, int startingEnergy, GridPane mapGrid, LineChart<String, Number> lineChart, Label[] simulationStats, Simulation simulation){
        super(width,  height, startingEnergy, mapGrid, lineChart, simulationStats, simulation);
    }
    protected void printCell(int column, int row, String backgroundColor) {
        Object object = super.worldMap.objectAt(new Vector2d(column, row));
        Label cellLabel = new Label(" ");
        Node cellNode = cellLabel;

        if (object instanceof Animal) {
            Circle dot = createDot("#31081F", 3.5);
            Button animalButton = new Button();
            dot.setOpacity(min((((double)((Animal) object).getEnergy())/(double) super.startingEnergy), 1));
            animalButton.setGraphic(dot);
            animalButton.setOnAction(event -> super.handleAnimalButtonClick((Animal) object));
            cellNode = animalButton;
        } else if (object instanceof Plant) {
            Circle circle = createDot("#F5FCE9", 4.0);
            cellLabel.setGraphic(circle);
            cellNode = cellLabel;
        } else {
            emptyCells += 1;
        }
        cellLabel.setContentDisplay(ContentDisplay.CENTER);
        addCellNode(cellNode, column, super.height - row + 1, backgroundColor);
    }
}
