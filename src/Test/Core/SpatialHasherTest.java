package Test.Core;

import Physics.Core.SpatialHasher;
import Physics.Mathematics.Constants;
import Physics.Mathematics.MassData;
import Physics.Mathematics.Vector;
import Physics.Objects.Circle;
import Physics.Objects.Material;
import Physics.Objects.PhysicsObject;
import Physics.Objects.Square;
import org.junit.Test;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SpatialHasherTest {

    @Test
    public void hasherSetupTest(){
        SpatialHasher setupHasher = new SpatialHasher();
        int width = Constants.worldDimensions.width;
        int height = Constants.worldDimensions.height;

        int columns = width/30;
        int rows = height/30;

        assertEquals(setupHasher.getBuckets().size(), columns * rows);
    }

    @Test
    public void registerAndClearBucketsTest(){
        SpatialHasher spatialHasher = new SpatialHasher();
        PhysicsObject testerObject1 = new PhysicsObject(new MassData(5, 5), Material.SOLID, new Circle(new Vector(10, 10), 5));
        PhysicsObject testerObject2 = new PhysicsObject(new MassData(5, 5), Material.SOLID, new Square(new Vector(5, 5), 5, 5));

        spatialHasher.registerObject(testerObject1);
        spatialHasher.registerObject(testerObject2);

        assertEquals(spatialHasher.getBuckets().get(0).size(), 2);

        spatialHasher.clearBuckets();
        assertTrue(checkHasherIsEmpty(spatialHasher));
    }

    private boolean checkHasherIsEmpty(SpatialHasher hasher){
        Hashtable<Integer, List<PhysicsObject>> hasherBuckets = hasher.getBuckets();
        for(Map.Entry<Integer, List<PhysicsObject>> bucket: hasherBuckets.entrySet()){
            if(!bucket.getValue().isEmpty())
                return false;
        }
        return true;
    }

    @Test
    public void getNearbyObjectsTest(){
        SpatialHasher spatialHasher = new SpatialHasher();
        PhysicsObject testerObject1 = new PhysicsObject(new MassData(5, 5), Material.SOLID, new Circle(new Vector(10, 10), 5));
        PhysicsObject testerObject2 = new PhysicsObject(new MassData(5, 5), Material.SOLID, new Square(new Vector(5, 5), 5, 5));

        spatialHasher.registerObject(testerObject1);
        spatialHasher.registerObject(testerObject2);

        Set<PhysicsObject> nearby = spatialHasher.getNearby(testerObject1);
        assertEquals(nearby.size(), 1);
        assertTrue(nearby.contains(testerObject2));

        spatialHasher.clearBuckets();
        spatialHasher.registerObject(testerObject1);
        testerObject2.setPosition(new Vector(50, 10));
        spatialHasher.registerObject(testerObject2);

        nearby = spatialHasher.getNearby(testerObject1);
        assertEquals(nearby.size(), 0);
    }
}