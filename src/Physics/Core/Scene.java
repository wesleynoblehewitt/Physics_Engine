package Physics.Core;

import Physics.Mathematics.*;
import Physics.Objects.Material;
import Physics.Objects.PhysicsObject;
import Physics.Objects.Square;
import org.newdawn.slick.Graphics;

import java.util.ArrayList;
import java.util.List;

class Scene {

    private List<PhysicsObject> objects = new ArrayList<>();
    private CollisionChecker collisionChecker = CollisionChecker.getInstance();
    private final ObjectMaths objectMaths = ObjectMaths.getInstance();
    private final SpatialHasher broadPhaser = new SpatialHasher();


    Scene(){
        initiateObjects();
    }

    private void initiateObjects(){
        PhysicsObject object = new PhysicsObject(new MassData(0, 0), Material.SOLID, new Square(new Vector(500, 450), 400, 40));
        addObject(object);

        PhysicsObject ball = new PhysicsObject(new MassData(20, 5), Material.SOLID, new Square(new Vector(500, 50), 30, 30));
        addObject(ball);
    }

    @SuppressWarnings("WeakerAccess")
    public void addObject(PhysicsObject object){
        objects.add(object);
    }

    public void removeObject(PhysicsObject object){
        objects.remove(object);
    }

    void step(){
        List<CollisionInfo> possibleCollisions = broadPhase();
        narrowPhase(possibleCollisions);
        objects.forEach(PhysicsObject::update);
    }

    private List<CollisionInfo> broadPhase(){
        return broadPhaser.findPossibleObjectCollisions(objects);
    }

    private void narrowPhase(List<CollisionInfo> possibleCollisions){
        possibleCollisions.forEach(this::checkForCollision);
    }

    private void checkForCollision(CollisionInfo collision) {
        if(collisionChecker.checkCollision(collision)){
            if(objectMaths.resolveCollision(collision) != null)
                objectMaths.sinkingObjectsCorrection(collision);
        }
    }

    void render(Graphics g){
        for(PhysicsObject object: objects){
            object.render(g);
        }
    }
}