package agh.ics.oop.model.util;

import agh.ics.oop.model.animal.Genotype;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MaxHeapTests {
    @Test
    public void heapTest() {
        MaxHeap heap = new MaxHeap();
        Genotype genotype1 = new Genotype(7, 0, 0);
        Genotype genotype2 = new Genotype(7, 0, 0);
        Genotype genotype3 = new Genotype(7, 0, 0);
        Genotype genotype4 = new Genotype(7, 0, 0);
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
        heap.print();
        Assertions.assertEquals(heap.get(0), genotype5);
        Assertions.assertEquals(heap.get(0), genotype4);
        Assertions.assertEquals(heap.get(1), genotype1);
        Assertions.assertEquals(heap.get(2), genotype3);
        Assertions.assertNull(heap.get(3));
    }
}
