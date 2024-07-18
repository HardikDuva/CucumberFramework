package com.sauceLab.models;

public class User {

	public String userName;

	public User() {

	}

	public User(String userName) {
		this.userName = userName;
	}

	public User generalUser(String username) {
		return new User(
				username);
	}

}
