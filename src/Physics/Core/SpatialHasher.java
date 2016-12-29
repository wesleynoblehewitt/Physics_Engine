package Physics.Core;

import Physics.Mathematics.CollisionInfo;
import Physics.Mathematics.Constants;
import Physics.Mathematics.Vector;
import Physics.Objects.PhysicsObject;

import java.util.*;
import java.util.stream.Collectors;

public class SpatialHasher {

    private int screenWidth = Constants.worldDimensions.width;
    private int screenHeight = Constants.worldDimensions.height;
    private Hashtable<Integer, List<PhysicsObject>> buckets;

    private int cellSize = 30;

    public SpatialHasher(){
        setup(screenWidth, screenHeight);
    }

    public SpatialHasher(int screenWidth, int screenHeight){
        setup(screenWidth, screenHeight);
    }

    private void setup(int screenWidth, int screenHeight){
        int columns = screenWidth/cellSize;
        int rows = screenHeight/cellSize;
        buckets = new Hashtable<>(columns * rows);
        for(int i = 0; i < columns * rows; i++){
            buckets.put(i, new ArrayList<>());
        }

        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    public void clearBuckets(){
        int columns = screenWidth/cellSize;
        int rows = screenHeight/cellSize;
        buckets.clear();
        for(int i = 0; i < columns * rows; i++){
            buckets.put(i, new ArrayList<>());
        }
    }

    public void registerObject(PhysicsObject obj){
        List<Integer> cellIDs = getCellsObjectIsIn(obj);
        for(Integer ID: cellIDs){
            buckets.get(ID).add(obj);
        }
    }

    private List<Integer> getCellsObjectIsIn(PhysicsObject obj){
        List<Integer> cells = new ArrayList<>();
        Vector min = obj.getBoundingBoxMin();
        Vector max = obj.getBoundingBoxMax();

        Vector range = max.minus(min);
        float width = screenWidth/cellSize;

        int jumpi = 1;
        int jumpj = 1;
        if(range.getX() > cellSize){
            jumpi = cellSize/2;
        }
        if(range.getY() > cellSize){
            jumpj = cellSize/2;
        }

        for(int i = 0; i < range.getX(); i += jumpi){
            for(int j = 0; j < range.getY(); j += jumpj){
                float x = min.getX() + i;
                float y = min.getY() + j;
                if(coordinateWithinWorldBounds(x, y))
                    addBucket(new Vector(x, y), width, cells);
            }
        }
        return cells;
    }

    private boolean coordinateWithinWorldBounds(float x, float y) {
        return x <= screenWidth && x >= 0
                && y <= screenHeight && y >= 0;
    }

    private void addBucket(Vector location, float width, List<Integer> cells){
        int cellPosition = (int)(
                (Math.floor(location.getX()/cellSize))
                + (Math.floor(location.getY()/cellSize))
                * width);

        if(!cells.contains(cellPosition))
            cells.add(cellPosition);
    }

    public Set<PhysicsObject> getNearby(PhysicsObject object){
        Set<PhysicsObject> nearbyObjects = new HashSet<>();
        List<Integer> cellsObjectIsIn = getCellsObjectIsIn(object);
        for(Integer i: cellsObjectIsIn) {
            nearbyObjects.addAll(buckets.get(i));
        }

        nearbyObjects.remove(object);

        return nearbyObjects;
    }

    List<CollisionInfo> findPossibleObjectCollisions(List<PhysicsObject> objects){
        Set<CollisionInfo> possibleCollisions = new HashSet<>();

        clearBuckets();
        objects.forEach(this::registerObject);

        for(PhysicsObject object : objects){
            Set<PhysicsObject> nearby = getNearby(object);
            possibleCollisions.addAll(nearby.stream().map(near -> new CollisionInfo(object, near)).collect(Collectors.toList()));
        }

        return new ArrayList<>(possibleCollisions);
    }

    public Hashtable<Integer, List<PhysicsObject>> getBuckets(){
        return buckets;
    }
}
