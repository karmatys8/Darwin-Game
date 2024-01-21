package agh.ics.oop.model.util.exceceptions;

import javafx.scene.Node;

public class UnexpectedNodeException extends IllegalArgumentException {
    public UnexpectedNodeException(Node node) {
        super("Received unexpected Node type: " + node.getClass().getName());
    }
}
