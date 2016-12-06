package Test.Objects;

import Physics.Mathematics.Constants;
import Physics.Mathematics.MassData;
import Physics.Mathematics.Vector;
import Physics.Objects.Circle;
import Physics.Objects.Material;
import Physics.Objects.PhysicsObject;
import org.junit.Test;

import org.junit.Assert;

import static org.junit.Assert.assertEquals;


public class PhysicsObjectTest {

    @Test
    public void initialisationTest(){
        Vector pos = new Vector(5.6, 5.4);
        PhysicsObject obj = new PhysicsObject(new MassData(0,0), Material.SOLID, new Circle(new Vector(5.6, 5.4), 5));
        Assert.assertTrue(obj.getPosition().equals(pos));
        assertEquals(obj.getMassData(), new MassData(0, 0));
        assertEquals(obj.getMaterial(), Material.SOLID);
        assertEquals(obj.getShape(), new Circle(new Vector(5.6, 5.4), 5));
    }

    @Test
    public void applyImpulseTest(){
        PhysicsObject object = new PhysicsObject(new MassData(2, 3), Material.SOLID, new Circle(new Vector(3.4, 2.2), 4));
        Vector impulse = new Vector(5, 5);
        assertEquals(object.getVelocity(), new Vector(0, 0));
        object.applyImpulse(impulse);
        Vector newVelocity = new Vector(2.5, 2.5);
        assertEquals(object.getVelocity(), newVelocity);
    }

    @Test
    public void update0MassObjectTest(){
        PhysicsObject object = new PhysicsObject(new MassData(0, 0), Material.SOLID, new Circle(new Vector(2, 5), 3));
        object.applyForce(new Vector(5, 5));
        assertEquals(object.getVelocity(), new Vector(0, 0));
        object.update();
        assertEquals(object.getVelocity(), new Vector(0, 0));
    }

    @Test
    public void updateTest(){
        Vector force = new Vector(5, 5);
        PhysicsObject object = new PhysicsObject(new MassData(3, 2), Material.SOLID, new Circle(new Vector(3, 2), 5));
        object.applyForce(force);
        assertEquals(object.getVelocity(), new Vector(0, 0));
        assertEquals(object.getPosition(), new Vector(3, 2));

        object.update();
        force = force.plus((Constants.gravity.multiply(Constants.gravityScale)).multiply(Constants.delta));

        Vector newVelocity = force.multiply(1.0/3.0).multiply(Constants.delta);
        Vector positionChange = newVelocity.multiply(Constants.delta);

        assertEquals(object.getVelocity(), newVelocity);
        assertEquals(object.getPosition(), new Vector(3, 2).plus(positionChange));
    }
}