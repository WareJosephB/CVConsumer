package com.qa.consumer.persistence.domain;

import java.io.File;

import org.springframework.data.annotation.Id;

public class CV {

	@Id
	private long _id;
	private File cv;
	private Trainee creator;

	public File getCV() {
		return cv;
	}

	public void setCV(File cV) {
		cv = cV;
	}

	public Trainee getCreator() {
		return creator;
	}

	public void setCreator(Trainee creator) {
		this.creator = creator;
	}

}