package agh.ics.oop.controllers;

import agh.ics.oop.model.util.exceceptions.DuplicateConfigNameException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;


public class GsonConfigs {
    private static final String savedConfigsUrl = "src/main/resources/savedConfigs";
    Path savedConfigsPath = Paths.get(savedConfigsUrl);
    GsonBuilder gsonBuilder = new GsonBuilder();
    List<TextField> textFields;
    List<ComboBox> comboBoxes;

    public GsonConfigs(List<TextField> textFields, List<ComboBox> comboBoxes) {
        this.textFields = textFields;
        this.comboBoxes = comboBoxes;
        gsonBuilder.setPrettyPrinting();
    }

    private static boolean isJsonFile(Path filePath) {
        return filePath.getFileName().toString().toLowerCase().endsWith(".json");
    }

    public void filesAsList(ObservableList<String> listOfConfigs) throws IOException {
        Files.walk(savedConfigsPath)
            .filter(Files::isRegularFile)
            .filter(GsonConfigs::isJsonFile)
            .forEach(path -> listOfConfigs.add(String.valueOf(path.getFileName())));
    }

    public void saveConfigs(String name) throws IOException, DuplicateConfigNameException {
        File file = new File(savedConfigsUrl, name + ".json");
        if (file.exists()) {
            throw new DuplicateConfigNameException(name);
        }

        Path createdFile = Files.createFile(Path.of(savedConfigsUrl, name + ".json"));
        Map<String, Object> keyValueMap = new HashMap<>();
        textFields.forEach(field -> keyValueMap.put(field.getId(), field.getText()));
        comboBoxes.forEach(box -> keyValueMap.put(box.getId(), box.getValue()));

        Gson gson = gsonBuilder.create();
        String json = gson.toJson(keyValueMap);

        Files.write(createdFile, Collections.singleton(json), StandardCharsets.UTF_8);
    }
}
