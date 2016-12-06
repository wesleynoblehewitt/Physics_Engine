package Physics.Objects;

import Physics.Mathematics.Constants;
import Physics.Mathematics.MassData;
import Physics.Mathematics.Vector;
import com.sun.istack.internal.NotNull;
import javafx.scene.transform.Transform;

import java.awt.*;

//import org.newdawn.slick.Graphics;

public class PhysicsObject {

    private Vector velocity = new Vector(0, 0);

    private Vector force = new Vector(0, 0);

    Transform transform;

    final private MassData massData;
    final private ObjectShape shape;
    final private Material material;

    public PhysicsObject(MassData massData, Material material, ObjectShape shape){
        this.massData = massData;
        this.shape = shape;
        this.material = material;
    }

    public void update(){

        if(Constants.floatEquals(massData.getMass(), 0f))
            return;

        force = force.plus((Constants.gravity.multiply(Constants.gravityScale)).multiply(Constants.delta));

        // v += (1/m * F) * dt
        velocity = velocity.plus(force.multiply(massData.getInverseMass()).multiply(Constants.delta));

        // x += v * dt
        shape.updatePosition(velocity.multiply(Constants.delta));

        force = new Vector(0f, 0f);
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

    public void applyImpulse(Vector impulse){
        velocity = velocity.plus(impulse.multiply(massData.getInverseMass()));
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