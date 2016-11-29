package com.github.mahnkong.testutils.byteman;

import org.junit.rules.TestRule;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

/**
 * Created by mahnkong on 19.04.15.
 */
public abstract class AbstractBytemanTestRule implements TestRule {
    private String bytemanHome;
    String bindAddress;
    int bindPort;

    private Runtime runtime = Runtime.getRuntime();

    private boolean isWindows = false;
    protected String bmsubmit = "";
    protected String bminstall = "";

    public AbstractBytemanTestRule(String bytemanHome, String bindAddress, int bindPort) {
        this.bytemanHome = bytemanHome;
        this.bindAddress = bindAddress;
        this.bindPort = bindPort;

        if (System.getProperty ("os.name").toLowerCase().contains("windows")) {
            isWindows = true;
        }
        String prefix = this.bytemanHome + File.separator + "bin" + File.separator;
        String suffix = (isWindows ? ".bat" : ".sh");

        bmsubmit = prefix + "bmsubmit" + suffix;
        bminstall = prefix + "bminstall" + suffix;
    }

    protected void execute(String command, boolean verbose) throws Exception {
        Process p = runtime.exec(command);
        p.waitFor();

        if (verbose) {
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line;
            while ((line = reader.readLine())!= null) {
                System.out.println(line);
            }
        }

        if (p.exitValue() != 0) {
            throw new IllegalStateException("Execution of command '" + command + "' failed with exit code '" + p.exitValue() + "'");
        }
    }

    public String getBytemanHome() {
        return bytemanHome;
    }
}
