package com.amazon.parallel;

import com.amazon.models.ProductURLs;
import com.amazon.pages.amazon.AmazonNavMenu;
import com.amazon.pages.amazon.home.HomePage;
import com.amazon.pages.amazon.home.ProductSearchResultPage;
import com.amazon.utilities.TestConstants;
import com.amazon.utilities.RemoteWebDriverFactoryMobile;
import com.amazon.utilities.RemoteWebDriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import com.amazon.models.User;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

import static com.amazon.utilities.TestLogger.info;
import static com.amazon.utilities.TestLogger.warn;

public class SystemEnvironment {

    /**
     * There should be no instance of this class that we create.
     * However, Cucumber will create one and so this constructor needs to be
     * public.
     */
    public SystemEnvironment() { }

    /**
     * The name of this feature file.
     */
    private String featureName;

    /**
     * The User class obj.
     */
    User testUser;

    /**
     * The name of user.
     */
    public String userName;

    /**
     * The log of this test.
     */
    private String log;

    /**
     * The start time of the test.
     */
    private LocalTime testStartTime;

    // Shared Parameters - Parameters that will be needed between tests

    /**
     * The width of the viewport.
     */
    private int width = 0;

    /**
     * The height of the viewport.
     */
    private int height = 0;

    /**
     * The Web Driver for this test.
     */
    private WebDriver driver;

    /**
     * The environment where the tests are being executed.
     */
    private final String testUrl = ProductURLs.getTestEnvironment();

    /**
     * The randomly generated username for this test.
     */
    private String createdUsername;

    /**
     * Amazon PAGE.
     */
    private AmazonNavMenu amazonNavMenu;

    /**
     * Retrieve the Amazon Menu.
     * @return A {@link AmazonNavMenu }
     */
    public AmazonNavMenu getLmsMenu() {
        return amazonNavMenu;
    }

    /**
     * Set the Amazon Menu.
     * @param page The page.
     */
    public void setLmsMenu(final AmazonNavMenu page) {
        this.amazonNavMenu = page;
    }

    /**
     * Retrieve the Test URL.
     * @return A {@link String } containing the Test URL.
     */
    public String getTestUrl() {
        return testUrl;
    }

    /**
     * Retrieve the WebDriver.
     * @return A {@link WebDriver }
     */
    public WebDriver getDriver() {
        return driver;
    }

    /**
     * Set the Driver for this Test.
     * @param webDriver The WebDriver used in this test.
     */
    public void setDriver(final WebDriver webDriver) {
        this.driver = webDriver;
    }

    /**
     * Set the Width of the viewport.
     * @param viewportWidth The width.
     */
    public void setWidth(final int viewportWidth) {
        this.width = viewportWidth;
    }

    /**
     * Set the Height of the viewport.
     * @param viewportHeight The height of the viewport.
     */
    public void setHeight(final int viewportHeight) {
        this.height = viewportHeight;
    }

    /**
     * Retrieve the Test User.
     * @return A {@link User } set as the test uesr.
     */
    public User getTestUser() {
        return testUser;
    }

    /**
     * Set the Test User.
     * @param user The user.
     */
    public void setTestUser(final User user) {
        this.testUser = user;
    }

    /**
     * Amazon PAGE.
     */
    private HomePage homePage;

    /**
     * Amazon PAGE.
     */
    private ProductSearchResultPage productSearchResultPage;

    /**
     * Retrieve the Home Page.
     * @return A {@link HomePage }
     */
    public HomePage getHomePage() {
        return homePage;
    }

    /**
     * Set the ProductSearchResultPage Page.
     * @param page The page.
     */
    public void setProductSearchResultPagePage(final ProductSearchResultPage page) {
        this.productSearchResultPage = page;
    }

    /**
     * Retrieve the ProductSearchResultPage Page.
     * @return A {@link HomePage }
     */
    public ProductSearchResultPage getProductSearchResultPagePage() {
        return productSearchResultPage;
    }

    /**
     * Set the Home Page.
     * @param page The page.
     */
    public void setHomePage(final HomePage page) {
        this.homePage = page;
    }

