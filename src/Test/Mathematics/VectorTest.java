package Test.Mathematics;

import Physics.Mathematics.Vector;
import org.junit.Test;

import static Physics.Mathematics.Constants.doubleEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class VectorTest {

    @Test
    public void initialisationTest(){
        Vector vector = new Vector(3.6, 4.8);
        assertTrue(doubleEquals(vector.getX(), 3.6));
        assertTrue(doubleEquals(vector.getY(), 4.8));

        vector = new Vector(-0, -0);
        assertEquals(vector, new Vector(0, 0));
    }

    @Test
    public void lengthSquaredTest(){
        Vector vector = new Vector(5, 5);
        assertTrue(doubleEquals(vector.lengthSquared(), 50.0));
    }

    @Test
    public void lengthTest(){
        Vector vector = new Vector(5, 5);
        assertTrue(doubleEquals(vector.length(), Math.sqrt(50)));
    }

    @Test
    public void distanceBetweenTest(){
        Vector vector = new Vector(0, 0);
        assertTrue(doubleEquals(vector.distanceBetween(new Vector(5, 0)), 5));
        assertTrue(doubleEquals(vector.distanceBetween(new Vector(0, 5)), 5));
        assertTrue(doubleEquals(vector.distanceBetween(new Vector(5, 5)), 7.0710678118654755));
    }

    @Test
    public void plusTest(){
        Vector vector = new Vector(0, 0);
        assertEquals(vector.plus(new Vector(1, 2)), new Vector(1, 2));
        assertEquals(vector.plus(new Vector(0, 0)), new Vector(0, 0));
        assertEquals(vector.plus(new Vector(-1, 3)), new Vector(-1, 3));
    }

    @Test
    public void minusTest(){
        Vector vector = new Vector(5, 5);
        assertEquals(vector.minus(new Vector(0, 0)), new Vector(5, 5));
        assertEquals(vector.minus(new Vector(2, 3)), new Vector(3, 2));
        assertEquals(vector.minus(new Vector(7, -3)), new Vector(-2, 8));
    }

    @Test
    public void multiplyTest(){
        Vector vector = new Vector(1, 1);
        assertEquals(vector.multiply(0), new Vector(0, 0));
        assertEquals(vector.multiply(5), new Vector(5, 5));
        assertEquals(vector.multiply(-0.0), new Vector(0, 0));
    }

    @Test
    public void divideTest(){
        Vector vector = new Vector(5, 5);
        assertEquals(vector.divide(5), new Vector(1, 1));
    }

    @Test(expected=IllegalArgumentException.class)
    public void divideBy0Test(){
        Vector vector = new Vector(5, 5);
        vector.divide(0);
    }

    @Test
    public void inverseTest(){
        Vector vector = new Vector(5, 5);
        assertEquals(vector.inverse(), new Vector(-5, -5));
        vector = new Vector(0, 0);
        assertEquals(vector.inverse(), new Vector(0, 0));
    }

    @Test
    public void normalizeTest(){
        Vector vector = new Vector(0, 0);
        assertEquals(vector.normalize(), new Vector(0, 0));
        vector = new Vector(10, 0);
        assertEquals(vector.normalize(), new Vector(1, 0));
        vector = new Vector(0, 10);
        assertEquals(vector.normalize(), new Vector(0, 1));
    }

    @Test
    public void dotTest(){
        Vector vector = new Vector(1, 1);
        assertTrue(doubleEquals(Vector.dotProduct(vector, new Vector(5, 1)), 6));
        assertTrue(doubleEquals(Vector.dotProduct(vector, new Vector(6, 7)), 13));
    }

    @Test
    public void crossProductTest(){
        Vector a = new Vector(4, 2);
        Vector b = new Vector(3, 5);

        assertTrue(doubleEquals(Vector.crossProduct(a, b), 14.0));
        assertEquals(Vector.crossProduct(2, a), new Vector(4, -8));
        assertEquals(Vector.crossProduct(a, 2), new Vector(-4, 8));
    }

    @Test
    public void equalsTest(){
        Vector vector = new Vector(5.6, 6.8);
        assertTrue(vector.equals(new Vector(5.6, 6.8)));
        assertFalse(vector.equals(new Vector(5.66, 6.88)));
    }
}
