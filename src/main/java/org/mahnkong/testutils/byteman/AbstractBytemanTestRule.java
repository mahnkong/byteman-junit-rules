package org.mahnkong.testutils.byteman;

import org.junit.rules.TestRule;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

/**
 * Created by mahnkong on 19.04.15.
 */
public abstract class AbstractBytemanTestRule implements TestRule {
    public static String BYTEMAN_HOME_SYSTEM_PROPERTY = "byteman.home";

    private String bytemanHome;

    private boolean isWindows = false;
    protected String bmsubmit = "";
    protected String bminstall = "";

    public AbstractBytemanTestRule(String bytemanHome) {
        if (bytemanHome != null && !bytemanHome.equals("")) {
            this.bytemanHome = bytemanHome;
        }
        if (System.getProperty ("os.name").toLowerCase().contains("windows")) {
            isWindows = true;
        }
        String prefix = this.bytemanHome + File.separator + "bin" + File.separator;
        String suffix = (isWindows ? ".bat" : ".sh");

        bmsubmit = prefix + "bmsubmit" + suffix;
        bminstall = prefix + "bminstall" + suffix;
    }

    protected void execute(String command, boolean verbose) throws Exception {
        Process p = Runtime.getRuntime().exec(command);
        p.waitFor();

        if (verbose) {
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line;
            while ((line = reader.readLine())!= null) {
                System.err.println(line);
            }
        }

        if (p.exitValue() != 0) {
            throw new IllegalStateException("Execution of command '" + command + "' failed with exit code '" + p.exitValue() + "'");
        }
    }
}
