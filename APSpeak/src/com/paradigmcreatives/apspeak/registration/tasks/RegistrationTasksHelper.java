package com.paradigmcreatives.apspeak.registration.tasks;

import android.content.Context;

import com.paradigmcreatives.apspeak.app.database.settingsdb.SettingsDAO;
import com.paradigmcreatives.apspeak.app.model.FacebookProfile;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;

public class RegistrationTasksHelper {

    private Context context;

    public RegistrationTasksHelper(Context context) {
	this.context = context;
    }

    /**
     * Stores all the facebook profile details in the persistent Settings DB
     * 
     * @param newProfile
     *            object of FacebookProfile that contains facebook user profile details
     * @param context
     *            the Context to be used while storing details in DB
     */
    public void storeFacebookProfileData(FacebookProfile newProfile) {

	if (newProfile != null && context != null) {
	    SettingsDAO settingsDAO = new SettingsDAO(context);
	    if (newProfile.getFacebookUserId() != null) {
		settingsDAO.insertOrReplace(Constants.KEY_FACEBOOK_PROFILE_ID, newProfile.getFacebookUserId());
	    }
	    if (newProfile.getFacebookAccessToken() != null) {
		settingsDAO.insertOrReplace(Constants.KEY_FACEBOOK_PROFILE_ACCESSTOKEN,
			newProfile.getFacebookAccessToken());
	    }
	    if (newProfile.getUserName() != null) {
		settingsDAO.insertOrReplace(Constants.KEY_FACEBOOK_PROFILE_USERNAME, newProfile.getUserName());

	    }
	    if (newProfile.getFirstName() != null) {
		settingsDAO.insertOrReplace(Constants.KEY_FACEBOOK_PROFILE_FIRSTNAME, newProfile.getFirstName());
	    }
	    if (newProfile.getLastName() != null) {
		settingsDAO.insertOrReplace(Constants.KEY_FACEBOOK_PROFILE_LASTNAME, newProfile.getLastName());
	    }
	    if (newProfile.getBirthday() != null) {
		settingsDAO.insertOrReplace(Constants.KEY_FACEBOOK_PROFILE_BIRTHDAY, newProfile.getBirthday());
	    }
	    if (newProfile.getBiography() != null) {
		settingsDAO.insertOrReplace(Constants.KEY_FACEBOOK_PROFILE_BIO, newProfile.getBiography());
	    }
	}
    }

}
