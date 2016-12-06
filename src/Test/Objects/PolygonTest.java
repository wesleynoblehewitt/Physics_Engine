package Test.Objects;

import Physics.Mathematics.Vector;
import Physics.Objects.Polygon;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PolygonTest {

    private List<Vector> generateSortedVertices(){
        List<Vector> vertices = new ArrayList<>();
        vertices.add(new Vector(8, 2));
        vertices.add(new Vector(7, 4));
        vertices.add(new Vector(4, 3));
        vertices.add(new Vector(3, 0));
        vertices.add(new Vector(7, 0));
        return vertices;
    }

    private List<Vector> generateUnsortedVertices(){
        List<Vector> vertices = new ArrayList<>();
        vertices.add(new Vector(7, 0));
        vertices.add(new Vector(4, 3));
        vertices.add(new Vector(8, 2));
        vertices.add(new Vector(3, 0));
        vertices.add(new Vector(7, 4));
        return vertices;
    }

    private Polygon createTestPolygon(){
        return new Polygon(new Vector(5, 2), generateSortedVertices());
    }

    @Test
    public void unsortedInitialisationTest(){
        Polygon polygon = new Polygon(new Vector(5, 2), generateUnsortedVertices());
        assertEquals(polygon.getVertices(), generateSortedVertices());
        assertEquals(polygon.getVertices().size(), 5);
        assertEquals(polygon.getPosition(), new Vector(5, 2));
    }

    @Test
    public void soredInitialisationTest(){
        Polygon polygon = new Polygon(new Vector(5, 2), generateSortedVertices());
        assertEquals(polygon.getVertices(), generateSortedVertices());
        assertEquals(polygon.getVertices().size(), 5);
        assertEquals(polygon.getPosition(), new Vector(5, 2));
    }

    @Test
    public void concaveInitialisationTest(){
        List<Vector> vertices = new ArrayList<>();
        vertices.add(new Vector(-3, -2));
        vertices.add(new Vector(4, 4));
        vertices.add(new Vector(6, 1));
        vertices.add(new Vector(2, 5));
        vertices.add(new Vector(4, 3));

        Polygon poly = new Polygon(new Vector(3, 2), vertices);
        assertEquals(poly.getVertices().size(), 4);

        List<Vector> convexVertices = new ArrayList<>();
        convexVertices.add(new Vector(6, 1));
        convexVertices.add(new Vector(4, 4));
        convexVertices.add(new Vector(2, 5));
        convexVertices.add(new Vector(-3, -2));

        assertEquals(poly.getVertices(), convexVertices);
    }

    @Test(expected=IllegalArgumentException.class)
    public void insufficientVerticesInitialisationTest(){
        List<Vector> vertices = new ArrayList<>();
        vertices.add(new Vector(2, 3));
        vertices.add(new Vector(-3, 1));

        Polygon polygon = new Polygon(new Vector(0, 0), vertices);
    }

    @Test
    public void getSupportVectorTest(){
        //todo
    }

    @Test
    public void getBoundingBoxTest(){
        Polygon poly = createTestPolygon();
        assertEquals(poly.getBoundingBoxMin(), new Vector(2, 0));

        List<Vector> vertices = new ArrayList<>();
        vertices.add(new Vector(-3, -2));
        vertices.add(new Vector(4, 4));
        vertices.add(new Vector(6, 1));
        vertices.add(new Vector(2, 5));
        vertices.add(new Vector(-4, 3));

        poly = new Polygon(new Vector(3, 2), vertices);
        assertEquals(poly.getBoundingBoxMin(), new Vector(-4, -2));
        assertEquals(poly.getBoundingBoxMax(), new Vector(10, 6));
    }

    @Test
    public void polygonEqualsTest(){
        Polygon poly1 = new Polygon(new Vector(5, 2), generateSortedVertices());
        Polygon poly2 = new Polygon(new Vector(5, 2), generateSortedVertices());
        assertTrue(poly1.equals(poly2));

        poly2 = new Polygon(new Vector(5, 2), generateUnsortedVertices());
        assertTrue(poly2.equals(poly1));

        poly1 = new Polygon(new Vector(6, 2), generateSortedVertices());
        assertFalse(poly1.equals(poly2));

        List<Vector> vertices = new ArrayList<>();
        vertices.add(new Vector(1, 4));
        vertices.add(new Vector(4, 6));
        vertices.add(new Vector(-1, -3));
        vertices.add(new Vector(-3, -2));
        vertices.add(new Vector(0, 3));

        Polygon poly3 = new Polygon(new Vector(5, 2), vertices);

        assertFalse(poly3.equals(poly2));
    }
}
