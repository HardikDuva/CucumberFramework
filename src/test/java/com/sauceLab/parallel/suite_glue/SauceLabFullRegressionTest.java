package com.sauceLab.parallel.suite_glue;

import com.sauceLab.models.ProductURLs;
import com.sauceLab.utilities.*;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.sauceLab.utilities.TestConstants.*;
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

	private String clientName = null;
	private String browserName = null;

    /**
     * The variables that pertain to the specifications of this test, that 
     * must be set prior to execution, are set in this method.
     * @param browser The name of the browser.
     * @param client The name of the client.
     */
    @Parameters({
            "BROWSER",
			"CLIENT"})
	@BeforeSuite
	private void setVariablesForTest(
            final String browser,
			final String client) {

		this.clientName = client;
		this.browserName = browser;
		// set the browser for this set of tests
		info("I am setting the browser for this suite of tests");

		RemoteWebDriverFactory.setCapabilities(
				browser.toLowerCase(),
				suiteName);

        FrameworkConfig.init(System.getProperty("user.dir")
                + "/dependencies/FW_Config/FW_Config.properties");

		UserDetailsConfig.init(System.getProperty("user.dir")
				+ "/dependencies/client/" + client + "/user.properties");

		// set the url for these tests
		ProductURLs.setCurrentProductEnvironment(PRODUCT_URL);

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
    private void sendReport() throws IOException {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM_dd_uuuu");
		LocalDateTime localDateTime = LocalDateTime.now();

		String folderPath = System.getProperty("user.dir") + "/_reports";
		String logsFolderPath = System.getProperty("user.dir") + "/_logs";
        String archivePath = String.valueOf(Paths
                .get(System.getProperty("user.dir")))
                .replace("\\", "/") + "/archive/";

		String destinationDirectory = archivePath
				+ ProductURLs.getProductName() + "/"
				+ dtf.format(localDateTime) + "/"
				+ RemoteWebDriverFactory.getBrowser();

		if(SEND_EMAIL) {
			//Create Zip Folder
			String zipFilePath = folderPath + ".zip";
			Path targetDir = Path.of(folderPath);
			FileSystemConnector.zipDirectory(targetDir,Path.of(zipFilePath));

			//Send Report
			EmailConnector emailConnector = new EmailConnector();

			String subject = "Execution Report for the client " + this.clientName;
			String bodyContent = "Please Find Attached Execution Report with" +
					"\n Browser : " + this.browserName +
					"\n Time    : " + DateTimeConnector.getTimeStampWithLocaleEnglish();

			File tempFile = new File(zipFilePath);

			emailConnector.sendEmailWithAttachment(EMAIL_TO,subject,bodyContent,tempFile);
			FileSystemConnector.deleteFile(zipFilePath);
		}

		// Save Report to archive
		FileSystemConnector.copyFolder(folderPath,
				destinationDirectory + "/reports");
		// Save Logs to archive
		FileSystemConnector.copyFolder(logsFolderPath,
				destinationDirectory + "/logs");

		//Delete old execution report
		FileSystemConnector.deleteDir(folderPath);
		FileSystemConnector.deleteDir(logsFolderPath);


	}
}