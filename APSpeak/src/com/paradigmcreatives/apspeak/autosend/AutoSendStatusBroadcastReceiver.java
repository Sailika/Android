package com.paradigmcreatives.apspeak.autosend;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.paradigmcreatives.apspeak.globalstream.AppNewChildActivity;
import com.paradigmcreatives.apspeak.home.AppNewHomeActivity;

/**
 * BroadcastReceiver that is used when an expression is submitted successfully
 * 
 * @author Dileep | neuv
 * 
 */
public class AutoSendStatusBroadcastReceiver extends BroadcastReceiver {

	private Activity activity;

	public AutoSendStatusBroadcastReceiver(Activity activity) {
		super();
		this.activity = activity;
	}

	@Override
	public void onReceive(Context paramContext, Intent paramIntent) {
		if (activity != null) {
			if (activity instanceof AppNewChildActivity) {
				((AppNewChildActivity) activity).refreshQueueLayout();
			}
		}
	}

}
