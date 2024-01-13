package agh.ics.oop.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class SimulationSetupController {

    @FXML private ComboBox<String> mapOption;
    @FXML private ComboBox<String> mutationOption;
    @FXML private TextField mapWidth;
    @FXML private TextField mapHeight;
    @FXML private TextField initialNumberOfPlants;
    @FXML private TextField energyFromOnePlant;
    @FXML private TextField initialNumberOfAnimals;
    @FXML private TextField plantsEachDay;
    @FXML private TextField initialEnergyOfAnimals;
    @FXML private TextField energyToBeWellFed;
    @FXML private TextField energyToReproduce;
    @FXML private TextField lengthOfGenotypes;
    @FXML private TextField maxNumberOfMutations;
    @FXML private TextField minNumberOfMutations;
    @FXML private Button startTheSimulation;
    List<TextField> nonNegativeFields;
    List<TextField> positiveFields;

    public void initialize() {
        setUpFields();
        ObservableList<String> optionsOfMap = FXCollections.observableArrayList(
                "Underground tunnels",
                "Globe"
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

        startTheSimulation.setOnAction(event -> startTheSimulation());
    }

    private void setUpFields() {
        nonNegativeFields = Arrays.asList(
                initialNumberOfPlants, energyFromOnePlant, plantsEachDay,
                initialNumberOfAnimals, initialEnergyOfAnimals, energyToBeWellFed, energyToReproduce,
                maxNumberOfMutations, minNumberOfMutations
        );
        positiveFields = Arrays.asList(
                mapHeight, mapWidth,
                lengthOfGenotypes
        );
        positiveFields.forEach(field -> field.setTextFormatter(positiveInteger()));
        nonNegativeFields.forEach(field -> field.setTextFormatter(nonNegativeInteger()));
    }

    private void startTheSimulation() {
        StringBuilder errorMessage = new StringBuilder();
        if (inputIsValid(errorMessage) & areNotGreater(errorMessage)) {
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
        } else {
            showValidationAlert(errorMessage.toString());
        }
    }

    private TextFormatter<Integer> nonNegativeInteger() {
        return new TextFormatter<>(value -> {
            if (value.isDeleted()) {
                return value;
            }
            String newText = value.getControlNewText();

            if (newText.isEmpty() || newText.matches("\\d+")) {
                try {
                    int n = Integer.parseInt(newText);
                    return n >= 0 ? value : null;
                } catch (NumberFormatException e) {
                    return null;
                }
            }
            return null;
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

    private boolean inputIsValid(StringBuilder errorMessage) {
        boolean isValid = positiveFields.stream().noneMatch(field -> field.getText().isEmpty())
                & nonNegativeFields.stream().noneMatch(field -> field.getText().isEmpty())
                & mutationOption.getValue() != null
                & mapOption.getValue() != null;
        if (!isValid) {
            errorMessage.append("Field cannot be empty.\n");
            return false;
        }
        return true;
    }

    private void showValidationAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validation Error");
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setHeaderText("Correct the input!");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean areNotGreater(StringBuilder errorMessage) {
        int mapArea = getValueFromTextField(mapWidth) * getValueFromTextField(mapHeight);
        boolean isNotGreater = true;

        if (getValueFromTextField(initialNumberOfPlants) > mapArea) {
            initialNumberOfPlants.clear();
            errorMessage.append("Initial number of plants cannot be greater than the map area.\n");
            isNotGreater = false;
        }

        if (getValueFromTextField(plantsEachDay) > mapArea) {
            plantsEachDay.clear();
            errorMessage.append("Number of plants growing each day cannot be greater than the map area.\n");
            isNotGreater = false;
        }

        if (getValueFromTextField(energyToReproduce) > getValueFromTextField(energyToBeWellFed)) {
            energyToReproduce.clear();
            errorMessage.append("Minimal energy to reproduce cannot be greater than the energy required to be well-fed.\n");
            isNotGreater = false;
        }

        if (getValueFromTextField(minNumberOfMutations) > getValueFromTextField(maxNumberOfMutations)) {
            minNumberOfMutations.clear();
            errorMessage.append("Minimal number of mutations cannot be greater than the maximal.\n");
            isNotGreater = false;
        }

        return isNotGreater;
    }

    private int getValueFromTextField(TextField textField) {
        try {
            return Integer.parseInt(textField.getText());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
