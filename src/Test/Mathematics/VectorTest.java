package Test.Mathematics;

import Physics.Mathematics.Vector;
import org.junit.Test;

import static Physics.Mathematics.Constants.floatEquals;
import static org.junit.Assert.*;

public class VectorTest {

    @Test
    public void initialisationTest(){
        Vector vector = new Vector(3.6f, 4.8f);
        assertTrue(floatEquals(vector.getX(), 3.6f));
        assertTrue(floatEquals(vector.getY(), 4.8f));

        vector = new Vector(-0, -0);
        assertEquals(vector, new Vector(0, 0));
    }

    @Test
    public void lengthSquaredTest(){
        Vector vector = new Vector(5, 5);
        assertTrue(floatEquals(vector.lengthSquared(), 50));
    }

    @Test
    public void lengthTest(){
        Vector vector = new Vector(5, 5);
        assertTrue(floatEquals(vector.length(), (float) Math.sqrt(50)));
    }

    @Test
    public void distanceBetweenTest(){
        Vector vector = new Vector(0, 0);
        assertTrue(floatEquals(vector.distanceBetween(new Vector(5, 0)), 5));
        assertTrue(floatEquals(vector.distanceBetween(new Vector(0, 5)), 5));
        assertTrue(floatEquals(vector.distanceBetween(new Vector(5, 5)), 7.0710678118654755f));
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
        assertEquals(vector.multiply(-0.0f), new Vector(0, 0));
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
        assertTrue(floatEquals(Vector.dotProduct(vector, new Vector(5, 1)), 6));
        assertTrue(floatEquals(Vector.dotProduct(vector, new Vector(6, 7)), 13));
    }

    @Test
    public void crossProductTest(){
        Vector a = new Vector(4, 2);
        Vector b = new Vector(3, 5);

        assertTrue(floatEquals(Vector.crossProduct(a, b), 14));
        assertEquals(Vector.crossProduct(2, a), new Vector(4, -8));
        assertEquals(Vector.crossProduct(a, 2), new Vector(-4, 8));
    }

    @Test
    public void equalsTest(){
        Vector vector = new Vector(5.6f, 6.8f);
        assertTrue(vector.equals(new Vector(5.6f, 6.8f)));
        assertFalse(vector.equals(new Vector(5.66f, 6.88f)));
    }
}
