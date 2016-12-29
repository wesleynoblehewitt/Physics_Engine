package Test.Mathematics;

import Physics.Mathematics.CollisionInfo;
import Physics.Mathematics.MassData;
import Physics.Mathematics.Vector;
import Physics.Objects.Circle;
import Physics.Objects.Material;
import Physics.Objects.PhysicsObject;
import org.junit.Test;

import static Physics.Mathematics.Constants.floatEquals;
import static org.junit.Assert.*;

public class CollisionInfoTest {

    @Test
    public void initialisationTest(){
        PhysicsObject a = new PhysicsObject(new MassData(2, 2), Material.SOLID, new Circle(new Vector(2, 3), 3));
        PhysicsObject b = new PhysicsObject(new MassData(1, 1), Material.SOLID, new Circle(new Vector(3, 4), 5));

        CollisionInfo info = new CollisionInfo(a, b);
        assertEquals(info.getObjectA(), a);
        assertEquals(info.getObjectB(), b);
        assertEquals(info.getCollisionNormal(), null);
        assertTrue(floatEquals(info.getPenetrationDepth(), 0));

        info = new CollisionInfo(a, b, new Vector(5, 3), 3);
        assertEquals(info.getObjectA(), a);
        assertEquals(info.getObjectB(), b);
        assertEquals(info.getCollisionNormal(), new Vector(5, 3));
        assertTrue(floatEquals(info.getPenetrationDepth(), 3));
    }

    @Test
    public void equalsTest(){
        PhysicsObject a = new PhysicsObject(new MassData(2, 2), Material.SOLID, new Circle(new Vector(2, 3), 3));
        PhysicsObject b = new PhysicsObject(new MassData(1, 1), Material.SOLID, new Circle(new Vector(3, 4), 5));
        CollisionInfo info = new CollisionInfo(a, b, new Vector(5, 3), 3);

        assertTrue(info.equals(new CollisionInfo(a, b, new Vector(5, 3), 3)));
        assertFalse(info.equals(new CollisionInfo(a, b, new Vector(3, 4), 4)));
        a = new PhysicsObject(new MassData(2, 2), Material.SOLID, new Circle(new Vector(5, 3), 3));
        assertFalse(info.equals(new CollisionInfo(a, b, new Vector(5, 3), 3)));
    }
}
