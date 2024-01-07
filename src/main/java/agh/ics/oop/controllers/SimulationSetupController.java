package agh.ics.oop.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class SimulationSetupController {

    @FXML
    private ComboBox<String> mapOption;
    @FXML
    private ComboBox<String> mutationOption;
    @FXML
    private TextField mapWidth;
    @FXML
    private TextField mapHeight;
    @FXML
    private TextField initialNumberOfPlants;
    @FXML
    private TextField energyFromOnePlant;
    @FXML
    private TextField initialNumberOfAnimals;
    @FXML
    private TextField plantsEachDay;
    @FXML
    private TextField initialEnergyOfAnimals;
    @FXML
    private TextField energyToBeWellFed;
    @FXML
    private TextField energyToReproduce;
    @FXML
    private TextField lengthOfGenotypes;
    @FXML
    private TextField maxNumberOfMutations;
    @FXML
    private TextField minNumberOfMutations;
    @FXML
    private Button startTheSimulation;

    public void initialize() {
        mapWidth.setTextFormatter(positiveInteger());
        mapHeight.setTextFormatter(positiveInteger());
        mapWidth.textProperty().addListener((observable, oldValue, newValue) -> {
            setTextFormatterLessThanWholeMap();
        });
        mapHeight.textProperty().addListener((observable, oldValue, newValue) -> {
            setTextFormatterLessThanWholeMap();
        });
        energyFromOnePlant.setTextFormatter(positiveInteger());
        initialEnergyOfAnimals.setTextFormatter(positiveInteger());
        energyToBeWellFed.setTextFormatter(positiveInteger());
        energyToBeWellFed.textProperty().addListener((observable, oldValue, newValue) -> {
            setTextFormatterLessThan(energyToBeWellFed, energyToReproduce);
        });
        lengthOfGenotypes.setTextFormatter(positiveInteger());
        maxNumberOfMutations.setTextFormatter(positiveInteger());
        maxNumberOfMutations.textProperty().addListener((observable, oldValue, newValue) -> {
            setTextFormatterLessThan(maxNumberOfMutations, minNumberOfMutations);
        });

        ObservableList<String> optionsOfMap = FXCollections.observableArrayList(
                "Underground tunnels",
                "Rectangular map"
        );
        mapOption.setItems(optionsOfMap);
        mapOption.setPromptText("Map option");
        mapOption.setStyle("-fx-font-family: 'Verdana'; -fx-background-color: #F3B153;");

        ObservableList<String> optionsOfMutation = FXCollections.observableArrayList(
                "Full randomness",
                "Swap"
        );
        mutationOption.setItems(optionsOfMutation);
        mutationOption.setPromptText("Mutation Option");
        mutationOption.setStyle("-fx-font-family: 'Verdana'; -fx-background-color: #F3B153;");

        startTheSimulation.setOnAction(event -> {
                startTheSimulation();
        });
    }
    private void startTheSimulation() {
        if (!areAllValuesFilled()) {
            showValidationAlert("Please fill in all the required fields.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/simulation.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            Stage currentStage = (Stage) startTheSimulation.getScene().getWindow();
            currentStage.hide();
            stage.show();
        } catch (IOException e) {
            System.err.println("Failed to start the simulation. Reason: " + e.getMessage());
            Platform.exit();
        }
    }
    private boolean areAllValuesFilled() {

        return !mapWidth.getText().isEmpty()
                && !mapHeight.getText().isEmpty()
                && !initialNumberOfPlants.getText().isEmpty()
                && !energyFromOnePlant.getText().isEmpty()
                && !initialNumberOfAnimals.getText().isEmpty()
                && !plantsEachDay.getText().isEmpty()
                && !initialEnergyOfAnimals.getText().isEmpty()
                && !energyToBeWellFed.getText().isEmpty()
                && !energyToReproduce.getText().isEmpty()
                && !lengthOfGenotypes.getText().isEmpty()
                && !maxNumberOfMutations.getText().isEmpty()
                && !minNumberOfMutations.getText().isEmpty()
                && mutationOption.getValue() != null
                && mapOption.getValue() != null;
    }
    private void showValidationAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void setTextFormatterLessThan(TextField maxValue, TextField lowerValue) {
        int max = getValueFromTextField(maxValue);

        checkAndClearIfGreaterThan(lowerValue, max);

        lowerValue.setTextFormatter(lessThan(maxValue));
    }

    private TextFormatter<Integer> lessThan(TextField maxValue) {
        return new TextFormatter<>(
                value -> {
                    if (value.isDeleted()) {
                        return value;
                    }

                    String newText = value.getControlNewText();
                    int max = getValueFromTextField(maxValue);

                    if (newText.isEmpty()) {
                        return value;
                    }

                    try {
                        int newValue = Integer.parseInt(newText);

                        if (newValue < max) {
                            return value;
                        }
                        else{
                            return null;
                        }
                    } catch (NumberFormatException e) {
                        return null;
                    }
                });
    }

    private TextFormatter<Integer> positiveInteger() {
        return new TextFormatter<>(value -> {
            if (value.isDeleted()) {
                return value;
            }
            String newText = value.getControlNewText();

            if (newText.isEmpty() || newText.matches("0\\d+")) {
                return null;
            }

            try {
                int n = Integer.parseInt(newText);
                return n > 0 ? value : null;
            } catch (NumberFormatException e) {
                return null;
            }
        });
    }
    private int getValueFromTextField(TextField textField) {
        try {
            return Integer.parseInt(textField.getText());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    private TextFormatter<Integer> lessThanWholeMap(){
        return new TextFormatter<>(
                value -> {
                    if (value.isDeleted()) {
                        return value;
                    }

                    String newText = value.getControlNewText();
                    int width = getValueFromTextField(mapWidth);
                    int height = getValueFromTextField(mapHeight);

                    if (newText.isEmpty()) {
                        return value;
                    }

                    try {
                        int newValue = Integer.parseInt(newText);

                        if (newValue < width * height) {
                            return value;
                        }
                        else{
                            return null;
                        }
                    } catch (NumberFormatException e) {
                        return null;
                    }
                });
    }

    private void setTextFormatterLessThanWholeMap() {
        int width = getValueFromTextField(mapWidth);
        int height = getValueFromTextField(mapHeight);

        checkAndClearIfGreaterThan(initialNumberOfPlants, width * height);
        checkAndClearIfGreaterThan(initialNumberOfAnimals, width * height);
        checkAndClearIfGreaterThan(plantsEachDay, width * height);

        initialNumberOfPlants.setTextFormatter(lessThanWholeMap());
        initialNumberOfAnimals.setTextFormatter(lessThanWholeMap());
        plantsEachDay.setTextFormatter(lessThanWholeMap());
    }
    private void checkAndClearIfGreaterThan(TextField textField, int value) {
        if (getValueFromTextField(textField) > value) {
            textField.clear();
        }
    }
}