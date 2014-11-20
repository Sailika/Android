package com.paradigmcreatives.apspeak.assets.tasks;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.paradigmcreatives.apspeak.app.util.constants.JSONConstants;
import com.paradigmcreatives.apspeak.assets.handlers.AssetDeleteHandler;
import com.paradigmcreatives.apspeak.assets.tasks.helpers.AssetsDeleteHelper;
import com.paradigmcreatives.apspeak.logging.Logger;

public class AssetDeleteThread extends Thread {
	private static final String TAG = "AssetDeleteThread";

	private Context context;
	private String assetId;
	private AssetDeleteHandler handler;

	public AssetDeleteThread(Context context, String assetId,
			AssetDeleteHandler handler) {
		super();
		this.context = context;
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
		AssetsDeleteHelper helper = new AssetsDeleteHelper(context);
		boolean isDeleted = helper.execute(createRequestJSON());
		if (isDeleted) {
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
			handler.success(assetId);
		} else {
			Logger.warn(TAG, "Handler is null");
		}
	}

	private JSONObject createRequestJSON() {
		JSONObject requestJSON = null;
		if (!TextUtils.isEmpty(assetId)) {
			try {
				requestJSON = new JSONObject();
				requestJSON.put(JSONConstants.ASSET_ID, assetId);
			} catch (JSONException e) {
				requestJSON = null;
			}
		}
		return requestJSON;
	}

}
