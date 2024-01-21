package agh.ics.oop.model.util.exceceptions;

import java.util.UUID;

public class CSVFileWritingException extends RuntimeException{
    public CSVFileWritingException(UUID simulationId, Throwable cause) {
        super("CSV file for Simulation with ID: " + simulationId + " was not created correctly.", cause);
    }
}
