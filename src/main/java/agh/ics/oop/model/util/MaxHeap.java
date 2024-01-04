package agh.ics.oop.model.util;

import agh.ics.oop.model.animal.Genotype;

import java.util.HashMap;
import java.util.Map;

public class MaxHeap {
    private final Genotype[] heap;
    private final Map<Genotype, Integer> genotypeCounterMap;
    private final Map<Genotype, Integer> genotypeIndexMap;
    private final int maxsize = 1000000;
    private int size;

    public MaxHeap() {
        this.size = 0;
        heap = new Genotype[this.maxsize];
        genotypeCounterMap = new HashMap<>();
        genotypeIndexMap = new HashMap<>();
    }

    private int parent(int i) {
        return ((i - 1) / 2);
    }

    private int left(int i) {
        return ((2 * i) + 1);
    }

    private int right(int i) {
        return ((2 * i) + 2);
    }

    private void swap(int i, int j) {
        genotypeIndexMap.remove(heap[i]);
        genotypeIndexMap.remove(heap[j]);
        genotypeIndexMap.put(heap[i], j);
        genotypeIndexMap.put(heap[j], i);

        Genotype temporary;
        temporary = heap[i];
        heap[i] = heap[j];
        heap[j] = temporary;
    }

    private void heapifyDown(int i) {
        int left = left(i);
        int right = right(i);
        int max = i;
        if (left < size && genotypeCounterMap.get(heap[left]) > genotypeCounterMap.get(heap[max])) {
            max = left;
        }
        if (right < size && genotypeCounterMap.get(heap[right]) > genotypeCounterMap.get(heap[max])) {
            max = right;
        }
        if (max != i) {
            swap(max, i);
            heapifyDown(max);
        }
    }

    private void heapifyUp(int i) {
        while (genotypeCounterMap.get(heap[i]) > genotypeCounterMap.get(heap[parent(i)])) {
            swap(i, parent(i));
            i = parent(i);
        }
    }

    public void insert(Genotype genotype) {
        if (size >= maxsize) {
            throw new IllegalStateException("Heap is full. Cannot insert new element.");
        }
        if (genotypeCounterMap.containsKey(genotype)) {
            int index = genotypeIndexMap.get(genotype);
            genotypeCounterMap.merge(genotype, 1, Integer::sum);
            heapifyUp(index);
        } else {
            int index = size;
            genotypeCounterMap.put(genotype, 1);
            genotypeIndexMap.put(genotype, size);
            heap[index] = genotype;
            size++;
            heapifyUp(size - 1);
        }
    }

    public void remove(Genotype genotype) {
        if (genotypeCounterMap.containsKey(genotype)) {
            genotypeCounterMap.merge(genotype, -1, Integer::sum);
            int index = genotypeIndexMap.get(genotype);
            if (genotypeCounterMap.get(genotype) == 0) {
                swap(index, size);
                size--;
                heapifyDown(index);
                genotypeCounterMap.remove(genotype);
                genotypeIndexMap.remove(genotype);
                heap[size + 1] = null;
            } else {
                heapifyDown(index);
            }
        }
    }

    public Genotype get(int i) {
        return heap[i];
    }

    public void print() {
        for (int i = 0; i < size / 2; i++) {
            System.out.print("Parent Node: " + heap[i]);
            if (left(i) < size)
                System.out.print(" Left Child Node: " + heap[left(i)]);
            if (right(i) < size)
                System.out.print(" Right Child Node: " + heap[right(i)]);
            System.out.println();
        }
    }
}
