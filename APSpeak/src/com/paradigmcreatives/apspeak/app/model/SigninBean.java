package com.paradigmcreatives.apspeak.app.model;

public class SigninBean {
    
    private String userID;
    private String handle;
    /**
     * @param userID
     * @param handle
     */
    public SigninBean(String userID, String handle) {
	this.userID = userID;
	this.handle = handle;
    }

    public SigninBean() {
	// TODO Auto-generated constructor stub
    }

    /**
     * @return the userID
     */
    public String getUserID() {
	return userID;
    }

    /**
     * @param userID
     *            the userID to set
     */
    public void setUserID(String userID) {
	this.userID = userID;
    }

    /**
     * @return the handle
     */
    public String getHandle() {
	return handle;
    }

    /**
     * @param handle
     *            the handle to set
     */
    public void setHandle(String handle) {
	this.handle = handle;
    }
    
    

}
