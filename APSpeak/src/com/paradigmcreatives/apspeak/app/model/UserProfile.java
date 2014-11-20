package com.paradigmcreatives.apspeak.app.model;

/**
 * UserProfile, used to hold all the details of a user. It contains several fields such as, user's uniqueHandle,
 * password, name, gender, phoneNumber, country, etc
 * 
 * @author Dileep | neuv
 * 
 */
public class UserProfile {

    protected String uniqueHandle;
    private String password;
    protected GENDER gender;
    private String email;
    protected String phoneNumber;
    protected String country;

    public enum GENDER {
	MALE, FEMALE
    };

    /**
     * Default constructor
     */
    public UserProfile() {
	super();
    }

    public void setUniqueHandle(String uniqueHandle) {
	this.uniqueHandle = uniqueHandle;
    }

    public void setPassword(String password) {
	this.password = password;
    }

    public void setGender(GENDER gender) {
	this.gender = gender;
    }

    public void setPhoneNumber(String phoneNumber) {
	this.phoneNumber = phoneNumber;
    }

    public void setCountry(String country) {
	this.country = country;
    }

    public String getUniqueHandle() {
	return this.uniqueHandle;
    }

    public String getPassword() {
	return this.password;
    }

    public GENDER getGender() {
	return this.gender;
    }

    public String getPhoneNumber() {
	return this.phoneNumber;
    }

    public String getCountry() {
	return this.country;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }
}
