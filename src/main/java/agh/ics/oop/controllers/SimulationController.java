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
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
    private boolean alertShowing = false;
    private Label genotypeLabel = new Label();
    private Label currentGenotypeLabel = new Label();
    private Label energyLabel = new Label();
    private Label eatenPlantsLabel = new Label();
    private Label childrenLabel = new Label();
    private Label descendantsLabel = new Label();
    private Label daysLivedOrDayOfDeathLabel = new Label();
    private Animal observedAnimal;

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
        Node cellNode = cellLabel;

        if (object instanceof Animal) {
            System.out.println(((Animal) object).getGenotype());
            Circle dot = createDot("#31081F", 3.5);
            Button animalButton = new Button();
            dot.setOpacity(min((((double)((Animal) object).getEnergy())/(double) animalConfig.startingEnergy()), 1));
            animalButton.setGraphic(dot);
            animalButton.setOnAction(event -> handleAnimalButtonClick((Animal) object));
            cellNode = animalButton;
        } else if (object instanceof Plant) {
            Circle circle = createDot("#F5FCE9", 4.0);
            cellLabel.setGraphic(circle);
            cellNode = cellLabel;
        } else {
            emptyCells += 1;
        }
        addCellNode(cellNode, column, height - row + 1, backgroundColor);
    }
    private Circle createDot(String color, Double radiusFraction) {
        double radius = Math.min(cellWidth, cellHeight) / radiusFraction;
        Circle dot = new Circle(radius);
        dot.setFill(Color.web(color));
        return dot;
    }
    private void addCellNode(Node cellNode, int col, int row, String backgroundColor) {
        configureNode(cellNode, col, row, backgroundColor, "#11150a");
        mapGrid.add(cellNode, col, row);
    }

    private void configureNode(Node node, int col, int row, String backgroundColor, String textColor) {
        if (node instanceof Button) {
            Button button = (Button) node;
            button.setMinSize(cellWidth, cellHeight);
            button.setMaxSize(cellWidth, cellHeight);
        } else if (node instanceof Label) {
            Label label = (Label) node;
            label.setAlignment(Pos.CENTER);
            label.setMinSize(cellWidth, cellHeight);
            label.setMaxSize(cellWidth, cellHeight);
        }

        node.setStyle(String.format("-fx-background-color: %s; -fx-text-fill: %s; -fx-border-color: #ffffff; -fx-border-width: 0 1 1 0;", backgroundColor, textColor));
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
    private void handleAnimalButtonClick(Animal animal) {
        if (!alertShowing) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Animal Information");
            alert.setHeaderText(null);

            ((Stage)alert.getDialogPane().getScene().getWindow()).setAlwaysOnTop(true);

            GridPane gridPane = new GridPane();
            observedAnimal = animal;
            genotypeLabel.setText("Genotype: ");
            gridPane.add(genotypeLabel, 0, 0);

            currentGenotypeLabel.setText("Current gene: ");
            gridPane.add(currentGenotypeLabel, 0, 1);

            energyLabel.setText("Energy: ");
            gridPane.add(energyLabel, 0, 2);

            eatenPlantsLabel.setText("Eaten plants: ");
            gridPane.add(eatenPlantsLabel, 0, 3);

            childrenLabel.setText("Number of children: ");
            gridPane.add(childrenLabel, 0, 4);

            descendantsLabel.setText("Number of descendants: ");
            gridPane.add(descendantsLabel, 0, 5);

            daysLivedOrDayOfDeathLabel.setText("");
            gridPane.add(daysLivedOrDayOfDeathLabel, 0, 6);

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


    private void updateAnimalInformation() {
        genotypeLabel.setText("Genotype: " + observedAnimal.getGenotype());
        currentGenotypeLabel.setText("Current gene: " + observedAnimal.getCurrentGeneIndex());
        energyLabel.setText("Energy: " + observedAnimal.getEnergy());
        eatenPlantsLabel.setText("Eaten plants: " + observedAnimal.getPlantsEaten());
        childrenLabel.setText("Number of children: " + observedAnimal.getNumberOfChildren());
        descendantsLabel.setText("Number of descendants: " + observedAnimal.getNumberOfDescendants());
        if (observedAnimal.getDayOfDeath() == -1) {
            daysLivedOrDayOfDeathLabel.setText("Days lived: " + observedAnimal.getDaysLived());
        } else {
            daysLivedOrDayOfDeathLabel.setText("Day of death: " + observedAnimal.getDayOfDeath());
        }
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
                    if(alertShowing){ updateAnimalInformation();}
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
