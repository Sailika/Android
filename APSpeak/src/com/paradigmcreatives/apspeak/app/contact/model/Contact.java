package com.paradigmcreatives.apspeak.app.contact.model;

public class Contact {
	private String contactName;
	private String contactNumber;
	
	public Contact(String number, String name){
		this.contactName = name;
		this.contactNumber = number;
	}
	
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	

}
