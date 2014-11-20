package com.paradigmcreatives.apspeak.feed.parsers;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.paradigmcreatives.apspeak.app.model.MyFeedBean;
import com.paradigmcreatives.apspeak.app.util.constants.JSONConstants;

public class MyFeedParser {

    private Context context;

    /**
     * Constructor
     * 
     * @param context
     */
    public MyFeedParser(Context context) {
	this.context = context;
    }

    /**
     * Parses given json and returns respective MyFeedBean object
     * 
     * @param myFeedBeanJSON
     * @return myFeedBean
     */
    public MyFeedBean parseMyFeedBeanJSON(JSONObject myFeedBeanJSON) {
	MyFeedBean myFeedBean = null;
	if (myFeedBeanJSON != null) {
	    try {
		// For time being, we are not showing CUEs under MyFeed
		if (myFeedBeanJSON.has(JSONConstants.TYPE)) {
		    String type = myFeedBeanJSON.getString(JSONConstants.TYPE);
		    if (TextUtils.isEmpty(type) || type.equalsIgnoreCase(JSONConstants.CUE)) {
			return null;
		    }
		}
		myFeedBean = new MyFeedBean();
		// parse id
		if (myFeedBeanJSON.has(JSONConstants._ID)) {
		    myFeedBean.setId(myFeedBeanJSON.getString(JSONConstants._ID));
		}
		// parse my feed snap_url
		if (myFeedBeanJSON.has(JSONConstants.SNAP_URL)) {
		    myFeedBean.setSnapUrl(myFeedBeanJSON.getString(JSONConstants.SNAP_URL));
		}
		// parse my feed name
		if (myFeedBeanJSON.has(JSONConstants.NAME)) {
		    myFeedBean.setName(myFeedBeanJSON.getString(JSONConstants.NAME));
		}
		// parse my feed user id
		if (myFeedBeanJSON.has(JSONConstants.USER_ID)) {
		    myFeedBean.setUserId(myFeedBeanJSON.getString(JSONConstants.USER_ID));
		}
		// parse my feed profile picture
		if (myFeedBeanJSON.has(JSONConstants.PROFILE_PICTURE)) {
		    myFeedBean.setProfilePicture(myFeedBeanJSON.getString(JSONConstants.PROFILE_PICTURE));
		}
		// parse my feed type
		if (myFeedBeanJSON.has(JSONConstants.TYPE)) {
		    myFeedBean.setType(myFeedBeanJSON.getString(JSONConstants.TYPE));
		}
		// parse my feed message
		if (myFeedBeanJSON.has(JSONConstants.MESSAGE)) {
		    myFeedBean.setMessage(myFeedBeanJSON.getString(JSONConstants.MESSAGE));
		}
		// parse my feed ts
		if (myFeedBeanJSON.has(JSONConstants.TS)) {
		    myFeedBean.setTs(myFeedBeanJSON.getString(JSONConstants.TS));
		}
		// parse my feed asset id
		if (myFeedBeanJSON.has(JSONConstants.ASSET_ID)) {
		    myFeedBean.setAssetId(myFeedBeanJSON.getString(JSONConstants.ASSET_ID));
		}
		// parse my feed value
		if (myFeedBeanJSON.has(JSONConstants.VALUE)) {
		    myFeedBean.setValue(myFeedBeanJSON.getString(JSONConstants.VALUE));
		}
		// parse my feed icon url
		if(myFeedBeanJSON.has(JSONConstants.ICON_URL)){
		    myFeedBean.setIconUrl(myFeedBeanJSON.getString(JSONConstants.ICON_URL));
		}

		// tags not created by user
		// set complete JSON to my feed
		myFeedBean.setMyFeedAsJSON(myFeedBeanJSON.toString());
	    } catch (JSONException e) {
		myFeedBean = null;
	    }
	}
	return myFeedBean;
    }

}
