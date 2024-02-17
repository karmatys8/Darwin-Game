package agh.ics.oop.controllers;

import agh.ics.oop.CSVWriter;
import agh.ics.oop.model.Simulation;
import agh.ics.oop.model.animal.Animal;
import agh.ics.oop.model.animal.Genotype;
import agh.ics.oop.model.util.Average;
import agh.ics.oop.model.util.exceceptions.CSVFileWritingException;
import javafx.scene.Node;
import javafx.scene.control.Label;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Stream;

public class Statistics {
    private final Simulation simulation;
    private Animal observedAnimal = null;

    private final Label[] animalStats;
    private Node[] simulationStats;
    private boolean shouldWriteToCSV;
    private CSVWriter csvWriter;
    private int emptyCellsCounter = 0;

    public Statistics(Simulation simulation, Label[] animalStats, Node[] simulationStats, boolean shouldWriteToCSV) {
        this.simulation = simulation;
        this.animalStats = animalStats;
        this.simulationStats = simulationStats;
        this.shouldWriteToCSV = shouldWriteToCSV;

        initializeCSVWriter();
    }

    private void initializeCSVWriter() {
        if (shouldWriteToCSV) {
            try {
                Label[] statsToTrack = (Label[]) (this.simulationStats = Arrays.stream(simulationStats, 0, 5)
                                        .map(node -> (Label) node)
                                        .toArray(Label[]::new));

                String[] columnNames = Stream.concat(Stream.of("Day number", "Animal count", "Plant count"),
                                Arrays.stream(statsToTrack)
                                        .map(Label::getId)
                                        .map(name -> name.replace("Label", "")))
                        .toArray(String[]::new);

                csvWriter = new CSVWriter(statsToTrack, simulation.getId(), columnNames);
            } catch (IOException e) {
                handleCSVError(e);
            }
        }
    }

    private void handleCSVError(IOException e) {
        shouldWriteToCSV = false;
        throw new CSVFileWritingException(simulation.getId(), e.getCause());
    }

    void updateAnimalInformation() {
        animalStats[0].setText("Genotype: " + observedAnimal.getGenotype());
        animalStats[1].setText("Current gene: " + observedAnimal.getCurrentGene());
        animalStats[2].setText("Energy: " + observedAnimal.getEnergy());
        animalStats[3].setText("Eaten plants: " + observedAnimal.getPlantsEaten());
        animalStats[4].setText("Number of children: " + observedAnimal.getNumberOfChildren());
        animalStats[5].setText("Number of descendants: " + observedAnimal.getNumberOfDescendants());
        animalStats[6].setText(observedAnimal.getDayOfDeath() == null
                ? "Days lived: " + observedAnimal.getDaysLived()
                : "Day of death: " + observedAnimal.getDayOfDeath());
    }
    void updateStats() {
        Average[] simulationAverageStats = simulation.getSimulationStats();
        ((Label) simulationStats[0]).setText(String.valueOf(emptyCellsCounter));
        Genotype mostCommonGenotype = simulation.getMostCommonGenotype();
        ((Label) simulationStats[1]).setText(mostCommonGenotype.toString());
        ((Label) simulationStats[2]).setText(String.valueOf(simulationAverageStats[0].getAverage()));
        ((Label) simulationStats[3]).setText(String.valueOf(simulationAverageStats[1].getAverage()));
        ((Label) simulationStats[4]).setText(String.valueOf(simulationAverageStats[2].getAverage()));

        if (shouldWriteToCSV) {
            try {
                csvWriter.addStatsToCSV(simulation.getCurrentDay(), simulation.getNumberOfAnimals(), simulation.getNumberOfPlants());
            } catch (IOException e) {
                handleCSVError(e);
            }
        }
    }

    void resetEmptyCellsCounter() {
        emptyCellsCounter = 0;
    }

    void countAnEmptyCell() {
        emptyCellsCounter++;
    }

    void setObservedAnimal(Animal animal) {
        observedAnimal = animal;
    }
}
