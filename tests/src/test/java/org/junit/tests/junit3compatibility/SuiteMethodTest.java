package org.junit.tests.junit3compatibility;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import junit.framework.JUnit4TestAdapter;
import junit.j3.framework.TestCase;
import junit.j3.framework.TestSuite;
import org.junit.Assert;
import org.junit.common.Ignore;
import org.junit.common.Test;
import org.junit.common.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.common.runner.Result;

public class SuiteMethodTest {
    public static boolean wasRun;

    static public class OldTest extends TestCase {
        public OldTest(String name) {
            super(name);
        }

        public static junit.j3.framework.Test suite() {
            TestSuite suite = new TestSuite();
            suite.addTest(new OldTest("notObviouslyATest"));
            return suite;
        }

        public void notObviouslyATest() {
            wasRun = true;
        }
    }

    @Test
    public void makeSureSuiteIsCalled() {
        wasRun = false;
        JUnitCore.runClasses(OldTest.class);
        Assert.assertTrue(wasRun);
    }

    static public class NewTest {
        @Test
        public void sample() {
            wasRun = true;
        }

        public static junit.j3.framework.Test suite() {
            return new JUnit4TestAdapter(NewTest.class);
        }
    }

    @Test
    public void makeSureSuiteWorksWithJUnit4Classes() {
        wasRun = false;
        JUnitCore.runClasses(NewTest.class);
        Assert.assertTrue(wasRun);
    }


    public static class CompatibilityTest {
        @Ignore
        @Test
        public void ignored() {
        }

        public static junit.j3.framework.Test suite() {
            return new JUnit4TestAdapter(CompatibilityTest.class);
        }
    }

    // when executing as JUnit 3, ignored tests are stripped out before execution
    @Test
    public void descriptionAndRunNotificationsAreConsistent() {
        Result result = JUnitCore.runClasses(CompatibilityTest.class);
        Assert.assertEquals(0, result.getIgnoreCount());

        Description description = Request.aClass(CompatibilityTest.class).getRunner().getDescription();
        Assert.assertEquals(0, description.getChildren().size());
    }

    static public class NewTestSuiteFails {
        @Test
        public void sample() {
            wasRun = true;
        }

        public static junit.j3.framework.Test suite() {
            Assert.fail("called with JUnit 4 runner");
            return null;
        }
    }

    @Test
    public void suiteIsUsedWithJUnit4Classes() {
        wasRun = false;
        Result result = JUnitCore.runClasses(NewTestSuiteFails.class);
        Assert.assertEquals(1, result.getFailureCount());
        Assert.assertFalse(wasRun);
    }

    static public class NewTestSuiteNotUsed {
        private static boolean wasIgnoredRun;

        @Test
        public void sample() {
            wasRun = true;
        }

        @Ignore
        @Test
        public void ignore() {
            wasIgnoredRun = true;
        }

        public static junit.j3.framework.Test suite() {
            return new JUnit4TestAdapter(NewTestSuiteNotUsed.class);
        }
    }

    @Test
    public void makeSureSuiteNotUsedWithJUnit4Classes2() {
        wasRun = false;
        NewTestSuiteNotUsed.wasIgnoredRun = false;
        Result res = JUnitCore.runClasses(NewTestSuiteNotUsed.class);
        Assert.assertTrue(wasRun);
        Assert.assertFalse(NewTestSuiteNotUsed.wasIgnoredRun);
        Assert.assertEquals(0, res.getFailureCount());
        Assert.assertEquals(1, res.getRunCount());
        Assert.assertEquals(0, res.getIgnoreCount());
    }
}
