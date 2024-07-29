package com.sauceLab.utilities;

public final class TestConstants {

	/**
	 * There should be no instance of this class.
	 */
	private TestConstants() { }

	/**
	 * The PRODUCT URL
	 */
	public static final String PRODUCT_URL
			= UserDetailsConfig.get("PRODUCT_URL");

	/**
	 * The UserName
	 */
	public static final String USERNAME
			= UserDetailsConfig.get("UserName");

	/**
	 * The HOMEPAGEURL
	 */
	public static final String HOMEPAGEURL
			= UserDetailsConfig.get("HomePageURL");

	/**
	 * The Password
	 */
	public static final String PASSWORD
			= UserDetailsConfig.get("Password");

	/**
	 * The
	 */
	public static final String EMAIL_USERNAME
			= FrameworkConfig.get("EMAIL_USERNAME");

	/**
	 * The
	 */
	public static final String EMAIL_PASSWORD
			= FrameworkConfig.get("EMAIL_PASSWORD");

	/**
	 * The
	 */
	public static final String EMAIL_TO
			= FrameworkConfig.get("EMAIL_TO");

	/**
	 * Condition Red there are failures.
	 */
	public static final String RED
			= "#ef5350";

	/**
	 * Condition Green all is good.
	 */
	public static final String GREEN
			= "#00c853";

	/**
	 * Condition Orange there are some problems.
	 */
	public static final String ORANGE
			= "#ff8c00";

	/**
	 * Condition Blue all is well.
	 */
	public static final String BLUE
			= "#0078FF";

	/***
	 * Wait for 2 seconds.
	 */
	public static final int WAIT_STANDARD
			= Integer.parseInt(
			FrameworkConfig.get("WAIT_STANDARD"));

	/***
	 * Explicit wait of 30 seconds (WebDriver variable).
	 */
	public static final int WAIT_EXPLICIT
			= Integer.parseInt(
			FrameworkConfig.get("WAIT_EXPLICIT"));

	/**
	 * System.getProperty("user.dir")
	 * + "dependencies/suites/test_artefacts/".
	 */
	public static final String TEST_ASSETS_FOLDER_PATH
			= FileSystemConnector.reconcilePathToFile(
			FrameworkConfig.get("TEST_ASSETS_FOLDER_PATH"));

	/**
	 * System.getProperty("user.dir") + "reports/screenshots/".
	 */
	public static final String SCREENSHOTS_FOLDER_PATH
			= FileSystemConnector.reconcilePathToFile(
			FrameworkConfig.get("SCREENSHOTS_FOLDER_PATH"));

	/**
	 * The Selenium GRID URL used to route tests to waiting nodes.
	 */
	public static final String DOCKER_GRID_URL
			= FrameworkConfig.get("DOCKER_GRID_URL");

	/**
	 * The Selenium GRID URL used to route tests to waiting nodes.
	 */
	public static final boolean SEND_EMAIL
			= Boolean.parseBoolean(FrameworkConfig.get("SEND_EMAIL"));

}
