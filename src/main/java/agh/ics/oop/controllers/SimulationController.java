package agh.ics.oop.controllers;

import agh.ics.oop.model.worldElements.animal.Animal;
import agh.ics.oop.model.movement.Vector2d;
import agh.ics.oop.model.simulation.Simulation;
import agh.ics.oop.model.worldElements.artificialElements.Plant;
import agh.ics.oop.model.worldElements.artificialElements.Tunnel;
import agh.ics.oop.model.worldMaps.AbstractWorldMap;
import agh.ics.oop.model.util.configs.AnimalConfig;
import agh.ics.oop.model.util.configs.PlantConfig;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import static java.lang.Math.min;
import static java.lang.StrictMath.max;

public class SimulationController {
    private AbstractWorldMap worldMap;
    private AnimalConfig animalConfig;
    private PlantConfig plantConfig;
    private double cellWidth;
    private double cellHeight;
    private int width;
    private int height;
    @FXML
    private Button startTheSimulation;

    @FXML
    private GridPane mapGrid;

    @FXML
    private LineChart<String, Number> lineChart;

    private int dataPointCounter = 0;

    private AnimationTimer animationTimer;

    private int equatorEnd;

    private int equatorStart;

    private Simulation simulation;

    private int emptyCells;

    public void setConfigs(AnimalConfig animalConfig, PlantConfig plantConfig, int width, int height) {
        this.animalConfig = animalConfig;
        this.plantConfig = plantConfig;
        this.width = width;
        this.height = height;
        this.cellWidth = 500.0/max(width, height);
        this.cellHeight =  500.0/max(width, height);
        this.equatorEnd = (int)(height * 0.6); //uzgodnić wersje
        this.equatorStart = (int)(height * 0.4);
    }
    public void setWorldMap(AbstractWorldMap worldMap) {
        this.worldMap = worldMap;
    }

    public void drawMap() {
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
    }

    private void printCell(int column, int row, String backgroundColor) {
        Object object = worldMap.objectAt(new Vector2d(column, row));
        Label cellLabel = new Label(" ");

        if (object instanceof Animal) {
            Circle dot = createDot("#31081F", 3.5);
            cellLabel.setGraphic(dot);
            dot.setOpacity(min((((double)((Animal) object).getEnergy())/(double) animalConfig.startingEnergy()), 1));
        } else if (object instanceof Plant) {
            Circle circle = createDot("#F5FCE9", 4.0);
            cellLabel.setGraphic(circle);
        } else {
            emptyCells += 1;
        }
        addCellLabel(cellLabel, column, height - row + 1, backgroundColor);
    }
    private Circle createDot(String color, Double radiusFraction) {
        double radius = Math.min(cellWidth, cellHeight) / radiusFraction;
        Circle dot = new Circle(radius);
        dot.setFill(Color.web(color));
        return dot;
    }



    private void addCellLabel(Label cellLabel, int col, int row, String backgroundColor) {
        configureLabel(cellLabel, col, row, backgroundColor, "#11150a");
    }

    private void configureLabel(Label label, int col, int row, String backgroundColor, String textColor) {
        label.setAlignment(Pos.CENTER);
        label.setMinSize(cellWidth, cellHeight);
        label.setMaxSize(cellWidth, cellHeight);
        label.setStyle(String.format("-fx-background-color: %s; -fx-text-fill: %s; -fx-border-color: #ffffff; -fx-border-width: 0 1 1 0;", backgroundColor, textColor));
        mapGrid.add(label, col, row);
    }

    private void initializeLineChart() {
        lineChart.setAnimated(false);
        lineChart.getXAxis().setAutoRanging(true);

        XYChart.Series<String, Number> animalSeries = new XYChart.Series<>(); //<String, Number> is a temporary solution- i coulndt get <number, number> to work
        animalSeries.setName("Number of Animals");

        XYChart.Series<String, Number> plantSeries = new XYChart.Series<>();
        plantSeries.setName("Number of Plants");

        lineChart.getData().addAll(animalSeries, plantSeries);
    }

    private void updateLineChart(){
        dataPointCounter++;

        XYChart.Series<String, Number> animalSeries = lineChart.getData().get(0);
        animalSeries.getData().add(new XYChart.Data<>(""+simulation.getCurrentDay(), simulation.getNumberOfAnimals()));

        XYChart.Series<String, Number> plantSeries = lineChart.getData().get(1);
        plantSeries.getData().add(new XYChart.Data<>(""+simulation.getCurrentDay(), simulation.getNumberOfPlants()));
    }
    private void removeOldData(){
        XYChart.Series<String, Number> animalSeries = lineChart.getData().get(0);
        animalSeries.getData().remove(0);

        XYChart.Series<String, Number> plantSeries = lineChart.getData().get(1);
        plantSeries.getData().remove(0);
    }

    @FXML
    public void onSimulationStartClicked() {
        if(worldMap == null) {
            simulation = new Simulation(width, height, plantConfig, animalConfig, this);
            setWorldMap(simulation.getMap());
            initializeLineChart();
            drawMap();

            animationTimer = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    if (!simulation.isRunning()) {
                        animationTimer.stop();
                        animationTimer = null;
                        startTheSimulation.setDisable(true);
                    } else {
                        simulation.run();
                        drawMap();
                    }
                }
            };
            animationTimer.start();

            startTheSimulation.setText("Stop the simulation");
        } else {
            if (!simulation.isRunning()) {
                simulation.resumeSimulation();
                animationTimer.start();
                startTheSimulation.setText("Stop the simulation");
            } else {
                simulation.pauseSimulation();
                animationTimer.stop();
                startTheSimulation.setText("Resume the simulation");
            }
        }
    }
    public void initialize() {
        startTheSimulation.setOnAction(event -> onSimulationStartClicked());

    }
}
