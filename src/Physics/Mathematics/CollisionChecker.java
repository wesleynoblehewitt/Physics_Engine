package Physics.Mathematics;

import Physics.Objects.*;

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
                return PolygonVsCircleCheck(info);
            } else {
                return PolygonVsSquareCheck(info);
            }
        } else if(shapeA instanceof Circle){
            if(shapeB instanceof Polygon){
                PhysicsObject circle = info.getObjectA();
                PhysicsObject polygon = info.getObjectB();
                info.setObjectA(polygon);
                info.setObjectB(circle);

                return PolygonVsCircleCheck(info);
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
            return true;
        } else {
            info.setCollisionNormal(new Vector(1, 0));
            info.setPenetrationDepth(shapeA.getRadius());
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

    public boolean PolygonVsCircleCheck(CollisionInfo info){
        return false;
    }

    public boolean PolygonVsSquareCheck(CollisionInfo info){
     return false;
    }

    public boolean PolygonVsPolygonCheck(CollisionInfo info){
        return false;
    }

    private float clamp(float lower, float higher, float current){
        if(current > higher)
            return higher;

        if(current < lower)
            return lower;

        return current;
    }
}
