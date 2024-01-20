package agh.ics.oop.controllers;

import agh.ics.oop.model.simulation.Simulation;
import agh.ics.oop.model.util.Average;
import agh.ics.oop.model.worldElements.animal.Animal;
import agh.ics.oop.model.worldMaps.AbstractWorldMap;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.stage.Modality;
import javafx.stage.Stage;

import static java.lang.StrictMath.max;

public abstract class MapDrawer {
    protected AbstractWorldMap worldMap;
    protected Simulation simulation;
    private final double cellWidth, cellHeight;
    protected final int width, height;
    protected int startingEnergy;
    @FXML private final GridPane mapGrid;
    @FXML private final LineChart<String, Number> lineChart;
    private int dataPointCounter = 0;
    protected int emptyCells;
    private final int equatorEnd, equatorStart;
    private boolean alertShowing = false;
    private Animal observedAnimal;
    Label[] animalStats = new Label[7];
    Label[] simulationStats;

    public MapDrawer(int width, int height, int startingEnergy, GridPane mapGrid, LineChart<String, Number> lineChart, Label[] simulationStats, Simulation simulation) {
        this.width = width;
        this.height = height;
        this.cellWidth = 500.0/max(width, height);
        this.cellHeight =  500.0/max(width, height);
        this.equatorEnd = (int)(height * 0.6);
        this.equatorStart = (int)(height * 0.4) + 1;
        this.mapGrid = mapGrid;
        this.lineChart = lineChart;
        this.startingEnergy = startingEnergy;
        this.simulation = simulation;
        this.worldMap = simulation.getMap();
        this.simulationStats = simulationStats;
    }

    protected void drawMap() {
        emptyCells = 0;
        mapGrid.getChildren().clear();
        updateLineChart();
        if(dataPointCounter > 10){
            removeOldData();
        }
        for (int row = height; row > equatorEnd; row--) {
            for (int column = 1; column <= width; column++) {
                printCell(column, row, "#C9E3AC");
            }
        }
        for (int row = equatorEnd; row >= equatorStart; row--) {
            for (int column = 1; column <= width; column++) {
                printCell(column, row, "#90BE6D");
            }
        }
        for (int row = equatorStart - 1; row >= 1; row--) {
            for (int column = 1; column <= width; column++) {
                printCell(column, row, "#C9E3AC");
            }
        }
        synchronized (this) {
            this.notify();
        }
        updateStats();
    }

    protected abstract void printCell(int column, int row, String backgroundColor);

    protected Circle createDot(String color, Double radiusFraction) {
        double radius = Math.min(cellWidth, cellHeight) / radiusFraction;
        Circle dot = new Circle(radius);
        dot.setFill(Color.web(color));
        return dot;
    }

    protected Shape createTriangle(String color) {
        double sideLength = Math.min(cellWidth, cellHeight) / 2.0;
        double height = sideLength * Math.sqrt(3) / 2;

        Polygon triangle = new Polygon();
        triangle.getPoints().addAll(
                0.0, 0.0,
                -sideLength / 2, height,
                sideLength / 2, height
        );
        triangle.setFill(Color.web(color));

        return triangle;
    }

    protected void addCellNode(Node cellNode, int col, int row, String backgroundColor) {
        if (cellNode instanceof Button button) {
            button.setMinSize(cellWidth, cellHeight);
            button.setMaxSize(cellWidth, cellHeight);
        } else if (cellNode instanceof Label label) {
            label.setPadding(new Insets(0, 0, 0, cellWidth / 4.0));
            label.setMinSize(cellWidth, cellHeight);
            label.setMaxSize(cellWidth, cellHeight);
        }
        cellNode.setStyle(String.format("-fx-background-color: %s; -fx-text-fill: %s; -fx-border-color: #ffffff; -fx-border-width: 0 1 1 0;", backgroundColor, "#11150a"));
        mapGrid.add(cellNode, col, row);
    }

    protected void initializeLineChart() {
        lineChart.setAnimated(false);
        lineChart.getXAxis().setAutoRanging(true);

        XYChart.Series<String, Number> animalSeries = new XYChart.Series<>(); //<String, Number> is a temporary solution- i coulndt get <number, number> to work
        animalSeries.setName("Number of Animals");

        XYChart.Series<String, Number> plantSeries = new XYChart.Series<>();
        plantSeries.setName("Number of Plants");

        lineChart.getData().addAll(animalSeries, plantSeries);
    }

    private void updateLineChart() {
        dataPointCounter++;

        XYChart.Series<String, Number> animalSeries = lineChart.getData().get(0);
        animalSeries.getData().add(new XYChart.Data<>("" + simulation.getCurrentDay(), simulation.getNumberOfAnimals()));

        XYChart.Series<String, Number> plantSeries = lineChart.getData().get(1);
        plantSeries.getData().add(new XYChart.Data<>("" + simulation.getCurrentDay(), simulation.getNumberOfPlants()));
    }

    private void removeOldData() {
        XYChart.Series<String, Number> animalSeries = lineChart.getData().get(0);
        animalSeries.getData().remove(0);

        XYChart.Series<String, Number> plantSeries = lineChart.getData().get(1);
        plantSeries.getData().remove(0);
    }

    protected void handleAnimalButtonClick(Animal animal) {
        if (!alertShowing) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Animal Information");
            alert.setHeaderText(null);

            ((Stage) alert.getDialogPane().getScene().getWindow()).setAlwaysOnTop(true);

            GridPane gridPane = new GridPane();
            observedAnimal = animal;

            for (int i = 0; i < animalStats.length; i++) {
                Label label = new Label("");
                gridPane.add(label, 0, i);
                animalStats[i] = label;
            }

            updateAnimalInformation();
            gridPane.setPrefWidth(250);

            alert.getDialogPane().setContent(gridPane);
            alert.setOnCloseRequest(event -> {
                alertShowing = false;
                observedAnimal = null;
            });
            alertShowing = true;
            alert.showAndWait();
        }
    }

    protected void updateAnimalInformation() {
        animalStats[0].setText("Genotype: " + observedAnimal.getGenotype());
        animalStats[1].setText("Current gene: " + observedAnimal.getCurrentGene());
        animalStats[2].setText("Energy: " + observedAnimal.getEnergy());
        animalStats[3].setText("Eaten plants: " + observedAnimal.getPlantsEaten());
        animalStats[4].setText("Number of children: " + observedAnimal.getNumberOfChildren());
        animalStats[5].setText("Number of descendants: " + observedAnimal.getNumberOfDescendants());
        if (observedAnimal.getDayOfDeath() == -1) {
            animalStats[6].setText("Days lived: " + observedAnimal.getDaysLived());
        } else {
            animalStats[6].setText("Day of death: " + observedAnimal.getDayOfDeath());
        }
    }

    public boolean alertShowing() {
        return alertShowing;
    }

    private void updateStats() {
        Average[] simulationAverageStats = simulation.getSimulationStats();
        simulationStats[0].setText(String.valueOf(emptyCells));
        simulationStats[1].setText(String.valueOf(simulation.getMostCommonGenotype()));
        simulationStats[2].setText(String.valueOf(simulationAverageStats[0].getAverage()));
        simulationStats[3].setText(String.valueOf(simulationAverageStats[1].getAverage()));
        simulationStats[4].setText(String.valueOf(simulationAverageStats[2].getAverage()));
    }
}