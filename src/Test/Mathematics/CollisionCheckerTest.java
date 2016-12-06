package Test.Mathematics;

import Physics.Mathematics.*;
import Physics.Objects.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CollisionCheckerTest {

    private final CollisionChecker collisionChecker = CollisionChecker.getInstance();
    private CollisionInfo info;

    private void produceInfo(ObjectShape shapeA, ObjectShape shapeB){
        PhysicsObject a = new PhysicsObject(new MassData(5, 5), Material.SOLID, shapeA);
        PhysicsObject b = new PhysicsObject(new MassData(6, 6), Material.SOLID, shapeB);

        info = new CollisionInfo(a, b);
    }

    @Test
    public void CircleVsCircleSamePositionTest(){
        Circle circleA = new Circle(new Vector(0, 0), 5);
        Circle circleB = new Circle(new Vector(0, 0), 8);

        produceInfo(circleA, circleB);

        assertTrue(collisionChecker.CircleVsCircleCheck(info));
        assertEquals(info.getPenetrationDepth(), circleA.getRadius(), Constants.epsilon);
        assertEquals(info.getCollisionNormal(), new Vector(1, 0));
    }

    @Test
    public void CircleVsCircleDifferentPositionTest(){
        Circle circleA = new Circle(new Vector(0, 0), 5);
        Circle circleB = new Circle(new Vector(5, 6), 6);

        produceInfo(circleA, circleB);

        assertTrue(collisionChecker.CircleVsCircleCheck(info));

        //worked example
        assertEquals(info.getPenetrationDepth(), 3.18975032409, Constants.epsilon);
        assertEquals(info.getCollisionNormal(), new Vector(0.64018439966, 0.76822127959));
    }

    @Test
    public void CircleVsCircleNoCollisionTest(){
        Circle circleA = new Circle(new Vector(0, 0), 2);
        Circle circleB = new Circle(new Vector(6, 3), 3);

        produceInfo(circleA, circleB);

        assertFalse(collisionChecker.CircleVsCircleCheck(info));

        //min distance check
        info.getObjectB().setPosition(new Vector(5.1, 0));
        assertFalse(collisionChecker.CircleVsCircleCheck(info));
    }

    @Test
    public void SquareVsSquareNoCollisionTest(){
        Square a = new Square(new Vector(0, 0), 4, 4);
        Square b = new Square(new Vector(8, 5), 4, 2);

        produceInfo(a, b);

        //No Overlap
        assertFalse(collisionChecker.SquareVsSquareCheck(info));

        //X Overlap
        info.getObjectA().setPosition(new Vector(5, 0));
        assertFalse(collisionChecker.SquareVsSquareCheck(info));

        //Y Overlap
        info.getObjectA().setPosition(new Vector(0, 4));
        assertFalse(collisionChecker.SquareVsSquareCheck(info));
    }

    @Test
    public void SquareVsSquareCollisionTest(){
        Square a = new Square(new Vector(0, 0), 4, 4);
        Square b = new Square(new Vector(2, 0), 4, 2);

        produceInfo(a, b);

        assertTrue(collisionChecker.SquareVsSquareCheck(info));
        assertEquals(info.getPenetrationDepth(), 2, Constants.epsilon);
        assertEquals(info.getCollisionNormal(), new Vector(1, 0));

        info.getObjectA().setPosition(new Vector(0, 1));
        assertTrue(collisionChecker.SquareVsSquareCheck(info));
        assertEquals(info.getPenetrationDepth(), 2, Constants.epsilon);
        assertEquals(info.getCollisionNormal(), new Vector(0, -1));

        info.getObjectA().setPosition(new Vector(0, 0));
        info.getObjectB().setPosition(new Vector(0.5, 0));

        assertTrue(collisionChecker.SquareVsSquareCheck(info));
        assertEquals(info.getPenetrationDepth(), 3, Constants.epsilon);
        assertEquals(info.getCollisionNormal(), new Vector(0, 1));

        info.getObjectB().setPosition(new Vector(-0.5, 0));

        assertTrue(collisionChecker.SquareVsSquareCheck(info));
        assertEquals(info.getPenetrationDepth(), 3, Constants.epsilon);
        assertEquals(info.getCollisionNormal(), new Vector(0, 1));
    }

    @Test
    public void SquareVsCircleInsideTest(){
        Square a =  new Square(new Vector(0, 0), 8, 8);
        Circle b = new Circle(new Vector(0, 0), 3);

        produceInfo(a, b);

        assertTrue(collisionChecker.SquareVsCircleCheck(info));
        assertEquals(info.getPenetrationDepth(), -1, Constants.epsilon);
        assertEquals(info.getCollisionNormal(), new Vector(0, -1));

        a = new Square(new Vector(0, 0), 2, 2);
        b = new Circle(new Vector(0, 0), 6);

        produceInfo(a, b);

        assertTrue(collisionChecker.SquareVsCircleCheck(info));
        assertEquals(info.getPenetrationDepth(), 5, Constants.epsilon);
        assertEquals(info.getCollisionNormal(), new Vector(0, -1));
    }

    @Test
    public void SquareVsCircleCollisionTest(){
        Square a = new Square(new Vector(2, 3), 4, 4);
        Circle b = new Circle(new Vector(5, 6), 3);

        produceInfo(a, b);

        assertTrue(collisionChecker.SquareVsCircleCheck(info));
        assertEquals(info.getPenetrationDepth(), 1.5857864376269049, Constants.epsilon);
        assertEquals(info.getCollisionNormal(), new Vector(0.7071067811, 0.7071067811));
    }

    @Test
    public void SquareVsCircleNoCollisionTest(){
        Square a = new Square(new Vector(0, 0), 6, 6);
        Circle b = new Circle(new Vector(10, 0), 3);

        produceInfo(a, b);

        assertFalse(collisionChecker.SquareVsCircleCheck(info));

        info.getObjectB().setPosition(new Vector(6.1, 0));
        assertFalse(collisionChecker.SquareVsCircleCheck(info));
    }
}
