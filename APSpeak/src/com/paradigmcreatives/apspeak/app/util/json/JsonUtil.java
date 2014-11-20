package com.paradigmcreatives.apspeak.app.util.json;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.paradigmcreatives.apspeak.app.model.Friend;
import com.paradigmcreatives.apspeak.app.model.User;
import com.paradigmcreatives.apspeak.app.util.Util;
import com.paradigmcreatives.apspeak.app.util.constants.JSONConstants;

/**
 * Utility class to perform several operations related to JSON
 * 
 * @author Dileep | neuv
 * 
 */
public class JsonUtil {

	/**
	 * Parses given json and returns respective User object
	 * 
	 * @param ownerJSON
	 * @return
	 */
	public static User parseUserJSON(JSONObject userJSON) {
		User user = null;
		if (userJSON != null) {
			user = new User();
			try {
				if (userJSON.has(JSONConstants.USER_ID)) {
					user.setUserId(userJSON.getString(JSONConstants.USER_ID));
				}
				if (userJSON.has(JSONConstants.HANDLE)) {
					user.setUniqueHandle(userJSON
							.getString(JSONConstants.HANDLE));
				}
				if (userJSON.has(JSONConstants.NAME)) {
					user.setName(userJSON.getString(JSONConstants.NAME));
				}
				if (userJSON.has(JSONConstants.GROUPS)) {
					ArrayList<String> stringArray = new ArrayList<String>();
					JSONArray jsonArray = userJSON
							.getJSONArray(JSONConstants.GROUPS);
					for (int i = 0; i < jsonArray.length(); i++) {
						try {
							String jsonString = jsonArray.getString(i);
							stringArray.add(jsonString);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					user.setUserGroupName(stringArray);
				}
				if (userJSON.has(JSONConstants.GENDER)) {
					user.setGender(Util.convertToGENDER(userJSON
							.getString(JSONConstants.GENDER)));
				}
				if (userJSON.has(JSONConstants.PROFILE_PICTURE)) {
					user.setProfilePicURL(userJSON
							.getString(JSONConstants.PROFILE_PICTURE));
				}
				if (userJSON.has(JSONConstants.COVER_IMAGE)) {
					user.setCoverImageURL(userJSON
							.getString(JSONConstants.COVER_IMAGE));
				}
			} catch (JSONException e) {
				user = null;
			}
		}
		return user;
	}

	/**
	 * Parses given json and returns respective Friend object
	 * 
	 * @param friendJSON
	 * @return
	 */
	public static Friend parseFriendJSON(JSONObject friendJSON) {
		Friend friend = null;
		if (friendJSON != null) {
			friend = new Friend();
			try {
				if (friendJSON.has(JSONConstants.USER_ID)) {
					friend.setUserId(friendJSON
							.getString(JSONConstants.USER_ID));
				}
				if (friendJSON.has(JSONConstants.HANDLE)) {
					friend.setUniqueHandle(friendJSON
							.getString(JSONConstants.HANDLE));
				}
				if (friendJSON.has(JSONConstants.PROFILE_PICTURE)) {
					friend.setProfilePicURL(friendJSON
							.getString(JSONConstants.PROFILE_PICTURE));
				}
				if (friendJSON.has(JSONConstants.NAME)) {
					friend.setName(friendJSON.getString(JSONConstants.NAME));
				}
				if (friendJSON.has(JSONConstants.COVER_IMAGE)) {
					friend.setCoverImageURL(friendJSON
							.getString(JSONConstants.COVER_IMAGE));
				}
				if (friendJSON.has(JSONConstants.LOCATION)) {
					friend.setLocation(friendJSON
							.getString(JSONConstants.LOCATION));
				}
			} catch (JSONException e) {
				friend = null;
			}
		}
		return friend;
	}
}
