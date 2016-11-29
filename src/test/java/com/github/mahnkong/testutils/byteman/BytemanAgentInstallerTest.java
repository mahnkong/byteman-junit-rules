package com.github.mahnkong.testutils.byteman;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Created by mahnkong on 19.04.15.
 */
public class BytemanAgentInstallerTest {

    @Mock
    Runtime runtime;

    @InjectMocks
    private BytemanAgentInstaller bytemanAgentInstaller = new BytemanAgentInstaller.Builder().build();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAgentInstallCommandBindAddressSet() throws Throwable {
        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        BytemanAgentInstaller b1 = spy(new BytemanAgentInstaller.Builder().bindAddress("myhost").build());
        doNothing().when(b1).execute(anyString(), anyBoolean());

        Statement statement = new Statement() {
            @Override
            public void evaluate() throws Throwable {
            }
        };
        Description description = mock(Description.class);

        b1.apply(statement, description).evaluate();

        verify(b1).execute(argument.capture(), anyBoolean());
        assertThat(argument.getValue(), containsString(" -h myhost"));
    }

    @Test
    public void testAgentInstallCommandBindAddressNotSet() throws Throwable {
        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        BytemanAgentInstaller b1 = spy(new BytemanAgentInstaller.Builder().build());
        doNothing().when(b1).execute(anyString(), anyBoolean());

        Statement statement = new Statement() {
            @Override
            public void evaluate() throws Throwable {
            }
        };
        Description description = mock(Description.class);

        b1.apply(statement, description).evaluate();

        verify(b1).execute(argument.capture(), anyBoolean());
        assertThat(argument.getValue(), not(containsString(" -h myhost")));
        assertThat(argument.getValue(), containsString(" -h localhost"));
    }

    @Test
    public void testAgentInstallCommandBindPortSet() throws Throwable {
        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        BytemanAgentInstaller b1 = spy(new BytemanAgentInstaller.Builder().bindPort(12345).build());
        doNothing().when(b1).execute(anyString(), anyBoolean());

        Statement statement = new Statement() {
            @Override
            public void evaluate() throws Throwable {
            }
        };
        Description description = mock(Description.class);

        b1.apply(statement, description).evaluate();

        verify(b1).execute(argument.capture(), anyBoolean());
        assertThat(argument.getValue(), containsString(" -p 12345"));
    }

    @Test
    public void testAgentInstallCommandBindPortNotSet() throws Throwable {
        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        BytemanAgentInstaller b1 = spy(new BytemanAgentInstaller.Builder().build());
        doNothing().when(b1).execute(anyString(), anyBoolean());

        Statement statement = new Statement() {
            @Override
            public void evaluate() throws Throwable {
            }
        };
        Description description = mock(Description.class);

        b1.apply(statement, description).evaluate();

        verify(b1).execute(argument.capture(), anyBoolean());
        assertThat(argument.getValue(), not(containsString(" -p 12345")));
        assertThat(argument.getValue(), containsString(" -p 9091"));
    }

    @Test
    public void testAgentInstallCommandCreationAccessAllAreasActive() throws Throwable {
        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        BytemanAgentInstaller b1 = spy(new BytemanAgentInstaller.Builder().accessAllAreas(true).build());
        doNothing().when(b1).execute(anyString(), anyBoolean());

        Statement statement = new Statement() {
            @Override
            public void evaluate() throws Throwable {
            }
        };
        Description description = mock(Description.class);

        b1.apply(statement, description).evaluate();

        verify(b1).execute(argument.capture(), anyBoolean());
        assertThat(argument.getValue(), containsString(" -s"));
    }

    @Test
    public void testAgentInstallCommandCreationAccessAllAreasNotActive() throws Throwable {
        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        BytemanAgentInstaller b1 = spy(new BytemanAgentInstaller.Builder().accessAllAreas(false).build());
        doNothing().when(b1).execute(anyString(), anyBoolean());

        Statement statement = new Statement() {
            @Override
            public void evaluate() throws Throwable {
            }
        };
        Description description = mock(Description.class);

        b1.apply(statement, description).evaluate();

        verify(b1).execute(argument.capture(), anyBoolean());
        assertThat(argument.getValue(), not(containsString(" -s")));
    }

    @Test
    public void testAgentInstallCommandCreationInstallIntoBoostrapClasspathActive() throws Throwable {
        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        BytemanAgentInstaller b1 = spy(new BytemanAgentInstaller.Builder().installIntoBootstrapClasspath(true).build());
        doNothing().when(b1).execute(anyString(), anyBoolean());

        Statement statement = new Statement() {
            @Override
            public void evaluate() throws Throwable {
            }
        };
        Description description = mock(Description.class);

        b1.apply(statement, description).evaluate();

        verify(b1).execute(argument.capture(), anyBoolean());
        assertThat(argument.getValue(), containsString(" -b"));
    }

    @Test
    public void testAgentInstallCommandCreationInstallIntoBoostrapClasspathNotActive() throws Throwable {
        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        BytemanAgentInstaller b1 = spy(new BytemanAgentInstaller.Builder().installIntoBootstrapClasspath(false).build());
        doNothing().when(b1).execute(anyString(), anyBoolean());

        Statement statement = new Statement() {
            @Override
            public void evaluate() throws Throwable {
            }
        };
        Description description = mock(Description.class);

        b1.apply(statement, description).evaluate();

        verify(b1).execute(argument.capture(), anyBoolean());
        assertThat(argument.getValue(), not(containsString(" -b")));
    }

