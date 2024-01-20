package agh.ics.oop.model.util;

import agh.ics.oop.model.worldElements.animal.Genotype;
import agh.ics.oop.model.worldElements.animal.RandomGenotype;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MostCommonGenotypeTests {
    @Test
    public void heapTest() {
        MostCommonGenotype heap = new MostCommonGenotype();

        Genotype genotype1 = new RandomGenotype(7, 0, 0);
        Genotype genotype2 = new RandomGenotype(8, 0, 0);
        Genotype genotype3 = new RandomGenotype(9, 0, 0);
        Genotype genotype4 = new RandomGenotype(10, 0, 0);
        Genotype genotype5 = new RandomGenotype(genotype4);

        heap.insert(genotype1);
        heap.insert(genotype2);
        heap.insert(genotype1);

        Assertions.assertEquals(heap.getMostCommonGenotype(), genotype1);

        heap.insert(genotype3);
        heap.insert(genotype4);
        heap.insert(genotype1);
        heap.insert(genotype1);
        heap.insert(genotype3);
        heap.insert(genotype4);
        heap.insert(genotype4);

        Assertions.assertEquals(heap.getMostCommonGenotype(), genotype1);

        heap.remove(genotype1);
        heap.remove(genotype1);

        Assertions.assertNotEquals(heap.getMostCommonGenotype(), genotype1);
        Assertions.assertEquals(heap.getMostCommonGenotype(), genotype4);

        heap.insert(genotype5);
        heap.remove(genotype4);
        heap.insert(genotype1);
        heap.insert(genotype5);
        heap.remove(genotype2);

        Assertions.assertEquals(heap.getMostCommonGenotype(), genotype5);
        Assertions.assertEquals(heap.getMostCommonGenotype(), genotype4);
    }
}

