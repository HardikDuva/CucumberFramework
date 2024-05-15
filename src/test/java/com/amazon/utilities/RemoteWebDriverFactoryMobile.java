package com.amazon.utilities;

import com.amazon.models.ProductURLs;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.SessionNotCreatedException;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.logging.Level;

import static com.amazon.utilities.TestLogger.info;
import static com.amazon.utilities.TestLogger.error;

public final class RemoteWebDriverFactoryMobile {

    /**
     * There should be no instance of this class.
     */
    private RemoteWebDriverFactoryMobile() { }

    /**
     * The browser name.
     */
    private static String mBrowser = "";

    /**
     * The browser version.
     */
    private static String mBrowserVersion = "";

    /**
     * The platform name.
     */
    private static String mPlatform = "";

    /**
     * The platform version.
     */
    private static String mPlatformVersion = "";

    /**
     * The device name.
     */
    private static String mDeviceName = "";

    /**
     * The native application path.
     */
    private static String mApp = "";

    /**
     * The build name of this suite.
     */
    private static String mBuildSuiteName = "";

    /**
     * The test name.
     */
    private static String mTestName = "";

    /**
     * Set the capabilities, application path and/or build name
     * for the RemoteWebDriver.
     * @param browser The browser name.
     * @param browserVersion The browser version.
     * @param platform The platform name.
     * @param platformVersion The platform version.
     * @param deviceName The device name.
     * @param app The native application path.
     * @param buildName The name of the build.
     */
    public static void setCapabilities(
            final String browser,
            final String browserVersion,
            final String platform,
            final String platformVersion,
            final String deviceName,
            final String app,
            final String buildName) {
        DateTimeFormatter dtf
                = DateTimeFormatter
                .ofPattern("MM_dd_uuuu_HH");
        LocalDateTime localDateTime = LocalDateTime.now();

        mBrowser = browser;
        mBrowserVersion = browserVersion;
        mPlatform = platform;
        mPlatformVersion = platformVersion;
        mDeviceName = deviceName;
        mApp = app;
        mBuildSuiteName = buildName
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
     * Retrieve the browser version.
     * @return A {@link String } containing the browser version.
     */
    public static String getBrowserVersion() {
        return mBrowserVersion;
    }

    /**
     * Retrieve the Platform name.
     * @return A {@link String } containing the name of the platform.
     */
    public static String getPlatform() {
        return mPlatform;
    }

    /**
     * Retrieve the platform version.
     * @return A {@link String } containing the version of the platform.
     */
    public static String getPlatformVersion() {
        return mPlatformVersion;
    }

    /**
     * Retrieve the name of the device.
     * @return A {@link String } containing the name of the device.
     */
    public static String getDeviceName() {
        return mDeviceName;
    }

    /**
     * Retrieve the native app path.
     * @return A {@link String } containing the path to the native app.
     */
    public static String getApp() {
        return mApp;
    }

    /**
     * Retrieve the Test name.
     * @return A {@link String } containing the Test name.
     */
    public static String getTestName() {
        return mTestName;
    }

    /**
     * Set the Test name.
     * @param testName The name of the test.
     */
    public static void setTestName(final String testName) {
        mTestName = testName;
    }

    /**
     * The singleton Remote WebDriver for this test.
     */
    private static final ThreadLocal<RemoteWebDriver> DRIVER_THREAD_LOCAL
            = new ThreadLocal<RemoteWebDriver>();

    /***
     * Returns a Thread safe WebDriver instance of the flavour passed
     * through the 'browser' parameter.
     * @return - The instantiated Thread Safe WebDriver object.
     */
    public static RemoteWebDriver getDriver() {
        return setDriver(mBrowser,
                mBrowserVersion,
                mPlatform,
                mPlatformVersion,
                mDeviceName,
                mApp,
                mBuildSuiteName);
    }

    /**
     * Initializes and returns the RemoteWebDriver.
     * @param browser - Which browser to use (Chrome, Firefox, Edge)
     * @param browserVersion The version of the browser.
     * @param platform The platform name.
     * @param platformVersion The platform version.
     * @param deviceName The name of the device.
     * @param app The path to the native app.
     * @param buildName The name of the build.
     * @return - The instantiated Thread safe WebDriver object.
     */
    private static RemoteWebDriver setDriver(
            final String browser,
            final String browserVersion,
            final String platform,
            final String platformVersion,
            final String deviceName,
            final String app,
            final String buildName) {
        instantiateWebDriver(browser,
                browserVersion,
                platform,
                platformVersion,
                deviceName,
                app,
                buildName);

        if (null != DRIVER_THREAD_LOCAL.get()
                && app.isEmpty()) {
            DRIVER_THREAD_LOCAL.get()
                    .manage()
                    .timeouts()
                    .pageLoadTimeout(
                            Duration.ofSeconds(
                                    TestConstants.WAIT_IMPLICIT_PAGE));
            DRIVER_THREAD_LOCAL.get()
                    .manage()
                    .timeouts()
                    .implicitlyWait(
                            Duration.ofSeconds(
                                    TestConstants.WAIT_IMPLICIT));
            DRIVER_THREAD_LOCAL.get()
                    .manage()
                    .timeouts()
                    .scriptTimeout(
                            Duration.ofSeconds(
                                    TestConstants.WAIT_EXPLICIT));
        }

        return DRIVER_THREAD_LOCAL.get();
    }

    /**
     * Instantiates the RemoteWebDriver to the capabilities passed
     * through the parameters.
     * @param browser - The Browser Brand
     *                example('chrome', 'firefox', 'edge').
     * @param browserVersion - The Version of the browser
     *                       example('71.0', '79').
     * @param platform - The Platform to run this test on
     *                 example('ios', 'windows').
     * @param platformVersion - The Platform version.
     * @param deviceName - The name of the device.
     * @param app - The Path to the native app.
     * @param buildName - The name of the build.
     */
    private static void instantiateWebDriver(final String browser,
                                             final String browserVersion,
                                             final String platform,
                                             final String platformVersion,
                                             final String deviceName,
                                             final String app,
                                             final String buildName) {
        // If there is an App defined then we don't need the web browser
        // to be set up
        if (!app.isEmpty()) {
            info("Testing App ["
                    + app
                    + "] in a ["
                    + platform
                    + " v=["
                    + platformVersion
                    + "]] container, using device ["
                    + deviceName
                    + "]");
        } else {
            info("Working in a ["
                    + platform
                    + " v=["
                    + platformVersion
                    + "]] container, setting up driver for ["
                    + browser
                    + " v=["
                    + browserVersion
                    + "]] using device ["
                    + deviceName
                    + "]");
        }

        MutableCapabilities options = null;
        options = new DesiredCapabilities();

        HashMap<String, Object> ltOptions;

        if (!app.isEmpty()) {
            ltOptions = getOptions(platform,
                    platformVersion,
                    deviceName,
                    buildName,
                    mTestName,
                    ProductURLs.getProductName(),
                    app);
        } else {
            ltOptions = getOptions(platform,
                    platformVersion,
                    deviceName,
                    buildName,
                    mTestName,
                    ProductURLs.getProductName());
        }

        if (platform.equals("ios")) {
            options = new DesiredCapabilities();
            options.setCapability("LT:Options", ltOptions);
        } else {
            // This is an Android Device.
            HashMap<String, Object> prefs
                    = new HashMap<String, Object>();
            prefs.put(
                    "profile.default_content_setting_values"
                            + ".notifications",
                    2);
            ChromeOptions cOptions = new ChromeOptions();
            cOptions.setExperimentalOption("prefs", prefs);
            LoggingPreferences logPrefs = new LoggingPreferences();
            // LogType: Browser, Server, Driver, Client, Performance
            // and Profiler
            logPrefs.enable(LogType.BROWSER, Level.ALL);
            cOptions.setCapability("goog:loggingPrefs", logPrefs);
            options.merge(cOptions);

            options.setCapability("LT:Options", ltOptions);
        }
        if (null != options) {
            options = options.merge(options);
        }

        try {
            if (null != options) {
                if (platform.equalsIgnoreCase("ios")) {
                    DRIVER_THREAD_LOCAL.set(
                            new IOSDriver(
                                    new URI(TestConstants.DOCKER_GRID_URL)
                                            .toURL(),
                                    options));
                } else if (platform.equalsIgnoreCase("android")) {
                    DRIVER_THREAD_LOCAL.set(
                            new AndroidDriver(
                                    new URI(TestConstants.DOCKER_GRID_URL)
                                            .toURL(),
                                    options));
                } else {
                    DRIVER_THREAD_LOCAL.set(
                            new RemoteWebDriver(
                                    new URI(TestConstants.DOCKER_GRID_URL)
                                            .toURL(),
                                    options));
                }
            }

            // If the WebDriver has been set
            if (null != DRIVER_THREAD_LOCAL.get()) {
                // Set this below to ensure that files can be found when using
                // a dockerized grid
                DRIVER_THREAD_LOCAL.get()
                        .setFileDetector(new LocalFileDetector());
            }

        } catch (URISyntaxException e) {
            error("\nURI Syntax Exception while connecting to the "
                    + "Selenium GRID Hub\n" + e.getMessage());
        } catch (MalformedURLException e) {
            error("\nMalformed URL Exception while connecting to the "
                    + "Selenium GRID Hub\n" + e.getMessage());
        } catch (SessionNotCreatedException e) {
            if (!app.isEmpty()) {
                error("Selenium Grid was unable to create a session"
                        + " using the following capabilities: \n"
                        + "PlatformName = " + platform + "\n"
                        + "PlatformVersion = " + platformVersion + "\n"
                        + "DeviceName = " + deviceName + "\n"
                        + "App = " + app, e);
            } else {
                error("Selenium Grid was unable to create a session"
                        + " using the following capabilities: \n"
                        + "BrowserName = " + browser + "\n"
                        + "BrowserVersion = " + browserVersion + "\n"
                        + "PlatformName = " + platform + "\n"
                        + "PlatformVersion = " + platformVersion + "\n"
                        + "DeviceName = " + deviceName + "\n", e);
            }
        }
        if (null != DRIVER_THREAD_LOCAL.get()) {
            info("Remote WebDriver has connected to the Grid");
        }
    }

    /**
     * Retrieve the LambdaTest Options for this Remote WebDriver instance.
     * @param platform The platform to test on.
     * @param platformVersion The version of the platform to test on.
     * @param deviceName The device name.
     * @param buildName The build name.
     * @param testName The test name.
     * @param projectName The project name.
     * @param app The path to the native app.
     * @return A {@link HashMap } containing the LambdaTest options.
     */
    private static HashMap<String, Object> getOptions(
            final String platform,
            final String platformVersion,
            final String deviceName,
            final String buildName,
            final String testName,
            final String projectName,
            final String... app) {

        HashMap<String, Object> options = new HashMap<>();
        options.put("w3c", true);
        options.put("platformName", platform);
        options.put("deviceName", deviceName);
        options.put("platformVersion", platformVersion);
        options.put("build", (TestConstants.DEPLOYMENT_RUN)
                ? "Deploy_" + buildName
                : buildName);
        options.put("name", testName);
        options.put("project", projectName);
        options.put("deviceOrientation", "portrait");
        options.put("autoGrantPermissions", true);
        options.put("autoAcceptAlerts", true);
        options.put("console", true);
        options.put("visual", true);
        options.put("terminal", true);

        // Only set the App path if it was passed in.
        if (app.length > 0) {
            options.put("app", app[0]);
        }

        return options;
    }
}
