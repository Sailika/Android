package com.paradigmcreatives.apspeak.stream.tasks;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.paradigmcreatives.apspeak.app.util.constants.JSONConstants;
import com.paradigmcreatives.apspeak.logging.Logger;
import com.paradigmcreatives.apspeak.stream.handlers.AssetRepostHandler;
import com.paradigmcreatives.apspeak.stream.tasks.helpers.AssetRepostHelper;

/**
 * Performs Asset Repost request to DoodlyDoo server
 * 
 * @author Dileep | neuv
 * 
 */
public class AssetRepostThread extends Thread {
	private static final String TAG = "AssetRepostThread";

	private Context context;
	private String userId;
	private String assetId;
	private AssetRepostHandler handler;

	public AssetRepostThread(Context context, String userId, String assetId,
			AssetRepostHandler handler) {
		super();
		this.context = context;
		this.userId = userId;
		this.assetId = assetId;
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
		AssetRepostHelper helper = new AssetRepostHelper(context);
		boolean isUpdated = helper.execute(createRequestJSON());
		if (isUpdated) {
			didUpdateRelationship();
		} else {
			failed(-1, -1, assetId);
		}
	}

	private void willStartTask() {
		if (handler != null) {
			handler.willStartTask();
		} else {
			Logger.warn(TAG, "Handler is null");
		}
	}

	private void failed(int statusCode, int errorCode, String assetId) {
		if (handler != null) {
			handler.failed(statusCode, errorCode, assetId);
		} else {
			Logger.warn(TAG, "Handler is null");
		}
	}

	private void didUpdateRelationship() {
		if (handler != null) {
			handler.didUpdateRelationship(assetId);
		} else {
			Logger.warn(TAG, "Handler is null");
		}
	}

	private JSONObject createRequestJSON() {
		JSONObject requestJSON = null;
		if (!TextUtils.isEmpty(userId) && !TextUtils.isEmpty(assetId)) {
			try {
				requestJSON = new JSONObject();
				requestJSON.put(JSONConstants.ASSET_ID, assetId);
				requestJSON.put(JSONConstants.USER_ID, userId);
			} catch (JSONException e) {
				requestJSON = null;
			}
		}
		return requestJSON;
	}

}
