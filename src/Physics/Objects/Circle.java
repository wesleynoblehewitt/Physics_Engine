package Physics.Objects;

import Physics.Mathematics.Constants;
import Physics.Mathematics.MassData;
import Physics.Mathematics.Vector;
import org.newdawn.slick.Graphics;

public class Circle extends ObjectShape{

    private float radius;

    public Circle(Vector position, float radius) {
        super(position);
        this.radius = radius;
    }

    public  float getRadius(){
        return radius;
    }

    @Override
    MassData calculateMassData(float density) {
        float mass = ((float) Math.PI) * radius * radius * density;
        float inertia = mass * radius * radius;
        return new MassData(mass, inertia);
    }

    @Override
    public Vector getBoundingBoxMin(){
        return new Vector(position.getX() - radius, position.getY() - radius);
    }

    @Override
    public Vector getBoundingBoxMax(){
        return new Vector(position.getX() + radius, position.getY() + radius);
    }

    @Override
    void render(Graphics g) {
        g.fillOval((position.getX() - radius), (position.getY() - radius), (radius * 2), (radius * 2));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        Circle circle = (Circle) obj;

        return circle.getPosition().equals(position) && Constants.floatEquals(radius, circle.getRadius());
    }

    @Override
    public int hashCode(){
        int result = 1;

        int hashCode = position.hashCode();
        hashCode += Float.floatToIntBits(radius);

        result = 31 * result + hashCode;
        return result;
    }
}