package com.sauceLab.pages.sauceLab.login;

import com.sauceLab.pages.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends BasePage {

	@FindBy(xpath = "//div[@class=\"login-box\"]//input[@id=\"user-name\"]")
	private WebElement usernameInputEle;

	@FindBy(xpath = "//div[@class=\"login-box\"]//input[@id=\"password\"]")
	private WebElement passwordInputEle;

	@FindBy(xpath = "//div[@class=\"login-box\"]//input[@id=\"login-button\"]")
	private WebElement loginButtonClickEle;

	public LoginPage(WebDriver _driver) {
		super(_driver);
	}

	/**
	 * The user enter username
	 */
	public LoginPage enterUsername(String userName) {
		waitForVisibility(usernameInputEle);
		input(usernameInputEle,userName);

		return this;
	}

	/**
	 * The user enter password
	 */
	public LoginPage enterPassword(String password) {
		waitForVisibility(passwordInputEle);
		input(passwordInputEle,password);

		return this;
	}

	/**
	 * The user click on Login button
	 */
	public LoginPage clickOnLoginInButton() {
		click(loginButtonClickEle);
		return this;
	}

}