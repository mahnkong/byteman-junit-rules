package org.mahnkong.testutils.byteman;

import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

/**
 * Created by mahnkong on 19.04.15.
 */
public class BytemanAgentInstaller extends AbstractBytemanTestRule {

    private boolean verbose;

    private BytemanAgentInstaller(String bytemanHome, boolean verbose) {
        super(bytemanHome);
        this.verbose = verbose;
    }

    private void installAgent() throws Exception {
        RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
        String jvmName = bean.getName();
        long pid = Long.valueOf(jvmName.split("@")[0]);
        execute(bminstall + " " + pid, verbose);
    }

    @Override
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

        public Builder() {
            this.bytemanHome = System.getProperty(AbstractBytemanTestRule.BYTEMAN_HOME_SYSTEM_PROPERTY);
            this.verbose = false;
        }

        public Builder bytemanHome(final String bytemanHome) {
            this.bytemanHome = bytemanHome;
            return this;
        }

        public Builder verbose(final boolean verbose) {
            this.verbose = verbose;
            return this;
        }

        public BytemanAgentInstaller build() {
            return new BytemanAgentInstaller(bytemanHome, verbose);
        }
    }
}
