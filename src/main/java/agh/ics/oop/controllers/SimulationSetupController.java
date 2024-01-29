package agh.ics.oop.controllers;

import agh.ics.oop.model.util.configs.AnimalConfig;
import agh.ics.oop.model.util.configs.PlantConfig;
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
    @FXML private TextField updateInterval;
    @FXML private CheckBox csvCheckBox;

    List<TextField> nonNegativeFields, positiveFields, allTextFields;
    List<ComboBox<String>> comboBoxes;
    GsonConfigs configsManager;


    public void initialize() {
        setUpFields();
        setUpComboBoxes();
        comboBoxes = List.of(mapOption, mutationOption);
        configsManager = new GsonConfigs(allTextFields, comboBoxes);

        setUpListOfSavedConfigs();
        setActions();
    }

    private void setActions() {
        startTheSimulation.setOnAction(event -> startTheSimulation());
        saveConfigs.setOnAction(event -> saveConfigs());
        listOfSavedConfigs.setOnAction(event -> {
            try {
                configsManager.readConfigs(listOfSavedConfigs.getValue());
            } catch (FileNotFoundException e) {
                showError("Missing file Error", "", "");
            }
        });
    }

    private void setUpListOfSavedConfigs() {
        try {
            ObservableList<String> listOfConfigs = FXCollections.observableArrayList();
            configsManager.filesAsList(listOfConfigs);

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
        positiveFields = Arrays.asList(mapHeight, mapWidth, lengthOfGenotypes, updateInterval);

        positiveFields.forEach(field -> field.setTextFormatter(positiveInteger()));
        nonNegativeFields.forEach(field -> field.setTextFormatter(nonNegativeInteger()));

        allTextFields = new ArrayList<>(nonNegativeFields);
        allTextFields.addAll(positiveFields);
    }

    private void startTheSimulation() {
        StringBuilder errorMessage = new StringBuilder();
        if (inputIsValid(errorMessage) & areInBoundaries(errorMessage)) {
            new Thread(() -> {
                try {
                    loadSimulationScene();
                } catch (IOException e) {
                    showError("Error", "Failed to start the simulation.", e.getMessage());
                    Platform.exit();
                }
            }).start();
        } else {
            showValidationAlert(errorMessage.toString());
        }
    }

    private void loadSimulationScene() throws IOException {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/simulation.fxml"));
                Scene scene = new Scene(loader.load());
                Stage stage = new Stage();
                stage.setScene(scene);

                SimulationController controller = loader.getController();
                setSimulationController(controller);
                stage.show();
            } catch (IOException e) {
                showError("Error", "Failed to start the simulation.", e.getMessage());
                Platform.exit();
            }
        });
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

    private boolean areInBoundaries(StringBuilder errorMessage) { // nieczytelna nazwa
        int mapArea = getValueFromTextField(mapWidth) * getValueFromTextField(mapHeight);
        return  checkMaxValues(100, mapWidth, "Map width cannot be greater than 100. That would lag the simulation!", errorMessage)
                & checkMaxValues(100, mapHeight, "Map height cannot be greater than 100. That would lag the simulation!", errorMessage)
                & checkMaxValues(mapArea, initialNumberOfPlants, "Initial number of plants cannot be greater than the map area.", errorMessage)
                & checkMaxValues(10 * mapArea, initialNumberOfAnimals, "That number of animals would lag the simulation!", errorMessage)
                & checkMaxValues(mapArea, plantsEachDay, "Number of plants growing each day cannot be greater than the map area.", errorMessage)
                & checkMaxValues(getValueFromTextField(energyToBeWellFed), energyToReproduce,
                "Minimal energy to reproduce cannot be greater than the energy to be well fed.", errorMessage)
                & checkMaxValues(getValueFromTextField(maxNumberOfMutations), minNumberOfMutations, "Minimal number of mutations cannot be greater than the maximal number", errorMessage);
    }

    private boolean checkMaxValues(int maxValue, TextField field, String message, StringBuilder errorMessage) {
        if (getValueFromTextField(field) > maxValue) {
            field.clear();
            errorMessage.append(message+"\n");
            return false;
        }
        return true;
    }

    private void saveConfigs() {
        StringBuilder errorMessage = new StringBuilder();
        if (inputIsValid(errorMessage) & areInBoundaries(errorMessage)) {
            Optional<String> fileName = showFileNameForm();
            try {
                if (fileName.isPresent()) {
                    configsManager.saveConfigs(fileName.get());
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

    private void setSimulationController(SimulationController simulationController) {
        AnimalConfig animalConfig = new AnimalConfig(getValueFromTextField(initialNumberOfAnimals), getValueFromTextField(initialEnergyOfAnimals), getValueFromTextField(energyToBeWellFed), getValueFromTextField(energyToReproduce), getValueFromTextField(minNumberOfMutations), getValueFromTextField(maxNumberOfMutations), getValueFromTextField(lengthOfGenotypes), mutationOption.getValue());
        PlantConfig plantConfig = new PlantConfig(getValueFromTextField(initialNumberOfPlants), getValueFromTextField(energyFromOnePlant), getValueFromTextField(plantsEachDay));
        simulationController.setConfigs(animalConfig, plantConfig, getValueFromTextField(mapWidth), getValueFromTextField(mapHeight), getValueFromTextField(updateInterval), mapOption.getValue(), csvCheckBox.isSelected());
    }
} // duża ta klasa
