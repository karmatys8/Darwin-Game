package agh.ics.oop.controllers;

import agh.ics.oop.model.simulation.Simulation;
import agh.ics.oop.model.util.configs.AnimalConfig;
import agh.ics.oop.model.util.configs.PlantConfig;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class SimulationController {
    private MapDrawer mapDrawer;
    private AnimalConfig animalConfig;
    private PlantConfig plantConfig;
    private int width;
    protected int height;
    private int updateInterval;
    private String mapOption;

    @FXML private Button startTheSimulation;
    @FXML private GridPane mapGrid;
    @FXML private LineChart<String, Number> lineChart;
    @FXML private GridPane mapLegend;

    private AnimationTimer animationTimer;
    private Simulation simulation = null;

    @FXML private Label emptyCellsLabel;
    @FXML private Label mostCommonGenotypeLabel;
    @FXML private Label animalEnergyLabel;
    @FXML private Label ageOfAliveAnimalsLabel;
    @FXML private Label ageOfDeadAnimalsLabel;
    @FXML private Button highlightMostCommonGenotype;
    Node[] simulationStats = new Node[6];


    void setConfigs(AnimalConfig animalConfig, PlantConfig plantConfig, int width, int height, int updateInterval, String mapOption) {
        this.animalConfig = animalConfig;
        this.plantConfig = plantConfig;
        this.width = width;
        this.height = height;
        this.updateInterval = updateInterval;
        this.mapOption = mapOption;

        initializeMapLegend();
        initializeStatistic();
    }

    @FXML
    private void onSimulationStartClicked() {
        if(simulation == null) {
            simulation = new Simulation(width, height, plantConfig, animalConfig, updateInterval, mapOption,this);
            mapDrawer = new MapDrawer(width, height, mapGrid, lineChart, simulationStats, simulation);
            mapDrawer.initializeLineChart();
            mapDrawer.drawMap();

            initializeAnimationTimer();

            startTheSimulation.setText("Stop the simulation");
        } else {
            if (simulation.isNotRunning()) {
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

    private void initializeAnimationTimer() {
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if(mapDrawer.getIsAlertShown()){ mapDrawer.updateAnimalInformation();}
                if (simulation.isNotRunning()) {
                    animationTimer.stop();
                    animationTimer = null;
                    startTheSimulation.setDisable(true);
                } else {
                    simulation.run();
                    mapDrawer.drawMap();
                }
            }
        };
        animationTimer.start();
    }

    @FXML
    public void initialize() {
        startTheSimulation.setOnAction(event -> onSimulationStartClicked());
    }
    private void initializeStatistic() {
        simulationStats[0] = emptyCellsLabel;
        simulationStats[1] = mostCommonGenotypeLabel;
        simulationStats[2] = animalEnergyLabel;
        simulationStats[3] = ageOfAliveAnimalsLabel;
        simulationStats[4] = ageOfDeadAnimalsLabel;
        simulationStats[5] = highlightMostCommonGenotype;
    }
    private void initializeMapLegend() {
        if(mapOption == "Underground tunnels"){
            Label label = new Label("Tunnels");
            label.getStyleClass().add("map-legend-text");

            Circle circle = new Circle();
            circle.setRadius(17.0);
            circle.setFill(Color.TRANSPARENT);
            circle.setStroke(Color.web("#1e3f20"));
            mapLegend.setMargin(circle, new Insets(0, 0, 0, 10));

            mapLegend.add(circle, 4, 0);
            mapLegend.add(label, 5, 0);
        }
    }
}