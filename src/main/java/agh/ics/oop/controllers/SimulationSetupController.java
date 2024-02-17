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
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class SimulationSetupController extends SimulationValidator {
    @FXML private ComboBox<String> listOfSavedConfigs;
    @FXML private TextField energyFromOnePlant;
    @FXML private TextField initialEnergyOfAnimals;
    @FXML private TextField lengthOfGenotypes;
    @FXML private TextField maxNumberOfMutations;
    @FXML private TextField minNumberOfMutations;
    @FXML private Button startTheSimulation;
    @FXML private Button saveConfigs;
    @FXML private TextField updateInterval;
    @FXML private CheckBox csvCheckBox;

    List<TextField> allTextFields;
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
                String value = listOfSavedConfigs.getValue();
                if (value != null) { // once list of possible values is updated this event fires but combobox value is null
                    configsManager.readConfigs(value);
                }
            } catch (FileNotFoundException e) {
                showError("Missing file Error", "", "");
            }
        });
    }

    void updateListOfSavedConfigs() throws IOException {
        ObservableList<String> listOfConfigs = FXCollections.observableArrayList();
        configsManager.filesAsList(listOfConfigs);
        listOfSavedConfigs.setItems(listOfConfigs);
    }

    private void setUpListOfSavedConfigs() {
        try {
            updateListOfSavedConfigs();
            listOfSavedConfigs.setPromptText("Saved configs");
        } catch (IOException e) {
            listOfSavedConfigs.setPromptText("Failed to load");
            showError("File load Error", "Saved files could not be loaded", e.getMessage());
        }
        listOfSavedConfigs.setStyle("-fx-font-family: 'Verdana'; -fx-background-color: #F3B153;");
    }

    protected void setUpComboBoxes() {
        mapOption.setItems(FXCollections.observableArrayList("Underground tunnels", "Globe"));
        mapOption.setPromptText("Map option");
        mapOption.setStyle("-fx-font-family: 'Verdana'; -fx-background-color: #F3B153;");

        mutationOption.setItems(FXCollections.observableArrayList("Full randomness", "Swap"));
        mutationOption.setPromptText("Mutation Option");
        mutationOption.setStyle("-fx-font-family: 'Verdana'; -fx-background-color: #F3B153;");
    }

    protected void setUpFields() {
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
        if (inputIsValid(errorMessage) & areParametersInRange(errorMessage)) {
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

    private Optional<String> showFileNameForm() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Configs file name");
        dialog.setHeaderText("Please enter a file name below.");
        dialog.setContentText("Name:");
        return dialog.showAndWait();
    }

    private void saveConfigs() {
        StringBuilder errorMessage = new StringBuilder();
        if (inputIsValid(errorMessage) & areParametersInRange(errorMessage)) {
            Optional<String> fileName = showFileNameForm();
            try {
                if (fileName.isPresent()) {
                    String name = fileName.get();
                    configsManager.saveConfigs(name);
                    updateListOfSavedConfigs();
                    listOfSavedConfigs.setValue(name + ".json");
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

    private void setSimulationController(SimulationController simulationController) {
        AnimalConfig animalConfig = new AnimalConfig(getValueFromTextField(initialNumberOfAnimals), getValueFromTextField(initialEnergyOfAnimals),
                getValueFromTextField(energyToBeWellFed), getValueFromTextField(energyToReproduce), getValueFromTextField(minNumberOfMutations),
                getValueFromTextField(maxNumberOfMutations), getValueFromTextField(lengthOfGenotypes), mutationOption.getValue());
        PlantConfig plantConfig = new PlantConfig(getValueFromTextField(initialNumberOfPlants),
                getValueFromTextField(energyFromOnePlant), getValueFromTextField(plantsEachDay));
        simulationController.setConfigs(animalConfig, plantConfig, getValueFromTextField(mapWidth), getValueFromTextField(mapHeight),
                getValueFromTextField(updateInterval), mapOption.getValue(), csvCheckBox.isSelected());
    }
}
