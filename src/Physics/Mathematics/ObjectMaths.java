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
    public CollisionInfo resolveCollision(CollisionInfo collision){
        PhysicsObject a = collision.getObjectA();
        PhysicsObject b = collision.getObjectB();
        float e = Math.min(a.getRestitution(), b.getRestitution());
        float massSum = a.getMassData().getMass() + b.getMassData().getMass();

        for(Vector contactPoint : collision.getContactPoints()) {
            Vector radiusFromA = contactPoint.minus(a.getPosition());
            Vector radiusFromB = contactPoint.minus(b.getPosition());

            Vector velocityA = a.getVelocity().plus(Vector.crossProduct(a.getAngularVelocity(), radiusFromA));
            Vector velocityB = b.getVelocity().plus(Vector.crossProduct(b.getAngularVelocity(), radiusFromB));
            Vector relativeVelocity = velocityB.minus(velocityA);

            float contactVelocity = Vector.dotProduct(relativeVelocity, collision.getCollisionNormal());

            if(contactVelocity > 0)
                continue;

            float aCrossNormal = Vector.crossProduct(radiusFromA, collision.getCollisionNormal());
            float bCrossNormal = Vector.crossProduct(radiusFromB, collision.getCollisionNormal());
            float invMassSum = a.getMassData().getInverseMass() + b.getMassData().getInverseMass() + (aCrossNormal * aCrossNormal) * a.getMassData().getInverseInertia() + (bCrossNormal * bCrossNormal) * b.getMassData().getInverseInertia();

            float j = ((-(1.0f + e) * contactVelocity) / invMassSum) / collision.getContactPoints().size();
            Vector impulse = collision.getCollisionNormal().multiply(j);


            float ratio = a.getMassData().getMass() / massSum;
            a.applyImpulse(impulse.multiply(ratio).inverse(), radiusFromA);

            ratio = b.getMassData().getMass() / massSum;
            b.applyImpulse(impulse.multiply(ratio), radiusFromB);

            Vector tangentToNormal = relativeVelocity.minus(collision.getCollisionNormal()
                    .multiply(Vector.dotProduct(relativeVelocity, collision.getCollisionNormal())));
            tangentToNormal = tangentToNormal.normalize();

            float jt = -Vector.dotProduct(relativeVelocity, tangentToNormal);
            jt /= invMassSum / collision.getContactPoints().size();
            if(floatEquals(jt, 0f))
                continue;

            float mu = pythagoreanSolve(a.getMaterial().getStaticFriction(), b.getMaterial().getStaticFriction());

            Vector frictionImpulse;
            if(Math.abs(jt) < j * mu){
                frictionImpulse = tangentToNormal.multiply(jt);
            } else {
                float dynamicFriction = pythagoreanSolve(a.getMaterial().getDynamicFriction(), b.getMaterial().getDynamicFriction());
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

    public void sinkingObjectsCorrection(CollisionInfo collision){
        PhysicsObject a = collision.getObjectA();
        PhysicsObject b = collision.getObjectB();

        float percentage = 0.2f;
        float slop = 0.01f;

        //(max( penetration - slop, 0.0 ) / (A.inv_mass + B.inv_mass)) * percent * collisionNormal
        float c = collision.getPenetrationDepth() - slop < Constants.epsilon ? 0.0f : collision.getPenetrationDepth() - slop;
        c /= (a.getMassData().getInverseMass() + b.getMassData().getInverseMass());
        c *= percentage;
        Vector correction = collision.getCollisionNormal().multiply(c);

        a.setPosition(a.getPosition().minus(correction.multiply(a.getMassData().getInverseMass())));
        b.setPosition(b.getPosition().plus(correction.multiply(b.getMassData().getInverseMass())));
    }
}