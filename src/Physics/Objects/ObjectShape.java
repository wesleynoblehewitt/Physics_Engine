package Physics.Objects;

import Physics.Mathematics.MassData;
import Physics.Mathematics.RotationalMatrix;
import Physics.Mathematics.Vector;
import org.newdawn.slick.Graphics;

public abstract class ObjectShape {

    Vector position;
    RotationalMatrix rotationalMatrix = new RotationalMatrix(0);
    float radius;

    ObjectShape(Vector position){
        this.position = position;
    }

    public void updatePosition(Vector positionChange){
        position = position.plus(positionChange);
    }

    void setPosition(Vector position){
        this.position = position;
    }

    public Vector getPosition(){
        return position;
    }

    public RotationalMatrix getRotationalMatrix() {
        return rotationalMatrix;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getRadius() {
        return radius;
    }

    public Vector getBoundingBoxMin(){
        return new Vector(position.getX() - radius, position.getY() - radius);
    }

    public Vector getBoundingBoxMax(){
        return new Vector(position.getX() + radius, position.getY() + radius);
    }

    abstract MassData calculateMassData(float density);
    abstract void render(Graphics g);
}
