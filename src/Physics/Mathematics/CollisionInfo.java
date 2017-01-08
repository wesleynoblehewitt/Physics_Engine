package Physics.Mathematics;

import Physics.Objects.PhysicsObject;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CollisionInfo {

    private PhysicsObject objectA;
    private PhysicsObject objectB;
    @Nullable private Vector collisionNormal;
    private float penetrationDepth = 0;
    private List<Vector> contactPoints = new ArrayList<>();

    public CollisionInfo(@NotNull PhysicsObject objectA, @NotNull PhysicsObject objectB,
                         @NotNull Vector collisionNormal, @NotNull float penetrationDepth){
        this.objectA = objectA;
        this.objectB = objectB;
        this.collisionNormal = collisionNormal;
        this.penetrationDepth = penetrationDepth;
    }

    public CollisionInfo(@NotNull PhysicsObject objectA, @NotNull PhysicsObject objectB){
        this.objectA = objectA;
        this.objectB = objectB;
    }

    public PhysicsObject getObjectA(){
        return objectA;
    }

    public PhysicsObject getObjectB(){
        return objectB;
    }

    public void setObjectA(PhysicsObject objectA){
        this.objectA = objectA;
    }

    public void setObjectB(PhysicsObject objectB){
        this.objectB = objectB;
    }

    public void setCollisionNormal(Vector collisionNormal){
        this.collisionNormal = collisionNormal;
    }

    @Nullable
    public Vector getCollisionNormal(){
        return collisionNormal;
    }

    public void setPenetrationDepth(float penetrationDepth){
        this.penetrationDepth = penetrationDepth;
    }

    @Nullable
    public float getPenetrationDepth(){
        return penetrationDepth;
    }

    public void addContactPoint(int index, Vector contactPoint) {
        contactPoints.add(index, contactPoint);
    }

    public List<Vector> getContactPoints(){
        return contactPoints;
    }

    public int getNumberOfContactPoints() {
        return contactPoints.size();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        CollisionInfo info = (CollisionInfo) obj;
        if(!info.getObjectA().equals(objectA))
            return false;
        if(!info.getObjectB().equals(objectB))
            return false;
        if(!info.getCollisionNormal().equals(collisionNormal))
            return false;
        if(!info.getContactPoints().equals(contactPoints))
            return false;
        return Constants.floatEquals(info.getPenetrationDepth(), penetrationDepth);
    }

    @Override
    public int hashCode(){
        int result = 1;
        int hashCode = Float.floatToIntBits(penetrationDepth);
        hashCode += collisionNormal != null ? collisionNormal.hashCode() : 0;
        hashCode += objectA.hashCode();
        hashCode += objectB.hashCode();
        hashCode += contactPoints.hashCode();

        result = 31 * result + hashCode;
        return result;
    }


}