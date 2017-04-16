package Physics.Core;

import Physics.Mathematics.CollisionChecker;
import Physics.Mathematics.CollisionInfo;
import Physics.Mathematics.ObjectMaths;
import Physics.Mathematics.Vector;
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
//        List<Vector> vertices = new ArrayList<>();
//        vertices.add(new Vector(600, 470));
//        vertices.add(new Vector(600, 430));
//        vertices.add(new Vector(400, 470));
//        vertices.add(new Vector(400, 430));
//
//        PhysicsObject object = new PhysicsObject(Material.SOLID, new Polygon(new Vector(500, 450), vertices));
////        object.setOrientation(24.8f);
//        addObject(object);
//
//        PhysicsObject ballSolid = new PhysicsObject(Material.SOLID, new Circle(new Vector(450, 450), 30));
////        addObject(ballSolid);
//
//        vertices.clear();
//        vertices.add(new Vector(310, 210));
//        vertices.add(new Vector(310, 190));
//        vertices.add(new Vector(490, 210));
//        vertices.add(new Vector(490, 190));
//        PhysicsObject polygon = new PhysicsObject(Material.TEST, new Polygon(new Vector(400, 200), vertices));
//        addObject(polygon);
//
//        vertices.clear();
//        vertices.add(new Vector(420, 320));
//        vertices.add(new Vector(430, 280));
//        vertices.add(new Vector(380, 320));
//        vertices.add(new Vector(380, 280));
//        PhysicsObject p2 = new PhysicsObject(Material.TEST, new Polygon(new Vector(400, 300), vertices));
//        p2.setOrientation((float) Math.PI / 2.5f);
////        addObject(p2);
//
//        PhysicsObject ball = new PhysicsObject(Material.TEST, new Circle(new Vector(480, 50), 10));
//        addObject(ball);
        List<Vector> points = new ArrayList<>();
        Vector centre = new Vector(300, 400);
        points.add(new Vector(100, 380));
        points.add(new Vector(100, 420));
        points.add(new Vector(500, 380));
        points.add(new Vector(500, 420));
        PhysicsObject base = new PhysicsObject(Material.SOLID, new Polygon(centre, points));

        points.clear();
        centre = new Vector(350, 380);
        points.add(new Vector(310, 380));
        points.add(new Vector(310, 350));
        points.add(new Vector(380, 380));
        PhysicsObject triangle = new PhysicsObject(Material.SOLID, new Polygon(centre, points));


        points.clear();
        centre = new Vector(300, 100);
        points.add(new Vector(100, 80));
        points.add(new Vector(100, 120));
        points.add(new Vector(500, 80));
        points.add(new Vector(500, 120));
        PhysicsObject base2 = new PhysicsObject(Material.SOLID, new Polygon(centre, points));

        points.clear();
        centre = new Vector(80, 415);
        points.add(new Vector(10, 395));
        points.add(new Vector(10, 435));
        points.add(new Vector(100, 395));
        points.add(new Vector(100, 435));
        points.add(new Vector(55, 375));
        PhysicsObject square = new PhysicsObject(Material.SOLID, new Polygon(centre, points));
        square.setOrientation((float) (Math.PI/6));

        points.clear();
        centre = new Vector(350, 250);
        points.add(new Vector(250, 230));
        points.add(new Vector(250, 270));
        points.add(new Vector(450, 230));
        points.add(new Vector(450, 270));
        PhysicsObject square2 = new PhysicsObject(Material.SOLID, new Polygon(centre, points));
        square2.setOrientation(24.8f);

        PhysicsObject circle2 = new PhysicsObject(Material.SOLID, new Circle(new Vector(230, 300), 15));

        addObject(base);
        addObject(base2);
        addObject(triangle);
        addObject(circle2);
        addObject(square);
        addObject(square2);
    }

    public void addObject(PhysicsObject object){
        objects.add(object);
    }

    public void removeObject(PhysicsObject object){
        objects.remove(object);
    }

    void reset() {
        objects.clear();
        initiateObjects();
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