package org.junit.tests.listening;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.common.Test;
import org.junit.common.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.notify.runner.notification.RunListener;

public class ListenerTest {
    static private String log;

    public static class OneTest {
        @Test
        public void nothing() {
        }
    }

    @Test
    public void notifyListenersInTheOrderInWhichTheyAreAdded() {
        JUnitCore core = new JUnitCore();
        log = "";
        core.addListener(new RunListener() {
            @Override
            public void testRunStarted(Description description) throws Exception {
                log += "first ";
            }
        });
        core.addListener(new RunListener() {
            @Override
            public void testRunStarted(Description description) throws Exception {
                log += "second ";
            }
        });
        core.run(OneTest.class);
        Assert.assertEquals("first second ", log);
    }
}
