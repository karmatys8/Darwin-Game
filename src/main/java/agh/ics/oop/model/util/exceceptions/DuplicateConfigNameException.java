package agh.ics.oop.model.util.exceceptions;


public class DuplicateConfigNameException extends Exception {
    public DuplicateConfigNameException(String fileName) {
        super("File named: \"" + fileName +  "\" already exists in savedConfigs directory.");
    }
}
