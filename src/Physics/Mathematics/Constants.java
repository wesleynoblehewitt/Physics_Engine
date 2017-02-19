package Physics.Mathematics;

import java.awt.*;

public class Constants {

    // Physics constants
    public final static float epsilon = 0.000001f;
    public final static Vector gravity = new Vector(0, 9.807f);
    public final static float gravityScale = 5f;

    // Game constants
    private final static int fps = 60;
    public final static float delta = 1.0f/fps;
    public static Dimension worldDimensions = new Dimension(1000, 600);

    public static boolean floatEquals(float a, float b){
        return Math.abs(a - b) < epsilon;
    }

    final static float relative_bias = 0.95f;
    final static float absolute_bias = 0.01f;
}
