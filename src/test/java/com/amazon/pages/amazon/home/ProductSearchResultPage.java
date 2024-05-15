package com.amazon.pages.amazon.home;

import com.amazon.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

import static com.amazon.utilities.TestLogger.info;

public class ProductSearchResultPage extends BasePage {

	@FindBy(xpath = "//span[@data-component-type=\"s-result-info-bar\"]" +
			"/div/h1/div/div/div/div/span[contains(@class,'a-text-bold')]")
	private WebElement searchedResultLabel;

	@FindBy(xpath = "//div[contains(@data-cel-widget,'search_result')]")
	private List<WebElement> searchResultProductList;

	public ProductSearchResultPage(WebDriver _driver) {
		super(_driver);
	}

	/**
	 * Return Search Label
	 * @return {@link String }
	 */
	public String getSearchResultLabel() {
		info("Retrieving the Search Label");
		waitForVisibility(searchedResultLabel);
		return getText(searchedResultLabel);
	}

	//Below method is just for the learning purpose
	//we can add more criteria and logic into that
	/**
	 * Determine if the product displayed in searched list
	 * @param expProductName The Product Name which need to be checked
	 * @return {@link Boolean }
	 */
	public Boolean isProductDisplayedInSearchedList(final String expProductName) {
		info("Checking if the product displayed in searched list with name " + expProductName);
		for (WebElement product : searchResultProductList) {
			scrollIntoView(product);
			String productNameXPath = "./div//h2/a/span";

			String actProductName = getText(findElementInChildren(product,
					By.xpath(productNameXPath)));

			if (actProductName.toLowerCase()
					.contains(expProductName)) {
				info("Product name " + expProductName +" found in searched list ");
				return true;
			}
		}
		return false;
	}

}
