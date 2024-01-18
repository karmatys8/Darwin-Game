package agh.ics.oop.model.util.exceceptions;

import java.nio.file.Path;

public class NonJsonFileException extends Exception {
    public NonJsonFileException(Path path) {
        super("Non-JSON file detected: \"" + path.getFileName() + "\" in savedConfigs directory.");
    }
}