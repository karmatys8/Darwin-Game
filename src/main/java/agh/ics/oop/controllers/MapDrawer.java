package agh.ics.oop.controllers;

import agh.ics.oop.model.movement.Vector2d;
import agh.ics.oop.model.Simulation;
import agh.ics.oop.model.animal.Animal;
import agh.ics.oop.model.animal.Genotype;
import agh.ics.oop.model.util.exceceptions.UnexpectedNodeException;
import agh.ics.oop.model.worldMaps.AbstractWorldMap;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

import static java.lang.StrictMath.max;

public class MapDrawer {
    private final AbstractWorldMap worldMap;
    private final Simulation simulation;
    private final double cellWidth, cellHeight;
    private final int width, height;
    @FXML private final GridPane mapGrid;
    @FXML private final LineChart<String, Number> lineChart;
    private int dataPointCounter = 0;
    private final int equatorStart, equatorEnd;
    private boolean isAlertShown = false;
    private Animal observedAnimal;
    private final Label[] animalStats = new Label[7];
    final Statistics statistics;

    public MapDrawer(int width, int height, GridPane mapGrid, LineChart<String, Number> lineChart, Node[] simulationStats, Simulation simulation, boolean shouldWriteToCSV) {
        this.width = width;
        this.height = height;
        this.cellWidth = 500.0/max(width, height);
        this.cellHeight =  500.0/max(width, height);
        this.equatorEnd = (int)(height * 0.6);
        this.equatorStart = (int)(height * 0.4) + 1;
        this.mapGrid = mapGrid;
        this.lineChart = lineChart;
        this.simulation = simulation;
        this.worldMap = simulation.getMap();
        ((Button)simulationStats[5]).setOnAction(event -> highlightMostCommonGenotype());

        statistics = new Statistics(simulation, animalStats, simulationStats, shouldWriteToCSV);
    }

    void drawMap() {
        statistics.resetEmptyCellsCounter();
        mapGrid.getChildren().clear();
        updateLineChart();
        if(dataPointCounter > 10){
            removeOldData();
        }
        for (int row = height; row >= 1; row--) {
            String color = (row >= equatorStart && row <= equatorEnd) ? "#90BE6D" : "#C9E3AC";
            for (int column = 1; column <= width; column++) {
                drawCell(column, row, color);
            }
        }
        synchronized (this) {
            this.notify();
        }

        statistics.updateStats();
        highlightObservedAnimal();
    }

    private void addCellNode(Node cellNode, int col, int row, String backgroundColor) {
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

    void initializeLineChart() {
        lineChart.setAnimated(false);
        lineChart.getXAxis().setAutoRanging(true);

        XYChart.Series<String, Number> animalSeries = new XYChart.Series<>();
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

    private void handleAnimalButtonClick(Animal animal) {
        if (!isAlertShown) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Animal Information");
            alert.setHeaderText(null);

            ((Stage) alert.getDialogPane().getScene().getWindow()).setAlwaysOnTop(true);

            GridPane gridPane = new GridPane();
            observedAnimal = animal;
            statistics.setObservedAnimal(animal);

            for (int i = 0; i < animalStats.length; i++) {
                Label label = new Label("");
                gridPane.add(label, 0, i);
                animalStats[i] = label;
            }

            statistics.updateAnimalInformation();
            gridPane.setPrefWidth(250);

            alert.getDialogPane().setContent(gridPane);
            alert.setOnCloseRequest(event -> {
                isAlertShown = false;
                observedAnimal = null;
                statistics.setObservedAnimal(null);
            });
            isAlertShown = true;
            alert.showAndWait();
        }
    }

    public boolean getIsAlertShown() {
        return isAlertShown;
    }

    private void highlightObservedAnimal(){
        if(observedAnimal != null && observedAnimal.getDayOfDeath() == null){
            Vector2d position =  observedAnimal.getPosition();
            drawCell(position.x(), position.y(), "#F57D51");
        }
    }

    private void highlightMostCommonGenotype(){
        Set<Animal> aliveAnimals = simulation.getAliveAnimals();
        Genotype mostCommonGenotype = simulation.getMostCommonGenotype();

        for (Animal animal : aliveAnimals) {
            if(animal.getGenotype().getGenes().equals(mostCommonGenotype.getGenes())){
                Vector2d position =  animal.getPosition();
                drawCell(position.x(), position.y(), "#F3B153");
            }
        }
    }

    private void drawCell(int column, int row, String backgroundColor) {
        Vector2d position = new Vector2d(column, row);
        Pair<Node, Optional<Animal>> pair = worldMap.getNodeAt(position);
        Node node = pair.getKey();

        if (node instanceof Button button) {
            pair.getValue().ifPresent(animal -> button.setOnAction(event -> handleAnimalButtonClick(animal)));
            button.setContentDisplay(ContentDisplay.CENTER);
        } else if (node instanceof Label label) {
            label.setContentDisplay(ContentDisplay.CENTER);
        } else if (node == null) {
            statistics.countAnEmptyCell();
            node = new Label(" ");
        } else throw new UnexpectedNodeException(node);

        addCellNode(node, column, height - row + 1, backgroundColor);
    }
}