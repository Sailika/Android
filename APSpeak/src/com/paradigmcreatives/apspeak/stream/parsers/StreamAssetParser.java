package com.paradigmcreatives.apspeak.stream.parsers;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.paradigmcreatives.apspeak.app.model.ASSET_TAG;
import com.paradigmcreatives.apspeak.app.model.ASSET_TYPE;
import com.paradigmcreatives.apspeak.app.model.ASSOCIATION_TYPE;
import com.paradigmcreatives.apspeak.app.model.Friend;
import com.paradigmcreatives.apspeak.app.model.StreamAsset;
import com.paradigmcreatives.apspeak.app.model.User;
import com.paradigmcreatives.apspeak.app.util.AppPropertiesUtil;
import com.paradigmcreatives.apspeak.app.util.Util;
import com.paradigmcreatives.apspeak.app.util.constants.JSONConstants;
import com.paradigmcreatives.apspeak.app.util.json.JsonUtil;
import com.paradigmcreatives.apspeak.discovery.fragments.UserNetworkFragment.UserNetwork;
import com.paradigmcreatives.apspeak.discovery.tasks.parsers.StreamAssetFriendsParser;

/**
 * Class used to parse Stream asset JSON
 * 
 * @author Dileep | neuv
 * 
 */
public class StreamAssetParser {

    private Context context;

    /**
     * Constructor
     * 
     * @param context
     */
    public StreamAssetParser(Context context) {
	this.context = context;
    }

    /**
     * Parses the given Stream json object and returns array of Stream Assets
     * 
     * @param streamJSONArray
     * @return StreamAsset
     */
    public ArrayList<StreamAsset> parseStreamJSONArray(JSONArray streamJSONArray) {
	ArrayList<StreamAsset> streamAssets = null;
	if (streamJSONArray != null && streamJSONArray.length() > 0) {
	    streamAssets = new ArrayList<StreamAsset>();
	    for (int i = 0; i < streamJSONArray.length(); i++) {
		try {
		    StreamAsset asset = parseAssetJSON(streamJSONArray.getJSONObject(i));
		    if (asset != null) {
			streamAssets.add(asset);
		    }
		} catch (JSONException e) {
		    streamAssets = null;
		}
	    }
	}
	return streamAssets;
    }

