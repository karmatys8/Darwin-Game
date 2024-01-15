package agh.ics.oop.model.util;

import agh.ics.oop.model.animal.Genotype;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class MostCommonGenotype {
    private final PriorityQueue<Map.Entry<Genotype, Integer>> maxHeap = new PriorityQueue<>(
            Comparator.comparingInt(entry -> (
                    (Map.Entry<Genotype, Integer>) entry
            ).getValue()).reversed()
    );
    private final Map<Genotype, Integer> genotypeCounterMap = new HashMap<>();

    public void insert(Genotype genotype) {
        int count = genotypeCounterMap.getOrDefault(genotype, 0) + 1;
        genotypeCounterMap.put(genotype, count);

        maxHeap.remove(Map.entry(genotype, count-1));
        maxHeap.add(Map.entry(genotype, count));
    }

    public void remove(Genotype genotype) {
        int count = genotypeCounterMap.get(genotype) - 1;
        maxHeap.remove(Map.entry(genotype, count+1));
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
