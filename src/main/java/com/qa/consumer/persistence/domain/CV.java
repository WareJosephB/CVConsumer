package com.qa.consumer.persistence.domain;

import java.io.File;

import org.springframework.data.annotation.Id;

public class CV {

	@Id
	private long _id;
	private File cvFile;
	private Trainee creator;

	public File getCV() {
		return cvFile;
	}

	public void setCV(File cV) {
		cvFile = cV;
	}

	public Trainee getCreator() {
		return creator;
	}

	public void setCreator(Trainee creator) {
		this.creator = creator;
	}

}