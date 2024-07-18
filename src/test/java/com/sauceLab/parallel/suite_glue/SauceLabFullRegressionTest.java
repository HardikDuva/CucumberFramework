package com.sauceLab.parallel.suite_glue;

import com.sauceLab.models.ProductURLs;
import com.sauceLab.utilities.FrameworkConfig;
import com.sauceLab.utilities.RemoteWebDriverFactory;
import com.sauceLab.utilities.UserDetailsConfig;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import com.sauceLab.utilities.FileSystemConnector;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.sauceLab.utilities.TestLogger.info;

@CucumberOptions(
		plugin = {"html:_reports/full_regression/full_regression-html-report.html",
				"json:_reports/full_regression/full_regression.json",
				"pretty"},
		glue = {"com.sauceLab.parallel"},
		features = {"src/test/resources/TestCases"},
		tags = "@full_regression"
)

public class SauceLabFullRegressionTest extends AbstractTestNGCucumberTests {

    /**
     * The name of the suite.
     */
    private final String suiteName = "full_regression";

    /**
     * The variables that pertain to the specifications of this test, that 
     * must be set prior to execution, are set in this method.
     * @param browser The name of the browser.
     * @param client The name of the client.
     */
    @Parameters({
            "BROWSER",
            "BROWSER_VERSION",
			"CLIENT",
            "PRODUCT_URL"})
	@BeforeSuite
	private void setVariablesForTest(
            final String browser,
			final String client,
			final String productURL) {

		// set the browser for this set of tests
		info("I am setting the browser for this suite of tests");

		RemoteWebDriverFactory.setCapabilities(
				browser.toLowerCase(),
				suiteName);

		UserDetailsConfig.init(System.getProperty("user.dir")
				+ "/dependencies/client" + client + "/fw_config/user.properties");

        FrameworkConfig.init(System.getProperty("user.dir")
                + "/dependencies/fw_config/fw_config.properties");

		// set the url for these tests
		ProductURLs.setCurrentProductEnvironment(productURL);

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
				+ ProductURLs.getProductName() + "/"
				+ dtf.format(localDateTime) + "/"
				+ RemoteWebDriverFactory.getBrowser();

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