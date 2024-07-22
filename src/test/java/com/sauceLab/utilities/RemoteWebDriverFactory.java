package com.sauceLab.utilities;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.SessionNotCreatedException;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;

import static com.sauceLab.utilities.TestConstants.DOCKER_GRID_URL;
import static com.sauceLab.utilities.TestLogger.error;
import static com.sauceLab.utilities.TestLogger.info;

public final class RemoteWebDriverFactory {

	/**
	 * There should not be an instance of this class made.
	 */
	private RemoteWebDriverFactory() { }

	/**
	 * The Browser name.
	 */
	private static String mBrowser = "";

	/**
	 * The Test Name.
	 */
	private static String mTestName = "";

	/**
	 * The name of the build.
	 */
	private static String mBuildName = "";

	/**
	 * Set the capabilities for the browser,
	 * platform and build of the Remote WebDriver used in this test.
	 * @param browser The browser name.
	 * @param buildName The build name.
	 */
	public static void setCapabilities(
			final String browser,
			final String buildName) {
		DateTimeFormatter dtf
				= DateTimeFormatter.ofPattern(
				"MM_dd_uuuu_HH");
		LocalDateTime localDateTime = LocalDateTime.now();

		mBrowser = browser;
		mBuildName = buildName
				+ "_"
				+ browser
				+ "_"
				+ dtf.format(localDateTime);
	}

	/**
	 * Retrieve the Browser name.
	 * @return A {@link String } containing the browser name.
	 */
	public static String getBrowser() {
		return mBrowser;
	}

	/**
	 * Set the Test Name.
	 * @param testName The name of the test.
	 */
	public static void setTestName(final String testName) {
		mTestName = testName;
	}

	/**
	 * The singleton Remote Web Driver Object that will be used for this
	 * test.
	 */
	private static final ThreadLocal<RemoteWebDriver> REMOTE_WEB_DRIVER
			= new ThreadLocal<>();

	/***
	 * Returns a Thread safe WebDriver instance of the flavour passed
	 * through the 'browser' parameter in the Suite file.
	 * @return - The instantiated {@link ThreadLocal }
	 * {@link RemoteWebDriver } object ready to take instructions
	 * from a test.
	 */
	public static RemoteWebDriver getDriver() {
		return setDriver(mBrowser);
	}

	/**
	 * Instantiates the RemoteWebDriver to the capabilities passed
	 * through the parameters.
	 * @param browser - The Browser Brand
	 *                   example(chrome, firefox, edge).

	 * @return A set-up {@link RemoteWebDriver } that is connecting
	 * to the intended GRID.
	 */
	private static RemoteWebDriver setDriver(
			final String browser) {

		instantiateWebDriverWithDocker(browser);

		if (null != REMOTE_WEB_DRIVER.get()) {
			REMOTE_WEB_DRIVER.get()
					.manage()
					.window()
					.maximize();
		}

		return REMOTE_WEB_DRIVER.get();
	}

	/**
	 * Instantiates the RemoteWebDriver to the capabilities passed
	 * through the parameters.
	 * @param browser - The Browser Brand
	 *                   example(chrome, firefox, edge).
	 */
	private static void instantiateWebDriverWithDocker(String browser) {
		String infoStatement = "Working in a []] container, setting up driver for [" + browser;

		info(infoStatement);

		MutableCapabilities options = null;

		switch (browser) {
			case "firefox" -> {
				options = new FirefoxOptions();
			}
			case "microsoftedge" -> {
				options = new EdgeOptions();

			} default -> {
				options = new ChromeOptions();
				LoggingPreferences logPrefs
						= new LoggingPreferences();
				logPrefs.enable(LogType.BROWSER, Level.ALL);
				options.setCapability(
						"goog:loggingPrefs",
						logPrefs);
			}
		}

        options = options.merge(options);

        try {
			if (null != options) {
				REMOTE_WEB_DRIVER.set(
						new RemoteWebDriver(new URL(DOCKER_GRID_URL), options));
			}

			// If the Web Driver has been set
			if (null != REMOTE_WEB_DRIVER.get()){
				// Set this below to ensure that files can be found when using a dockerized grid
				REMOTE_WEB_DRIVER.get().setFileDetector(new LocalFileDetector());
			}

		} catch (MalformedURLException e) {
			error("\nMalformed URL Exception while connecting to the Selenium GRID Hub\n" + e.getMessage());
		} catch (SessionNotCreatedException e) {
			error("Selenium Grid was unable to create a session using the following capabilities: \n"
					+ "BrowserName = " + browser, e);
		}
        if (null != REMOTE_WEB_DRIVER.get()) {
			info("Remote WebDriver has connected to the Grid");
		}

	}
}
