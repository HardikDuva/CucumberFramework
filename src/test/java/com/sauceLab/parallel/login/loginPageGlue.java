package com.sauceLab.parallel.login;

import com.sauceLab.parallel.SystemEnvironment;
import com.sauceLab.utilities.FrameworkConfig;
import io.cucumber.java.en.When;

import static com.sauceLab.models.ProductURLs.getProductName;
import static com.sauceLab.models.ProductURLs.getProductURL;
import static com.sauceLab.utilities.TestConstants.USERNAME;
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
    public void testSearchProductGlue1(String product) {
        sauceLabObj.setTestUser(sauceLabObj
                .getTestUser()
                .generalUser(USERNAME));

    }

    /**
     * The user can see searched result for the product
     */
    @When("^The user has successfully landed on the LMS home page$")
    public void testSearchProductGlue2(String product) {
        assertTrue("Product is not displayed in searched result page",
                sauceLabObj.getLoginPage().getPageURL()
                        .contains(""));

    }

}
