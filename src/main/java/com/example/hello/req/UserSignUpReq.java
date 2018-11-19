package com.example.hello.req;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserSignUpReq {
	// region -- Fields --
	
	@JsonProperty(value = "userName")
	private String userName;

	@JsonProperty(value = "password")
	private String password;

	@JsonProperty(value = "firstName")
	private String firstName;

	@JsonProperty(value = "lastName")
	private String lastName;

	@JsonProperty(value = "email")
	private String email;

	
	//end
	
	// region -- Get set --
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	// region -- Methods --
	public UserSignUpReq() {
		
	}

	
	
}
