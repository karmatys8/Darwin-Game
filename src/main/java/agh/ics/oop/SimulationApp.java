package agh.ics.oop;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class SimulationApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/simulationSetup.fxml"));
        Scene scene = new Scene(loader.load());
        Image icon = new Image(getClass().getResourceAsStream("/logo.png"));
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.show();
    }
}