package com.paradigmcreatives.apspeak.app.model;

public class FollowCheckBean {

	private boolean follows = false;
	private String error = null;
	private String user1 = null;
	private String user2 = null;
	private String status = null;

	/**
	 * @param follows
	 * @param error
	 */
	public FollowCheckBean(String user1, String user2, boolean follows, String error, String status) {
		this.user1 = user1;
		this.user2 = user2;
		this.follows = follows;
		this.error = error;
		this.status = status;
	}

	public boolean isFollows() {
		return follows;
	}

	public void setFollows(boolean follows) {
		this.follows = follows;
	}

	public String geterror() {
		return error;
	}

	public void seterror(String error) {
		this.error = error;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUser1() {
		return user1;
	}

	public void setUser1(String user1) {
		this.user1 = user1;
	}

	public String getUser2() {
		return user2;
	}

	public void setUser2(String user2) {
		this.user2 = user2;
	}

}
