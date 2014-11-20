package com.paradigmcreatives.apspeak.stream.tasks;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.paradigmcreatives.apspeak.app.model.ASSOCIATION_TYPE;
import com.paradigmcreatives.apspeak.app.util.constants.JSONConstants;
import com.paradigmcreatives.apspeak.logging.Logger;
import com.paradigmcreatives.apspeak.stream.handlers.RemoveAssetRelationshipHandler;
import com.paradigmcreatives.apspeak.stream.tasks.helpers.RemoveAssetRelationshipHelper;

/**
 * Post's User's Asset Relationship remove request to DoodlyDoo server
 * 
 * @author Dileep | neuv
 * 
 */
public class RemoveAssetRelationshipThread extends Thread {
	private static final String TAG = "RemoveAssetRelationshipThread";

	private Context context;
	private String userId;
	private String assetId;
	private ASSOCIATION_TYPE relationship;
	private RemoveAssetRelationshipHandler handler;

	public RemoveAssetRelationshipThread(Context context, String userId, String assetId, ASSOCIATION_TYPE relationship,
			RemoveAssetRelationshipHandler handler) {
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
		RemoveAssetRelationshipHelper helper = new RemoveAssetRelationshipHelper(context);
		boolean isUpdated = helper.execute(createRequestJSON());
		if (isUpdated) {
			didRemoveRelationship();
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

	private void didRemoveRelationship() {
		if (handler != null) {
			handler.didRemoveRelationship(assetId, relationship);
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
			} catch (JSONException e) {
				requestJSON = null;
			}
		}
		return requestJSON;
	}

}
