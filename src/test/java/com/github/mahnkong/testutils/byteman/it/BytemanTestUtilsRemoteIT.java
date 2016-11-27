package com.github.mahnkong.testutils.byteman.it;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.github.mahnkong.testutils.byteman.BytemanRuleFile;
import com.github.mahnkong.testutils.byteman.BytemanRuleSubmitter;

import javax.ejb.EJB;

import static org.junit.Assert.assertEquals;

/**
 * Created by mahnkong on 03.05.15.
 */
@RunWith(Arquillian.class)
public class BytemanTestUtilsRemoteIT {

    @Rule
    public BytemanRuleSubmitter bytemanRuleSubmitter = new BytemanRuleSubmitter.Builder().build();

    @EJB
    private SimpleBean simpleBean;

    @Deployment
    public static JavaArchive createDeployment() {
        JavaArchive javaArchive = ShrinkWrap.create(JavaArchive.class);
        javaArchive.addClass(SimpleBean.class).addPackage(BytemanRuleSubmitter.class.getPackage());
        return javaArchive;
    }

    @Test
    public void testSayHelloOriginal() {
        assertEquals("Hello!", simpleBean.sayHello());
    }

    @Test
    @BytemanRuleFile(filepath = "build/resources/test/testSayHello.btm")
    public void testSayHelloByteman() {
        assertEquals("Moin!", simpleBean.sayHello());
    }

}
