package com.qa.consumer.persistence.domain;

public class Request {

	public enum requestType {
		CREATE, READ, UPDATE, DELETE, READALL
	}

	private CV cv;
	private requestType type;
	private long cv_id;

	public CV getCv() {
		return cv;
	}

	public void setCv(CV cv) {
		this.cv = cv;
	}

	public requestType getType() {
		return type;
	}

	public void setType(requestType type) {
		this.type = type;
	}

	public long getcv_id() {
		return cv_id;
	}

	public void setcv_id(long cv_id) {
		this.cv_id = cv_id;
	}

}