    /**
     * This method is called before each scenario executes. It sets the
     * initial username of the Test User, Creates the Framework log and a log
     * for this scenario, logs the metadata of the scenario to the log, and
     * logs the start time of the test.
     * @param scenario The {@link Scenario } that is just about to begin.
     */
    @Before
    public void beforeScenario(final Scenario scenario) {
        final int max = 250000;
        final int min = 0;

        // get the MM_DD_YYYY for today
        DateTimeFormatter dtfmonth = DateTimeFormatter.ofPattern("MM_dd_uuuu");
        LocalDateTime localDateTime = LocalDateTime.now();
        double formulaResult = ((Math.random() * ((max - min) + 1)) + min);
        createdUsername = dtfmonth.format(localDateTime)
                + "_"
                + Math.round(formulaResult)
                + "_"
                + Math.round(formulaResult);

        // Log test details into an HTML table to display in the HTML report
        log = "<h2>Framework Console Logs</h2>";
        log += "</br><span><table><tbody>";
        log += "<tr><th><b>Test Data</b></th></tr>";

        // if you are a mobile test you will have a device name
        // otherwise you are a browser test
        if (RemoteWebDriverFactoryMobile.getDeviceName().isEmpty()) {
            // This is a WebDriver Test
            // set the browsername, version, platform name and version
            log += "<tr><td>Browser Name :</td><td>"
                    + RemoteWebDriverFactory.getBrowser()
                    + "</td></tr>";
            log += "<tr><td>Browser Version :</td><td>"
                    + RemoteWebDriverFactory.getBrowserVersion()
                    + "</td></tr>";
            log += "<tr><td>Platform Name :</td><td>"
                    + RemoteWebDriverFactory.getPlatform()
                    + "</td></tr>";
            log += "<tr><td>Platform Version :</td><td>"
                    + RemoteWebDriverFactory.getPlatformVersion()
                    + "</td></tr>";
        } else {
            if (RemoteWebDriverFactoryMobile.getApp().isEmpty()) {
                // This is an Appium Browser Test
                // set the browsername and version
                log += "<tr><td>Browser Name :</td><td>"
                        + RemoteWebDriverFactoryMobile.getBrowser()
                        + "</td></tr>";
                log += "<tr><td>Browser Version :</td><td>"
                        + RemoteWebDriverFactoryMobile.getBrowserVersion()
                        + "</td></tr>";
            } else {
                // This is an Appium Application Test
                // set the app
                log += "<tr><td>Application Path :</td><td>"
                        + RemoteWebDriverFactoryMobile.getApp()
                        + "</td></tr>";
            }
        }

        log += "<tr><td>User Name :</td><td>" + createdUsername + "</td></tr>";

        featureName = "TEST-" + String.valueOf(scenario.getUri())
                .split("TEST-")[1].split("\\.")[0];

        if (RemoteWebDriverFactoryMobile.getDeviceName().isEmpty()) {
            featureName = featureName
                    + "_"
                    + RemoteWebDriverFactory.getBrowser()
                    + "_"
                    + dtfmonth.format(localDateTime);
        } else {
            if (RemoteWebDriverFactoryMobile.getApp().isEmpty()) {
                featureName = featureName
                        + "_"
                        + RemoteWebDriverFactoryMobile.getBrowser()
                        + "_"
                        + dtfmonth.format(localDateTime);
            } else {
                featureName = featureName
                        + "_"
                        + RemoteWebDriverFactory.getBrowser()
                        + "_"
                        + dtfmonth.format(localDateTime);
            }
        }

        // The test begins now
        testStartTime = LocalTime.now();
    }

    /**
     * This method is called after each scenario. It cleans up the Remote
     * WebDriver, collects all logs and takes a screenshot if required then
     * packages that all up in the Cucumber Report.
     * @param scenario The {@link Scenario } that just ended.
     */
    @After
    public void afterScenario(final Scenario scenario) {
        String consoleLogs = captureConsoleLogs();
        // report the status of the scenario to standard output
        info("\n>>>>>> Feature : [" + featureName.split("_")[0] + "]"
                + "\n>>>>>> Scenario : [" + scenario.getName() + "]"
                + " "
                + "\n>>>>>> Status :[" + scenario.getStatus() + "]\n");

        if (scenario.isFailed()) {
            // Take a screenshot and embed it in the Cucumber Report
            if (null != driver && RemoteWebDriverFactoryMobile
                    .getDeviceName()
                    .isEmpty()) {
                final byte[] screenshot = ((TakesScreenshot) driver)
                        .getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png",
                        scenario.getName());

                // Log the current URL into the HTML table
                log += "<tr><td>URL :</td><td>"
                        + driver.getCurrentUrl()
                        + "</td></tr>";

                // if a Test User was created for this test

                if (null != testUser) {
                    // log the test user's name into the table
                    log += "<tr><td>TestUser name :</td><td>"
                            + testUser.userName
                            + "</td></tr>";
                }

                // indicate that a screenshot was captured in the
                // standard output
                info("The Scenario : " + scenario.getName()
                        + " has failed. A screenshot was attempted "
                        + "and if successful, was embedded in the Cucumber "
                        + "Report.");

            } else {
                if (null != driver) {
                    final byte[] screenshot = ((TakesScreenshot) driver)
                            .getScreenshotAs(OutputType.BYTES);

                    // remove any line breaks in the file (/r or /n)
                    System.out.println(Arrays.toString(screenshot));
                    scenario.attach(screenshot, "image/png",
                            scenario.getName());
                }
            }
        }

        // the driver should absolutely be quit by now, if not, quit it
        if (null != driver) {
            driver.quit();
        }

        // The test has ended
        LocalTime testStopTime = LocalTime.now();

        // Log the test times to the HTML table for the html report
        log += logMetaDataToReport(testStartTime, testStopTime, width, height);

        String path = System.getProperty("user.dir") + "/_logs/Test-Case-Logs/"
                + featureName + ".html";

        // Read the HTML Framework log file for this test into memory and
        // parse it for the table of data it holds
        File file = new File(path);

