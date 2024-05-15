package com.amazon.pages.amazon;

import com.amazon.pages.BasePage;
import com.amazon.pages.amazon.home.HomePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static com.amazon.utilities.TestLogger.info;

public class AmazonNavMenu extends BasePage {

	@FindBy(xpath = "//a[@id=\"nav-logo-sprites\"]")
	private WebElement amazonHomeLogoButton;

	public AmazonNavMenu(WebDriver _driver) {
		super(_driver);
	}

	/**
	 * Click the Amazon Home Logo Button.
	 * @return {@link HomePage}
	 */
	public HomePage clickHomeButton() {
		info("Clicking the Amazon Home Logo Button");
		click(amazonHomeLogoButton);
		waitStandard();
		return new HomePage(driver);
	}

}
