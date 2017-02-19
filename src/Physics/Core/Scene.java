package Physics.Core;

import Physics.Mathematics.*;
import Physics.Objects.Circle;
import Physics.Objects.Material;
import Physics.Objects.PhysicsObject;
import Physics.Objects.Polygon;
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
        List<Vector> vertices = new ArrayList<>();
        vertices.add(new Vector(600, 470));
        vertices.add(new Vector(600, 430));
        vertices.add(new Vector(400, 470));
        vertices.add(new Vector(400, 430));

        PhysicsObject object = new PhysicsObject(new MassData(0, 0), Material.SOLID, new Polygon(new Vector(500, 450), vertices));
        addObject(object);

        vertices.clear();
        vertices.add(new Vector(310, 110));
        vertices.add(new Vector(310, 90));
        vertices.add(new Vector(490, 110));
        vertices.add(new Vector(490, 90));
        PhysicsObject polygon = new PhysicsObject(new MassData(20, 5), Material.SOLID, new Polygon(new Vector(300, 100), vertices));
//        addObject(polygon);

        PhysicsObject ball = new PhysicsObject(new MassData(20, 5), Material.SOLID, new Circle(new Vector(500, 50), 10));
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