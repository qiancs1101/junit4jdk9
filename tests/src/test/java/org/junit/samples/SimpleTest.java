package org.junit.samples;

import static org.junit.Assert.assertEquals;

import junit.framework.JUnit4TestAdapter;
import org.junit.Assert;
import org.junit.common.Before;
import org.junit.common.Test;

/**
 * Some simple tests.
 *
 * <p>This test is expected to fail.
 */
public class SimpleTest {
    protected int fValue1;
    protected int fValue2;

    @Before
    public void setUp() {
        fValue1 = 2;
        fValue2 = 3;
    }

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(SimpleTest.class);
    }

    public int unused;

    @Test
    public void divideByZero() {
        int zero = 0;
        int result = 8 / zero;
        unused = result; // avoid warning for not using result
    }

    @Test
    public void testEquals() {
        Assert.assertEquals(12, 12);
        Assert.assertEquals(12L, 12L);
        Assert.assertEquals(new Long(12), new Long(12));

        Assert.assertEquals("Size", 12, 13);
        Assert.assertEquals("Capacity", 12.0, 11.99, 0.0);
    }

}