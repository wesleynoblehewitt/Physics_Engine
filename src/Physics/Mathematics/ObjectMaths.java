package Physics.Mathematics;

import Physics.Objects.PhysicsObject;
import com.sun.istack.internal.Nullable;

import static Physics.Mathematics.Constants.floatEquals;

public class ObjectMaths {

    private static ObjectMaths objectMaths = new ObjectMaths();

    private ObjectMaths(){}

    public static ObjectMaths getInstance(){
        return objectMaths;
    }

    @Nullable
    public CollisionInfo resolveCollision(CollisionInfo collision) {
        PhysicsObject a = collision.getObjectA();
        PhysicsObject b = collision.getObjectB();

        if(a.getMassData().getMass() + b.getMassData().getMass() == 0)
            return null;

        float e = Math.min(a.getRestitution(), b.getRestitution());
        float contactPointCount = (float) collision.getContactPoints().size();

        for(Vector contactPoint : collision.getContactPoints()){
            Vector radiusFromA = contactPoint.minus(a.getPosition());
            Vector radiusFromB = contactPoint.minus(b.getPosition());

            Vector relativeVelocity = b.getVelocity().plus(Vector.crossProduct(b.getAngularVelocity(), radiusFromB))
                    .minus(a.getVelocity()).minus(Vector.crossProduct(a.getAngularVelocity(), radiusFromA));

            if(relativeVelocity.lengthSquared() < (Constants.gravityForce.multiply(Constants.delta)).lengthSquared() + Constants.epsilon){
                e = 0.0f;
            }
        }

        Vector velocityOfA = a.getVelocity();
        float angularVelocityOfA = a.getAngularVelocity();
        Vector velocityOfB = b.getVelocity();
        float angularVelocityOfB = b.getAngularVelocity();

        for(Vector contactPoint : collision.getContactPoints()) {
            Vector radiusFromA = contactPoint.minus(a.getPosition());
            Vector radiusFromB = contactPoint.minus(b.getPosition());

            Vector relativeVelocity = velocityOfB.plus(Vector.crossProduct(angularVelocityOfB, radiusFromB))
                    .minus(velocityOfA).minus(Vector.crossProduct(angularVelocityOfA, radiusFromA));

            float contactVelocity = Vector.dotProduct(relativeVelocity, collision.getCollisionNormal());

            if(contactVelocity > 0)
                continue;

            float aCrossNormal = Vector.crossProduct(radiusFromA, collision.getCollisionNormal());
            float bCrossNormal = Vector.crossProduct(radiusFromB, collision.getCollisionNormal());
            float invMassSum = a.getMassData().getInverseMass() + b.getMassData().getInverseMass();
            invMassSum += ((aCrossNormal * aCrossNormal) * a.getMassData().getInverseInertia()) + ((bCrossNormal * bCrossNormal) * b.getMassData().getInverseInertia());

            float j = -(1.0f + e) * contactVelocity;
            j /= invMassSum;
            j /= contactPointCount;

            Vector impulse = collision.getCollisionNormal().multiply(j);
            a.applyImpulse(impulse.inverse(), radiusFromA);
            b.applyImpulse(impulse, radiusFromB);

            relativeVelocity = b.getVelocity().plus(Vector.crossProduct(b.getAngularVelocity(), radiusFromB))
                    .minus(a.getVelocity()).minus(Vector.crossProduct(a.getAngularVelocity(), radiusFromA));

            Vector tangentToNormal = relativeVelocity.minus(
                    collision.getCollisionNormal().multiply(Vector.dotProduct(relativeVelocity, collision.getCollisionNormal())));
            tangentToNormal = tangentToNormal.normalize();

            float jt = -Vector.dotProduct(relativeVelocity, tangentToNormal);
            jt /= invMassSum;
            jt /= contactPointCount;

            if(floatEquals(jt, 0.0f))
                continue;

            float mu = sqrt(a.getMaterial().getStaticFriction() * b.getMaterial().getStaticFriction());

            Vector frictionImpulse;
            if(Math.abs(jt) < j * mu){
                frictionImpulse = tangentToNormal.multiply(jt);
            } else {
                float dynamicFriction = sqrt(a.getMaterial().getDynamicFriction() * b.getMaterial().getDynamicFriction());
                frictionImpulse = tangentToNormal.multiply(-j).multiply(dynamicFriction);
            }

            a.applyImpulse(frictionImpulse.inverse(), radiusFromA);
            b.applyImpulse(frictionImpulse, radiusFromB);
        }
        return collision;
    }

    private float pythagoreanSolve(float a, float b){
        return (float) Math.sqrt(a * a + b * b);
    }

    private float sqrt(float v) {
        return (float) Math.sqrt(v);
    }

    public void sinkingObjectsCorrection(CollisionInfo collision){
        PhysicsObject a = collision.getObjectA();
        PhysicsObject b = collision.getObjectB();

        float percentage = 0.6f;
        float slop = 0.05f;

        //(max( penetration - slop, 0.0 ) / (A.inv_mass + B.inv_mass)) * percent * collisionNormal
        float pd = Math.max(collision.getPenetrationDepth() - slop, 0.0f);
        pd /= (a.getMassData().getInverseMass() + b.getMassData().getInverseMass());
        pd *= percentage;
        Vector correction = collision.getCollisionNormal().multiply(pd);

        a.updatePosition(correction.multiply(a.getMassData().getInverseMass()).inverse());
        b.updatePosition(correction.multiply(b.getMassData().getInverseMass()));
    }
}