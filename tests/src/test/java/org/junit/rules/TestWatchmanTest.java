package org.junit.rules;

import static junit.j3.framework.Assert.fail;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.common.Assume.assumeTrue;
import static org.junit.runner.JUnitCore.runClasses;

import org.junit.Assert;
import org.junit.common.Assume;
import org.junit.common.BeforeClass;
import org.junit.common.Rule;
import org.junit.common.Test;
import org.junit.runner.JUnitCore;
import org.junit.runners.model.FrameworkMethod;

@SuppressWarnings("deprecation")
public class TestWatchmanTest {
    public static class ViolatedAssumptionTest {
        static StringBuilder log;

        @BeforeClass
        public static void initLog() {
            log = new StringBuilder();
        }

        @Rule
        public LoggingTestWatchman watchman = new LoggingTestWatchman(log);

        @Test
        public void succeeds() {
            Assume.assumeTrue(false);
        }
    }

    @Test
    public void neitherLogSuccessNorFailedForViolatedAssumption() {
        JUnitCore.runClasses(ViolatedAssumptionTest.class);
        Assert.assertThat(ViolatedAssumptionTest.log.toString(),
                is("starting finished "));
    }

    public static class FailingTest {
        static StringBuilder log;

        @BeforeClass
        public static void initLog() {
            log = new StringBuilder();
        }

        @Rule
        public LoggingTestWatchman watchman = new LoggingTestWatchman(log);

        @Test
        public void succeeds() {
            fail();
        }
    }

    @Test
    public void logFailingTest() {
        JUnitCore.runClasses(FailingTest.class);
        Assert.assertThat(FailingTest.log.toString(),
                is("starting failed finished "));
    }

    private static class LoggingTestWatchman extends TestWatchman {
        private final StringBuilder log;

        private LoggingTestWatchman(StringBuilder log) {
            this.log = log;
        }

        @Override
        public void succeeded(FrameworkMethod method) {
            log.append("succeeded ");
        }

        @Override
        public void failed(Throwable e, FrameworkMethod method) {
            log.append("failed ");
        }

        @Override
        public void starting(FrameworkMethod method) {
            log.append("starting ");
        }

        @Override
        public void finished(FrameworkMethod method) {
            log.append("finished ");
        }
    }
}