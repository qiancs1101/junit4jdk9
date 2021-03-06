package org.junit.tests.experimental.results;

import org.junit.Assert;
import org.junit.common.Test;
import org.junit.experimental.results.PrintableResult;
import org.junit.experimental.results.ResultMatchers;
import org.junit.experimental.theories.Theory;
import org.junit.common.runner.Description;
import org.junit.notify.runner.notification.Failure;

import java.util.ArrayList;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ResultMatchersTest {

    @Test
    public void hasFailuresHasGoodDescription() {
        Assert.assertThat(ResultMatchers.failureCountIs(3).toString(),
                is("has 3 failures"));
    }

    @Theory
    public void hasFailuresDescriptionReflectsInput(int i) {
        Assert.assertThat(ResultMatchers.failureCountIs(i).toString(),
                containsString("" + i));
    }

    @Test
    public void hasFailureContaining_givenResultWithNoFailures() {
        PrintableResult resultWithNoFailures = new PrintableResult(new ArrayList<Failure>());

        Assert.assertThat(ResultMatchers.hasFailureContaining("").matches(resultWithNoFailures), is(false));
    }

    @Test
    public void hasFailureContaining_givenResultWithOneFailure() {
        PrintableResult resultWithOneFailure = new PrintableResult(Collections.singletonList(
                new Failure(Description.EMPTY, new RuntimeException("my failure"))));

        Assert.assertThat(ResultMatchers.hasFailureContaining("my failure").matches(resultWithOneFailure), is(true));
        Assert.assertThat(ResultMatchers.hasFailureContaining("his failure").matches(resultWithOneFailure), is(false));
    }
}
