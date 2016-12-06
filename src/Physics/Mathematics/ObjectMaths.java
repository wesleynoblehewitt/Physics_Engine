package Physics.Mathematics;

import Physics.Objects.PhysicsObject;
import com.sun.istack.internal.Nullable;

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

        Vector relativeVelocity = b.getVelocity().minus(a.getVelocity());

        float velocityAlongNormal = Vector.dotProduct(relativeVelocity, collision.getCollisionNormal());

        if(velocityAlongNormal > 0)
            return null;

        float e = Math.min(a.getRestitution(), b.getRestitution());
        float j = (-(1 + e) * velocityAlongNormal) / (a.getMassData().inverseMass + b.getMassData().inverseMass);
        Vector impulse = collision.getCollisionNormal().multiply(j);
        float massSum = a.getMassData().mass + b.getMassData().mass;

        float ratio = a.getMassData().mass / massSum;
        a.applyImpulse(impulse.multiply(ratio).inverse());

        ratio = b.getMassData().mass / massSum;
        b.applyImpulse(impulse.multiply(ratio));

        applyFriction(collision, j);

        return collision;
    }

    private void applyFriction(CollisionInfo collision, float j){
        PhysicsObject a = collision.getObjectA();
        PhysicsObject b = collision.getObjectB();

        Vector relativeVelocity = b.getVelocity().minus(a.getVelocity());
        Vector tangentToNormal = relativeVelocity.minus(collision.getCollisionNormal()
                .multiply(Vector.dotProduct(relativeVelocity, collision.getCollisionNormal())));
        tangentToNormal = tangentToNormal.normalize();

        float jt = -Vector.dotProduct(relativeVelocity, tangentToNormal);
        jt /= (a.getMassData().getInverseMass() + b.getMassData().getInverseMass());

        float mu = pythagoreanSolve(a.getMaterial().getStaticFriction(), b.getMaterial().getStaticFriction());

        Vector frictionImpulse;
        if(Math.abs(jt) < j * mu){
            frictionImpulse = tangentToNormal.multiply(jt);
        } else {
            float dynamicFriction = pythagoreanSolve(a.getMaterial().getDynamicFriction(), b.getMaterial().getDynamicFriction());
            frictionImpulse = tangentToNormal.multiply(-j).multiply(dynamicFriction);
        }

        a.applyImpulse(frictionImpulse.inverse());
        b.applyImpulse(frictionImpulse);
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
        c /= (a.getMassData().inverseMass + b.getMassData().inverseMass);
        c *= percentage;
        Vector correction = collision.getCollisionNormal().multiply(c);

        a.setPosition(a.getPosition().minus(correction.multiply(a.getMassData().inverseMass)));
        b.setPosition(b.getPosition().plus(correction.multiply(b.getMassData().inverseMass)));
    }
}