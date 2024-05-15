package com.amazon.models;

import com.amazon.utilities.TestConstants;

public class User {
	
	public String firstName;
	public String lastName;
	public String emailAddress;
	public String userName;
	public String password;

	public User() {

	}

	public User(String username) {
		this.firstName = username;
		this.lastName = username;
		this.emailAddress = username + "@example.com";
		this.userName = username;
		this.password = TestConstants.PASSWORD;
	}

	public User(String firstName,
                String lastName,
                String emailAddress,
                String userName,
                String password) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailAddress = emailAddress;
		this.userName = userName;
		this.password = password;
	}

	public User anonymous() {
		return new User(
				"anonymous",
				"anonymous",
				"anonymous" + "@example.com",
				"anonymous",
				"");
	}

	public User generalUser(String username) {
		return new User(
				username,
				username,
				username + "@example.com",
				username,
				TestConstants.PASSWORD);
	}

}
