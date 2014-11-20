package com.paradigmcreatives.apspeak.app.model;

import android.graphics.Bitmap;

/**
 * FacebookProfile, is used to hold the user's facebook profile data, such as, firstName, lastName, userName, birthday,
 * userId, bio, profilePictureUrl, coverImageUrl, etc
 * 
 * @author Dileep
 * 
 */
public class FacebookProfile extends UserProfile {

    private String facebookUserId;
    private String facebookAccessToken;
    private String userName;
    private String firstName;
    private String lastName;
    private String fullName;
    private String birthday;
    private String bio; // denotes AboutMe section of facebook profile
    private String profilePictureUrl;
    private String coverImageUrl;
    private Bitmap profileBitmap = null;

    public FacebookProfile() {
	super();
    }

    public void setFacebookUserId(String facebookUserId) {
	this.facebookUserId = facebookUserId;
    }

    public void setFacebookAccessToken(String facebookAccessToken){
	this.facebookAccessToken = facebookAccessToken;
    }
    
    public void setUserName(String userName) {
	this.userName = userName;
    }

    public void setFirstName(String firstName) {
	this.firstName = firstName;
    }

    public void setLastName(String lastName) {
	this.lastName = lastName;
    }

    public void setBirthday(String birthday) {
	this.birthday = birthday;
    }

    public void setBiography(String biography) {
	this.bio = biography;
    }

    public void setProfilePicUrl(String profilePictureUrl) {
	this.profilePictureUrl = profilePictureUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
	this.coverImageUrl = coverImageUrl;
    }

    public String getFacebookUserId() {
	return this.facebookUserId;
    }

    public String getFacebookAccessToken(){
	return this.facebookAccessToken;
    }
    
    public String getUserName() {
	return this.userName;
    }

    public String getFirstName() {
	return this.firstName;
    }

    public String getLastName() {
	return this.lastName;
    }

    public String getBirthday() {
	return this.birthday;
    }

    public String getBiography() {
	return this.bio;
    }

    public String getProfilePictureUrl() {
	return this.profilePictureUrl;
    }

    public String getCoverImageUrl() {
	return this.coverImageUrl;
    }

    public Bitmap getProfileBitmap() {
	return profileBitmap;
    }

    public void setProfileBitmap(Bitmap profileBitmap) {
	this.profileBitmap = profileBitmap;
    }

    public String getFullName() {
	return fullName;
    }

    public void setFullName(String fullName) {
	this.fullName = fullName;
    }
}