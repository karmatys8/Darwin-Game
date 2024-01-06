package agh.ics.oop.model.util;

import agh.ics.oop.model.animal.Genotype;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MostCommonGenotypeTests {
    @Test
    public void heapTest() {
        MostCommonGenotype heap = new MostCommonGenotype();
        Genotype genotype1 = new Genotype(7, 0, 0);
        Genotype genotype2 = new Genotype(8, 0, 0);
        Genotype genotype3 = new Genotype(9, 0, 0);
        Genotype genotype4 = new Genotype(10, 0, 0);
        Genotype genotype5 = new Genotype(genotype4);
        heap.insert(genotype1);
        heap.insert(genotype2);
        heap.insert(genotype1);
        heap.insert(genotype3);
        heap.insert(genotype4);
        heap.insert(genotype1);
        heap.insert(genotype1);
        heap.insert(genotype3);
        heap.insert(genotype4);
        heap.insert(genotype4);
        heap.remove(genotype1);
        heap.remove(genotype1);
        heap.insert(genotype5);
        heap.remove(genotype4);
        heap.insert(genotype1);
        heap.insert(genotype5);
        heap.remove(genotype2);
        Assertions.assertEquals(heap.getMostPopularGenotype(), genotype5);
        Assertions.assertEquals(heap.getMostPopularGenotype(), genotype4);
    }
}

