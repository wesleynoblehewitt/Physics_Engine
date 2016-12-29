package Test.Objects;

import Physics.Mathematics.Vector;
import Physics.Objects.Circle;
import org.junit.Test;

import static Physics.Mathematics.Constants.floatEquals;
import static org.junit.Assert.*;

public class CircleTest {

    @Test
    public void intialisationTest(){
        Circle circle = new Circle(new Vector(4, 8), 2);
        assertEquals(circle.getPosition(), new Vector(4, 8));
        assertTrue(floatEquals(circle.getRadius(), 2));
    }

    @Test
    public void boundingBoxTests(){
        Circle circle = new Circle(new Vector(0, 0), 5);
        assertEquals(circle.getBoundingBoxMin(), new Vector(-5, -5));
        assertEquals(circle.getBoundingBoxMax(), new Vector(5, 5));
    }

    @Test
    public void circleEqualsTest(){
        Circle circle = new Circle(new Vector(2, 6), 7);
        assertTrue(circle.equals(new Circle(new Vector(2, 6), 7)));
        assertFalse(circle.equals(new Circle(new Vector(5, 7), 2)));
    }
}
