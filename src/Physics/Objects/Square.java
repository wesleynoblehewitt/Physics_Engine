package Physics.Objects;

import Physics.Mathematics.MassData;
import Physics.Mathematics.Vector;
import org.newdawn.slick.Graphics;

import static Physics.Mathematics.Constants.floatEquals;

public class Square extends ObjectShape {

    private float height;
    private float width;

    public Square(Vector position, float width, float height) {
        super(position);
        this.width = width;
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    @Override
    MassData calculateMassData(float density) {
        return new MassData(0, 0);
    }

    public Vector getBoundingBoxMin(){
        return new Vector(position.getX() - width/2, position.getY() - height/2);
    }

    public Vector getBoundingBoxMax(){
        return new Vector(position.getX() + width/2, position.getY() + height/2);
    }

    @Override
    void render(Graphics g) {
        g.fillRect((position.getX() - width/2), (position.getY() - height/2), width, height);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        Square square = (Square) obj;

        return square.getPosition().equals(position) && floatEquals(height, square.getHeight()) && floatEquals(width, square.getWidth());
    }

    @Override
    public int hashCode(){
        int result = 1;

        int hashCode = position.hashCode();
        hashCode += Float.floatToIntBits(height);

        hashCode += Float.floatToIntBits(width);

        result = 31 * result + hashCode;
        return result;
    }
}