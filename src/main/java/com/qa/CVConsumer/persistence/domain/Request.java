package com.qa.CVConsumer.persistence.domain;

public class Request {

	public enum requestType {
		CREATE, READ, UPDATE, DELETE, TAG
	}

	private CV cv;
	private requestType type;
	private long CVID;

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

	public long getCVID() {
		return CVID;
	}

	public void setCVID(long cVID) {
		CVID = cVID;
	}

}
