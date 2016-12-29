package Test.Mathematics;

import Physics.Mathematics.MassData;
import org.junit.Test;

import static Physics.Mathematics.Constants.floatEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MassDataTest {

    @Test
    public void initialisationTest(){
        MassData data = new MassData(4, 5);
        assertTrue(floatEquals(data.getMass(), 4));
        assertTrue(floatEquals(data.getInertia(), 5));
        assertTrue(floatEquals(data.getInverseInertia(), 0.2f));
        assertTrue(floatEquals(data.getInverseMass(), 0.25f));

        data = new MassData(0, 0);
        assertTrue(floatEquals(data.getInverseMass(), 0));
        assertTrue(floatEquals(data.getInverseInertia(), 0));
    }

    @Test
    public void equalsTest(){
        MassData data = new MassData(4, 5);
        assertTrue(data.equals(new MassData(4, 5)));
        assertFalse(data.equals(new MassData(4.1f, 5.01f)));
    }
}