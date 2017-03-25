package Physics.Mathematics;

import Physics.Objects.*;

import java.util.List;

import static Physics.Mathematics.Constants.*;

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

                boolean result = CircleVsPolygonCheck(info);

                info.setObjectA(circle);
                info.setObjectB(polygon);
                return result;
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

    private boolean CircleVsPolygonCheck(CollisionInfo info){
        PhysicsObject a = info.getObjectA();
        PhysicsObject b = info.getObjectB();

        Polygon polygon = (Polygon) a.getShape();
        Circle circle = (Circle) b.getShape();

        //Find vector between the objects rotated into the current world space
        Vector vectorFromAToB = polygon.getRotationalMatrix().transpose().multiply(circle.getPosition().minus(polygon.getPosition()));

        FaceQueryResults edgeOfMinPenetration = findAxisOfMinimumPenetration(polygon, circle, vectorFromAToB);
        if(edgeOfMinPenetration == null)
            return false;

        // Circle is fully within Polygon
        if(edgeOfMinPenetration.separation < Constants.epsilon) {
            calculateCircleInPolygonCollision(info, polygon, circle, edgeOfMinPenetration);
            return true;
        }

        // Circle is partially colliding
        return calculateCirclePartiallyInPolygonCollision(info, polygon, circle, edgeOfMinPenetration, vectorFromAToB);
    }

    private FaceQueryResults findAxisOfMinimumPenetration(Polygon polygon, Circle circle, Vector vectorFromAtoB){
        float separation = -Float.MAX_VALUE;
        int index = 0;
        List<Vector> vertices = polygon.getVertices();
        List<Vector> normals = polygon.getVerticesNormals();
        Vector position = polygon.getPosition();
        float radius = circle.getRadius();

        for(int i = 0; i < vertices.size(); i++){
            float s = Vector.dotProduct(normals.get(i), vectorFromAtoB.minus(vertices.get(i).minus(position)));

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
        Vector normal = polygon.getRotationalMatrix().multiply((polygon.getVerticesNormals().get(edgeOfMinPenetration.index)));
        info.setCollisionNormal(normal.inverse());
        info.addContactPoint(0, info.getCollisionNormal().multiply(circle.getRadius()).plus(circle.getPosition()));
    }

    private boolean calculateCirclePartiallyInPolygonCollision(CollisionInfo info, Polygon polygon, Circle circle, FaceQueryResults edgeOfMinPenetration, Vector vectorFromAtoB) {
        List<Vector> vertices = polygon.getVertices();
        float radius = circle.getRadius();
        Vector polygonPosition= polygon.getPosition();

        Vector v1 = vertices.get(edgeOfMinPenetration.index).minus(polygonPosition);
        Vector v2 = vertices.get(edgeOfMinPenetration.index + 1 < vertices.size() ? edgeOfMinPenetration.index + 1 : 0).minus(polygonPosition);
        info.setPenetrationDepth(circle.getRadius() - edgeOfMinPenetration.separation);

        float dot = Vector.dotProduct(vectorFromAtoB.minus(v1), v2.minus(v1));
        if(dot < 0.0f || floatEquals(dot, 0.0f)){
            if(vectorFromAtoB.squareDistanceBetween(v1) > radius * radius)
                return false;
            info.setCollisionNormal(polygon.getRotationalMatrix().multiply(v1.minus(vectorFromAtoB)).normalize());
            info.addContactPoint(0, polygon.getRotationalMatrix().multiply(v1).plus(polygon.getPosition()));
            return true;
        }

        dot = Vector.dotProduct(vectorFromAtoB.minus(v2), v1.minus(v2));
        if(dot < 0.0f || floatEquals(dot, 0.0f)){
            if(vectorFromAtoB.squareDistanceBetween(v2) > radius * radius)
                return false;

            info.setCollisionNormal(polygon.getRotationalMatrix().multiply(v2.minus(vectorFromAtoB)).normalize());
            info.addContactPoint(0, polygon.getRotationalMatrix().multiply(v2).plus(polygon.getPosition()));
            return true;
        }

        Vector normal = polygon.getVerticesNormals().get(edgeOfMinPenetration.index);
        if(Vector.dotProduct(vectorFromAtoB.minus(v1), normal) > radius)
            return false;

        info.setCollisionNormal(polygon.getRotationalMatrix().multiply(normal).inverse());
        info.addContactPoint(0, info.getCollisionNormal().multiply(radius).plus(circle.getPosition()));
        return true;
    }

    private boolean PolygonVsSquareCheck(CollisionInfo info){
     return false;
    }

    private boolean PolygonVsPolygonCheck(CollisionInfo info){
        Polygon polygonA = (Polygon) info.getObjectA().getShape();
        Polygon polygonB = (Polygon) info.getObjectB().getShape();

        FaceQueryResults queryA = findAxisOfMinimumPenetration(polygonA, polygonB);
        if(queryA.separation > 0.0f) return false;

        FaceQueryResults queryB = findAxisOfMinimumPenetration(polygonB, polygonA);
        if(queryB.separation > 0.0f) return false;

        // Calculate penetration depth, collision normal

        boolean queryAisSmaller = queryA.separation < (queryB.separation * relative_bias + queryA.separation * absolute_bias);
        int referenceFaceIndex = queryAisSmaller ? queryB.index : queryA.index;
        Polygon referencePolygon = queryAisSmaller ? polygonB : polygonA;
        Polygon incidentPolygon = queryAisSmaller ? polygonA : polygonB;

        VectorPair incidentFace = findIncidentFace(referencePolygon, incidentPolygon, referenceFaceIndex);

        // get reference face vertices
        List<Vector> referencePolygonVertices = referencePolygon.getVertices();
        Vector referenceFaceVector1 = referencePolygonVertices.get(referenceFaceIndex);
        Vector referenceFaceVector2 = referenceFaceIndex + 1 >= referencePolygonVertices.size() ? referencePolygonVertices.get(0) : referencePolygonVertices.get(referenceFaceIndex + 1);

        // transform vertices to world space
        RotationalMatrix referenceMatrix = referencePolygon.getRotationalMatrix();
        referenceFaceVector1 = referenceMatrix.multiply(referenceFaceVector1);
        referenceFaceVector2 = referenceMatrix.multiply(referenceFaceVector2);

        // calculate reference face side normals
        Vector sideNormal = (referenceFaceVector2.minus(referenceFaceVector1)).normalize();

        // orthogonize side normals
        Vector referenceFaceNormal = new Vector(sideNormal.getY(), -sideNormal.getX());

        // clip
        float negSide = -Vector.dotProduct(sideNormal, referenceFaceVector1);
        float posSide = Vector.dotProduct(sideNormal, referenceFaceVector2);


        if (clip(incidentFace, sideNormal.inverse(), negSide) < 2) return false;
        if (clip(incidentFace, sideNormal, posSide) < 2) return false;

        // calculate c
        float referenceC = Vector.dotProduct(referenceFaceNormal, referenceFaceVector1);

        // flip
        info.setCollisionNormal(queryAisSmaller ? referenceFaceNormal.inverse() : referenceFaceNormal);

        // calculate penetration depth
        int cp = 0;
        float separation = Vector.dotProduct(referenceFaceNormal, incidentFace.vectorA) - referenceC;
        float penetrationDepth = 0;
        if(separation <= 0.0f){
            info.addContactPoint(cp++, incidentFace.vectorA);
            penetrationDepth -= -separation;
        }

        separation = Vector.dotProduct(referenceFaceNormal, incidentFace.vectorB) - referenceC;
        if(separation <= 0.0f){
            info.addContactPoint(cp++, incidentFace.vectorB);
            penetrationDepth -= separation;
        }

        penetrationDepth /= cp;
        info.setPenetrationDepth(penetrationDepth);

        return true;
    }

    private FaceQueryResults findAxisOfMinimumPenetration(Polygon polygonA, Polygon polygonB)   {
        List<Vector> verticesA = polygonA.getVertices();
        List<Vector> normalsA = polygonA.getVerticesNormals();

        RotationalMatrix rotationalMatrixA = polygonA.getRotationalMatrix();
        RotationalMatrix rotationalMatrixB = polygonB.getRotationalMatrix().transpose();

        float bestDistance = -Float.MAX_VALUE;
        int bestIndex = 0;

        for(int i = 0; i < verticesA.size(); i++) {
            Vector normal = rotationalMatrixB.multiply(rotationalMatrixA.multiply(normalsA.get(i)));
            Vector supportVector = polygonB.getSupportVector(normal.inverse());
            Vector v = rotationalMatrixB.multiply(rotationalMatrixA.multiply(verticesA.get(i)));

            float distance = Vector.dotProduct(normal, supportVector.minus(v));

            if (distance > bestDistance) {
                bestDistance = distance;
                bestIndex = i;
            }

        }
        return new FaceQueryResults(bestDistance, bestIndex);
    }

    private VectorPair findIncidentFace(Polygon referencePolygon, Polygon incidentPolygon, int referenceFaceIndex){
        Vector referenceNormal = referencePolygon.getVerticesNormals().get(referenceFaceIndex);
        referenceNormal = referencePolygon.getRotationalMatrix().multiply(referenceNormal);

        RotationalMatrix incidentMatrix = incidentPolygon.getRotationalMatrix();
        referenceNormal = incidentMatrix.transpose().multiply(referenceNormal);

        int incidentFace = 0;
        float minDot = Float.MAX_VALUE;
        List<Vector> incidentNormals = incidentPolygon.getVerticesNormals();
        for(int i = 0; i < incidentNormals.size(); i++){
            float dot = Vector.dotProduct(referenceNormal, incidentNormals.get(i));
            if (dot < minDot) {
                minDot = dot;
                incidentFace = i;
            }
        }

        List<Vector> incidentVertices = incidentPolygon.getVertices();
        Vector edge1 = incidentMatrix.multiply(incidentVertices.get(incidentFace));
        incidentFace = incidentFace + 1 >= incidentVertices.size() ? 0 : incidentFace + 1;
        Vector edge2 = incidentMatrix.multiply(incidentVertices.get(incidentFace));

        return new VectorPair(edge1, edge2);
    }

    private int clip(VectorPair incidentFace, Vector normal, float c){
        int sp = 0;
        float distanceToNormalA = Vector.dotProduct(normal, incidentFace.vectorA) - c;
        float distanceToNormalB = Vector.dotProduct(normal, incidentFace.vectorB) - c;

        Vector[] out = { incidentFace.vectorA, incidentFace.vectorB };

        if(distanceToNormalA <= 0.0f){
            out[sp++] = incidentFace.vectorA;
        }
        if(distanceToNormalB <= 0.0f){
            out[sp++] = incidentFace.vectorB;
        }

        if(distanceToNormalA * distanceToNormalB < 0.0f){
            float alpha = distanceToNormalA / (distanceToNormalA - distanceToNormalB);
            out[sp] = incidentFace.vectorA.plus(((incidentFace.vectorB.minus(incidentFace.vectorA)).multiply(alpha)));
            ++sp;
        }

        incidentFace.vectorA = out[0];
        incidentFace.vectorB = out[1];

        assert(sp != 3);

        return sp;
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

    private class VectorPair {
        Vector vectorA;
        Vector vectorB;

        VectorPair(Vector vectorA, Vector vectorB){
            this.vectorA = vectorA;
            this.vectorB = vectorB;
        }
    }
}
