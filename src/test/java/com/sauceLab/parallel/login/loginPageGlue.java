package com.sauceLab.parallel.login;

import com.sauceLab.parallel.SystemEnvironment;
import io.cucumber.java.en.When;

import static com.sauceLab.utilities.TestConstants.PASSWORD;
import static com.sauceLab.utilities.TestConstants.USERNAME;
import static com.sauceLab.utilities.TestConstants.HOMEPAGEURL;
import static org.junit.Assert.*;

public class loginPageGlue {

    /**
     * The local reference to the environment.
     */
    private final SystemEnvironment sauceLabObj;

    /**
     * Glue Object.
     * @param sysEnv The reference to the environment.
     */
    public loginPageGlue(final SystemEnvironment sysEnv) {
        this.sauceLabObj = sysEnv;
    }

    /**
     * Login with Valid username/email and password
     */
    @When("^I try to login with valid username/email and password$")
    public void loginWithValidCredentials() {
        sauceLabObj.setUsername(USERNAME);

        sauceLabObj.getLoginPage().enterUsername(USERNAME)
                .enterPassword(PASSWORD)
                .clickOnLoginInButton();
    }

    /**
     * Login with In-valid username/email and password
     */
    @When("^I try to login with In-valid username and password$")
    public void loginWithInValidCredentials() {
        sauceLabObj.setUsername(USERNAME);

        sauceLabObj.getLoginPage().enterUsername(USERNAME)
                .enterPassword(PASSWORD)
                .clickOnLoginInButton();
    }

    /**
     * The user can Log in successfully
     */
    @When("^The user should successfully landed on the LMS home page$")
    public void userIsLoggedIn() {
        assertTrue("User is not successfully landed on the LMS Home page",
                sauceLabObj.getLoginPage()
                .getPageURL().contains(HOMEPAGEURL));
    }

    /**
     * The user should not successfully logged-in
     */
    @When("^The user should not successfully landed on the LMS home page$")
    public void userIsNotLoggedIn() {
        assertFalse("User is successfully landed on the LMS Home page even " +
                        "invalid username & password",
                sauceLabObj.getLoginPage()
                        .getPageURL().contains(HOMEPAGEURL));
    }

}
