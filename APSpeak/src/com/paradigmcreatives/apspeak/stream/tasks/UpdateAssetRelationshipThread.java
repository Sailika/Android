package com.paradigmcreatives.apspeak.stream.tasks;

import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.paradigmcreatives.apspeak.app.model.ASSOCIATION_TYPE;
import com.paradigmcreatives.apspeak.app.util.constants.JSONConstants;
import com.paradigmcreatives.apspeak.logging.Logger;
import com.paradigmcreatives.apspeak.stream.handlers.UpdateAssetRelationshipHandler;
import com.paradigmcreatives.apspeak.stream.tasks.helpers.UpdateAssetRelationshipHelper;

/**
 * Post's User's Asset Relationship remove to DoodlyDoo server
 * 
 * @author Dileep | neuv
 * 
 */
public class UpdateAssetRelationshipThread extends Thread {
	private static final String TAG = "UpdateAssetRelationshipThread";

	private Context context;
	private String userId;
	private String assetId;
	private ASSOCIATION_TYPE relationship;
	private UpdateAssetRelationshipHandler handler;

	public UpdateAssetRelationshipThread(Context context, String userId, String assetId, ASSOCIATION_TYPE relationship,
			UpdateAssetRelationshipHandler handler) {
		super();
		this.context = context;
		this.userId = userId;
		this.assetId = assetId;
		this.relationship = relationship;
		this.handler = handler;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		willStartTask();
		UpdateAssetRelationshipHelper helper = new UpdateAssetRelationshipHelper(context);
		boolean isUpdated = helper.execute(createRequestJSON());
		if (isUpdated) {
			didUpdateRelationship();
		} else {
			failed(-1, -1, assetId, relationship);
		}
	}

	private void willStartTask() {
		if (handler != null) {
			handler.willStartTask();
		} else {
			Logger.warn(TAG, "Handler is null");
		}
	}

	private void failed(int statusCode, int errorCode, String assetId, ASSOCIATION_TYPE relationship) {
		if (handler != null) {
			handler.failed(statusCode, errorCode, assetId, relationship);
		} else {
			Logger.warn(TAG, "Handler is null");
		}
	}

	private void didUpdateRelationship() {
		if (handler != null) {
			handler.didUpdateRelationship(assetId, relationship);
		} else {
			Logger.warn(TAG, "Handler is null");
		}
	}

	private JSONObject createRequestJSON() {
		JSONObject requestJSON = null;
		if (!TextUtils.isEmpty(userId) && !TextUtils.isEmpty(assetId) && relationship != null) {
			try {
				requestJSON = new JSONObject();
				requestJSON.put(JSONConstants.USER_ID, userId);
				requestJSON.put(JSONConstants.ASSET_ID, assetId);
				requestJSON.put(JSONConstants.RELATIONSHIP, relationship.name());
				requestJSON.put(JSONConstants.CREATED_AT, Calendar.getInstance().getTimeInMillis());
			} catch (JSONException e) {
				requestJSON = null;
			}
		}
		return requestJSON;
	}

}
