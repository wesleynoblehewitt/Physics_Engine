package Physics.Mathematics;

import static Physics.Mathematics.Constants.floatEquals;

public class Vector {

    private float x;
    private float y;

    public Vector(float x, float y){
        if(floatEquals(x, -0.0f))
            x = 0;
        if(floatEquals(y, -0.0f))
            y = 0;
        this.x = x;
        this.y = y;
    }

    public float lengthSquared(){
        return x * x + y * y;
    }

    public float length(){
        return (float) Math.sqrt(lengthSquared());
    }

    public float squareDistanceBetween(Vector b){
        float xVal = x - b.x;
        float yVal = y - b.y;
        return xVal * xVal + yVal * yVal;
    }

    public float distanceBetween(Vector b){
        return (float) Math.sqrt(squareDistanceBetween(b));
    }

    public Vector plus(Vector b){
        return new Vector(x + b.x, y + b.y);
    }

    public Vector minus(Vector b){
        return new Vector(x - b.x, y - b.y);
    }

    public Vector multiply(float n){
        Vector mult = new Vector(x * n, y * n);
        if(mult.getX() == -0.0)
            mult.setX(0);
        if(mult.getY() == -0.0)
            mult.setY(0);
        return mult;
    }

    public Vector divide(float n){
        if(n == 0)
            throw new IllegalArgumentException("Attempted to divide by 0");

        return new Vector(x / n, y / n);
    }

    public Vector inverse(){
        return new Vector(x == 0 ? 0 : -x, y == 0 ? 0 : -y);
    }

    public Vector normalize(){
        float newX = x;
        float newY = y;
        float length = length();

        if(length > Constants.epsilon){
            if(!floatEquals(x, 0f)){
                newX /= length;
            }
            if(!floatEquals(y , 0f)){
                newY /= length;
            }
        }

        return new Vector(newX, newY);
    }

    public static float dotProduct(Vector a, Vector b){
        return a.getX() * b.getX() + a.getY() * b.getY();
    }

    public static float crossProduct(Vector a, Vector b){
        return a.x * b.y - a.y * b.x;
    }

    public static Vector crossProduct(float s, Vector a){
        return new Vector(s * a.y, -s * a.x);
    }

    public static Vector crossProduct(Vector a, float s){
        return new Vector(-s * a.y, s * a.x);
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        Vector vec = (Vector) obj;
        return floatEquals(x, vec.getX()) && floatEquals(y, vec.getY());
    }

    @Override
    public int hashCode(){
        int result = 1;

        int hashCode = Float.floatToIntBits(x);
        hashCode += Float.floatToIntBits(y);

        result = 31 * result + hashCode;
        return result;
    }
}