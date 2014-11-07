package com.example.model;


public class FridgeStatusItems {
	private String heading;

	private String state;

	public FridgeStatusItems(String title, String state) {
		this.heading = title;
		this.state = state;

	}

	public String getHeading() {
		return heading;
	}

	public void setHeading(String heading) {
		this.heading = heading;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
}
