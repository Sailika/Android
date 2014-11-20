package com.paradigmcreatives.apspeak.registration.handlers;

import android.os.Handler;
import android.os.Message;

import com.paradigmcreatives.apspeak.logging.Logger;

/**
 * Handles the messaging during update of gcm id
 * 
 * @author Soumya Behera | Paradigm Creatives
 * 
 */
public class UpdateGCMIdHandler extends Handler {
    private static final String TAG = "UpdateGCMIdHandler";

    private static final int PRE_EXECUTE = 1;
    private static final int SUCCESS = 2;
    private static final int FAILURE = 3;

    public void willStartTask() {
	sendEmptyMessage(PRE_EXECUTE);
    }

    public void didUpdate() {
	sendEmptyMessage(SUCCESS);
    }

    public void failed(int httpStatusCode, int errorCode, String reason) {
	Message msg = new Message();
	msg.what = FAILURE;
	msg.arg1 = httpStatusCode;
	msg.arg2 = errorCode;
	msg.obj = reason;
	sendMessage(msg);
    }

    @Override
    public void handleMessage(Message msg) {
	if (msg.what == PRE_EXECUTE) {
	} else if (msg.what == SUCCESS) {
	    Logger.info(TAG, "updated new push id to server");
	} else if (msg.what == FAILURE) {
	    Logger.fatal(TAG, "Failed to update push id to server");
	}
	super.handleMessage(msg);
    }
}
