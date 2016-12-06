package Physics.Mathematics;

public class RotationalMatrix {

    private float m00, m01, m10, m11;

    public RotationalMatrix(Vector a, Vector b){
        m00 = a.getX();
        m01 = a.getY();
        m10 = b.getX();
        m11 = b.getY();
    }

    public RotationalMatrix(double radians){
        float c = (float) Math.cos(radians);
        float s = (float) Math.sin(radians);

        m00 = c;
        m01 = -s;
        m10 = s;
        m11 = c;
    }

    public Vector rotate(Vector a){
        return new Vector( m00 * a.getX() + m01 * a.getY(), m10 * a.getX() + m11 * a.getY());
    }

}
