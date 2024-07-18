package com.sauceLab.models;
import static com.sauceLab.utilities.TestLogger.info;

public class ProductURLs {

	private static String productURL = "";

	public static void setCurrentProductEnvironment(String env) {
		info("***********setting the product environment to " + env + " **********************");
		productURL = env;
	}

	public static String getProductURL(){
		return productURL;
	}

	public static String getProductName() {
		return productURL.split("www.")[1].split(".com")[0];
	}


}