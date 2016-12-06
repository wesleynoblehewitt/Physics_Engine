package Test.Mathematics;

import Physics.Mathematics.MassData;
import org.junit.Test;

import static Physics.Mathematics.Constants.doubleEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MassDataTest {

    @Test
    public void initialisationTest(){
        MassData data = new MassData(4, 5);
        assertTrue(doubleEquals(data.getMass(), 4.0));
        assertTrue(doubleEquals(data.getInertia(), 5.0));
        assertTrue(doubleEquals(data.getInverseInertia(), 0.2));
        assertTrue(doubleEquals(data.getInverseMass(), 0.25));

        data = new MassData(0, 0);
        assertTrue(doubleEquals(data.getInverseMass(), 0.0));
        assertTrue(doubleEquals(data.getInverseInertia(), 0.0));
    }

    @Test
    public void equalsTest(){
        MassData data = new MassData(4, 5);
        assertTrue(data.equals(new MassData(4, 5)));
        assertFalse(data.equals(new MassData(4.1, 5.01)));
    }
}