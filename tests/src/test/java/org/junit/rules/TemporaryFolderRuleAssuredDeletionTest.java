package org.junit.rules;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.experimental.results.PrintableResult.testResult;
import static org.junit.experimental.results.ResultMatchers.failureCountIs;
import static org.junit.experimental.results.ResultMatchers.isSuccessful;

import org.junit.Assert;
import org.junit.common.Rule;
import org.junit.common.Test;
import org.junit.experimental.results.PrintableResult;
import org.junit.experimental.results.ResultMatchers;

public class TemporaryFolderRuleAssuredDeletionTest {

    public static class TestClass {
        static TemporaryFolder injectedRule;

        @Rule
        public TemporaryFolder folder = injectedRule;

        @Test
        public void alwaysPassesButDeletesRootFolder() {
            //we delete the folder in the test so that it cannot be deleted by
            //the rule
            folder.getRoot().delete();
        }
    }

    @Test
    public void testFailsWhenCreatedFolderCannotBeDeletedButDeletionIsAssured() {
        TestClass.injectedRule = TemporaryFolder.builder()
                .assureDeletion()
                .build();
        PrintableResult result = PrintableResult.testResult(TestClass.class);
        Assert.assertThat(result, ResultMatchers.failureCountIs(1));
        Assert.assertThat(result.toString(), containsString("Unable to clean up temporary folder"));
    }

    @Test
    public void byDefaultTestDoesNotFailWhenCreatedFolderCannotBeDeleted() {
        TestClass.injectedRule = new TemporaryFolder();
        PrintableResult result = PrintableResult.testResult(TestClass.class);
        Assert.assertThat(result, ResultMatchers.isSuccessful());
    }
}
