package com.github.mahnkong.testutils.byteman;

import org.jboss.byteman.agent.submit.Submit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Created by mahnkong on 19.04.15.
 */
public class BytemanRuleSubmitterTest {
    @Mock
    Runtime runtime;

    @Mock
    Submit submit;

    @InjectMocks
    private BytemanRuleSubmitter bytemanRuleSubmitter = spy(new BytemanRuleSubmitter.Builder().build());

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @BytemanRuleFile(filepath = "/test/myClassRule.btm")
    class AnnotatedTestClass {

    }

    class UnannotatedTestClass {

    }

    @Test
    public void testStatementExecutionBytemanRuleFileClassAnnotation() throws Throwable {
        final List<String> list = new ArrayList();
        Statement statement = new Statement() {
            @Override
            public void evaluate() throws Throwable {
                list.add("test");
            }
        };
        Description description = mock(Description.class);
        when(description.getTestClass()).thenReturn((Class) AnnotatedTestClass.class);

        bytemanRuleSubmitter.apply(statement, description).evaluate();
        assertEquals(1, list.size());

        verify(submit, times(1)).addRulesFromFiles(Arrays.asList("/test/myClassRule.btm"));
        verify(submit, times(1)).deleteRulesFromFiles(Arrays.asList("/test/myClassRule.btm"));
    }

    @Test
    public void testStatementExecutionBytemanRuleFileClassAndMethodAnnotation() throws Throwable {
        BytemanRuleFile bytemanRuleFile = mock(BytemanRuleFile.class);
        when(bytemanRuleFile.filepath()).thenReturn("/test/myMethodRule.btm");

        final List<String> list = new ArrayList();
        Statement statement = new Statement() {
            @Override
            public void evaluate() throws Throwable {
                list.add("test");
            }
        };
        Description description = mock(Description.class);
        when(description.getAnnotation(BytemanRuleFile.class)).thenReturn(bytemanRuleFile);
        when(description.getTestClass()).thenReturn((Class) AnnotatedTestClass.class);

        bytemanRuleSubmitter.apply(statement, description).evaluate();
        assertEquals(1, list.size());

        verify(submit, times(1)).addRulesFromFiles(Arrays.asList("/test/myMethodRule.btm"));
        verify(submit, times(1)).deleteRulesFromFiles(Arrays.asList("/test/myMethodRule.btm"));
        verify(submit, times(1)).addRulesFromFiles(Arrays.asList("/test/myClassRule.btm"));
        verify(submit, times(1)).deleteRulesFromFiles(Arrays.asList("/test/myClassRule.btm"));
    }

    @Test
    public void testStatementExecutionBytemanRuleFileClassAndMethodAnnotationIgnoreClass() throws Throwable {
        BytemanRuleFile bytemanRuleFile = mock(BytemanRuleFile.class);
        IgnoreBytemanClassRuleFile ignoreBytemanClassRuleFile = mock(IgnoreBytemanClassRuleFile.class);
        when(bytemanRuleFile.filepath()).thenReturn("/test/myMethodRule.btm");

        final List<String> list = new ArrayList();
        Statement statement = new Statement() {
            @Override
            public void evaluate() throws Throwable {
                list.add("test");
            }
        };
        Description description = mock(Description.class);
        when(description.getAnnotation(BytemanRuleFile.class)).thenReturn(bytemanRuleFile);
        //method has IgnoreBytemanClassRuleFile annotated
        when(description.getAnnotation(IgnoreBytemanClassRuleFile.class)).thenReturn(ignoreBytemanClassRuleFile);
        //using AnnotatedTestClass as test class
        when(description.getTestClass()).thenReturn((Class) AnnotatedTestClass.class);

        bytemanRuleSubmitter.apply(statement, description).evaluate();
        assertEquals(1, list.size());

        verify(submit, times(1)).addRulesFromFiles(Arrays.asList("/test/myMethodRule.btm"));
        verify(submit, times(1)).deleteRulesFromFiles(Arrays.asList("/test/myMethodRule.btm"));
        verify(submit, times(0)).addRulesFromFiles(Arrays.asList("/test/myClassRule.btm"));
        verify(submit, times(0)).deleteRulesFromFiles(Arrays.asList("/test/myClassRule.btm"));

    }

    @Test
    public void testStatementExecutionBytemanRuleFileMethodAnnotation() throws Throwable {
        BytemanRuleFile bytemanRuleFile = mock(BytemanRuleFile.class);
        when(bytemanRuleFile.filepath()).thenReturn("/test/myMethodRule.btm");

        final List<String> list = new ArrayList();
        Statement statement = new Statement() {
            @Override
            public void evaluate() throws Throwable {
                list.add("test");
            }
        };
        Description description = mock(Description.class);
        when(description.getTestClass()).thenReturn((Class) UnannotatedTestClass.class);
        when(description.getAnnotation(BytemanRuleFile.class)).thenReturn(bytemanRuleFile);

        bytemanRuleSubmitter.apply(statement, description).evaluate();
        assertEquals(1, list.size());

        verify(submit, times(1)).addRulesFromFiles(Arrays.asList("/test/myMethodRule.btm"));
        verify(submit, times(1)).deleteRulesFromFiles(Arrays.asList("/test/myMethodRule.btm"));
    }
}
