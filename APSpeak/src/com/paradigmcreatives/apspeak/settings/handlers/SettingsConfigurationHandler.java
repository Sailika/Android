package com.paradigmcreatives.apspeak.settings.handlers;


import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import com.paradigmcreatives.apspeak.app.util.AppPropertiesUtil;
import com.paradigmcreatives.apspeak.logging.Logger;
/**
 * Message handler for User's Settings Configuration request
 *
 * @author Dileep | neuv
 *
 */
public class SettingsConfigurationHandler extends Handler {
	private static final String TAG = "SettingsConfigurationHandler";
	private static final int PRE_EXECUTE = 1;
	private static final int SUCCESS = 2;
	private static final int FAILURE = 3;
	private Fragment fragment;
	public SettingsConfigurationHandler(Fragment fragment) {
		super();
		this.fragment = fragment;
	}
	public void willStartTask() {
		sendEmptyMessage(PRE_EXECUTE);
	}
	public void didUpdateSettings() {
		Message msg = new Message();
		msg.what = SUCCESS;
		sendMessage(msg);
	}
	public void failed(int statusCode, int errorCode) {
		Message msg = new Message();
		msg.what = FAILURE;
		msg.arg1 = statusCode;
		msg.arg2 = errorCode;
		sendMessage(msg);
	}
	/*
	 * (non-Javadoc)
	 *
	 * @see android.os.Handler#handleMessage(android.os.Message)
	 */
	@Override
	public void handleMessage(Message msg) {
		if (fragment != null) {
			switch (msg.what) {
			case PRE_EXECUTE:
				// do nothing
				break;
			case SUCCESS:
				// TODO:
				break;
			case FAILURE:
				// TODO:
				break;
			default:
				break;
			}
		} else {
			Logger.warn(TAG, "Context is null");
		}
		super.handleMessage(msg);
	}
}