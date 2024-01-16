package agh.ics.oop.model.worldElements.artificialElements;

import agh.ics.oop.model.worldElements.WorldElement;
import javafx.scene.control.Label;

public class Plant implements WorldElement {
    @Override
    public String getElementString() {
        return "*";
    }
}
