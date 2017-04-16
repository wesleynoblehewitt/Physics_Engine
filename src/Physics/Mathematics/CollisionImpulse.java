package Physics.Mathematics;

public class CollisionImpulse {

    private final Vector impulse;
    private final Vector contactVector;
    private final Vector tangentImpulse;

    CollisionImpulse(Vector impulse, Vector contactVector, Vector tangentImpulse) {
        this.impulse = impulse;
        this.contactVector = contactVector;
        this.tangentImpulse = tangentImpulse;
    }

    public Vector getImpulse() {
        return  impulse;
    }

    public Vector getContactVector() {
        return contactVector;
    }

    public Vector getTangentImpulse() {
        return tangentImpulse;
    }

}
