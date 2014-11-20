package com.paradigmcreatives.apspeak.user.parsers;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.paradigmcreatives.apspeak.app.model.Friend;
import com.paradigmcreatives.apspeak.app.model.User;
import com.paradigmcreatives.apspeak.app.model.Friend.FRIEND_TYPE;
import com.paradigmcreatives.apspeak.app.util.constants.JSONConstants;
import com.paradigmcreatives.apspeak.logging.Logger;

public class FullProfileParser {
    private static final String TAG = "FullProfileParser";
    private String stringToParse = null;

    public FullProfileParser(String result) {
	this.stringToParse = result;
    }

    public User parse() {
	User result = null;

	if (!TextUtils.isEmpty(stringToParse)) {
	    try {
		result = new User();
		JSONObject object = new JSONObject(stringToParse);
		boolean success = object.getBoolean("success");
		if (success) {
		    if (object.has("result")) {
			JSONObject resultObject = object.getJSONObject("result");
			if (resultObject.has(JSONConstants.USER_ID)) {
			    result.setUserId(resultObject.getString(JSONConstants.USER_ID));
			}
			if (resultObject.has(JSONConstants.PROFILE_PICTURE)) {
			    result.setProfilePicURL(resultObject.getString(JSONConstants.PROFILE_PICTURE));
			}
			if (resultObject.has(JSONConstants.HANDLE)) {
			    result.setUniqueHandle(resultObject.getString(JSONConstants.HANDLE));
			}
			if(resultObject.has(JSONConstants.LOCATION)){
				result.setLocation(resultObject.getString(JSONConstants.LOCATION));
			}
			if (resultObject.has(JSONConstants.TOTAL_POSTS)) {
			    result.setPosts(resultObject.getInt(JSONConstants.TOTAL_POSTS));
			}
			if (resultObject.has(JSONConstants.TOTAL_REPOSTS)) {
			    result.setReposts(resultObject.getInt(JSONConstants.TOTAL_REPOSTS));
			}
			if (resultObject.has(JSONConstants.TOTAL_TAGS_CREATED)) {
			    result.setTagsCreated(resultObject.getInt(JSONConstants.TOTAL_TAGS_CREATED));
			}
			if (resultObject.has(JSONConstants.TOTAL_TAGS_USED)) {
			    result.setTagsUsed(resultObject.getInt(JSONConstants.TOTAL_TAGS_USED));
			}
			if (resultObject.has(JSONConstants.TOTAL_FOLLOWING)) {
			    result.setFollowing(resultObject.getInt(JSONConstants.TOTAL_FOLLOWING));
			}
			if (resultObject.has(JSONConstants.TOTAL_FOLLOWERS)) {
			    result.setFollowers(resultObject.getInt(JSONConstants.TOTAL_FOLLOWERS));
			}
			if (resultObject.has(JSONConstants.NAME)) {
			    result.setName(resultObject.getString(JSONConstants.NAME));
			}
			if (resultObject.has(JSONConstants.COVER_IMAGE)) {
			    result.setCoverImageURL(resultObject.getString(JSONConstants.COVER_IMAGE));
			}

			if (resultObject.has("contacts") && !resultObject.isNull("contacts")) {
			    JSONArray contactsArray = resultObject.getJSONArray("contacts");
			    result.addFriends(parseFriendsArray(contactsArray, FRIEND_TYPE.CONTACT));
			}

			if (resultObject.has("facebook_friends") && !resultObject.isNull("contacts")) {
			    JSONArray facebookFriendsArray = resultObject.getJSONArray("facebook_friends");
			    result.addFriends(parseFriendsArray(facebookFriendsArray, FRIEND_TYPE.FACEBOOK));
			}
		    } else {
			Logger.warn(TAG, "No result object sent while in the find friends response");
		    }
		}
	    } catch (JSONException e) {
		Logger.warn(TAG, "JSON Exception occurred while parsing the result of find friends. Exception: " + e);
	    } catch (Exception e) {
		Logger.warn(TAG, "Unknown Exception occurred while parsing the result of find friends. Exception: " + e);
	    }

	} else {
	    Logger.warn(TAG, "Empty string sent to parse the result of find friends");
	}

	return result;

    }

    private List<Friend> parseFriendsArray(JSONArray array, FRIEND_TYPE friendType) throws JSONException, Exception {
	List<Friend> result = null;
	if (array != null) {
	    result = new ArrayList<Friend>();
	    int length = array.length();
	    Friend friend = null;
	    for (int i = 0; i < length; i++) {
		friend = parseFriendObject(array.getJSONObject(i));
		if (friend != null) {
		    friend.setFriendType(friendType);
		    result.add(friend);
		}

	    }
	}

	return result;
    }

    private Friend parseFriendObject(JSONObject object) throws JSONException, Exception {
	Friend result = null;
	if (object != null) {
	    result = new Friend();
	    result.setUniqueHandle(getStringValueOfKey(object, "handle"));
	    result.setUserId(getStringValueOfKey(object, "user_id"));
	    result.setName(getStringValueOfKey(object, "name"));
	    result.setLocation(getStringValueOfKey(object, "location"));
	    result.setProfilePicURL(getStringValueOfKey(object, "profile_picture"));
	}

	return result;

    }

    private String getStringValueOfKey(JSONObject object, String key) throws JSONException, Exception {
	if (object.has(key)) {
	    return object.getString(key);
	}
	return null;
    }

}
