package org.junit.tests.validation;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.Assert;
import org.junit.common.Before;
import org.junit.common.Test;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.JUnitCore;
import org.junit.notify.runner.Result;
import org.junit.notify.runner.RunWith;
import org.junit.notify.runner.notification.Failure;

@SuppressWarnings("deprecation")
public class BadlyFormedClassesTest {
    public static class FaultyConstructor {
        public FaultyConstructor() throws Exception {
            throw new Exception("Thrown during construction");
        }

        @Test
        public void someTest() {
            /*
                * Empty test just to fool JUnit and IDEs into running this class as
                * a JUnit test
                */
        }
    }

    @RunWith(JUnit4ClassRunner.class)
    public static class BadBeforeMethodWithLegacyRunner {
        @Before
        void before() {

        }

        @Test
        public void someTest() {
        }
    }

    public static class NoTests {
        // class without tests
    }

    @Test
    public void constructorException() {
        String message = exceptionMessageFrom(FaultyConstructor.class);
        Assert.assertEquals("Thrown during construction", message);
    }

    @Test
    public void noRunnableMethods() {
        Assert.assertThat(exceptionMessageFrom(NoTests.class), containsString("No runnable methods"));
    }

    @Test
    public void badBeforeMethodWithLegacyRunner() {
        Assert.assertEquals("Method before should be public",
                exceptionMessageFrom(BadBeforeMethodWithLegacyRunner.class));
    }

    private String exceptionMessageFrom(Class<?> testClass) {
        JUnitCore core = new JUnitCore();
        Result result = core.run(testClass);
        Failure failure = result.getFailures().get(0);
        String message = failure.getException().getMessage();
        return message;
    }
}
