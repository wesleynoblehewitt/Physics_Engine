package Physics.Mathematics;

public class RotationalMatrix {

    private float m00, m01, m10, m11;

    public RotationalMatrix(Vector a, Vector b) {
        m00 = a.getX();
        m01 = a.getY();
        m10 = b.getX();
        m11 = b.getY();
    }

    public RotationalMatrix(double radians) {
        float c = (float) Math.cos(radians);
        float s = (float) Math.sin(radians);

        m00 = c;
        m01 = -s;
        m10 = s;
        m11 = c;
    }

    public RotationalMatrix(float m00, float m01, float m10, float m11) {
        this.m00 = m00;
        this.m01 = m01;
        this.m10 = m10;
        this.m11 = m11;
    }

    Vector rotate(Vector a) {
        return new Vector( m00 * a.getX() + m01 * a.getY(), m10 * a.getX() + m11 * a.getY());
    }

    RotationalMatrix transpose() {
        return new RotationalMatrix(m00, m10, m01, m11);
    }

    Vector multiply(Vector vector) {
        return new Vector(m00 * vector.getX() + m01 * vector.getY(), m10 * vector.getX() + m11 * vector.getY());
    }

}
