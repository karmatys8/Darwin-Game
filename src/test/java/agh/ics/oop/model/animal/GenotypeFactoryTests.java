package agh.ics.oop.model.animal;

import agh.ics.oop.model.movement.Vector2d;
import agh.ics.oop.model.util.RandomInteger;
import agh.ics.oop.model.util.configs.AnimalConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GenotypeFactoryTests {
    private static final Vector2d position = new Vector2d(1, 1);
    private static final AnimalConfig animalConfig = new AnimalConfig(1, 1, 1, 1, 1, 1, 1, "Full randomness");

    @Test
    public void testGetGenotype1() {
        for (int i = 0; i < 30; i++) {
            Assertions.assertInstanceOf(RandomGenotype.class, GenotypeFactory.getGenotype("Full randomness",
                    RandomInteger.getRandomInt(1, 16), RandomInteger.getRandomInt(1, 16), RandomInteger.getRandomInt(1, 16)));
            Assertions.assertInstanceOf(SwitchGenotype.class, GenotypeFactory.getGenotype("Swap",
                    RandomInteger.getRandomInt(1, 16), RandomInteger.getRandomInt(1, 16), RandomInteger.getRandomInt(1, 16)));
            Assertions.assertThrows(IllegalArgumentException.class, () -> GenotypeFactory.getGenotype("Random string",
                    RandomInteger.getRandomInt(1, 16), RandomInteger.getRandomInt(1, 16), RandomInteger.getRandomInt(1, 16)));
            Assertions.assertThrows(IllegalArgumentException.class, () -> GenotypeFactory.getGenotype("Full randomnes",
                    RandomInteger.getRandomInt(1, 16), RandomInteger.getRandomInt(1, 16), RandomInteger.getRandomInt(1, 16)));
            Assertions.assertThrows(IllegalArgumentException.class, () -> GenotypeFactory.getGenotype("",
                    RandomInteger.getRandomInt(1, 16), RandomInteger.getRandomInt(1, 16), RandomInteger.getRandomInt(1, 16)));
        }
    }

    @Test
    public void testGetGenotype2() {
        for (int i = 0; i < 30; i++) {
            Animal animal1 = new Animal(position, animalConfig);
            Animal animal2 = new Animal(position, animalConfig);
            Assertions.assertInstanceOf(RandomGenotype.class, GenotypeFactory.getGenotype("Full randomness", animal1, animal2));
            Assertions.assertInstanceOf(SwitchGenotype.class, GenotypeFactory.getGenotype("Swap", animal1, animal2));
            Assertions.assertThrows(IllegalArgumentException.class, () -> GenotypeFactory.getGenotype("Even more random string", animal1, animal2));
            Assertions.assertThrows(IllegalArgumentException.class, () -> GenotypeFactory.getGenotype("Fullrandomness", animal1, animal2));
            Assertions.assertThrows(IllegalArgumentException.class, () -> GenotypeFactory.getGenotype("", animal1, animal2));
        }
    }

    @Test
    public void testGetGenotype3() {
        for (int i = 0; i < 30; i++) {
            RandomGenotype randomGenotype = new RandomGenotype(8, 1, 7);
            Assertions.assertEquals(randomGenotype, GenotypeFactory.getGenotype(randomGenotype));
            SwitchGenotype switchGenotype = new SwitchGenotype(8, 1, 7);
            Assertions.assertEquals(switchGenotype, GenotypeFactory.getGenotype(switchGenotype));
        }
    }
}
