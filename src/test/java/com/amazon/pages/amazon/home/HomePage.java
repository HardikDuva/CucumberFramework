package com.amazon.pages.amazon.home;

import com.amazon.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

import static com.amazon.utilities.TestLogger.debug;
import static com.amazon.utilities.TestLogger.info;

public class HomePage extends BasePage {

	@FindBy(xpath = "//*[@id=\"twotabsearchtextbox\"]")
	private WebElement searchBar;

	@FindBy(xpath = "//*[@id=\"nav-search-submit-text\"]")
	private WebElement searchButton;

	public HomePage(WebDriver _driver) {
		super(_driver);
	}

	/**
	 * Enter product label into search bar
     */
	public void addProductIntoSearchBar(String productName) {
		info("Add product label into search bar");
		waitForVisibility(searchBar);
		input(searchBar,productName);
		waitStandard();
	}

	/**
	 * Click on search button
	 * @return {@link ProductSearchResultPage }
     */
	public ProductSearchResultPage searchButton() {
		info("Click on search button");
		waitForVisibility(searchButton);
		click(searchButton);
		waitStandard();
		return new ProductSearchResultPage(driver);
	}

}
