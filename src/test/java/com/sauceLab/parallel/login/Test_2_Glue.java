package com.sauceLab.parallel.login;

import com.sauceLab.parallel.SystemEnvironment;
import io.cucumber.java.en.When;

import static com.sauceLab.utilities.TestConstants.*;
import static org.junit.Assert.assertTrue;

public class Test_2_Glue {

    /**
     * The local reference to the environment.
     */
    private final SystemEnvironment sauceLabObj;

    /**
     * Glue Object.
     * @param sysEnv The reference to the environment.
     */
    public Test_2_Glue(final SystemEnvironment sysEnv) {
        this.sauceLabObj = sysEnv;
    }

    /**
     * The user can see error on login page
     */
    @When("^I should see validation error on login page$")
    public void loginWithValidCredentials() {

        sauceLabObj.getLoginPage().enterUsername(USERNAME)
                .enterPassword(PASSWORD)
                .clickOnLoginInButton();
    }


}
