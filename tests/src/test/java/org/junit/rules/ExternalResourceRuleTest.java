package org.junit.rules;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.experimental.results.PrintableResult.testResult;
import static org.junit.util.internal.matchers.ThrowableCauseMatcher.hasCause;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.junit.Assert;
import org.junit.common.Rule;
import org.junit.common.Test;
import org.junit.common.TestCouldNotBeSkippedException;
import org.junit.common.internal.AssumptionViolatedException;
import org.junit.experimental.results.PrintableResult;
import org.junit.experimental.results.ResultMatchers;
import org.junit.internal.runners.statements.Fail;
import org.junit.common.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.notify.runner.Result;
import org.junit.notify.runner.notification.Failure;
import org.junit.runners.model.MultipleFailureException;
import org.junit.runners.model.Statement;

public class ExternalResourceRuleTest {
    private static String callSequence;

    public static class UsesExternalResource {
        @Rule
        public ExternalResource resource = new ExternalResource() {
            @Override
            protected void before() throws Throwable {
                callSequence += "before ";
            }

            @Override
            protected void after() {
                callSequence += "after ";
            }
        };

        @Test
        public void testFoo() {
            callSequence += "test ";
        }
    }

    @Test
    public void externalResourceGeneratesCorrectSequence() {
        callSequence = "";
        MatcherAssert.assertThat(PrintableResult.testResult(UsesExternalResource.class), ResultMatchers.isSuccessful());
        Assert.assertEquals("before test after ", callSequence);
    }

    @Test
    public void shouldThrowMultipleFailureExceptionWhenTestFailsAndClosingResourceFails() throws Throwable {
        // given
        ExternalResource resourceRule = new ExternalResource() {
            @Override
            protected void after() {
                throw new RuntimeException("simulating resource tear down failure");
            }
        };
        Statement failingTest = new Fail(new RuntimeException("simulated test failure"));
        Description dummyDescription = Description.createTestDescription(
                "dummy test class name", "dummy test name");

        try {
            resourceRule.apply(failingTest, dummyDescription).evaluate();
            Assert.fail("ExternalResource should throw");
        } catch (MultipleFailureException e) {
            MatcherAssert.assertThat(e.getMessage(), allOf(
                    containsString("simulated test failure"),
                    containsString("simulating resource tear down failure")
            ));
        }
    }

    public static class TestFailsAndTwoClosingResourcesFail {
        @Rule
        public ExternalResource resourceRule1 = new ExternalResource() {
            @Override
            protected void after() {
                throw new RuntimeException("simulating resource1 tear down failure");
            }
        };

        @Rule
        public ExternalResource resourceRule2 = new ExternalResource() {
            @Override
            protected void after() {
                throw new RuntimeException("simulating resource2 tear down failure");
            }
        };

        @Test
        public void failingTest() {
            throw new RuntimeException("simulated test failure");
        }
    }

    @Test
    public void shouldThrowMultipleFailureExceptionWhenTestFailsAndTwoClosingResourcesFail() {
        Result result = JUnitCore.runClasses(TestFailsAndTwoClosingResourcesFail.class);
        Assert.assertEquals(3, result.getFailures().size());
        List<String> messages = new ArrayList<String>();
        for (Failure failure : result.getFailures()) {
            messages.add(failure.getMessage());
        }
        assertThat(messages, CoreMatchers.hasItems(
                "simulated test failure",
                "simulating resource1 tear down failure",
                "simulating resource2 tear down failure"
        ));
    }

    @Test
    public void shouldWrapAssumptionFailuresWhenClosingResourceFails() throws Throwable {
        // given
        final AtomicReference<Throwable> externalResourceException = new AtomicReference<Throwable>();
        ExternalResource resourceRule = new ExternalResource() {
            @Override
            protected void after() {
                RuntimeException runtimeException = new RuntimeException("simulating resource tear down failure");
                externalResourceException.set(runtimeException);
                throw runtimeException;
            }
        };
        final AtomicReference<Throwable> assumptionViolatedException = new AtomicReference<Throwable>();
        Statement skippedTest = new Statement() {
            @Override
            public void evaluate() throws Throwable {
                AssumptionViolatedException assumptionFailure = new AssumptionViolatedException("skip it");
                assumptionViolatedException.set(assumptionFailure);
                throw assumptionFailure;
            }
        };
        Description dummyDescription = Description.createTestDescription(
                "dummy test class name", "dummy test name");

        try {
            resourceRule.apply(skippedTest, dummyDescription).evaluate();
            Assert.fail("ExternalResource should throw");
        } catch (MultipleFailureException e) {
            MatcherAssert.assertThat(e.getFailures(), hasItems(
                    instanceOf(TestCouldNotBeSkippedException.class),
                    sameInstance(externalResourceException.get())
            ));
            MatcherAssert.assertThat(e.getFailures(), hasItems(
                    hasCause(sameInstance(assumptionViolatedException.get())),
                    sameInstance(externalResourceException.get())
            ));
        }
    }

    @SuppressWarnings("unchecked")
    private Matcher<? super List<Throwable>> hasItems(
            Matcher<? super Throwable> one, Matcher<? super Throwable> two) {
        return CoreMatchers.hasItems(one, two);
    }
}
