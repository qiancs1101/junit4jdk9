package org.junit.internal.matchers;

import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.util.internal.matchers.StacktracePrintingMatcher.isException;
import static org.junit.util.internal.matchers.StacktracePrintingMatcher.isThrowable;

import org.junit.Assert;
import org.junit.common.Test;

public class StacktracePrintingMatcherTest {

    @Test
    public void succeedsWhenInnerMatcherSucceeds() throws Exception {
        Assert.assertTrue(isThrowable(any(Throwable.class)).matches(new Exception()));
    }

    @Test
    public void failsWhenInnerMatcherFails() throws Exception {
        Assert.assertFalse(isException(notNullValue(Exception.class)).matches(null));
    }

    @Test
    public void assertThatIncludesStacktrace() {
        Exception actual = new IllegalArgumentException("my message");
        Exception expected = new NullPointerException();

        try {
            Assert.assertThat(actual, isThrowable(equalTo(expected)));
        } catch (AssertionError e) {
            Assert.assertThat(e.getMessage(), containsString("Stacktrace was: java.lang.IllegalArgumentException: my message"));
        }
    }
}
