package com.qa.CVConsumer.persistence.domain;

import java.util.ArrayList;

public class Trainee {

	private String firstName;
	private String lastName;
	private String email;
	private ArrayList<String> alertees;

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

	public ArrayList<String> getAlertees() {
		return alertees;
	}

	public void setAlertees(ArrayList<String> alertees) {
		this.alertees = alertees;
	}

}