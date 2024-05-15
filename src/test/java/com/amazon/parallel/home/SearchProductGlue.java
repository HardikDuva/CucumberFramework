package com.amazon.parallel.home;

import com.amazon.parallel.SystemEnvironment;
import io.cucumber.java.en.When;

import static org.junit.Assert.*;

public class SearchProductGlue {

    /**
     * The local reference to the environment.
     */
    private final SystemEnvironment executeObj;

    /**
     * Glue Object.
     * @param sysEnv The reference to the environment.
     */
    public SearchProductGlue(final SystemEnvironment sysEnv) {
        this.executeObj = sysEnv;
    }

    /**
     * The user search the product
     */
    @When("^I search \"([^\"]*)\" product$")
    public void testSearchProductGlue1(String product) {
        executeObj.getHomePage()
                .addProductIntoSearchBar(product);

        executeObj.setProductSearchResultPagePage(
                executeObj.getHomePage()
                .searchButton());
    }

    /**
     * The user can see searched result for the product
     */
    @When("^I can see searched result for the \"([^\"]*)\"$")
    public void testSearchProductGlue2(String product) {
        assertTrue("Product is not displayed in searched result page",
                executeObj.getProductSearchResultPagePage().
                isProductDisplayedInSearchedList(product));

    }

}
