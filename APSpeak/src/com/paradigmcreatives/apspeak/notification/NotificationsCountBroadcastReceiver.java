package com.paradigmcreatives.apspeak.notification;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.paradigmcreatives.apspeak.home.AppNewHomeActivity;

/**
 * Notifications Count broadcast receiver to be used by activitie(s)
 * 
 * @author Dileep | neuv
 * 
 */
public class NotificationsCountBroadcastReceiver extends BroadcastReceiver {

	private Activity activity;

	public NotificationsCountBroadcastReceiver(Activity activity) {
		super();
		this.activity = activity;
	}

	@Override
	public void onReceive(Context paramContext, Intent paramIntent) {
		if (activity != null) {
			if (activity instanceof AppNewHomeActivity) {
				((AppNewHomeActivity) activity).showHideNotificationsCount();
			}
		}
	}

}
