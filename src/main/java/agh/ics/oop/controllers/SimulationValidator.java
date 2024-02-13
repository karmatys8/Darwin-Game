package agh.ics.oop.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Region;

import java.util.List;
import java.util.function.Predicate;

abstract public class SimulationValidator {
    @FXML protected ComboBox<String> mapOption;
    @FXML protected ComboBox<String> mutationOption;
    @FXML protected TextField mapWidth;
    @FXML protected TextField mapHeight;
    @FXML protected TextField initialNumberOfPlants;
    @FXML protected TextField initialNumberOfAnimals;
    @FXML protected TextField plantsEachDay;
    @FXML protected TextField energyToBeWellFed;
    @FXML protected TextField energyToReproduce;
    @FXML protected TextField maxNumberOfMutations;
    @FXML protected TextField minNumberOfMutations;

    List<TextField> nonNegativeFields, positiveFields;

    abstract protected void setUpComboBoxes();

    abstract protected void setUpFields();

    protected TextFormatter<Integer> createIntegerFormatter(Predicate<Integer> condition) {
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

    protected TextFormatter<Integer> nonNegativeInteger() {
        return createIntegerFormatter(n -> n >= 0);
    }

    protected TextFormatter<Integer> positiveInteger() {
        return createIntegerFormatter(n -> n > 0);
    }

    protected boolean inputIsValid(StringBuilder errorMessage) {
        boolean isValid = positiveFields.stream().noneMatch(field -> field.getText().isEmpty())
                && nonNegativeFields.stream().noneMatch(field -> field.getText().isEmpty())
                && mutationOption.getValue() != null
                && mapOption.getValue() != null;
        if (!isValid) {
            errorMessage.append("Field cannot be empty.\n");
        }
        return isValid;
    }

    protected void showValidationAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validation Error");
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setHeaderText("Correct the input!");
        alert.setContentText(message);
        alert.showAndWait();
    }

    protected void showError(String title, String header, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    protected boolean areParametersInRange(StringBuilder errorMessage) {
        int mapArea = getValueFromTextField(mapWidth) * getValueFromTextField(mapHeight);
        return  checkMaxValues(100, mapWidth, "Map width cannot be greater than 100. That would lag the simulation!", errorMessage)
                & checkMaxValues(100, mapHeight, "Map height cannot be greater than 100. That would lag the simulation!", errorMessage)
                & checkMaxValues(mapArea, initialNumberOfPlants, "Initial number of plants cannot be greater than the map area.", errorMessage)
                & checkMaxValues(10 * mapArea, initialNumberOfAnimals, "That number of animals would lag the simulation!", errorMessage)
                & checkMaxValues(mapArea, plantsEachDay, "Number of plants growing each day cannot be greater than the map area.", errorMessage)
                & checkMaxValues(getValueFromTextField(energyToBeWellFed), energyToReproduce,
                "Minimal energy to reproduce cannot be greater than the energy to be well fed.", errorMessage)
                & checkMaxValues(getValueFromTextField(maxNumberOfMutations), minNumberOfMutations,
                "Minimal number of mutations cannot be greater than the maximal number", errorMessage);
    }

    protected boolean checkMaxValues(int maxValue, TextField field, String message, StringBuilder errorMessage) {
        if (getValueFromTextField(field) > maxValue) {
            field.clear();
            errorMessage.append(message+"\n");
            return false;
        }
        return true;
    }

    protected int getValueFromTextField(TextField textField) {
        try {
            return Integer.parseInt(textField.getText());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
