package com.amazon.models;
import static com.amazon.utilities.TestLogger.info;

public class ProductURLs {

	public static final String AMAZON_UAT = "https://www.amazon.uat.in/";
	public static final String AMAZON_DEV = "https://www.amazon.dev.in/";
	public static final String AMAZON_PROD = "https://www.amazon.in";
	private static String currentProductTestEnvironment = "";

	public static void setCurrentProductEnvironment(String env) {
		info("***********setting the product environment to " + env + " **********************");
		currentProductTestEnvironment = env;
	}

	public static String getTestEnvironment(){
		return currentProductTestEnvironment;
	}

	public static String getCurrentVersion()  {
		String returnVersion = "NOT_OBTAINED";

		if (currentProductTestEnvironment.contains("amazon")) {
			if (currentProductTestEnvironment.contains("uat")) {
				returnVersion = "AMAZON_UAT";
			} else if (currentProductTestEnvironment.contains("dev")) {
				returnVersion = "AMAZON_DEV";
			}
			else  {
				returnVersion = "AMAZON_PROD";
			 }
		}

		return returnVersion;

	}

	public static String getProductName() {
		return currentProductTestEnvironment.split("www.")[1].substring(0, 5).toUpperCase();
	}


}