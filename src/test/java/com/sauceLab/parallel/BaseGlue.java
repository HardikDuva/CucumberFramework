package com.sauceLab.parallel;

import com.sauceLab.pages.sauceLab.login.LoginPage;
import com.sauceLab.utilities.RemoteWebDriverFactory;
import io.cucumber.java.en.Given;
import org.slf4j.MDC;

import static com.sauceLab.utilities.TestLogger.info;

public class BaseGlue {

    /**
     * The local instance of the System
     * Under test.
     */
    private final SystemEnvironment sauceLabObj;

    /**
     * There should be no instance of this class.
     * @param sysEnv The environment.
     */
    public BaseGlue(final SystemEnvironment sysEnv) {
        this.sauceLabObj = sysEnv;

    }

    /**
     * Initialize the framework including the logger and
     * the WebDriver.
     * @param id The Test id (ex. TEST-0000)
     */
    @Given("^I Initialize the framework with \"([^\"]*)\"$")
    public void initializeGlue(final String id) {
        MDC.put("test-case-id", id + "_"
                + RemoteWebDriverFactory.getBrowser());
        RemoteWebDriverFactory.setTestName(id);

        sauceLabObj.setDriver(RemoteWebDriverFactory
                .getDriver());

        sauceLabObj.setWidth(sauceLabObj
                .getDriver()
                .manage()
                .window()
                .getSize()
                .getWidth());
        sauceLabObj.setHeight(sauceLabObj
                .getDriver()
                .manage()
                .window()
                .getSize()
                .getHeight());
        info("Framework Initialized and Log file Name set to : ["
                + id + "]");

        sauceLabObj.getDriver()
                .navigate()
                .to(sauceLabObj.getProductUrl());

        sauceLabObj.setLoginPage(new LoginPage(sauceLabObj
                .getDriver()));
    }

}