package com.qa.consumer.persistence.domain;

import org.springframework.data.annotation.Id;
import org.springframework.web.multipart.MultipartFile;

public class CV {

	@Id
	private long _id;
	private MultipartFile cvFile;
	private Trainee creator;

	public MultipartFile getCV() {
		return cvFile;
	}

	public void setCV(MultipartFile cv) {
		cvFile = cv;
	}

	public Trainee getCreator() {
		return creator;
	}

	public void setCreator(Trainee creator) {
		this.creator = creator;
	}

}