package Test.Mathematics;

import org.junit.Test;

import static Physics.Mathematics.Constants.doubleEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ConstantsTest {

    @Test
    public void doubleEqualsTest(){
        assertTrue(doubleEquals(5.0, 5.0));
        assertFalse(doubleEquals(5.0001, 5.0002));
        assertTrue(doubleEquals(5.00000001, 5.00000001));
    }
}