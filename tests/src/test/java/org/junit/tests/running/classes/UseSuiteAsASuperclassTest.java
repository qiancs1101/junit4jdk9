package org.junit.tests.running.classes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.common.Test;
import org.junit.runner.JUnitCore;
import org.junit.notify.runner.Result;
import org.junit.notify.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;

public class UseSuiteAsASuperclassTest {

    public static class TestA {
        @Test
        public void pass() {
        }
    }

    public static class TestB {
        @Test
        public void dontPass() {
            Assert.fail();
        }
    }

    public static class MySuite extends Suite {
        public MySuite(Class<?> klass) throws InitializationError {
            super(klass, new Class[]{TestA.class, TestB.class});
        }
    }

    @RunWith(MySuite.class)
    public static class AllWithMySuite {
    }

    @Test
    public void ensureTestsAreRun() {
        JUnitCore core = new JUnitCore();
        Result result = core.run(AllWithMySuite.class);
        Assert.assertEquals(2, result.getRunCount());
        Assert.assertEquals(1, result.getFailureCount());
    }
}
