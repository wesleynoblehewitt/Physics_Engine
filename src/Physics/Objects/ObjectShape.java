package Physics.Objects;

import Physics.Mathematics.Vector;

import java.awt.*;
//import org.newdawn.slick.Graphics;

public abstract class ObjectShape {

    Vector position;

    ObjectShape(Vector position){
        this.position = position;
    }

    void updatePosition(Vector positionChange){
        position = position.plus(positionChange);
    }

    void setPosition(Vector position){
        this.position = position;
    }

    public Vector getPosition(){
        return position;
    }

    public abstract Vector getBoundingBoxMin();
    public abstract Vector getBoundingBoxMax();

    abstract void render(Graphics g);
}
