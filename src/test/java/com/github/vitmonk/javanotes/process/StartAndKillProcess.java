package com.github.vitmonk.javanotes.process;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.time.StopWatch;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Starting and killing process programmatically.
 */
public class StartAndKillProcess {
    private static final Logger LOGGER = LoggerFactory.getLogger(StartAndKillProcess.class);

    private static final int PROCESS_LIFESPAN_IN_SECONDS = 8;

    @Test
    public void testProcessKill() throws Exception {
        // java.lang.ProcessBuilder
        Process process = new ProcessBuilder("ping", "bing.com", "-n", "100").start();
        // java.lang.Runtime
        // Process process = Runtime.getRuntime().exec("ping bing.com -n 100");
        LOGGER.info("Started process");

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        BufferedReader consoleOutput = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String outputLine = null;
        while ((outputLine = consoleOutput.readLine()) != null) {

            if (processShouldBeKilled(stopWatch)) {
                process.destroyForcibly();
                LOGGER.info("DESTROY PROCESS EMITTED!");
            }
            LOGGER.info(outputLine);
        }
        LOGGER.info("Process destroyed");
    }

    private boolean processShouldBeKilled(StopWatch stopWatch) {
        return TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) > PROCESS_LIFESPAN_IN_SECONDS;
    }

}
