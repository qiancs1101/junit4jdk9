package org.junit.tests.experimental;

import org.junit.categories.AllCategoriesTests;
import org.junit.notify.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.junit.tests.experimental.max.AllMaxTests;
import org.junit.tests.experimental.parallel.AllParallelTests;
import org.junit.tests.experimental.results.AllResultsTests;
import org.junit.tests.experimental.theories.AllTheoriesTests;
import org.junit.tests.experimental.theories.extendingwithstubs.StubbedTheoriesTest;

@RunWith(Suite.class)
@SuiteClasses({
        AllCategoriesTests.class,
        AllMaxTests.class,
        AllParallelTests.class,
        AllResultsTests.class,
        AllTheoriesTests.class,
        AssumptionTest.class,
        MatcherTest.class,
        StubbedTheoriesTest.class
})
public class AllExperimentalTests {
}
