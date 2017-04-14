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
//        object.setOrientation(15f);
        addObject(object);

        PhysicsObject ballSolid = new PhysicsObject(new MassData(0, 0), Material.SOLID, new Circle(new Vector(450, 450), 30));
//        addObject(ballSolid);

        vertices.clear();
        vertices.add(new Vector(310, 210));
        vertices.add(new Vector(310, 190));
        vertices.add(new Vector(490, 210));
        vertices.add(new Vector(490, 190));
        PhysicsObject polygon = new PhysicsObject(new MassData(5760, 1.62912013E9f), Material.SOLID, new Polygon(new Vector(400, 200), vertices));
        addObject(polygon);

        PhysicsObject ball = new PhysicsObject(new MassData(500, 0.6E10f), Material.SOLID, new Circle(new Vector(480, 50), 10));
        addObject(ball);
    }

    public void addObject(PhysicsObject object){
        objects.add(object);
    }

    public void removeObject(PhysicsObject object){
        objects.remove(object);
    }

    void step() {
        List<CollisionInfo> possibleCollisions = broadPhase();
        //Integrate forces
        objects.forEach(PhysicsObject::updateVelocity);
        //Solve collisions
        List<CollisionInfo> collisions = narrowPhase(possibleCollisions);
        //Integrate velocities
        objects.forEach(PhysicsObject::updatePosition);
        //Correct positions
        for(CollisionInfo collision : collisions)
            objectMaths.sinkingObjectsCorrection(collision);
        //Reset forces
        objects.forEach(PhysicsObject::resetForces);
    }

    private List<CollisionInfo> broadPhase(){
        return broadPhaser.findPossibleObjectCollisions(objects);
    }

    private List<CollisionInfo> narrowPhase(List<CollisionInfo> possibleCollisions){
        List<CollisionInfo> collisions = new ArrayList<>();
        for(CollisionInfo collision : possibleCollisions) {
            CollisionInfo result = resolveCollisions(collision);
            if(result != null)
                collisions.add(result);
        }
        return collisions;
    }

    private CollisionInfo resolveCollisions(CollisionInfo collision) {
        return collisionChecker.checkCollision(collision) ? objectMaths.resolveCollision(collision) : null;
    }

    void render(Graphics g){
        for(PhysicsObject object: objects){
            object.render(g);
        }
    }
}