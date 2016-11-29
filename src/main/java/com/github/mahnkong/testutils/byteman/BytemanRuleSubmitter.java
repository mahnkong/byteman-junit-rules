package com.github.mahnkong.testutils.byteman;

import org.jboss.byteman.agent.submit.Submit;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.Arrays;

/**
 * Created by mahnkong on 21.03.15.
 */
public class BytemanRuleSubmitter extends AbstractBytemanTestRule {

    private Submit submit;

    private BytemanRuleSubmitter(Builder builder) {
        super(builder.bytemanHome, builder.bindAddress, builder.bindPort);
        submit = new Submit(bindAddress, bindPort);
    }

    public Statement apply(Statement base, Description description) {
        BytemanRuleFile bytemanRuleFileClassAnnotation = description.getTestClass().getAnnotation(BytemanRuleFile.class);
        BytemanRuleFile bytemanRuleFileMethodAnnotation = description.getAnnotation(BytemanRuleFile.class);
        IgnoreBytemanClassRuleFile ignoreBytemanClassRuleFile = description.getAnnotation(IgnoreBytemanClassRuleFile.class);

        if (bytemanRuleFileMethodAnnotation != null) {
            base = processBytemanRuleFile(base, bytemanRuleFileMethodAnnotation);
        }
        if (ignoreBytemanClassRuleFile == null) {
            return processBytemanRuleFile(base, bytemanRuleFileClassAnnotation);
        }
        return base;
    }

    private Statement processBytemanRuleFile(final Statement statement, final BytemanRuleFile bytemanRuleFile) {

        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                submitBytemanRule(bytemanRuleFile);
                try {
                    statement.evaluate();
                } finally {
                    removeBytemanRule(bytemanRuleFile);
                }
            }
        };
    }

    private void submitBytemanRule(final BytemanRuleFile bytemanRuleFile) throws Exception {
        if (bytemanRuleFile != null) {
            String result = submit.addRulesFromFiles(Arrays.asList(bytemanRuleFile.filepath()));
            if (bytemanRuleFile.verbose()) {
                System.out.println("Rule submitting of file '" + bytemanRuleFile.filepath() + "' returned: " + result);
            }
        }
    }

    private void removeBytemanRule(final BytemanRuleFile bytemanRuleFile) throws Exception {
        if (bytemanRuleFile != null) {
            String result = submit.deleteRulesFromFiles(Arrays.asList(bytemanRuleFile.filepath()));
            if (bytemanRuleFile.verbose()) {
                System.out.println("Rule deletion of file '" + bytemanRuleFile.filepath() + "' returned: " + result);
            }
        }
    }

    public static class Builder {
        private String bytemanHome;
        private String bindAddress;
        private int bindPort;

        public Builder() {
            this.bytemanHome = System.getenv("BYTEMAN_HOME");
            this.bindAddress = "localhost";
            this.bindPort = 9091;
        }

        public Builder bytemanHome(final String bytemanHome) {
            this.bytemanHome = bytemanHome;
            return this;
        }

        public Builder bindAddress(final String bindAddress) {
            this.bindAddress = bindAddress;
            return this;
        }

        public Builder bindPort(final int bindPort) {
            this.bindPort = bindPort;
            return this;
        }

        public BytemanRuleSubmitter build() {
            return new BytemanRuleSubmitter(this);
        }
    }
}