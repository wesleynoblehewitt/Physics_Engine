package Test.Mathematics;

import org.junit.Test;

import static Physics.Mathematics.Constants.floatEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ConstantsTest {

    @Test
    public void doubleEqualsTest(){
        assertTrue(floatEquals(5, 5));
        assertFalse(floatEquals(5.0001f, 5.0002f));
        assertTrue(floatEquals(5.00000001f, 5.00000001f));
    }
}