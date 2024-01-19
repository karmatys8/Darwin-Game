package agh.ics.oop.controllers;

import agh.ics.oop.model.util.exceceptions.DuplicateConfigNameException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;

public class SimulationSetupController {
    @FXML private ComboBox<String> mapOption;
    @FXML private ComboBox<String> mutationOption;
    @FXML private ComboBox<String> listOfSavedConfigs;
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
    @FXML private Button saveConfigs;

    List<TextField> nonNegativeFields, positiveFields, allTextFields;
    List<ComboBox<String>> comboBoxes;
    GsonConfigs gsonConfigs;


    public void initialize() {
        setUpFields();
        setUpComboBoxes();

        comboBoxes = List.of(mapOption, mutationOption);
        gsonConfigs = new GsonConfigs(allTextFields, comboBoxes);

        setUpListOfSavedConfigs();
        setActions();
    }

    private void setActions() {
        startTheSimulation.setOnAction(event -> startTheSimulation());
        saveConfigs.setOnAction(event -> saveConfigs());
        listOfSavedConfigs.setOnAction(event -> {
            try {
                gsonConfigs.readConfigs(listOfSavedConfigs.getValue());
            } catch (FileNotFoundException e) {
                showError("Missing file Error", "", "");
            }
        });
    }

    private void setUpListOfSavedConfigs() {
        try {
            ObservableList<String> listOfConfigs = FXCollections.observableArrayList();
            gsonConfigs.filesAsList(listOfConfigs);

            listOfSavedConfigs.setItems(listOfConfigs);
            listOfSavedConfigs.setPromptText("Saved configs");
        } catch (IOException e) {
            listOfSavedConfigs.setPromptText("Failed to load");
            showError("File load Error", "Saved files could not be loaded", e.getMessage());
        }
        listOfSavedConfigs.setStyle("-fx-font-family: 'Verdana'; -fx-background-color: #F3B153;");
    }

    private void setUpComboBoxes() {
        mapOption.setItems(FXCollections.observableArrayList("Underground tunnels", "Globe"));
        mapOption.setPromptText("Map option");
        mapOption.setStyle("-fx-font-family: 'Verdana'; -fx-background-color: #F3B153;");

        mutationOption.setItems(FXCollections.observableArrayList("Full randomness", "Swap"));
        mutationOption.setPromptText("Mutation Option");
        mutationOption.setStyle("-fx-font-family: 'Verdana'; -fx-background-color: #F3B153;");
    }

    private void setUpFields() {
        nonNegativeFields = Arrays.asList(
                initialNumberOfPlants, energyFromOnePlant, plantsEachDay, initialNumberOfAnimals, initialEnergyOfAnimals,
                energyToBeWellFed, energyToReproduce, maxNumberOfMutations, minNumberOfMutations);
        positiveFields = Arrays.asList(mapHeight, mapWidth,lengthOfGenotypes);

        positiveFields.forEach(field -> field.setTextFormatter(positiveInteger()));
        nonNegativeFields.forEach(field -> field.setTextFormatter(nonNegativeInteger()));

        allTextFields = new ArrayList<>(nonNegativeFields);
        allTextFields.addAll(positiveFields);
    }

    private void startTheSimulation() {
        StringBuilder errorMessage = new StringBuilder();
        if (inputIsValid(errorMessage) & areNotGreater(errorMessage)) {
            try {
                loadSimulationScene();
            } catch (IOException e) {
                System.err.println("Failed to start the simulation. Reason: " + e.getMessage()); // maybe use showError() instead?
                Platform.exit();
            }
        } else {
            showValidationAlert(errorMessage.toString());
        }
    }

    private void loadSimulationScene() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/simulation.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        Stage currentStage = (Stage) startTheSimulation.getScene().getWindow();
        currentStage.hide();
        stage.show();
    }

    private TextFormatter<Integer> createIntegerFormatter(Predicate<Integer> condition) {
        return new TextFormatter<>(value -> {
            if (value.isDeleted()) {
                return value;
            }
            String newText = value.getControlNewText();

            if (newText.matches("\\d+")) {
                try {
                    int n = Integer.parseInt(newText);
                    return condition.test(n) ? value : null;
                } catch (NumberFormatException e) {
                    return null;
                }
            }
            return null;
        });
    }

    private TextFormatter<Integer> nonNegativeInteger() {
        return createIntegerFormatter(n -> n >= 0);
    }

    private TextFormatter<Integer> positiveInteger() {
        return createIntegerFormatter(n -> n > 0);
    }

    private boolean inputIsValid(StringBuilder errorMessage) {
        boolean isValid = positiveFields.stream().noneMatch(field -> field.getText().isEmpty())
                && nonNegativeFields.stream().noneMatch(field -> field.getText().isEmpty())
                && mutationOption.getValue() != null
                && mapOption.getValue() != null;
        if (!isValid) {
            errorMessage.append("Field cannot be empty.\n");
        }
        return isValid;
    }

    private void showValidationAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validation Error");
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setHeaderText("Correct the input!");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String title, String header, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private Optional<String> showFileNameForm() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Configs file name");
        dialog.setHeaderText("Please enter a file name below.");
        dialog.setContentText("Name:");

        return dialog.showAndWait();
    }

    private boolean areNotGreater(StringBuilder errorMessage) {
        int mapArea = getValueFromTextField(mapWidth) * getValueFromTextField(mapHeight);
        return checkMaxValues(mapArea, initialNumberOfPlants, "Initial number of plants", errorMessage)
                && checkMaxValues(mapArea, plantsEachDay, "Number of plants growing each day", errorMessage)
                && checkMaxValues(getValueFromTextField(energyToBeWellFed), energyToReproduce,
                "Minimal energy to reproduce", errorMessage)
                && checkMaxValues(getValueFromTextField(maxNumberOfMutations), minNumberOfMutations,
                "Minimal number of mutations", errorMessage);
    }

    private boolean checkMaxValues(int maxValue, TextField field, String violated, StringBuilder errorMessage) {
        if (getValueFromTextField(field) > maxValue) {
            field.clear();
            errorMessage.append(violated + " cannot be greater than the map area.\n");
            return false;
        }
        return true;
    }

    private void saveConfigs() {
        StringBuilder errorMessage = new StringBuilder();
        if (inputIsValid(errorMessage) & areNotGreater(errorMessage)) {
            Optional<String> fileName = showFileNameForm();
            try {
                if (fileName.isPresent()) {
                    gsonConfigs.saveConfigs(fileName.get());
                }
            } catch (DuplicateConfigNameException e) {
                showError("Duplicate file name Error", "", e.getMessage());
            } catch (IOException e) {
                showError("Error", "Something went wrong during traversing savedConfigs folder.", e.getMessage());
            }
        } else {
            showValidationAlert(errorMessage.toString());
        }
    }

    private int getValueFromTextField(TextField textField) {
        try {
            return Integer.parseInt(textField.getText());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
