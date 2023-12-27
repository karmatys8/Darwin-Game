package agh.ics.oop.presenter;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloPresenter {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}