package com.amazon.parallel.suite_glue.desktop;

import com.amazon.models.ProductURLs;
import com.amazon.utilities.FrameworkConfig;
import com.amazon.utilities.RemoteWebDriverFactory;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import com.amazon.utilities.FileSystemConnector;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.amazon.models.ProductURLs.AMAZON_PROD;

@CucumberOptions(
        plugin = {"html:_reports/smoke_regression/smoke_regression-html-report.html",
                "json:_reports/smoke_regression/smoke_regression.json",
                "pretty"},
        glue = {"com.amazon.parallel"},
        features = {"src/test/resources/TestCases"},
        tags = "@smoke_regression"
)

public class AmazonPCSmokeTest extends AbstractTestNGCucumberTests {

    /**
     * The name of the suite.
     */
    private final String suiteName = "smoke_regression";

    /**
     * The variables that pertain to the specifications of this test, that 
     * must be set prior to execution, are set in this method.
     * @param browser The name of the browser.
     * @param browserVersion The version of the browser.
     * @param platform The name of the platform.
     * @param platformVersion The version of the platform.
     */
    @Parameters({
            "BROWSER",
            "BROWSER_VERSION",
            "PLATFORM",
            "PLATFORM_VERSION"})
    @BeforeSuite
    private void setVariablesForTest(
            final String browser,
            final String browserVersion,
            final String platform,
            final String platformVersion) {

        RemoteWebDriverFactory.setCapabilities(
                browser.toLowerCase(),
                browserVersion.toLowerCase(),
                platform.toLowerCase(),
                platformVersion.toLowerCase(),
                suiteName);
        FrameworkConfig.init(System.getProperty("user.dir")
                + "/dependencies/fw_config/fw_config.properties");

        // set the url for these tests
        ProductURLs.setCurrentProductEnvironment(AMAZON_PROD);

    }

    /**
     * Converts all Scenarios into TestNG tests and treats this method as a 
     * data provider, so we can parallelize the execution of the cucumber 
     * tests.
     * @return This method returns an object array of objects (a 
     * two-dimensional array) of objects. Each object represents one scenario
     * / test. 
     */
    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }

    /**
     * Finalize and flush the report.
     */
    @AfterSuite
    private void sendReport() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM_dd_uuuu");
        LocalDateTime localDateTime = LocalDateTime.now();

        String folderPath = System.getProperty("user.dir") + "/_reports";
        String logsFolderPath = System.getProperty("user.dir") + "/_logs";
        String archivePath = String.valueOf(Paths
                .get(System.getProperty("user.dir"))
                .getParent())
                .replace("\\", "/") + "/archive/";
        String destinationDirectory = archivePath
                + ProductURLs.getCurrentVersion() + "/"
                + dtf.format(localDateTime) + "/"
                + RemoteWebDriverFactory.getBrowser();

        //SendReportEmail.sendReportEmail(suiteName, destinationDirectory);

        // Save Report to archive
        FileSystemConnector.copyFolder(folderPath,
                destinationDirectory + "/reports");
        // Save Logs to archive
        FileSystemConnector.copyFolder(logsFolderPath,
                destinationDirectory + "/logs");

        //Delete old execution report
        FileSystemConnector.deleteDir(folderPath);
        FileSystemConnector.deleteDir(destinationDirectory + "/logs");

    }
}
