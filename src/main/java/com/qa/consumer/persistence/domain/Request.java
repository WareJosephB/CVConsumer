package com.qa.consumer.persistence.domain;

public class Request {

	public enum requestType {
		CREATE, READ, UPDATE, DELETE, READALL
	}

	private CV cv;
	private requestType type;
	private long cvIDtoActUpon;

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

	public long getcvIDtoActUpon() {
		return cvIDtoActUpon;
	}

	public void setcvIDtoActUpon(long cvID) {
		this.cvIDtoActUpon = cvID;
	}

}