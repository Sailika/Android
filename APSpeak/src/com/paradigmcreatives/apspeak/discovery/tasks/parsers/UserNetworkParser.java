package com.paradigmcreatives.apspeak.discovery.tasks.parsers;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.paradigmcreatives.apspeak.app.model.Friend;
import com.paradigmcreatives.apspeak.app.util.constants.JSONConstants;
import com.paradigmcreatives.apspeak.discovery.fragments.UserNetworkFragment.UserNetwork;
import com.paradigmcreatives.apspeak.logging.Logger;

/**
 * Parser for find friends
 * 
 * @author robin
 * 
 */
public class UserNetworkParser {
	private static final String TAG = "FindFriendsParser";

	private String stringToParse = null;
	private UserNetwork network = null;

	public UserNetworkParser(String stringToParse, UserNetwork network) {
		this.stringToParse = stringToParse;
		this.network = network;
	}

	public void setUserNetwork(UserNetwork newNetwork) {
		this.network = newNetwork;
	}

	public Set<Friend> parse() {
		Set<Friend> result = null;
		switch (network) {
		case FRIENDS:
		case SUGGESTED_FRIENDS:
			result = parseFriends(network);
			break;
		case FOLLOWERS:
			result = parseFollowersFollowing("followers");
			break;
		case FOLLOWING:
			result = parseFollowersFollowing("following");
			break;
		case FACEBOOK_FRIENDS:
			result = parseFacebookFriends(network);
			break;
		default:
			break;
		}
		return result;
	}

	private Set<Friend> parseFollowersFollowing(String attribute) {
		Set<Friend> result = null;
		if (!TextUtils.isEmpty(stringToParse)) {
			try {
				result = new ConcurrentSkipListSet<Friend>();
				JSONObject object = new JSONObject(stringToParse);
				boolean success = object.getBoolean("success");
				if (success) {
					if (object.has("result")) {
						JSONObject resultObject = object
								.getJSONObject("result");
						if (resultObject.has(attribute)
								&& !resultObject.isNull(attribute)) {
							JSONArray usersArray = resultObject
									.getJSONArray(attribute);
							result.addAll(parseFriendsArray(usersArray));
						}
					} else {
						Logger.warn(TAG,
								"No result object sent while in the find friends response");
					}
				}
			} catch (JSONException e) {
				Logger.warn(TAG,
						"JSON Exception occurred while parsing the result of find friends. Exception: "
								+ e);
			} catch (Exception e) {
				Logger.warn(
						TAG,
						"Unknown Exception occurred while parsing the result of find friends. Exception: "
								+ e);
			}

		} else {
			Logger.warn(TAG,
					"Empty string sent to parse the result of find friends");
		}

		return result;

	}

	private Set<Friend> parseFriends(UserNetwork network) {
		Set<Friend> result = null;
		if (!TextUtils.isEmpty(stringToParse)) {
			try {
				result = new ConcurrentSkipListSet<Friend>();
				JSONObject object = new JSONObject(stringToParse);
				boolean success = object.getBoolean("success");
				if (success) {
					if (object.has("result")) {
						JSONObject resultObject = object
								.getJSONObject("result");
						/*
						 * if (resultObject.has("contacts") &&
						 * !resultObject.isNull("contacts")) { JSONArray
						 * contactsArray =
						 * resultObject.getJSONArray("contacts");
						 * result.addAll(parseFriendsArray(contactsArray)); }
						 */
						switch (network) {
						case FRIENDS:
							if (resultObject.has("facebook_friends")) {
								JSONArray facebookFriendsArray = resultObject
										.getJSONArray("facebook_friends");
								result.addAll(parseFriendsArray(facebookFriendsArray));
							}
							break;

						case SUGGESTED_FRIENDS:
							if (resultObject.has("suggested_friends")) {
								JSONArray facebookFriendsArray = resultObject
										.getJSONArray("suggested_friends");
								result.addAll(parseFriendsArray(facebookFriendsArray));
							}
							break;

						default:
							break;
						}
					} else {
						Logger.warn(TAG,
								"No result object sent while in the find friends response");
					}
				}
			} catch (JSONException e) {
				Logger.warn(TAG,
						"JSON Exception occurred while parsing the result of find friends. Exception: "
								+ e);
			} catch (Exception e) {
				Logger.warn(
						TAG,
						"Unknown Exception occurred while parsing the result of find friends. Exception: "
								+ e);
			}

		} else {
			Logger.warn(TAG,
					"Empty string sent to parse the result of find friends");
		}

		return result;

	}

