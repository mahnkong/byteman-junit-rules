package org.mahnkong.testutils.byteman.it;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.mahnkong.testutils.byteman.BytemanAgentInstaller;
import org.mahnkong.testutils.byteman.BytemanRuleFile;
import org.mahnkong.testutils.byteman.BytemanRuleSubmitter;

import static org.junit.Assert.assertEquals;

/**
 * Created by mahnkong on 03.05.15.
 */
public class BytemanTestUtilsIT {

    private SimpleBean simpleBean;

    @ClassRule
    public static BytemanAgentInstaller bytemanAgentInstaller = new BytemanAgentInstaller.Builder().build();

    @Rule
    public BytemanRuleSubmitter bytemanRuleSubmitter = new BytemanRuleSubmitter.Builder().build();

    @Before
    public void setUp() {
        simpleBean = new SimpleBean();
    }

    @Test
    public void testSayHelloOriginal() {
        assertEquals("Hello!", simpleBean.sayHello());
    }

    @Test
    @BytemanRuleFile(filepath = "target/test-classes/testSayHello.btm")
    public void testSayHelloByteman() {
        assertEquals("Moin!", simpleBean.sayHello());
    }
}