    /**
     * Parses given json and returns respective StreamAsset object
     * 
     * @param streamAssetJSON
     * @return
     */
    public StreamAsset parseAssetJSON(JSONObject streamAssetJSON) {
	StreamAsset asset = null;
	if (streamAssetJSON != null) {
	    asset = new StreamAsset();
	    try {
		// parse id
		if (streamAssetJSON.has(JSONConstants.ID)) {
		    asset.setAssetId(streamAssetJSON.getString(JSONConstants.ID));
		}
		// parse asset tags
		if (streamAssetJSON.has(JSONConstants.TAGS)) {
		    JSONArray tagsJSON = streamAssetJSON.getJSONArray(JSONConstants.TAGS);
		    if (tagsJSON != null && tagsJSON.length() > 0) {
			ASSET_TAG[] tags = new ASSET_TAG[tagsJSON.length()];
			for (int i = 0; i < tagsJSON.length(); i++) {
			    ASSET_TAG tag = Util.convertToAssetTag(tagsJSON.getString(i));
			    if (tag != null) {
				tags[i] = tag;
			    }
			}
			asset.setAssetTagsArray(tags);
		    }
		}
		// parse asset's snap_url
		if (streamAssetJSON.has(JSONConstants.SNAP_URL)) {
		    asset.setAssetSnapURL(streamAssetJSON.getString(JSONConstants.SNAP_URL));
		}
		// parse asset's thumbnail_url
		if (streamAssetJSON.has(JSONConstants.THUMBNAIL_URL)) {
		    asset.setAssetThumbnailURL(streamAssetJSON.getString(JSONConstants.THUMBNAIL_URL));
		}
		// parse asset download url
		if (streamAssetJSON.has(JSONConstants.URL)) {
		    asset.setAssetDownloadURL(streamAssetJSON.getString(JSONConstants.URL));
		}
		// parse asset share url
		if (streamAssetJSON.has(JSONConstants.SHARE_URL)) {
		    asset.setAssetShareURL(streamAssetJSON.getString(JSONConstants.SHARE_URL));
		}
		// parse asset description
		if (streamAssetJSON.has(JSONConstants.DESCRIPTION)) {
		    asset.setAssetDescription(streamAssetJSON.getString(JSONConstants.DESCRIPTION));
		}
		// parse owner
		if (streamAssetJSON.has(JSONConstants.OWNER)) {
		    User owner = JsonUtil.parseUserJSON(streamAssetJSON.getJSONObject(JSONConstants.OWNER));
		    if (owner != null) {
			asset.setAssetOwner(owner);
		    }
		}
		// parse created_by
		if (streamAssetJSON.has(JSONConstants.CREATED_BY)) {
		    if (asset.getAssetOwner() != null) {
			asset.getAssetOwner().setUserId(streamAssetJSON.getString(JSONConstants.CREATED_BY));
		    }
		}
		// parse created_at
		if (streamAssetJSON.has(JSONConstants.CREATED_AT)) {
		    asset.setAssetCreatedTimestamp(streamAssetJSON.getString(JSONConstants.CREATED_AT));
		}
		// parse type
		if (streamAssetJSON.has(JSONConstants.TYPE)) {
		    ASSET_TYPE type = Util.convertToAssetType(streamAssetJSON.getString(JSONConstants.TYPE));
		    if (type != null) {
			asset.setAssetType(type);
		    }
		}
		// parse associations
		if (streamAssetJSON.has(JSONConstants.ASSOCIATIONS)) {
		    HashMap<ASSOCIATION_TYPE, Integer> associations = parseAssociation(
			    streamAssetJSON.getJSONArray(JSONConstants.ASSOCIATIONS), asset);
		    if (associations != null) {
			asset.setAssetAssociations(associations);
		    }
		}
		// parse whether current user has LOVED or not
		if (streamAssetJSON.has(JSONConstants.LOVED) && !streamAssetJSON.isNull(JSONConstants.LOVED)) {
		    asset.setAssetIsLoved(true);
		}else{
			asset.setAssetIsLoved(false);
		}
		// parse LOVE
		if (streamAssetJSON.has(JSONConstants.LOVE)) {
		    HashMap<ASSOCIATION_TYPE, Integer> associations = new HashMap<ASSOCIATION_TYPE, Integer>();
		    if (associations != null) {
			associations.put(ASSOCIATION_TYPE.LOVE, streamAssetJSON.getInt(JSONConstants.LOVE));
			asset.setAssetAssociations(associations);
		    }
		}

		// parse whether current user has COMMENTED or not
		if (streamAssetJSON.has(JSONConstants.COMMENTED)) {
		    asset.setAssetIsCommented(streamAssetJSON.getBoolean(JSONConstants.COMMENTED));
		}

		// parse COMMENTs count
		if (streamAssetJSON.has(JSONConstants.COMMENT_ON)) {
		    asset.setAssetCommentsCount(streamAssetJSON.getInt(JSONConstants.COMMENT_ON));
		}

		// parse total reposts
		if (streamAssetJSON.has(JSONConstants.TOTAL_REPOSTS)) {
		    asset.setAssetRepostsCount(streamAssetJSON.getInt(JSONConstants.TOTAL_REPOSTS));
		}

		// parse reposted users and check whether current user reposted this asset or not
		StreamAssetFriendsParser parser = new StreamAssetFriendsParser(streamAssetJSON,
			UserNetwork.REPOSTED_USERS);
		if (parser != null) {
		    asset.setAssetIsReposted(parser.parseUserRepostedStatus(AppPropertiesUtil.getUserID(context)));
		}

		// tags not created by user
		// set complete JSON to stream asset
		asset.setAssetAsJSON(streamAssetJSON.toString());
	    } catch (JSONException e) {
		asset = null;
	    }
	}
	return asset;
    }