	private Set<Friend> parseFacebookFriends(UserNetwork network) {
		Set<Friend> result = null;
		if (!TextUtils.isEmpty(stringToParse)) {
			try {
				result = new ConcurrentSkipListSet<Friend>();
				JSONArray facebookFriendsArray = new JSONArray(stringToParse);

				result.addAll(parseFriendsArray(facebookFriendsArray));

			} catch (JSONException e) {
				Logger.warn(TAG,
						"JSON Exception occurred while parsing the result of find friends. Exception: "
								+ e);
			} catch (Exception e) {
				Logger.warn(
						TAG,
						"Unknown Exception occurred while parsing the result of find friends. Exception: "
								+ e);
			}

		} else {
			Logger.warn(TAG,
					"Empty string sent to parse the result of find friends");
		}

		return result;

	}

	private Collection<Friend> parseFriendsArray(JSONArray array)
			throws JSONException, Exception {
		Set<Friend> result = null;
		if (array != null) {
			// result = new ConcurrentSkipListSet<Friend>();
			result = new HashSet<Friend>();
			int length = array.length();
			Friend friend = null;
			for (int i = 0; i < length; i++) {
				friend = parseFriendObject(array.getJSONObject(i));
				if (friend != null) {
					/*
					 * In case of FIND FRIENDS response, do not add friend to
					 * the list if the follow status is true
					 */
					if (network == UserNetwork.FRIENDS
							&& friend.getFollowStatus()) {
						// Do not add friend to the list
					} else {
						result.add(friend);
					}
				}

			}
		}

		return result;
	}

	private Friend parseFriendObject(JSONObject object) throws JSONException,
			Exception {
		Friend result = null;
		if (object != null) {
			result = new Friend();
			result.setUniqueHandle(getStringValueOfKey(object, "handle"));
			result.setUserId(getStringValueOfKey(object, "user_id"));
			result.setFacebookId(getStringValueOfKey(object, "id"));
			result.setName(getStringValueOfKey(object, "name"));
			result.setLocation(getStringValueOfKey(object, "location"));
			result.setProfilePicURL(getStringValueOfKey(object,
					"profile_picture"));
			if (network == UserNetwork.FOLLOWING) {
				result.setFollowStatus(true);
			} else {
				if (object.has(JSONConstants.FOLLOWS)) {
					result.setFollowStatus(getBooleanValueOfKey(object,
							JSONConstants.FOLLOWS));
				} else if (object.has(JSONConstants.FOLLOWING)) {
					result.setFollowStatus(getBooleanValueOfKey(object,
							JSONConstants.FOLLOWING));
				}
			}
		}
		return result;
	}

	private String getStringValueOfKey(JSONObject object, String key)
			throws JSONException, Exception {
		if (object.has(key)) {
			if (!TextUtils.equals(object.getString(key), "null")) {
				return object.getString(key);
			} else {
				return null;
			}
		}
		return null;
	}

	private boolean getBooleanValueOfKey(JSONObject object, String key)
			throws JSONException, Exception {
		if (object.has(key)) {
			if (!TextUtils.equals(object.getString(key), "null")) {
				return object.getBoolean(key);
			} else {
				return false;
			}
		}
		return false;
	}

}
