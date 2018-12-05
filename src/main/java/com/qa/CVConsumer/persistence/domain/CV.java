package com.qa.CVConsumer.persistence.domain;

import java.io.File;

public class CV {

	private File CV;
	private Trainee creator;
	private String[] alertees;

	public File getCV() {
		return CV;
	}

	public void setCV(File cV) {
		CV = cV;
	}

	public Trainee getCreator() {
		return creator;
	}

	public void setCreator(Trainee creator) {
		this.creator = creator;
	}

	public String[] getAlertees() {
		return alertees;
	}

	public void setAlertees(String[] alertees) {
		this.alertees = alertees;
	}
}
