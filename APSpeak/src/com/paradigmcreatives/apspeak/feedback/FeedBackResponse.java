package com.paradigmcreatives.apspeak.feedback;

public class FeedBackResponse {
	private int errorCode;
	private String message;

	public String getErrorMessage() {
		return message;
	}

	public void setErrorMessage(String errorMessage) {
		this.message = errorMessage;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	

}
