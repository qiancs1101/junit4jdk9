package org.junit.categories;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.common.Test;
import org.junit.experimental.categories.Categories;
import org.junit.experimental.categories.Categories.IncludeCategory;
import org.junit.experimental.categories.Category;
import org.junit.runner.JUnitCore;
import org.junit.notify.runner.Result;
import org.junit.notify.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Suite.SuiteClasses;

public class CategoriesAndParameterizedTest {
    public static class Token {

    }

    @RunWith(Parameterized.class)
    public static class ParameterizedTestWithoutCategory {
        @Parameters
        public static Iterable<String> getParameters() {
            return Arrays.asList("first", "second");
        }

        @Parameterized.Parameter
        public String value;

        @Test
        public void testSomething() {
            Assert.assertTrue(true);
        }
    }

    @Category(Token.class)
    public static class TestThatAvoidsNoTestRemainsException {
        @Test
        public void testSomething() {
            Assert.assertTrue(true);
        }
    }

    @RunWith(Categories.class)
    @IncludeCategory(Token.class)
    @SuiteClasses({ TestThatAvoidsNoTestRemainsException.class,
            ParameterizedTestWithoutCategory.class })
    public static class SuiteWithParameterizedTestWithoutCategory {
    }

    @Test
    public void doesNotRunTestsWithoutCategory() {
        Result result = new JUnitCore()
                .run(SuiteWithParameterizedTestWithoutCategory.class);
        Assert.assertEquals(1, result.getRunCount());
        Assert.assertEquals(0, result.getFailureCount());
    }

    @RunWith(Parameterized.class)
    @Category(Token.class)
    public static class ParameterizedTestWithCategory {
        @Parameters
        public static Iterable<String> getParameters() {
            return Arrays.asList("first", "second");
        }

        @Parameterized.Parameter
        public String value;

        @Test
        public void testSomething() {
            Assert.assertTrue(true);
        }
    }

    @RunWith(Categories.class)
    @IncludeCategory(Token.class)
    @SuiteClasses({ ParameterizedTestWithCategory.class })
    public static class SuiteWithParameterizedTestWithCategory {
    }

    @Test
    public void runsTestsWithoutCategory() {
        Result result = new JUnitCore()
                .run(SuiteWithParameterizedTestWithCategory.class);
        Assert.assertEquals(2, result.getRunCount());
        Assert.assertEquals(0, result.getFailureCount());
    }

    @RunWith(Parameterized.class)
    public static class ParameterizedTestWithMethodWithCategory {
        @Parameters
        public static Iterable<String> getParameters() {
            return Arrays.asList("first", "second");
        }

        @Parameterized.Parameter
        public String value;

        @Test
        @Category(Token.class)
        public void testSomething() {
            Assert.assertTrue(true);
        }

        @Test
        public void testThatIsNotExecuted() {
            Assert.assertTrue(true);
        }
    }

    @RunWith(Categories.class)
    @IncludeCategory(Token.class)
    @SuiteClasses({ ParameterizedTestWithMethodWithCategory.class })
    public static class SuiteWithParameterizedTestWithMethodWithCategory {
    }

    @Test
    public void runsTestMethodWithCategory() {
        Result result = new JUnitCore()
                .run(SuiteWithParameterizedTestWithMethodWithCategory.class);
        Assert.assertEquals(2, result.getRunCount());
        Assert.assertEquals(0, result.getFailureCount());
    }
}