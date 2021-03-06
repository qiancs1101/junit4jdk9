package org.junit.tests.deprecated;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.common.Test;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.JUnitCore;
import org.junit.notify.runner.Result;
import org.junit.notify.runner.RunWith;

/**
 * @deprecated This is a simple smoke test to make sure the old JUnit4ClassRunner basically works.
 *             Delete this test when JUnit4ClassRunner goes to the Great Heap In The Sky.
 */
@Deprecated
public class JUnit4ClassRunnerTest {

    @SuppressWarnings("deprecation")
    @RunWith(JUnit4ClassRunner.class)
    public static class Example {
        @Test
        public void success() {
        }

        @Test
        public void failure() {
            Assert.fail();
        }
    }

    @Test
    public void runWithOldJUnit4ClassRunner() {
        Result result = JUnitCore.runClasses(Example.class);
        Assert.assertThat(result.getRunCount(), is(2));
        Assert.assertThat(result.getFailureCount(), is(1));
    }

    @SuppressWarnings("deprecation")
    @RunWith(JUnit4ClassRunner.class)
    public static class UnconstructableExample {
        public UnconstructableExample() {
            throw new UnsupportedOperationException();
        }

        @Test
        public void success() {
        }

        @Test
        public void failure() {
            Assert.fail();
        }
    }


    @Test
    public void runWithOldJUnit4ClassRunnerAndBadConstructor() {
        Result result = JUnitCore.runClasses(UnconstructableExample.class);
        Assert.assertThat(result.getRunCount(), is(2));
        Assert.assertThat(result.getFailureCount(), is(2));
    }
}
