package com.amazon.utilities;

import com.amazon.models.ProductURLs;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.logging.Level;

import static com.amazon.utilities.TestConstants.DOCKER_GRID_URL;
import static com.amazon.utilities.TestConstants.WAIT_EXPLICIT;
import static com.amazon.utilities.TestConstants.WAIT_IMPLICIT;
import static com.amazon.utilities.TestConstants.WAIT_IMPLICIT_PAGE;
import static com.amazon.utilities.TestLogger.info;
import static com.amazon.utilities.TestLogger.error;

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
	 * The Browser version.
	 */
	private static String mBrowserVersion = "";

	/**
	 * The Platform name.
	 */
	private static String mPlatform = "";

	/**
	 * The Platform Version.
	 */
	private static String mPlatformVersion = "";

	/**
	 * The Test Name.
	 */
	private static String mTestName = "";

	/**
	 * The name of the build.
	 */
	private static String mBuildName = "";

	/**
	 * The width of the browser window for desktop computers
	 * using full screen.
	 */
	private static final int WIDTH = 1920;

	/**
	 * The height of the browser window for desktop computers
	 * using full screen.
	 */
	private static final int HEIGHT = 1080;

	/**
	 * Set the capabilities for the browser,
	 * platform and build of the Remote WebDriver used in this test.
	 * @param browser The browser name.
	 * @param browserVersion The browser version.
	 * @param platform The platform name.
	 * @param platformVersion The platform version.
	 * @param buildName The build name.
	 */
	public static void setCapabilities(
			final String browser,
			final String browserVersion,
			final String platform,
			final String platformVersion,
			final String buildName) {
		DateTimeFormatter dtf
				= DateTimeFormatter.ofPattern(
				"MM_dd_uuuu_HH");
		LocalDateTime localDateTime = LocalDateTime.now();

		mBrowser = browser;
		mBrowserVersion = browserVersion;
		mPlatform = platform;
		mPlatformVersion = platformVersion;
		mBuildName = buildName
				+ "_"
				+ browser
				+ "_"
				+ platform
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
	 * Retrieve the Browser Version.
	 * @return A {@link String } containing the browser version.
	 */
	public static String getBrowserVersion() {
		return mBrowserVersion;
	}

	/**
	 * Retrieve the Platform.
	 * @return A {@link String } containing the platform name.
	 */
	public static String getPlatform() {
		return mPlatform;
	}

	/**
	 * Retrieve the Platform Version.
	 * @return A {@link String } containing the platform version.
	 */
	public static String getPlatformVersion() {
		return mPlatformVersion;
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
		return setDriver(mBrowser,
				mBrowserVersion,
				mPlatform,
				mPlatformVersion);
	}

	/**
	 * Instantiates the RemoteWebDriver to the capabilities passed
	 * through the parameters.
	 * @param browser - The Browser Brand
	 *                   example(chrome, firefox, edge).
	 * @param browserVersion - The Version of the browser
	 *                          example(71.0, 79).
	 * @param platform - The Platform to run this test on
	 *                    example(ios, windows).
	 * @param platformVersion - The version of the platform
	 *                           AKA operating system.
	 * @return A set-up {@link RemoteWebDriver } that is connecting
	 * to the intended GRID.
	 */
	private static RemoteWebDriver setDriver(
			final String browser,
			final String browserVersion,
			final String platform,
			final String platformVersion) {

		if (TestConstants.DEPLOYMENT_RUN
				&& !mBuildName.contains("Deploy_")) {
			mBuildName = "Deploy_" + mBuildName;
		}

		instantiateWebDriverWithDocker(browser,
				browserVersion,
				platform,
				platformVersion);

		if (null != REMOTE_WEB_DRIVER.get()) {
			REMOTE_WEB_DRIVER
					.get()
					.manage()
					.timeouts()
					.pageLoadTimeout(Duration.ofSeconds(WAIT_IMPLICIT_PAGE));
			REMOTE_WEB_DRIVER
					.get()
					.manage()
					.timeouts()
					.implicitlyWait(Duration.ofSeconds(WAIT_IMPLICIT));
			REMOTE_WEB_DRIVER
					.get()
					.manage()
					.timeouts()
					.scriptTimeout(
							Duration.ofSeconds(WAIT_EXPLICIT));

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
	 * @param browserVersion - The Version of the browser
	 *                          example(71.0, 79).
	 * @param platform - The Platform to run this test on
	 *                    example(ios, windows).
	 * @param platformVersion - The version of the platform
	 *                           AKA operating system.
	 */
	private static void instantiateWebDriverWithDocker(
			String browser,
			final String browserVersion,
			final String platform,
			final String platformVersion) {
		String infoStatement = "Working in a ["
				+ platform
				+ " v=["
				+ platformVersion
				+ "]] container, setting up driver for ["
				+ browser
				+ " v=["
				+ browserVersion
				+ "]]";

		info(infoStatement);

		MutableCapabilities options = null;
		switch (browser) {
			case "firefox" -> options = new FirefoxOptions();
			case "chrome" -> options = new ChromeOptions();
			case "microsoftedge" -> {
				options = new EdgeOptions();
				browser = "MicrosoftEdge";
			}
		}

		if (null != options) {
			options = options.merge(options);
		}

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
					+ "BrowserName = " + browser + "\n"
					+ "BrowserVersion = " + browserVersion + "\n"
					+ "PlatformName = " + platform + "\n"
					+ "PlatformVersion = " + platformVersion + "\n", e);
		}
        if (null != REMOTE_WEB_DRIVER.get()) {
			info("Remote WebDriver has connected to the Grid");
		}

	}
}
