package com.paradigmcreatives.apspeak.app.model;

public class RegisterBean {
    private String userID = null;
    private boolean success = false;

    /**
     * @param userID
     * @param success
     */
    public RegisterBean(String userID, boolean success) {
	this.userID = userID;
	this.success = success;
    }

    public String getUserID() {
	return userID;
    }

    public void setUserID(String userID) {
	this.userID = userID;
    }

    public boolean isSuccess() {
	return success;
    }

    public void setSuccess(boolean success) {
	this.success = success;
    }

}
