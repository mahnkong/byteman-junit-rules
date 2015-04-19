package org.mahnkong.testutils.byteman;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Created by mahnkong on 19.04.15.
 */
public class BytemanRuleSubmitterTest {
    @Mock
    Runtime runtime;

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
        doNothing().when(bytemanRuleSubmitter).execute(anyString(), anyBoolean());

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

        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        verify(bytemanRuleSubmitter, times(2)).execute(argument.capture(), anyBoolean());

        List<String> capturedArgs = argument.getAllValues();
        assertTrue(capturedArgs.get(0).matches(".*bmsubmit\\.\\w+ /test/myClassRule.btm$"));
        assertTrue(capturedArgs.get(1).matches(".*bmsubmit\\.\\w+ -u /test/myClassRule.btm$"));
    }

    @Test
    public void testStatementExecutionBytemanRuleFileClassAndMethodAnnotation() throws Throwable {
        doNothing().when(bytemanRuleSubmitter).execute(anyString(), anyBoolean());

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

        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        verify(bytemanRuleSubmitter, times(4)).execute(argument.capture(), anyBoolean());

        List<String> capturedArgs = argument.getAllValues();
        assertTrue(capturedArgs.get(0).matches(".*bmsubmit\\.\\w+ /test/myClassRule.btm$"));
        assertTrue(capturedArgs.get(1).matches(".*bmsubmit\\.\\w+ /test/myMethodRule.btm$"));

        assertTrue(capturedArgs.get(2).matches(".*bmsubmit\\.\\w+ -u /test/myMethodRule.btm$"));
        assertTrue(capturedArgs.get(3).matches(".*bmsubmit\\.\\w+ -u /test/myClassRule.btm$"));
    }

    @Test
    public void testStatementExecutionBytemanRuleFileClassAndMethodAnnotationIgnoreClass() throws Throwable {
        doNothing().when(bytemanRuleSubmitter).execute(anyString(), anyBoolean());

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

        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        verify(bytemanRuleSubmitter, times(2)).execute(argument.capture(), anyBoolean());

        List<String> capturedArgs = argument.getAllValues();
        assertTrue(capturedArgs.get(0).matches(".*bmsubmit\\.\\w+ /test/myMethodRule.btm$"));
        assertTrue(capturedArgs.get(1).matches(".*bmsubmit\\.\\w+ -u /test/myMethodRule.btm$"));

    }

    @Test
    public void testStatementExecutionBytemanRuleFileMethodAnnotation() throws Throwable {
        doNothing().when(bytemanRuleSubmitter).execute(anyString(), anyBoolean());

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

        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        verify(bytemanRuleSubmitter, times(2)).execute(argument.capture(), anyBoolean());

        List<String> capturedArgs = argument.getAllValues();
        assertTrue(capturedArgs.get(0).matches(".*bmsubmit\\.\\w+ /test/myMethodRule.btm$"));
        assertTrue(capturedArgs.get(1).matches(".*bmsubmit\\.\\w+ -u /test/myMethodRule.btm$"));
    }
}
