package com.amazon.parallel;

import com.amazon.models.User;
import com.amazon.pages.amazon.home.HomePage;
import com.amazon.utilities.RemoteWebDriverFactory;
import com.amazon.utilities.RemoteWebDriverFactoryMobile;
import io.cucumber.java.en.Given;
import org.slf4j.MDC;

import static com.amazon.utilities.TestLogger.info;

public class BaseGlue {

    /**
     * The local instance of the System
     * Under test.
     */
    private final SystemEnvironment executeObj;

    /**
     * The potential number of staggers to make.
     */
    private static int staggerCounter;

    /**
     * The maximum number of stagger starts.
     */
    private final int maxStaggers = 4;

    /**
     * There should be no instance of this class.
     * @param sysEnv The environment.
     */
    public BaseGlue(final SystemEnvironment sysEnv) {
        this.executeObj = sysEnv;
        staggerCounter = maxStaggers;
    }

    /**
     * Initialize the framework including the logger and
     * the WebDriver.
     * @param id The Test id (ex. TEST-0000)
     */
    @Given("^I Initialize the framework with \"([^\"]*)\"$")
    public void initializeGlue(final String id) {

        boolean isMobileTest = !RemoteWebDriverFactoryMobile
                .getDeviceName()
                .isEmpty();
        if (isMobileTest) {
            MDC.put("test-case-id", id + "_"
                    + RemoteWebDriverFactoryMobile.getBrowser());
            RemoteWebDriverFactoryMobile.setTestName(id);
            executeObj.setDriver(RemoteWebDriverFactoryMobile
                    .getDriver());
        } else {
            MDC.put("test-case-id", id + "_"
                    + RemoteWebDriverFactory.getBrowser());
            RemoteWebDriverFactory.setTestName(id);
            executeObj.setDriver(RemoteWebDriverFactory
                    .getDriver());
        }

        executeObj.getDriver()
                .navigate()
                .to(executeObj.getTestUrl());

        executeObj.setWidth(executeObj
                .getDriver()
                .manage()
                .window()
                .getSize()
                .getWidth());
        executeObj.setHeight(executeObj
                .getDriver()
                .manage()
                .window()
                .getSize()
                .getHeight());
        info("Framework Initialized and Log file Name set to : ["
                + id + "]");
        executeObj.setHomePage(new HomePage(executeObj
                .getDriver()));

    }

    /**
     * I am anonymous user on Amazon Home Page
     */
    @Given("^I am anonymous user on Amazon Home Page$")
    public void loginAsaNewUser() {
        executeObj.setTestUser(new User());
        executeObj.setTestUser(executeObj
                .getTestUser()
                .anonymous());
    }

}