package Test.Objects;

import Physics.Mathematics.Vector;
import Physics.Objects.Circle;
import Physics.Objects.Square;
import org.junit.Test;

import static Physics.Mathematics.Constants.doubleEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SquareTest {

    @Test
    public void initialisationTest(){
        Square square = new Square(new Vector(5, 2), 4, 6);
        assertTrue(doubleEquals(square.getWidth(), 4.0));
        assertTrue(doubleEquals(square.getHeight(), 6.0));
        assertEquals(square.getPosition(), new Vector(5, 2));
    }

    @Test
    public void boundingBoxTest(){
        Square square = new Square(new Vector(0, 0), 4, 6);
        assertEquals(square.getBoundingBoxMin(), new Vector(-2, -3));
        assertEquals(square.getBoundingBoxMax(), new Vector(2, 3));
    }

    @Test
    public void squareEqualsTest(){
        Square square = new Square(new Vector(5, 9), 3, 8);
        assertTrue(square.equals(new Square(new Vector(5, 9), 3, 8)));
        assertFalse(square.equals(new Square(new Vector(4, 3), 2, 3)));
        assertFalse(square.equals(new Circle(new Vector(4, 3), 2)));

    }
}