    /**
     * Parses given associations json array and returns prepared HashMap<ASSOCIATION_TYPE, Integer>
     * 
     * @param associationsJSONArray
     * @param streamAsset
     * @return
     */
    public HashMap<ASSOCIATION_TYPE, Integer> parseAssociation(JSONArray associationsJSONArray, StreamAsset asset) {
	HashMap<ASSOCIATION_TYPE, Integer> associations = null;
	if (associationsJSONArray != null && associationsJSONArray.length() > 0) {
	    associations = new HashMap<ASSOCIATION_TYPE, Integer>();
	    try {
		for (int i = 0; i < associationsJSONArray.length(); i++) {
		    JSONObject association = associationsJSONArray.getJSONObject(i);
		    if (association != null) {
			if (association.has(JSONConstants.TYPE)) {
			    if (association.getString(JSONConstants.TYPE).equals(JSONConstants.LOVE)) {
				// Parse total count of loves/likes
				if (association.has(JSONConstants.TOTAL_TAGS)) {
				    associations.put(ASSOCIATION_TYPE.LOVE,
					    association.getInt(JSONConstants.TOTAL_TAGS));
				}
				// Parse whether current user has loved this
				// asset or not
				if (association.has(JSONConstants.USERS)) {
				    JSONArray usersJSONArray = association.getJSONArray(JSONConstants.USERS);
				    if (usersJSONArray != null) {
					for (int j = 0; j < usersJSONArray.length(); j++) {
					    Friend likedUserDetails = JsonUtil.parseFriendJSON(usersJSONArray
						    .getJSONObject(j));
					    String currentUserId = AppPropertiesUtil.getUserID(context);
					    if (likedUserDetails != null
						    && likedUserDetails.getUserId().equals(currentUserId)) {
						if (asset != null) {
						    asset.setAssetIsLoved(true);
						}
						break;
					    }
					}
				    }
				}
			    }
			}
		    }
		}
	    } catch (JSONException e) {
		associations = null;
	    }
	}
	return associations;
    }

    /**
     * Parses given associations json array and returns an HashMap with ASSOCIATION_TYPE and array of users who all
     * liked the asset
     * 
     * @param associationsJSONArray
     * @param streamAsset
     * @return
     */
    public HashMap<ASSOCIATION_TYPE, ArrayList<Friend>> parseUsersInAssociation(JSONArray associationsJSONArray) {
	HashMap<ASSOCIATION_TYPE, ArrayList<Friend>> associations = null;
	if (associationsJSONArray != null && associationsJSONArray.length() > 0) {
	    associations = new HashMap<ASSOCIATION_TYPE, ArrayList<Friend>>();
	    try {
		for (int i = 0; i < associationsJSONArray.length(); i++) {
		    JSONObject association = associationsJSONArray.getJSONObject(i);
		    if (association != null) {
			if (association.has(JSONConstants.TYPE)) {
			    if (association.getString(JSONConstants.TYPE).equals(JSONConstants.LOVE)) {
				/*
				 * // Parse total count of loves/likes if (association.has(JSONConstants.TOTAL_TAGS)) {
				 * associations.put(ASSOCIATION_TYPE.LOVE,
				 * association.getInt(JSONConstants.TOTAL_TAGS)); }
				 */
				// Parse whether current user has loved this
				// asset or not
				if (association.has(JSONConstants.USERS)) {
				    JSONArray usersJSONArray = association.getJSONArray(JSONConstants.USERS);
				    if (usersJSONArray != null && usersJSONArray.length() > 0) {
					ArrayList<Friend> friendsList = new ArrayList<Friend>();
					for (int j = 0; j < usersJSONArray.length(); j++) {
					    Friend likedUserDetails = JsonUtil.parseFriendJSON(usersJSONArray
						    .getJSONObject(j));
					    friendsList.add(likedUserDetails);
					}
					associations.put(ASSOCIATION_TYPE.LOVE, friendsList);
				    }
				}
			    }
			}
		    }
		}
	    } catch (JSONException e) {
		associations = null;
	    }
	}
	return associations;
    }
}
