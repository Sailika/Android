package com.paradigmcreatives.apspeak.discovery.tasks.parsers;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.paradigmcreatives.apspeak.app.model.Friend;
import com.paradigmcreatives.apspeak.app.util.constants.JSONConstants;
import com.paradigmcreatives.apspeak.app.util.json.JsonUtil;
import com.paradigmcreatives.apspeak.discovery.fragments.UserNetworkFragment.UserNetwork;

/**
 * Parser to parse list of friends of given type from the given Stream Asset JSON
 * 
 * @author Dileep | neuv
 * 
 */
public class StreamAssetFriendsParser {

	private JSONObject jsonToParse = null;
	private UserNetwork usersListToFetch;

	public StreamAssetFriendsParser(JSONObject jsonToParse, UserNetwork type) {
		super();
		this.jsonToParse = jsonToParse;
		this.usersListToFetch = type;
	}

	/**
	 * Parses the stream asset json depends on type of friends to be returned
	 * 
	 * @return
	 */
	public Collection<Friend> parse() {
		Collection<Friend> friendsSet = null;
		switch (usersListToFetch) {
		case LOVED_USERS:
			friendsSet = parseLovedFriends();
			break;

		case REPOSTED_USERS:
			friendsSet = parseRepostedFriends();
			break;

		default:
			break;
		}
		return friendsSet;
	}

	/**
	 * Parses the given asset JSON string and returns list of users/friends who all liked/loved the asset
	 * 
	 * @return
	 */
	public Collection<Friend> parseLovedFriends() {
		Set<Friend> friendsSet = null;
		if (jsonToParse != null) {
			try {
				JSONObject assetJSON = jsonToParse;
				if (assetJSON != null && assetJSON.has(JSONConstants.ASSOCIATIONS)) {
					JSONArray associations = assetJSON.getJSONArray(JSONConstants.ASSOCIATIONS);
					if (associations != null) {
						for (int i = 0; i < associations.length(); i++) {
							JSONObject association = associations.getJSONObject(i);
							if (association != null && association.has(JSONConstants.TYPE)
									&& association.getString(JSONConstants.TYPE).equals(JSONConstants.LOVE)) {
								if (association.has(JSONConstants.USERS)) {
									JSONArray users = association.getJSONArray(JSONConstants.USERS);
									if (users != null) {
										for (int j = 0; j < users.length(); j++) {
											Friend friend = JsonUtil.parseFriendJSON(users.getJSONObject(j));
											if (friend != null) {
												if (friendsSet == null) {
													friendsSet = new ConcurrentSkipListSet<Friend>();
												}
												friendsSet.add(friend);
											}
										}
									}
								}
							}
						}
					}
				}
			} catch (JSONException e) {

			}
		}
		return friendsSet;
	}

	/**
	 * Parses the given asset JSON string and returns list of users/friends who all reposted the asset
	 * 
	 * @return
	 */
	public Collection<Friend> parseRepostedFriends() {
		Set<Friend> friendsSet = null;
		if (jsonToParse != null) {
			try {
				JSONObject assetJSON = jsonToParse;
				if (assetJSON != null && assetJSON.has(JSONConstants.REPOSTERS)) {
					JSONArray reposters = assetJSON.getJSONArray(JSONConstants.REPOSTERS);
					if (reposters != null) {
						for (int i = 0; i < reposters.length(); i++) {
							Friend friend = JsonUtil.parseFriendJSON(reposters.getJSONObject(i));
							if (friend != null) {
								if (friendsSet == null) {
									friendsSet = new ConcurrentSkipListSet<Friend>();
								}
								friendsSet.add(friend);
							}
						}
					}
				}
			} catch (JSONException e) {

			}
		}
		return friendsSet;
	}

	/**
	 * Parses and checks whether current user reposted the asset or not
	 * 
	 * @return
	 */
	public boolean parseUserRepostedStatus(String userId) {
		boolean isReposted = false;
		if (jsonToParse != null && !TextUtils.isEmpty(userId)) {
			try {
				JSONObject assetJSON = jsonToParse;
				if (assetJSON != null && assetJSON.has(JSONConstants.REPOSTERS)) {
					JSONArray reposters = assetJSON.getJSONArray(JSONConstants.REPOSTERS);
					if (reposters != null) {
						for (int i = 0; i < reposters.length(); i++) {
							Friend friend = JsonUtil.parseFriendJSON(reposters.getJSONObject(i));
							if (friend != null) {
								String friend_userId = friend.getUserId();
								if(!TextUtils.isEmpty(friend_userId) && userId.equals(friend_userId)){
									isReposted = true;
									break;
								}
							}
						}
					}
				}
			} catch (JSONException e) {

			}
		}
		return isReposted;
	}
}
