package com.amazon.pages.amazon;

import com.amazon.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static com.amazon.utilities.TestLogger.info;
import static org.junit.Assert.fail;

public class LoginPage extends BasePage {

	@FindBy(xpath = "//*[@id=\"email\"]")
	private WebElement emailAddressField;
	
	@FindBy(xpath = "//*[@id=\"password\"]")
	private WebElement passwordField;

	public LoginPage(WebDriver _driver) {
		super(_driver);
	}

	/**
	 * Enter the Email Address as 'emailAddress'
	 * @param emailAddress The email address
	 * @return {@link LoginPage }
	 */
	public LoginPage inputEmailAddress(String emailAddress) {
		info("Entering Email Address as " + emailAddress);
		waitForTextToBePresentInElement(By.xpath("//p[@id=\"login-card-content\"]")
				,"Welcome back. Please login by entering the information below.");
		waitForVisibility(emailAddressField);
		input(emailAddressField, emailAddress);
		waitStandard();
		return this;
	}

	/**
	 * Enter the Password as 'password'
	 * @param password The password
	 * @return {@link LoginPage }
	 */
	public LoginPage inputPassword(String password) {
		info("Entering Password");
		waitForVisibility(passwordField);
		input(passwordField, password);
		waitStandard();
		return this;
	}

}