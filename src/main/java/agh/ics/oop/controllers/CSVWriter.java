package agh.ics.oop.controllers;

import javafx.scene.Node;
import javafx.scene.control.Label;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CSVWriter {
    private static final String CSV_FILES_URL = "src/main/resources/csvFiles";
    private final Label[] simulationStats;
    private final UUID mapId;
    private Path createdFile;

    public CSVWriter(Node[] simulationStats, UUID mapId) throws IOException {
        this.simulationStats = Arrays.stream(simulationStats, 0, 5)
                                        .map(node -> (Label) node)
                                        .toArray(Label[]::new);
        this.mapId = mapId;
        initializeFile();
    }

    private void initializeFile() throws IOException {
        Path filePath = Paths.get(CSV_FILES_URL, mapId + ".csv");
        if (Files.exists(filePath)) {
            throw new RuntimeException("File already exists");
        }
        createdFile = Files.createFile(filePath);
        String[] names = streamFormat(Stream.of(simulationStats)
                .map(Label::getId)
                .map(name -> name.replace("Label", "")),
                "Day number", "Animal count", "Plant count");

        Files.write(createdFile, Collections.singleton(convertToCSV(names)), StandardCharsets.UTF_8);
    }

    public void addStatsToCSV(int day, int animalCount, int plantCount) throws IOException {
        String[] data = streamFormat(Stream.of(simulationStats)
                        .map(Label::getText),
                day + "", animalCount + "", plantCount + "");

        Files.writeString(createdFile,
                convertToCSV(data) + System.lineSeparator(),
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    private String[] streamFormat(Stream<String> stream, String firstValue, String secondValue, String thirdValue) {
        return Stream.concat(Stream.of(firstValue, secondValue, thirdValue), stream).toArray(String[]::new);
    }

    private String convertToCSV(String[] data) {
        return Stream.of(data)
                .map(this::escapeSpecialCharacters)
                .collect(Collectors.joining(","));
    }

    private String escapeSpecialCharacters(String data) {
        if (data == null) {
            throw new IllegalArgumentException("Input data cannot be null");
        }
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }
}
