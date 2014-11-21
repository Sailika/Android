package com.paradigmcreatives.apspeak.feedback;

public class FeedBack {
	

	private String asset_id;
	private String user_id;
	private FeedBackType feedback;

	public String getAsset_id() {
		return asset_id;
	}

	public void setAsset_id(String asset_id) {
		this.asset_id = asset_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public FeedBackType getFeedback() {
		return feedback;
	}

	public void setFeedback(FeedBackType feedback) {
		this.feedback = feedback;
	}

}
