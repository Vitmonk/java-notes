package com.github.vitmonk.javanotes.process;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.time.StopWatch;
import org.junit.Test;

public class StartAndKillProcess {

    private static final int PROCESS_LIFESPAN_IN_SECONDS = 8;

    // For naked IDEs without helpful plugins
    public static void main(String[] args) throws Exception {
        new StartAndKillProcess().testProcessKill();
    }

    @Test
    public void testProcessKill() throws Exception {
        // java.lang.ProcessBuilder
        Process process = new ProcessBuilder("ping", "bing.com", "-n", "100").start();
        // java.lang.Runtime
        // Process process = Runtime.getRuntime().exec("ping bing.com -n 100");
        System.out.println("Started process");

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        BufferedReader consoleOutput = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String outputLine = null;
        while ((outputLine = consoleOutput.readLine()) != null) {

            if (processShouldBeKilled(stopWatch)) {
                process.destroyForcibly();
                System.err.println("DESTROY PROCESS EMITTED!");
            }
            System.out.println(outputLine);
        }
        System.out.println("Process destroyed");
    }

    private boolean processShouldBeKilled(StopWatch stopWatch) {
        return TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) > PROCESS_LIFESPAN_IN_SECONDS;
    }

}
