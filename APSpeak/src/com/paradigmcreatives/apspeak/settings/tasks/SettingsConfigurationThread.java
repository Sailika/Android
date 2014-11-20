package com.paradigmcreatives.apspeak.settings.tasks;

import android.content.Context;

import com.paradigmcreatives.apspeak.logging.Logger;
import com.paradigmcreatives.apspeak.settings.handlers.SettingsConfigurationHandler;
import com.paradigmcreatives.apspeak.settings.tasks.helpers.RequireFollowPermissionHelper;

/**
 * Sends User's Settings configuration to Whatsay server
 * 
 * @author Dileep | neuv
 * 
 */
public class SettingsConfigurationThread extends Thread {
    private static final String TAG = "SettingsConfigurationThread";

    private Context context;
    private String userId;
    private SettingsConfigurationHandler handler;

    public SettingsConfigurationThread(Context context, String userId, SettingsConfigurationHandler handler) {
	super();
	this.context = context;
	this.userId = userId;
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
	RequireFollowPermissionHelper helper = new RequireFollowPermissionHelper(context, userId, false);
	boolean isUpdated = helper.execute();
	if (isUpdated) {
	    didUpdateSettings();
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

    private void didUpdateSettings() {
	if (handler != null) {
	    handler.didUpdateSettings();
	} else {
	    Logger.warn(TAG, "Handler is null");
	}
    }
}
