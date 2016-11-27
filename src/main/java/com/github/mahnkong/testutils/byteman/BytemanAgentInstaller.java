package com.github.mahnkong.testutils.byteman;

import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

/**
 * Created by mahnkong on 19.04.15.
 */
public class BytemanAgentInstaller extends AbstractBytemanTestRule {

    private boolean installIntoBootstrapClasspath;
    private boolean verbose;
    private boolean transformAll;
    private RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();


    private BytemanAgentInstaller(Builder builder) {
        super(builder.bytemanHome);
        this.verbose = builder.verbose;
        this.installIntoBootstrapClasspath = builder.installIntoBootstrapClasspath;
        this.transformAll = builder.transformAll;
    }

    private void installAgent() throws Exception {
        String jvmName = bean.getName();
        long pid = Long.valueOf(jvmName.split("@")[0]);
        execute(bminstall + (installIntoBootstrapClasspath ? " -b" : "") + (transformAll ? " -Dorg.jboss.byteman.transform.all" : "") +  " -Dorg.jboss.byteman.home=" + getBytemanHome() + " " + pid, verbose);
    }

    public Statement apply(final Statement statement, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                installAgent();
                try {
                    statement.evaluate();
                } finally {
                    //
                }
            }
        };
    }

    public static class Builder {
        private String bytemanHome;
        private boolean verbose;
        private boolean installIntoBootstrapClasspath;
        private boolean transformAll;

        public Builder() {
            this.bytemanHome = System.getenv("BYTEMAN_HOME");
            this.verbose = false;
            this.installIntoBootstrapClasspath = false;
            this.transformAll = false;
        }

        public Builder bytemanHome(final String bytemanHome) {
            this.bytemanHome = bytemanHome;
            return this;
        }

        public Builder transformAll(final boolean transformAll) {
            this.transformAll = transformAll;
            return this;
        }

        public Builder verbose(final boolean verbose) {
            this.verbose = verbose;
            return this;
        }

        public Builder installIntoBootstrapClasspath(final boolean installIntoBootstrapClasspath) {
            this.installIntoBootstrapClasspath = installIntoBootstrapClasspath;
            return this;
        }

        public BytemanAgentInstaller build() {
            return new BytemanAgentInstaller(this);
        }
    }
}
