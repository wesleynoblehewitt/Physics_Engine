package Physics.Mathematics;

import Physics.Objects.*;
import com.sun.istack.internal.Nullable;

import java.util.List;

import static Physics.Mathematics.Constants.floatEquals;

public class CollisionChecker {

    private static CollisionChecker collisionChecker = new CollisionChecker();

    private CollisionChecker(){}

    public static CollisionChecker getInstance(){
        return collisionChecker;
    }

    public boolean checkCollision(CollisionInfo info){
        ObjectShape shapeA = info.getObjectA().getShape();
        ObjectShape shapeB = info.getObjectB().getShape();

        if(shapeA instanceof Polygon){
            if(shapeB instanceof Polygon){
                return PolygonVsPolygonCheck(info);
            } else if(shapeB instanceof Circle){
                return CircleVsPolygonCheck(info);
            } else {
                return PolygonVsSquareCheck(info);
            }
        } else if(shapeA instanceof Circle){
            if(shapeB instanceof Polygon){
                PhysicsObject circle = info.getObjectA();
                PhysicsObject polygon = info.getObjectB();
                info.setObjectA(polygon);
                info.setObjectB(circle);

                return CircleVsPolygonCheck(info);
            } else if(shapeB instanceof Circle){
                return CircleVsCircleCheck(info);
            } else {
                PhysicsObject circle = info.getObjectA();
                PhysicsObject square = info.getObjectB();
                info.setObjectA(square);
                info.setObjectB(circle);

                return SquareVsCircleCheck(info);
            }
        } else {
            if(shapeB instanceof Polygon){
                PhysicsObject square = info.getObjectA();
                PhysicsObject polygon = info.getObjectB();
                info.setObjectA(polygon);
                info.setObjectB(square);

                return PolygonVsSquareCheck(info);
            } else if(shapeB instanceof Circle){
                return SquareVsCircleCheck(info);
            } else {
                return SquareVsSquareCheck(info);
            }
        }
    }

    public boolean CircleVsCircleCheck(CollisionInfo info){
        PhysicsObject a = info.getObjectA();
        PhysicsObject b = info.getObjectB();

        Vector vectorFromAToB = b.getPosition().minus(a.getPosition());
        Circle shapeA = (Circle) a.getShape();
        Circle shapeB = (Circle) b.getShape();

        float r = shapeA.getRadius() + shapeB.getRadius();
        if(vectorFromAToB.lengthSquared() > r * r)
            return false;

        float distanceBetweenCircles = vectorFromAToB.length();

        if(!floatEquals(distanceBetweenCircles,0)){
            info.setCollisionNormal(vectorFromAToB.divide(distanceBetweenCircles));
            info.setPenetrationDepth(r - distanceBetweenCircles);
            info.addContactPoint(0, info.getCollisionNormal().multiply(r).plus(a.getPosition()));
            return true;
        } else {
            info.setCollisionNormal(new Vector(1, 0));
            info.setPenetrationDepth(shapeA.getRadius());
            info.addContactPoint(0, a.getPosition());
            return true;
        }
    }

