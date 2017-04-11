package Physics.Objects;

import Physics.Mathematics.Constants;
import Physics.Mathematics.MassData;
import Physics.Mathematics.Vector;
import com.sun.istack.internal.NotNull;
import javafx.scene.transform.Transform;
import org.newdawn.slick.Graphics;

import static Physics.Mathematics.Constants.gravityForce;
import static Physics.Mathematics.Vector.crossProduct;

public class PhysicsObject {

    private Vector velocity = new Vector(0, 0);
    private Vector force = new Vector(0, 0);

    private float orientation = 0f;
    private float angularVelocity = 0f;
    private float torque = 0f;

    Transform transform;

    final private MassData massData;
    final private ObjectShape shape;
    final private Material material;

    public PhysicsObject(MassData massData, Material material, ObjectShape shape){
        this.massData = massData;
        this.shape = shape;
        this.material = material;
    }

    public void updateVelocity() {
        if(Constants.floatEquals(massData.getMass(), 0f))
            return;

        //velocity = velocity + (force * inverse_mass + gravity) * (dt/2)
        //angular_velocity = angular_velocity + torque * inverse_inertia * dt//2
        velocity = velocity.plus((force.multiply(massData.getInverseMass()).plus(gravityForce)).multiply(Constants.delta / 2.0f));

        // v += (1/m * F) * dt
//        velocity = velocity.plus(force.multiply(massData.getInverseMass()).multiply(Constants.delta / 2.0f));

        angularVelocity += torque *  massData.getInverseInertia() * (Constants.delta / 2.0f);
//        angularVelocity *= angularVelocity * Constants.dragForce;
//
//        if(angularVelocity > 10f)
//            angularVelocity = 10f;
//        if(angularVelocity < -10f)
//            angularVelocity = -10f;
    }

    public void updatePosition(){
        if(Constants.floatEquals(massData.getMass(), 0f))
            return;

        setOrientation(orientation + angularVelocity * Constants.delta);
        // x += v * dt
        shape.updatePosition(velocity.multiply(Constants.delta));

        updateVelocity();
    }

    public void setOrientation(float orientation) {
        this.orientation = orientation;
        shape.getRotationalMatrix().setRotation(orientation);
    }

    public void setPosition(Vector position) {
        shape.setPosition(position);
    }

    public void setVelocity(Vector velocity) {
        this.velocity = velocity;
    }

    public void applyForce(Vector additionalForce){
        force = force.plus(additionalForce);
    }

    public void applyImpulse(Vector impulse, Vector contactPoint) {
        velocity = velocity.plus(impulse.multiply(massData.getInverseMass()));
        angularVelocity += crossProduct(contactPoint, impulse) * massData.getInverseInertia();
    }

    public void resetForces() {
        force = new Vector(0f, 0f);
        torque = 0f;
    }

    @NotNull
    public Vector getPosition(){
        return shape.getPosition();
    }

    @NotNull
    public Vector getVelocity() {
        return velocity;
    }

    @NotNull
    public MassData getMassData(){
        return massData;
    }

    @NotNull
    public ObjectShape getShape(){ return shape;}

    @NotNull
    public Material getMaterial() {
        return material;
    }

    @NotNull
    public float getRestitution(){
        return material.getRestitution();
    }

    public Vector getBoundingBoxMin(){
        return shape.getBoundingBoxMin();
    }

    public Vector getBoundingBoxMax(){
        return shape.getBoundingBoxMax();
    }

    public float getOrientation() { return orientation; }

    public float getAngularVelocity() { return angularVelocity; }

    public void render(Graphics g){
        shape.render(g);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        PhysicsObject physicsObject = (PhysicsObject) obj;

        return velocity.equals(physicsObject.getVelocity())
                && massData.equals(physicsObject.getMassData())
                && material.equals(physicsObject.getMaterial())
                && shape.equals(physicsObject.getShape());
    }

    @Override
    public int hashCode(){
        int result = 1;
        int hashCode = velocity.hashCode();
        hashCode += force.hashCode();
        hashCode += massData.hashCode();
        hashCode += shape.hashCode();

        result = 31 * result + hashCode;
        return result;
    }
}