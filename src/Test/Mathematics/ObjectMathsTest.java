package Test.Mathematics;

import Physics.Mathematics.CollisionInfo;
import Physics.Mathematics.MassData;
import Physics.Mathematics.ObjectMaths;
import Physics.Mathematics.Vector;
import Physics.Objects.Circle;
import Physics.Objects.Material;
import Physics.Objects.PhysicsObject;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ObjectMathsTest {

    private final ObjectMaths objectMaths = ObjectMaths.getInstance();

    private CollisionInfo makeCollision(){
        PhysicsObject objectA = new PhysicsObject(new MassData(5, 5), Material.SOLID, new Circle(new Vector(0, 0), 5));
        PhysicsObject objectB = new PhysicsObject(new MassData(5, 5), Material.SOLID, new Circle(new Vector(2, 0), 5));
        return new CollisionInfo(objectA, objectB, new Vector(1, 0) , 1);
    }

    @Test
    public void resolveNullWhenObjectsMovingAwayFromEachOther(){
        CollisionInfo info = makeCollision();
        info.getObjectA().setVelocity(new Vector(-1, 0));
        info.getObjectB().setVelocity(new Vector(1, 0));
        assertNull(objectMaths.resolveCollision(info));
    }

    @Test
    public void sinkingObjectCorrectionDoesntOccurOnSmallPenetrationDepths() {
        CollisionInfo info = makeCollision();
        info.getObjectA().setVelocity(new Vector(5, 3));
        info.getObjectB().setVelocity(new Vector(2, 1));
        info.setCollisionNormal(new Vector(1, 0));
        info.setPenetrationDepth(0.01);

        objectMaths.sinkingObjectsCorrection(info);
        assertEquals(info.getObjectA().getVelocity(), new Vector(5, 3));
        assertEquals(info.getObjectB().getVelocity(), new Vector(2, 1));
    }

    @Test
    public void sinkingObjectsCorrectByASmallAmountOnLargerPenetrationDepths(){
        CollisionInfo info = makeCollision();
        info.getObjectA().setVelocity(new Vector(5, 3));
        info.getObjectB().setVelocity(new Vector(2, 1));
        info.setCollisionNormal(new Vector(1, 0));
        info.setPenetrationDepth(1.0);

        //Before correction - A (0, 0), B (2, 0)
        objectMaths.sinkingObjectsCorrection(info);
        assertEquals(info.getObjectA().getPosition(), new Vector(-0.09899999999, 0));
        assertEquals(info.getObjectB().getPosition(), new Vector(2.099, 0));
    }
}