        Scanner scan = null;
        boolean output = false;
        StringBuilder outputString = new StringBuilder();
        try {
            if (file.exists()) {
                scan = new Scanner(file);
                while (scan.hasNext()) {
                    String line = scan.nextLine();
                    // find the line in the file where the html begins
                    // (should be the first line)
                    if (line.toLowerCase().contains("<html>")) {
                        // start outputting the table to a string
                        output = true;
                    }
                    if (output) {
                        // Implicitly set the font to black on the
                        // Cucumber report
                        if (line.toLowerCase()
                                .contains("tr.even { background: #ffffff; }")) {
                            line = line
                                    .toLowerCase()
                                    .replace("tr.even { "
                                                    + "background: "
                                                    + "#ffffff; }",
                                            "tr.even { background: "
                                                    + "#ffffff; color: "
                                                    + "#000000; }");
                        }
                        if (line.toLowerCase()
                                .contains("tr.odd { background: #eaeaea; }")) {
                            line = line
                                    .toLowerCase()
                                    .replace("tr.odd { "
                                            + "background: #eaeaea; }",
                                            "tr"
                                                    + ".odd { background: "
                                                    + "#eaeaea; color: "
                                                    + "#000000; }");
                        }
                        // add every line of the table until we reach the
                        // end of the file
                        outputString.append(line);
                        if (line.toLowerCase().contains("</html>")) {
                            // stop outputting the table to a string
                            output = false;
                        }
                    }
                }
            } else {
                warn("The path : [" + path + "] does not exist. The logs "
                        + "will not be added to the report for this test.");
            }
        } catch (FileNotFoundException e) {
            warn("File Not Found Exception thrown while reading"
                            + " log file [" + path + "] to memory.", e);
        }
        // Attach the framework log to the scenario log for the html report
        log += "</br><span>" + outputString + "</span>";
        // output the scenario logs to the logger
        scenario.log(log);
        scenario.log(consoleLogs);
    }

    /**
     * Capture the Console Logs from a Desktop Browser and if capable
     * a mobile browser.
     * @return A {@link String } containing the console logs from the current
     * tests browser.
     */
    public String captureConsoleLogs() {
        assert (null != driver);
        LogEntries logs;
        logs = driver.manage().logs().get(LogType.BROWSER);
        StringBuilder compiledConsoleLogs = new StringBuilder();
        info("Size of log entries : " + logs.getAll().size());

        compiledConsoleLogs
                .append("<h2>Browser Console Logs</h2></br><span><ul>");

        if (logs.getAll().isEmpty()) {
            compiledConsoleLogs.append("<p>There were no browser console "
                    + "logs to add</p>");
        }

        for (LogEntry logEntry : logs.getAll()) {
            info(new Date(logEntry.getTimestamp()) + " " + logEntry.getLevel() + " "
                    + logEntry.getMessage());
            String openingListItemTag = "<li";

            if (logEntry.toString().contains("[SEVERE]")) {
                openingListItemTag += " style=\"color: " + TestConstants.RED
                        + "\">";
            } else if (logEntry.toString().contains("[WARNING]")) {
                openingListItemTag += " style=\"color: " + TestConstants.ORANGE
                        + "\">";
            } else if (logEntry.toString().contains("[FINE]")) {
                openingListItemTag += " style=\"color: " + TestConstants.GREEN
                        + "\">";
            } else if (logEntry.toString().contains("[INFO]")) {
                openingListItemTag += " style=\"color: " + TestConstants.BLUE
                        + "\">";
            }

            compiledConsoleLogs.append(openingListItemTag)
                    .append("<p>")
                    .append(new Date(logEntry.getTimestamp()))
                    .append(" ")
                    .append(logEntry.getLevel())
                    .append(" ")
                    .append(logEntry.getMessage())
                    .append("</p>")
                    .append("</li>");
        }

        compiledConsoleLogs.append("</ul></span>");

        return compiledConsoleLogs.toString();
    }

    /**
     * Logs the provided metadata to the HTML report.
     * @param testStart The start time of this scenario.
     * @param testStop The stop time of this scenario.
     * @param viewportWidth The viewportWidth of the browser/device viewport.
     * @param viewportHeight The viewportHeight of the browser/device viewport.
     * @return A {@link String } containing the formatted metadata ready for
     * embedding in the report.
     */
    private String logMetaDataToReport(final LocalTime testStart,
                                       final LocalTime testStop,
                                       final int viewportWidth,
                                       final int viewportHeight) {
        return "<tr><th><b>Test Times</b></th></tr>"
                + "<tr><td>Start Time :</td><td>"
                + testStart
                + "</td></tr>"
                + "<tr><td>Stop  Time :</td><td>"
                + testStop
                + "</td></tr>"
                + "<tr><th><b>Browser Dimensions</b></th></tr>"
                + "<tr><td>Width :</td><td>"
                + viewportWidth
                + "</td></tr>"
                + "<tr><td>Height :</td><td>"
                + viewportHeight
                + "</td></tr>"
                + "</tbody></table></span>";
    }
}
