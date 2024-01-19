package agh.ics.oop.controllers;

import agh.ics.oop.model.util.exceceptions.DuplicateConfigNameException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;


public class GsonConfigs {
    private static final String SAVED_CONFIGS_URL = "src/main/resources/savedConfigs";
    private final static Path savedConfigsPath = Paths.get(SAVED_CONFIGS_URL);
    private final static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final JsonParser jsonParser = new JsonParser();
    private final List<TextField> textFields;
    private final List<ComboBox> comboBoxes;

    public GsonConfigs(List<TextField> textFields, List<ComboBox> comboBoxes) {
        this.textFields = textFields;
        this.comboBoxes = comboBoxes;
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
        File file = new File(SAVED_CONFIGS_URL, name + ".json");
        if (file.exists()) {
            throw new DuplicateConfigNameException(name);
        }

        Path createdFile = Files.createFile(Path.of(SAVED_CONFIGS_URL, name + ".json"));
        Map<String, Object> keyValueMap = new HashMap<>();
        textFields.forEach(field -> keyValueMap.put(field.getId(), field.getText()));
        comboBoxes.forEach(box -> keyValueMap.put(box.getId(), box.getValue()));

        String json = gson.toJson(keyValueMap);
        Files.write(createdFile, Collections.singleton(json), StandardCharsets.UTF_8);
    }

    public void readConfigs(String fileName) throws FileNotFoundException {
        File file = new File(SAVED_CONFIGS_URL, fileName);
        FileReader reader = new FileReader(file);

        Type type = new TypeToken<Map<String, Object>>(){}.getType();
        Map<String, String> jsonMap = gson.fromJson(reader, type);

        textFields.forEach(field -> field.setText(jsonMap.get(field.getId())));
        comboBoxes.forEach(box -> box.setValue(jsonMap.get(box.getId())));
    }
}