    public boolean SquareVsSquareCheck(CollisionInfo info){
        PhysicsObject a = info.getObjectA();
        PhysicsObject b = info.getObjectB();
        Square shapeA = (Square) a.getShape();
        Square shapeB = (Square) b.getShape();

        Vector vectorFromAToB = b.getPosition().minus(a.getPosition());
        float aExtent =  shapeA.getWidth()/2;
        float bExtent = shapeB.getWidth()/2;

        float xOverlap = aExtent + bExtent - Math.abs(vectorFromAToB.getX());

        if(xOverlap > 0){
            aExtent = shapeA.getHeight()/2;
            bExtent = shapeB.getHeight()/2;

            float yOverlap = aExtent + bExtent - Math.abs(vectorFromAToB.getY());
            if(yOverlap > 0){
                if(xOverlap < yOverlap){
                    if(vectorFromAToB.getX() < 0){
                        info.setCollisionNormal(new Vector(-1, 0));
                    } else
                        info.setCollisionNormal(new Vector(1, 0));
                    info.setPenetrationDepth(xOverlap);
                    return true;
                } else {
                    if(vectorFromAToB.getY() < 0){
                        info.setCollisionNormal(new Vector(0, -1));
                    } else
                        info.setCollisionNormal(new Vector(0, 1));
                    info.setPenetrationDepth(yOverlap);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean SquareVsCircleCheck(CollisionInfo info){
        PhysicsObject a = info.getObjectA();
        PhysicsObject b = info.getObjectB();

        Square shapeA = (Square) a.getShape();
        Circle shapeB = (Circle) b.getShape();

        Vector vectorFromAToB = b.getPosition().minus(a.getPosition());
        Vector closestPointToCentreB = new Vector(vectorFromAToB.getX(), vectorFromAToB.getY());

        float xExtent = shapeA.getWidth()/2;
        float yExtent = shapeA.getHeight()/2;

        closestPointToCentreB.setX(clamp(-xExtent, xExtent, closestPointToCentreB.getX()));
        closestPointToCentreB.setY(clamp(-yExtent, yExtent, closestPointToCentreB.getY()));

        boolean inside = false;
        if(vectorFromAToB.equals(closestPointToCentreB)){
            inside = true;

            if(Math.abs(vectorFromAToB.getX()) > Math.abs(vectorFromAToB.getY())){
                if(closestPointToCentreB.getX() > 0){
                    closestPointToCentreB.setX(xExtent);
                } else
                    closestPointToCentreB.setX(-xExtent);
            } else {
                if(closestPointToCentreB.getY() > 0){
                    closestPointToCentreB.setY(yExtent);
                } else
                    closestPointToCentreB.setY(-yExtent);
            }
        }

        Vector normal = vectorFromAToB.minus(closestPointToCentreB);

        float d = normal.lengthSquared();
        float r = shapeB.getRadius();

        if(d > r * r && !inside)
            return false;

        d = (float) Math.sqrt(d);

        if(inside)
            normal = normal.multiply(-1);

        normal = normal.normalize();
        info.setCollisionNormal(normal.normalize());
        info.setPenetrationDepth(r - d);
        return true;
    }

    public boolean CircleVsPolygonCheck(CollisionInfo info){
        PhysicsObject a = info.getObjectA();
        PhysicsObject b = info.getObjectB();

        Circle circle = (Circle) a.getShape();
        Polygon polygon = (Polygon) b.getShape();
        Vector vectorFromAToB = polygon.getPosition().minus(circle.getPosition());

        FaceQueryResults edgeOfMinPenetration = findEdgeOfMinimumPenetration(polygon, circle, vectorFromAToB);
        if(edgeOfMinPenetration == null)
            return false;

        // Circle is fully within Polygon
        if(edgeOfMinPenetration.separation < Constants.epsilon){
            calculateCircleInPolygonCollision(info, polygon, circle, edgeOfMinPenetration);
            return true;
        }

        // Circle is partially colliding
        return calculateCirclePartillyInPolygonCollision(info, polygon, circle, edgeOfMinPenetration, vectorFromAToB);
    }

    @Nullable
    private FaceQueryResults findEdgeOfMinimumPenetration(Polygon polygon, Circle circle, Vector vectorFromAtoB){
        float separation = -Float.MAX_VALUE;
        int index = 0;
        List<Vector> vertices = polygon.getVertices();
        List<Vector> normals = polygon.getVerticesNormals();
        float radius = circle.getRadius();


        for(int i = 0; i < vertices.size(); i++){
            float s = Vector.dotProduct(normals.get(i), vectorFromAtoB.minus(vertices.get(i)));
            if(s > radius)
                return null;
            if(s > separation) {
                separation = s;
                index = i;
            }
        }
        return new FaceQueryResults(separation, index);
    }

    private void calculateCircleInPolygonCollision(CollisionInfo info, Polygon polygon, Circle circle, FaceQueryResults edgeOfMinPenetration){
        info.setPenetrationDepth(circle.getRadius());
        Vector normal = polygon.getVerticesNormals().get(edgeOfMinPenetration.index);
        info.setCollisionNormal(normal.inverse());
        info.addContactPoint(0, normal.multiply(circle.getRadius()).plus(circle.getPosition()));
    }

    private boolean calculateCirclePartillyInPolygonCollision(CollisionInfo info, Polygon polygon, Circle circle, FaceQueryResults edgeOfMinPenetration, Vector vectorFromAtoB){
        List<Vector> vertices = polygon.getVertices();
        float radius = circle.getRadius();

        Vector v1 = vertices.get(edgeOfMinPenetration.index);
        Vector v2 = vertices.get(edgeOfMinPenetration.index + 1 < vertices.size() ? edgeOfMinPenetration.index + 1 : 0);
        info.setPenetrationDepth(circle.getRadius() - edgeOfMinPenetration.separation);

        float dot = Vector.dotProduct(vectorFromAtoB.minus(v1), v2.minus(v1));
        if(dot < 0.0f || floatEquals(dot, 0.0f)){
            if(vectorFromAtoB.squareDistanceBetween(v1) > radius * radius)
                return false;
            info.setCollisionNormal(v1.minus(vectorFromAtoB).normalize());
            info.addContactPoint(0, v1.plus(polygon.getPosition()));
            return true;
        }

        dot = Vector.dotProduct(vectorFromAtoB.minus(v2), v1.minus(v2));
        if(dot < 0.0f || floatEquals(dot, 0.0f)){
            if(vectorFromAtoB.squareDistanceBetween(v2) > radius * radius)
                return false;

            info.setCollisionNormal(v2.minus(vectorFromAtoB).normalize());
            info.addContactPoint(0, v2.plus(polygon.getPosition()));
            return true;
        }

        Vector normal = polygon.getVerticesNormals().get(edgeOfMinPenetration.index);
        if(Vector.dotProduct(vectorFromAtoB.minus(v1), normal) > radius)
            return false;

        info.setCollisionNormal(normal.inverse());
        info.addContactPoint(0, info.getCollisionNormal().multiply(radius).plus(circle.getPosition()));
        return true;
    }

    public boolean PolygonVsSquareCheck(CollisionInfo info){
     return false;
    }

    public boolean PolygonVsPolygonCheck(CollisionInfo info){
        PhysicsObject a = info.getObjectA();
        PhysicsObject b = info.getObjectB();

        Polygon polygonA = (Polygon) a.getShape();
        Polygon polygonB = (Polygon) b.getShape();

        FaceQueryResults queryA = findAxisOfMinimumPenetration(polygonA, polygonB);
        if(queryA.separation > 0.0f)
            return false;

        FaceQueryResults queryB = findAxisOfMinimumPenetration(polygonB, polygonA);
        if(queryB.separation > 0.0f)
            return false;

        // Calculate penetration depth, collision normal

        return true;
    }

    private FaceQueryResults findAxisOfMinimumPenetration(Polygon polygonA, Polygon polygonB){
        List<Vector> verticesA = polygonA.getVertices();
        List<Vector> normalsA = polygonA.getVerticesNormals();

        float bestDistance = -Float.MAX_VALUE;
        int bestIndex = 0;

        for(int i = 0; i < verticesA.size(); i++){
            Vector normal = normalsA.get(i);
            Vector supportVector = polygonB.getSupportVector(normal);
            float distance = verticesA.get(i).distanceBetween(supportVector);

            if(distance > bestDistance){
                bestDistance = distance;
                bestIndex = i;
            }

            return new FaceQueryResults(bestDistance, bestIndex);
        }
        return new FaceQueryResults(0,0);
    }

    private float clamp(float lower, float higher, float current){
        if(current > higher)
            return higher;

        if(current < lower)
            return lower;

        return current;
    }

    private class FaceQueryResults {
        float separation;
        int index;

        FaceQueryResults(float separation, int index){
            this.separation = separation;
            this.index = index;
        }
    }
}
