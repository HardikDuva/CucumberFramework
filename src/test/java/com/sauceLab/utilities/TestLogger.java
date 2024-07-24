package com.sauceLab.utilities;

import org.slf4j.LoggerFactory;

import static org.junit.Assert.fail;

public final class TestLogger {

    /**
     * The singleton Logger for this test / framework.
     * - each test has its own logger which creates its own log file and
     * logs to the main log.
     * - there is a main log which collects data from all core operations,
     * including from each test.
     */
    private static final org.slf4j.Logger LOGGER
            = LoggerFactory.getLogger(TestLogger.class);

    /**
     * There should be no instance of this class.
     */
    private TestLogger() { }

    /**
     * Log an information message providing any exceptions that occurred.
     * @param message The message to log.
     * @param exceptions The exception that occurred.
     */
    public static void info(final String message,
                            final Exception...exceptions) {

        for (Exception exception : exceptions) {
            LOGGER.info(message, exception);
        }
    }

    /**
     * Log an information message.
     * @param message The message to log.
     */
    public static void info(final String message) {
        LOGGER.info(message);
    }

    /**
     * Log a warning message.
     * @param message The message to log.
     */
    public static void warn(final String message) {
        LOGGER.warn(message);
    }

    /**
     * Log a debug message.
     * @param message The message to log.
     */
    public static void debug(final String message) {
        LOGGER.debug(message);
    }

    /**
     * Log an error message providing any exceptions that occurred.
     * @param message The message to log.
     * @param exceptions The exception that occurred.
     */
    public static void error(final String message,
                             final Exception...exceptions) {
        StringBuilder exceptionText = new StringBuilder();
        for (Exception exception : exceptions) {
            exceptionText.append(exception);
        }
        LOGGER.error(message);
        fail(message + "\n" + exceptionText);
    }

    /**
     * Log an error message.
     * @param message The message to log.
     */
    public static void error(final String message) {
        LOGGER.error(message);
        fail(message);
    }

}
