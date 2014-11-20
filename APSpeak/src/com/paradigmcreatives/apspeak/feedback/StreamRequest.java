package com.paradigmcreatives.apspeak.feedback;

public class StreamRequest {
	private String user_id;
	private String coverage = "ALL";
	private String type = "GENERAL";

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getCoverage() {
		return coverage;
	}

	public void setCoverage(String coverage) {
		this.coverage = coverage;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
