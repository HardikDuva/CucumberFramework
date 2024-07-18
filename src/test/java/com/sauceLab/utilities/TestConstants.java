package com.sauceLab.utilities;

public final class TestConstants {

	/**
	 * There should be no instance of this class.
	 */
	private TestConstants() { }

	/**
	 * The UserName
	 */
	public static final String USERNAME
			= UserDetailsConfig.get("UserName");

	/**
	 * The Password
	 */
	public static final String PASSWORD
			= UserDetailsConfig.get("Password");

	/**
	 * The
	 */
	public static final String EMAIL_STORE_TYPE
			= FrameworkConfig.get("EMAIL_STORE_TYPE");

	/**
	 * The
	 */
	public static final String EMAIL_HOST
			= FrameworkConfig.get("EMAIL_HOST");

	/**
	 * The
	 */
	public static final String EMAIL_PORT
			= FrameworkConfig.get("EMAIL_PORT");

	/**
	 * Whether this will be a deployment execution.
	 */
	public static final boolean DEPLOYMENT_RUN
			= FrameworkConfig
			.get("DEPLOYMENT_RUN")
			.equalsIgnoreCase("true");

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
	 * Implicit wait of 4 seconds (WebDriver variable).
	 */
	public static final int WAIT_IMPLICIT
			= Integer.parseInt(
			FrameworkConfig.get("WAIT_IMPLICIT"));

	/***
	 * Implicit wait of 20 seconds for pages to load.
	 */
	public static final int WAIT_IMPLICIT_PAGE
			= Integer.parseInt(
			FrameworkConfig.get("WAIT_IMPLICIT_PAGE"));

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

}
