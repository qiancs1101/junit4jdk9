package org.junit.notify.runner.notification;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.common.AssumptionViolatedException;
import org.junit.common.Ignore;
import org.junit.common.runner.Description;
import org.junit.notify.runner.Result;

public class RunListener {

    /**
     * Called before any tests have been run. This may be called on an
     * arbitrary thread.
     *
     * @param description describes the tests to be run
     */
    public void testRunStarted(Description description) throws Exception {
    }

    /**
     * Called when all tests have finished. This may be called on an
     * arbitrary thread.
     *
     * @param result the summary of the test run, including all the tests that failed
     */
    public void testRunFinished(Result result) throws Exception {
    }

    /**
     * Called when a test suite is about to be started. If this method is
     * called for a given {@link Description}, then {@link #testSuiteFinished(Description)}
     * will also be called for the same {@code Description}.
     *
     * <p>Note that not all runners will call this method, so runners should
     * be prepared to handle {@link #testStarted(Description)} calls for tests
     * where there was no cooresponding {@code testSuiteStarted()} call for
     * the parent {@code Description}.
     *
     * @param description the description of the test suite that is about to be run
     *                    (generally a class name)
     * @since 4.13
     */
    public void testSuiteStarted(Description description) throws Exception {
    }

    /**
     * Called when a test suite has finished, whether the test suite succeeds or fails.
     * This method will not be called for a given {@link Description} unless
     * {@link #testSuiteStarted(Description)} was called for the same @code Description}.
     *
     * @param description the description of the test suite that just ran
     * @since 4.13
     */
    public void testSuiteFinished(Description description) throws Exception {
    }

    /**
     * Called when an atomic test is about to be started.
     *
     * @param description the description of the test that is about to be run
     * (generally a class and method name)
     */
    public void testStarted(Description description) throws Exception {
    }

    /**
     * Called when an atomic test has finished, whether the test succeeds or fails.
     *
     * @param description the description of the test that just ran
     */
    public void testFinished(Description description) throws Exception {
    }

    /**
     * Called when an atomic test fails, or when a listener throws an exception.
     *
     * <p>In the case of a failure of an atomic test, this method will be called
     * with the same {@code Description} passed to
     * {@link #testStarted(Description)}, from the same thread that called
     * {@link #testStarted(Description)}.
     *
     * <p>In the case of a listener throwing an exception, this will be called with
     * a {@code Description} of {@link Description#TEST_MECHANISM}, and may be called
     * on an arbitrary thread.
     *
     * @param failure describes the test that failed and the exception that was thrown
     */
    public void testFailure(Failure failure) throws Exception {
    }

    /**
     * Called when an atomic test flags that it assumes a condition that is
     * false
     *
     * @param failure describes the test that failed and the
     * {@link AssumptionViolatedException} that was thrown
     */
    public void testAssumptionFailure(Failure failure) {
    }

    /**
     * Called when a test will not be run, generally because a test method is annotated
     * with {@link Ignore}.
     *
     * @param description describes the test that will not be run
     */
    public void testIgnored(Description description) throws Exception {
    }


    /**
     * Indicates a {@code RunListener} that can have its methods called
     * concurrently. This implies that the class is thread-safe (i.e. no set of
     * listener calls can put the listener into an invalid state, even if those
     * listener calls are being made by multiple threads without
     * synchronization).
     *
     * @since 4.12
     */
    @Documented
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ThreadSafe {
    }
}
