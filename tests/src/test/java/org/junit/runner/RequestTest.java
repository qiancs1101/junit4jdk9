package org.junit.runner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.rules.EventCollector.hasSingleFailureWithMessage;

import org.hamcrest.MatcherAssert;
import org.junit.common.Test;
import org.junit.common.runner.Description;
import org.junit.notify.runner.Runner;
import org.junit.rules.EventCollector;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

public class RequestTest {

    /**
     * #1320 A root of a {@link Description} produced by
     * {@link Request#classes(Class...)} should be named "classes"
     */
    @Test
    public void createsADescriptionWithANameForClasses() {
        Description description = Request
                .classes(RequestTest.class, RequestTest.class).getRunner()
                .getDescription();
        MatcherAssert.assertThat(description.toString(), is("classes"));
    }

    @Test
    public void reportsInitializationErrorThrownWhileCreatingSuite() {
        EventCollector collector = new EventCollector();
        JUnitCore core = new JUnitCore();
        core.addListener(collector);

        core.run(new FailingComputer(), FooTest.class, BarTest.class);

        assertThat(collector, hasSingleFailureWithMessage("cannot create suite"));
    }

    private static class FailingComputer extends Computer {
        @Override
        public Runner getSuite(RunnerBuilder builder, Class<?>[] classes)
                throws InitializationError {
            throw new InitializationError("cannot create suite");
        }
    }

    private static class FooTest {
    }

    private static class BarTest {
    }
}
