package agh.ics.oop.model.util;

import agh.ics.oop.model.animal.Genotype;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class MaxHeap {
    private PriorityQueue<Map.Entry<Genotype, Integer>> maxHeap;
    private final Map<Genotype, Integer> genotypeCounterMap;

    public MaxHeap() {
        maxHeap = new PriorityQueue<>(Comparator.comparingInt(entry -> ((Map.Entry<Genotype, Integer>) entry).getValue()).reversed());
        genotypeCounterMap = new HashMap<>();
    }

    public void insert(Genotype genotype) {
        int count = genotypeCounterMap.getOrDefault(genotype, 0) + 1;
        genotypeCounterMap.put(genotype, count);

        maxHeap.removeIf(entry -> entry.getKey().equals(genotype));
        maxHeap.add(Map.entry(genotype, count));
    }

    public void remove(Genotype genotype) {
        int count = genotypeCounterMap.get(genotype) - 1;
        maxHeap.removeIf(entry -> entry.getKey().equals(genotype));

        if (count > 0) {
            maxHeap.add(Map.entry(genotype, count));
            genotypeCounterMap.put(genotype, count);
        } else {
            genotypeCounterMap.remove(genotype);
        }
    }
    public Genotype getMostPopularGenotype(){
        return maxHeap.peek().getKey();
    }
}
