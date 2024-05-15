package com.amazon.utilities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import static com.amazon.utilities.TestLogger.error;

public final class FrameworkConfig {

	/**
	 * The filepath to the configuration file.
	 */
	private static String filePath;

	/**
	 * The Configuration file data.
	 */
	private static Properties configuration;

	/**
	 * There should be no instance of this class.
	 */
	private FrameworkConfig() { }

	/**
	 * Initialize the Framework Configuration loading the configuration
	 * from the file at configFilePath.
	 * @param configFilePath - The file path to the configuration file.
	 */
	public static void init(final String configFilePath) {
		filePath = configFilePath;
		System.out.println("File Path : ==== " + filePath);
		loadFromFile();
		System.out.println("Operating System : ==== "
				+ System.getProperty("os.name"));

		String progFiles = "C:/Program Files";
		String progFilesX86 = "C:/Program Files (x86)";
		String appData = "C:/Users/Testing/AppData";

		if (System.getProperty("user.name")
				.toLowerCase()
				.contains("testing")) {
			FrameworkConfig.set(
					"FIREFOX_BINARY_PATH",
					"/usr/bin/firefox");
			FrameworkConfig.set(
					"CHROME_BINARY_PATH",
					"/usr/bin/google-chrome");
			FrameworkConfig.set(
					"EDGE_BINARY_PATH",
					"/usr/bin/microsoft-edge");
		} else {
			if (System.getProperty("os.name")
					.contains("Windows 11")) {
				FrameworkConfig.set("FIREFOX_BINARY_PATH",
						progFiles
						+ "/Mozilla Firefox/firefox.exe");
				FrameworkConfig.set("CHROME_BINARY_PATH",
						progFiles
						+ "/Google/Chrome/Application/chrome.exe");
				FrameworkConfig.set("EDGE_BINARY_PATH",
						progFilesX86
						+ "/Microsoft/Edge/Application/msedge.exe");
			} else if (System.getProperty("os.name")
					.contains("Windows 10")) {
				FrameworkConfig.set("FIREFOX_BINARY_PATH",
						appData
						+ "/Local/Mozilla Firefox/firefox.exe");
				FrameworkConfig.set("CHROME_BINARY_PATH",
						progFilesX86
						+ "/Google/Chrome/Application/chrome.exe");
				FrameworkConfig.set("EDGE_BINARY_PATH",
						progFilesX86
                        + "/Microsoft/Edge/Application/msedge.exe");
			}
		}
		System.out.println("Chrome Binary path : ==== "
				+ FrameworkConfig.get("CHROME_BINARY_PATH"));
		System.out.println("Firefox Binary path : ==== "
				+ FrameworkConfig.get("FIREFOX_BINARY_PATH"));
		System.out.println("Edge Binary path : ==== "
				+ FrameworkConfig.get("EDGE_BINARY_PATH"));
	}

	/**
	 * Load the configuration file.
	 */
	private static void loadFromFile() {
		configuration = new Properties();
		try {
			configuration.load(
					Files.newInputStream(
							Paths.get(filePath)));
		} catch (IOException e) {
			error("Failed to load configuration file from path :"
                    + "\n"
					+ filePath
					+ "\n"
					+ e.getMessage());
		}
	}

	/**
	 * Retrieve a value from the Configuration File using the 'key'.
	 * @param key - The key to use to locate the value in the
	 *               configuration file.
	 * @return A {@link String } containing the value stored at the key
	 * in the configuration file.
	 */
	public static String get(final String key) {
		return configuration.getProperty(key);
	}

	/**
	 * Set a value in the FW Config properties file.
	 * @param key The value used to store the value.
	 * @param value The value to store.
	 */
	public static void set(final String key, final String value) {
		configuration.setProperty(key, value);
	}

}
