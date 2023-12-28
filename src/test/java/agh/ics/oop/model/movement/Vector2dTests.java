package agh.ics.oop.model.movement;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Vector2dTests { // sometimes I declare the same vector few times; imo it is not a big deal
    @Test
    public void testToString() {
        Assertions.assertEquals("(8,0)", (new Vector2d(8, 0).toString()));
        Assertions.assertEquals("(1,3)", (new Vector2d(1, 3).toString()));
        Assertions.assertEquals("(100,-5)", (new Vector2d(100, -5).toString()));
        Assertions.assertEquals("(9394,9394)", (new Vector2d(9394, 9394).toString()));
        Assertions.assertEquals("(-2147483648,2147483647)", (new Vector2d(Integer.MIN_VALUE, Integer.MAX_VALUE).toString()));
    }

    @Test
    public void testPrecedes() {
        Vector2d vector1 = new Vector2d(1, 3);
        Vector2d vector2 = new Vector2d(10, -6);
        Vector2d vector3 = new Vector2d(50, 50);
        Vector2d vector4 = new Vector2d(Integer.MIN_VALUE, Integer.MIN_VALUE);

        Assertions.assertFalse(vector1.precedes(vector2));
        Assertions.assertTrue(vector1.precedes(vector3));
        Assertions.assertFalse(vector1.precedes(vector4));

        Assertions.assertFalse(vector2.precedes(vector1));
        Assertions.assertTrue(vector2.precedes(vector2));

        Assertions.assertTrue(vector4.precedes(vector1));
        Assertions.assertTrue(vector4.precedes(vector2));
        Assertions.assertTrue(vector4.precedes(vector3));
    }

    @Test
    public void testFollows() {
        Vector2d vector1 = new Vector2d(10, 4);
        Vector2d vector2 = new Vector2d(0, 8);
        Vector2d vector3 = new Vector2d(-9, -3);
        Vector2d vector4 = new Vector2d(Integer.MAX_VALUE, Integer.MAX_VALUE);

        Assertions.assertFalse(vector1.follows(vector2));
        Assertions.assertTrue(vector1.follows(vector3));
        Assertions.assertFalse(vector1.follows(vector4));

        Assertions.assertFalse(vector2.follows(vector1));
        Assertions.assertTrue(vector2.follows(vector2));

        Assertions.assertTrue(vector4.follows(vector1));
        Assertions.assertTrue(vector4.follows(vector2));
        Assertions.assertTrue(vector4.follows(vector3));
    }

    @Test
    public void testAdd() {
        Vector2d vector1 = new Vector2d(3, -2);
        Vector2d vector2 = new Vector2d(40, 2);
        Vector2d vector3 = new Vector2d(0, 0);

        Assertions.assertEquals(new Vector2d(43, 0), vector1.add(vector2));
        Assertions.assertEquals(new Vector2d(43, 0), vector2.add(vector1));

        Assertions.assertEquals(new Vector2d(80, 4), vector2.add(vector2));

        Assertions.assertEquals(vector1, vector1.add(vector3));
        Assertions.assertEquals(vector3, vector3.add(vector3));
    }

    @Test
    public void testSubtract() {
        Vector2d vector1 = new Vector2d(1, 7);
        Vector2d vector2 = new Vector2d(1, -8);
        Vector2d vector3 = new Vector2d(0, 0);

        Assertions.assertEquals(new Vector2d(0, 15), vector1.subtract(vector2));
        Assertions.assertEquals(new Vector2d(0, -15), vector2.subtract(vector1));

        Assertions.assertEquals(vector1, vector1.subtract(vector3));
        Assertions.assertEquals(new Vector2d(-1, 8), vector3.subtract(vector2));

        Assertions.assertEquals(vector3, vector2.subtract(vector2));
        Assertions.assertEquals(vector3, vector3.subtract(vector3));
    }

    @Test
    public void testUpperRight() {
        Vector2d vector1 = new Vector2d(100, 5);
        Vector2d vector2 = new Vector2d(-9, -9);
        Vector2d vector3 = new Vector2d(60, Integer.MAX_VALUE);
        Vector2d vector4 = new Vector2d(Integer.MIN_VALUE, Integer.MIN_VALUE);

        Assertions.assertEquals(vector1, vector1.upperRight(vector2));
        Assertions.assertEquals(new Vector2d(100, Integer.MAX_VALUE), vector1.upperRight(vector3));

        Assertions.assertEquals(vector1, vector1.upperRight(vector4));
        Assertions.assertEquals(vector2, vector2.upperRight(vector4));
        Assertions.assertEquals(vector3, vector3.upperRight(vector4));

        Assertions.assertEquals(vector3, vector2.upperRight(vector3));
        Assertions.assertEquals(vector3, vector3.upperRight(vector2));
    }

    @Test
    public void testLowerLeft() {
        Vector2d vector1 = new Vector2d(102, 50);
        Vector2d vector2 = new Vector2d(-1, 3);
        Vector2d vector3 = new Vector2d(-500, Integer.MAX_VALUE);
        Vector2d vector4 = new Vector2d(Integer.MAX_VALUE, Integer.MAX_VALUE);

        Assertions.assertEquals(vector2, vector1.lowerLeft(vector2));
        Assertions.assertEquals(new Vector2d(-500, 50), vector1.lowerLeft(vector3));

        Assertions.assertEquals(vector1, vector1.lowerLeft(vector4));
        Assertions.assertEquals(vector2, vector2.lowerLeft(vector4));
        Assertions.assertEquals(vector3, vector3.lowerLeft(vector4));

        Assertions.assertEquals(new Vector2d(-500, 3), vector2.lowerLeft(vector3));
        Assertions.assertEquals(new Vector2d(-500, 3), vector3.lowerLeft(vector2));
    }

    @Test
    public void testOpposite() {
        Vector2d vector1 = new Vector2d(2, 5);
        Vector2d vector2 = new Vector2d(-4, 9);
        Vector2d vector3 = new Vector2d(0, 0);

        Assertions.assertEquals(new Vector2d(-2, -5), vector1.opposite());
        Assertions.assertEquals(new Vector2d(4, -9), vector2.opposite());

        Assertions.assertEquals(vector2, vector2.opposite().opposite());
        Assertions.assertEquals(vector3, vector3.opposite());
    }
}
