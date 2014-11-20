package com.paradigmcreatives.apspeak.assets.tasks;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.paradigmcreatives.apspeak.app.util.constants.JSONConstants;
import com.paradigmcreatives.apspeak.assets.handlers.InappropriateFlagHandler;
import com.paradigmcreatives.apspeak.assets.tasks.helpers.UserInappropriateHelper;
import com.paradigmcreatives.apspeak.logging.Logger;

public class UserInappropriateThread extends Thread {
	private static final String TAG = "AssetDeleteThread";

	private Context context;
	private String userId;
	private String flaggedUserId;
	private InappropriateFlagHandler handler;

	public UserInappropriateThread(Context context, String userId,
			String flaggedUserId, InappropriateFlagHandler handler) {
		super();
		this.context = context;
		this.userId = userId;
		this.flaggedUserId = flaggedUserId;
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
		UserInappropriateHelper helper = new UserInappropriateHelper(context);
		boolean isFlagged = helper.execute(createRequestJSON());
		if (isFlagged) {
			success();
		} else {
			failed(-1, -1);
		}

	}

	private void willStartTask() {
		if (handler != null) {
			handler.willStartTask();
		} else {
			Logger.warn(TAG, "Handler is null");
		}
	}

	private void failed(int statusCode, int errorCode) {
		if (handler != null) {
			handler.failed(statusCode, errorCode);
		} else {
			Logger.warn(TAG, "Handler is null");
		}
	}

	private void success() {
		if (handler != null) {
			handler.success();
		} else {
			Logger.warn(TAG, "Handler is null");
		}
	}

	private JSONObject createRequestJSON() {
		JSONObject requestJSON = null;
		if (!TextUtils.isEmpty(userId) && !TextUtils.isEmpty(flaggedUserId)) {
			try {
				requestJSON = new JSONObject();
				requestJSON.put(JSONConstants.USER_ID, userId);
				requestJSON.put(JSONConstants.FLAGGED_USER_ID, flaggedUserId);
				requestJSON.put(JSONConstants.FLAG_TYPE, "inappropriate");
			} catch (JSONException e) {
				requestJSON = null;
			}
		}
		return requestJSON;
	}

}
