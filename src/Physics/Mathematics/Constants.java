package Physics.Mathematics;

import java.awt.*;

public class Constants {

    // Physics constants
    public final static float epsilon = 0.000001f;
    private final static Vector gravity = new Vector(0, 9.807f);
    private final static float gravityScale = 8f;
    public final static Vector gravityForce = gravity.multiply(gravityScale);

    // Game constants
    private final static int fps = 60;
    public final static float delta = 1.0f/fps;
    public static Dimension worldDimensions = new Dimension(1000, 600);

    public static boolean floatEquals(float a, float b){
        return Math.abs(a - b) < epsilon;
    }

    final static float relative_bias = 0.95f;
    final static float absolute_bias = 0.01f;

    public final static int LeftClick = 0;
    public final static int MiddleClick = 2;
    public final static int RightClick = 1;
}
