package Physics.Objects;

import Physics.Mathematics.MassData;
import Physics.Mathematics.RotationalMatrix;
import Physics.Mathematics.Vector;
import org.newdawn.slick.Graphics;

public abstract class ObjectShape {

    Vector position;
    RotationalMatrix rotationalMatrix = new RotationalMatrix(0);

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

    abstract MassData calculateMassData(float density);
    public abstract Vector getBoundingBoxMin();
    public abstract Vector getBoundingBoxMax();

    abstract void render(Graphics g);
}
