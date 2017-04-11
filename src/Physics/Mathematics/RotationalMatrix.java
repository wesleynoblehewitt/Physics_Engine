package Physics.Mathematics;

import static Physics.Mathematics.Constants.floatEquals;

public class RotationalMatrix {

    private float m00, m01, m10, m11;

    public RotationalMatrix(Vector a, Vector b) {
        m00 = a.getX();
        m01 = a.getY();
        m10 = b.getX();
        m11 = b.getY();
    }

    public RotationalMatrix(float orientation) {
        setRotation(orientation);
    }

    public RotationalMatrix(float m00, float m01, float m10, float m11) {
        this.m00 = m00;
        this.m01 = m01;
        this.m10 = m10;
        this.m11 = m11;
    }

    public void setRotation(float radians) {
        float c = (float) Math.cos(radians);
        c = floatEquals(c, 0f) ? 0f : c;
        c = floatEquals(c, 1f) ? 1f : c;
        c = floatEquals(c, -1f) ? -1f : c;

        float s = (float) Math.sin(radians);
        s = floatEquals(s, 0) ? 0f : s;
        s = floatEquals(s, 1f) ? 1f : s;
        s = floatEquals(s, -1f) ? -1f : s;

        m00 = c;
        m01 = -s;
        m10 = s;
        m11 = c;
    }

    Vector rotate(Vector a) {
        return new Vector( m00 * a.getX() + m01 * a.getY(), m10 * a.getX() + m11 * a.getY());
    }

    RotationalMatrix transpose() {
        return new RotationalMatrix(m00, m10, m01, m11);
    }

    public Vector multiply(Vector vector) {
        return new Vector(m00 * vector.getX() + m01 * vector.getY(), m10 * vector.getX() + m11 * vector.getY());
    }

}