    @Test
    public void testAgentInstallCommandCreationTransformAllActive() throws Throwable {
        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        BytemanAgentInstaller b1 = spy(new BytemanAgentInstaller.Builder().transformAll(true).build());
        doNothing().when(b1).execute(anyString(), anyBoolean());

        Statement statement = new Statement() {
            @Override
            public void evaluate() throws Throwable {
            }
        };
        Description description = mock(Description.class);

        b1.apply(statement, description).evaluate();

        verify(b1).execute(argument.capture(), anyBoolean());
        assertThat(argument.getValue(), containsString("-Dorg.jboss.byteman.transform.all"));
    }

    @Test
    public void testAgentInstallCommandCreationTransformAllNotActive() throws Throwable {
        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        BytemanAgentInstaller b1 = spy(new BytemanAgentInstaller.Builder().transformAll(false).build());
        doNothing().when(b1).execute(anyString(), anyBoolean());

        Statement statement = new Statement() {
            @Override
            public void evaluate() throws Throwable {
            }
        };
        Description description = mock(Description.class);

        b1.apply(statement, description).evaluate();

        verify(b1).execute(argument.capture(), anyBoolean());
        assertThat(argument.getValue(), not(containsString("-Dorg.jboss.byteman.transform.all")));
    }

    @Test
    public void testAgentInstallCommandCreationVerboseActive() throws Throwable {
        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        BytemanAgentInstaller b1 = spy(new BytemanAgentInstaller.Builder().verbose(true).build());
        doNothing().when(b1).execute(anyString(), anyBoolean());

        Statement statement = new Statement() {
            @Override
            public void evaluate() throws Throwable {
            }
        };
        Description description = mock(Description.class);

        b1.apply(statement, description).evaluate();

        verify(b1).execute(argument.capture(), anyBoolean());
        assertThat(argument.getValue(), containsString("-Dorg.jboss.byteman.verbose"));
    }

    @Test
    public void testAgentInstallCommandCreationVerboseNotActive() throws Throwable {
        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        BytemanAgentInstaller b1 = spy(new BytemanAgentInstaller.Builder().build());
        doNothing().when(b1).execute(anyString(), anyBoolean());

        Statement statement = new Statement() {
            @Override
            public void evaluate() throws Throwable {
            }
        };
        Description description = mock(Description.class);

        b1.apply(statement, description).evaluate();

        verify(b1).execute(argument.capture(), anyBoolean());
        assertThat(argument.getValue(), not(containsString("-Dorg.jboss.byteman.verbose")));
    }

     @Test
    public void testAgentInstallCommandCreationDebugActive() throws Throwable {
        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        BytemanAgentInstaller b1 = spy(new BytemanAgentInstaller.Builder().debug(true).build());
        doNothing().when(b1).execute(anyString(), anyBoolean());

        Statement statement = new Statement() {
            @Override
            public void evaluate() throws Throwable {
            }
        };
        Description description = mock(Description.class);

        b1.apply(statement, description).evaluate();

        verify(b1).execute(argument.capture(), anyBoolean());
        assertThat(argument.getValue(), containsString("-Dorg.jboss.byteman.debug"));
    }

    @Test
    public void testAgentInstallCommandCreationDebugNotActive() throws Throwable {
        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        BytemanAgentInstaller b1 = spy(new BytemanAgentInstaller.Builder().build());
        doNothing().when(b1).execute(anyString(), anyBoolean());

        Statement statement = new Statement() {
            @Override
            public void evaluate() throws Throwable {
            }
        };
        Description description = mock(Description.class);

        b1.apply(statement, description).evaluate();

        verify(b1).execute(argument.capture(), anyBoolean());
        assertThat(argument.getValue(), not(containsString("-Dorg.jboss.byteman.debug")));
    }

    @Test
    public void testAgentInstallCommandCreationBytemanHome() throws Throwable {
        BytemanAgentInstaller b1 = spy(new BytemanAgentInstaller.Builder().bytemanHome("/test_bytemanhome").build());
        doNothing().when(b1).execute(anyString(), anyBoolean());

        Statement statement = new Statement() {
            @Override
            public void evaluate() throws Throwable {
            }
        };
        Description description = mock(Description.class);

        b1.apply(statement, description).evaluate();

        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        verify(b1).execute(argument.capture(), anyBoolean());
        assertThat(argument.getValue(), containsString("/test_bytemanhome" + File.separator + "bin" + File.separator + "bminstall"));
    }

    @Test
    public void testStatementExecutionAfterAgentInstallExecutionOK() throws Throwable {
        Process mockProcess = mock(Process.class);
        when(mockProcess.waitFor()).thenReturn(0);
        when(mockProcess.exitValue()).thenReturn(0);
        when(runtime.exec(anyString())).thenReturn(mockProcess);

        final List<String> list = new ArrayList();

        Statement statement = new Statement() {
            @Override
            public void evaluate() throws Throwable {
                list.add("test");
            }
        };
        Description description = mock(Description.class);

        bytemanAgentInstaller.apply(statement, description).evaluate();
        assertEquals(1, list.size());
    }

    @Test(expected = IllegalStateException.class)
    public void testStatementExecutionNotDoneAfterAgentInstallExecutionNOK() throws Throwable {
        Process mockProcess = mock(Process.class);
        when(mockProcess.waitFor()).thenReturn(1);
        when(mockProcess.exitValue()).thenReturn(1);
        when(runtime.exec(anyString())).thenReturn(mockProcess);

        final List<String> list = new ArrayList();

        Statement statement = new Statement() {
            @Override
            public void evaluate() throws Throwable {
                list.add("test");
            }
        };
        Description description = mock(Description.class);

        bytemanAgentInstaller.apply(statement, description).evaluate();
        fail("should not have reached here!");
    }



}
