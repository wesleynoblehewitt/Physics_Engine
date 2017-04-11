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

    void addContactPoint(int index, Vector contactPoint) {
        contactPoints.add(index, contactPoint);
    }

    List<Vector> getContactPoints(){
        return contactPoints;
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
        // A CollisionInfo is considered equal if they contain the two same objects, regardless of position
        return info.getObjectA().equals(objectA) && info.getObjectB().equals(objectB) || info.getObjectA().equals(objectB) && info.getObjectB().equals(objectA);
    }

    @Override
    public int hashCode(){
        int result = 1;
        int hashCode = objectA.hashCode();
        hashCode += objectB.hashCode();

        result = 31 * result + hashCode;
        return result;
    }


}