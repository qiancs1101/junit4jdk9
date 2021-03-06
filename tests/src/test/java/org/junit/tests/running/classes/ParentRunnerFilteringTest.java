package org.junit.tests.running.classes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.junit.experimental.results.PrintableResult.testResult;
import static org.junit.common.runner.Description.createSuiteDescription;
import static org.junit.common.runner.Description.createTestDescription;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.common.Test;
import org.junit.common.runner.Description;
import org.junit.experimental.results.PrintableResult;
import org.junit.experimental.results.ResultMatchers;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.notify.runner.Result;
import org.junit.notify.runner.RunWith;
import org.junit.notify.runner.Runner;
import org.junit.runner.manipulation.Filter;
import org.junit.common.runner.manipulation.NoTestsRemainException;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

public class ParentRunnerFilteringTest {
    private static Filter notThisMethodName(final String methodName) {
        return new Filter() {
            @Override
            public boolean shouldRun(Description description) {
                return description.getMethodName() == null
                        || !description.getMethodName().equals(methodName);
            }

            @Override
            public String describe() {
                return "don't run method name: " + methodName;
            }
        };
    }

    private static class CountingFilter extends Filter {
        private final Map<Description, Integer> countMap = new HashMap<Description, Integer>();

        @Override
        public boolean shouldRun(Description description) {
            Integer count = countMap.get(description);
            if (count == null) {
                countMap.put(description, 1);
            } else {
                countMap.put(description, count + 1);
            }
            return true;
        }

        @Override
        public String describe() {
            return "filter counter";
        }

        public int getCount(final Description desc) {
            if (!countMap.containsKey(desc)) {
                throw new IllegalArgumentException("Looking for " + desc
                        + ", but only contains: " + countMap.keySet());
            }
            return countMap.get(desc);
        }
    }

    public static class ExampleTest {
        @Test
        public void test1() throws Exception {
            // passes
        }
    }

    @RunWith(Suite.class)
    @SuiteClasses({ExampleTest.class})
    public static class ExampleSuite {
    }

    @Test
    public void testSuiteFiltering() throws Exception {
        Runner runner = Request.aClass(ExampleSuite.class).getRunner();
        Filter filter = notThisMethodName("test1");
        try {
            filter.apply(runner);
        } catch (NoTestsRemainException e) {
            return;
        }
        Assert.fail("Expected 'NoTestsRemainException' due to complete filtering");
    }

    public static class SuiteWithUnmodifyableChildList extends Suite {

        public SuiteWithUnmodifyableChildList(
                Class<?> klass, RunnerBuilder builder)
                throws InitializationError {
            super(klass, builder);
        }

        @Override
        protected List<Runner> getChildren() {
            return Collections.unmodifiableList(super.getChildren());
        }
    }

    @RunWith(SuiteWithUnmodifyableChildList.class)
    @SuiteClasses({ExampleTest.class})
    public static class ExampleSuiteWithUnmodifyableChildList {
    }

    @Test
    public void testSuiteFilteringWithUnmodifyableChildList() throws Exception {
        Runner runner = Request.aClass(ExampleSuiteWithUnmodifyableChildList.class)
                .getRunner();
        Filter filter = notThisMethodName("test1");
        try {
            filter.apply(runner);
        } catch (NoTestsRemainException e) {
            return;
        }
        Assert.fail("Expected 'NoTestsRemainException' due to complete filtering");
    }

    @Test
    public void testRunSuiteFiltering() throws Exception {
        Request request = Request.aClass(ExampleSuite.class);
        Request requestFiltered = request.filterWith(notThisMethodName("test1"));
        Assert.assertThat(PrintableResult.testResult(requestFiltered),
                ResultMatchers.hasSingleFailureContaining("don't run method name: test1"));
    }

    @Test
    public void testCountClassFiltering() throws Exception {
        JUnitCore junitCore = new JUnitCore();
        Request request = Request.aClass(ExampleTest.class);
        CountingFilter countingFilter = new CountingFilter();
        Request requestFiltered = request.filterWith(countingFilter);
        Result result = junitCore.run(requestFiltered);
        Assert.assertEquals(1, result.getRunCount());
        Assert.assertEquals(0, result.getFailureCount());

        Description desc = Description.createTestDescription(ExampleTest.class, "test1");
        Assert.assertEquals(1, countingFilter.getCount(desc));
    }

    @Test
    public void testCountSuiteFiltering() throws Exception {
        Class<ExampleSuite> suiteClazz = ExampleSuite.class;
        Class<ExampleTest> clazz = ExampleTest.class;

        JUnitCore junitCore = new JUnitCore();
        Request request = Request.aClass(suiteClazz);
        CountingFilter countingFilter = new CountingFilter();
        Request requestFiltered = request.filterWith(countingFilter);
        Result result = junitCore.run(requestFiltered);
        Assert.assertEquals(1, result.getRunCount());
        Assert.assertEquals(0, result.getFailureCount());

        Description suiteDesc = Description.createSuiteDescription(clazz);
        Assert.assertEquals(1, countingFilter.getCount(suiteDesc));

        Description desc = Description.createTestDescription(ExampleTest.class, "test1");
        Assert.assertEquals(1, countingFilter.getCount(desc));
    }
}