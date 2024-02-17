package agh.ics.oop;

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
    private final Path createdFile;

    public CSVWriter(Label[] statsToTrack, UUID mapId, String[] columnNames) throws IOException {
        this.simulationStats = statsToTrack;

        Path filePath = Paths.get(CSV_FILES_URL, mapId + ".csv");
        if (Files.exists(filePath)) {
            throw new RuntimeException("File already exists");
        }

        createdFile = Files.createFile(filePath);
        Files.write(createdFile, Collections.singleton(convertToCSV(columnNames)), StandardCharsets.UTF_8);
    }

    public void addStatsToCSV(int day, int animalCount, int plantCount) throws IOException {
        String[] data = Stream.concat(Stream.of(day + "", animalCount + "", plantCount + ""),
                        Arrays.stream(simulationStats)
                                .map(Label::getText))
                .toArray(String[]::new);

        Files.writeString(createdFile,
                convertToCSV(data) + System.lineSeparator(),
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
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
