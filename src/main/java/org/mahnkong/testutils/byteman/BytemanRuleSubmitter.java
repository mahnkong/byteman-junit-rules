package org.mahnkong.testutils.byteman;

import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.io.File;

/**
 * Created by mahnkong on 21.03.15.
 */
public class BytemanRuleSubmitter extends AbstractBytemanTestRule {

    private BytemanRuleSubmitter(Builder builder) {
        super(builder.bytemanHome);
    }

    @Override
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
            String command = bmsubmit + " " + new File(bytemanRuleFile.filepath()).getPath();
            execute(command, bytemanRuleFile.verbose());
            if (bytemanRuleFile.verbose()) {
                execute(bmsubmit, bytemanRuleFile.verbose());
            }
        }
    }

    private void removeBytemanRule(final BytemanRuleFile bytemanRuleFile) throws Exception {
        if (bytemanRuleFile != null) {
            String command = bmsubmit + " -u " + new File(bytemanRuleFile.filepath()).getPath();
            execute(command, bytemanRuleFile.verbose());
        }
    }

    public static class Builder {
        private String bytemanHome;


        public Builder() {
            this.bytemanHome = System.getenv("BYTEMAN_HOME");
        }

        public Builder bytemanHome(final String bytemanHome) {
            this.bytemanHome = bytemanHome;
            return this;
        }

        public BytemanRuleSubmitter build() {
            return new BytemanRuleSubmitter(this);
        }
    }
}