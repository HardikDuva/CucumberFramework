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
     * The user search the product
     */
    @When("^I try to login with valid email and password$")
    public void loginWithValidCredentials() {
        sauceLabObj.setUsername(USERNAME);

        sauceLabObj.getLoginPage().enterUsername(USERNAME)
                .enterPassword(PASSWORD)
                .clickOnLoginInButton();

    }

    /**
     * The user can see searched result for the product
     */
    @When("^The user has successfully landed on the LMS home page$")
    public void userIsOnHomePage() {
        assertTrue("Product is not displayed in searched result page",
                sauceLabObj.getLoginPage()
                .getPageURL().contains(HOMEPAGEURL));
    }

}
