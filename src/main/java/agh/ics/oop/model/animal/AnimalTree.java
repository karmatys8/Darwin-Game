package agh.ics.oop.model.animal;

import java.util.ArrayList;
import java.util.List;

public class AnimalTree {
    private final List<AnimalTree> children;
    private final Animal animal;

    public AnimalTree(Animal animal) {
        this.animal = animal;
        this.children = new ArrayList<>();
    }

    public void addChild(AnimalTree child) {
        children.add(child);
    }
    public int getChildrenCount() {
        return children.size();
    }

    public int getDescendantsCount() {
        int descendantsCount = children.size(); // liczba bezpośrednich potomków

        for (AnimalTree child : children) {
            descendantsCount += child.getDescendantsCount(); // rekurencyjnie dodaj potomków potomka
        }

        return descendantsCount;
    }
}
