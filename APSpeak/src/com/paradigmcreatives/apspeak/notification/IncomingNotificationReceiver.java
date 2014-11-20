package com.paradigmcreatives.apspeak.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.gcm.PushNotificationConfig;
import com.paradigmcreatives.apspeak.user.tasks.GetFriendCompactProfileThread;

/**
 * Handles the incoming notifications from server
 * 
 * @author Dileep | neuv
 * 
 */
public class IncomingNotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
	String userId = null;
	String assetId = null;
	String announcementId = null;
	String message = null;

	if (intent.hasExtra(Constants.USERID)) {
	    userId = intent.getStringExtra(Constants.USERID);
	}
	if (intent.hasExtra(Constants.ASSETID)) {
	    assetId = intent.getStringExtra(Constants.ASSETID);
	}
	if (intent.hasExtra(Constants.ANNOUNCEMENTID)) {
	    announcementId = intent.getStringExtra(Constants.ANNOUNCEMENTID);
	}
	if (intent.hasExtra(Constants.ANNOUNCEMNETMESSAGE)) {
	    message = intent.getStringExtra(Constants.ANNOUNCEMNETMESSAGE);
	}
	if (intent.getAction().equals(PushNotificationConfig.CURRENT_PKG_NAME + Constants.USER_FOLLOWED)) {
	    GetFriendCompactProfileThread thread = new GetFriendCompactProfileThread(context, userId,
		    Constants.USER_FOLLOWED);
	    thread.start();
	} else if (intent.getAction().equals(PushNotificationConfig.CURRENT_PKG_NAME + Constants.NEW_FRIEND)) {
	    GetFriendCompactProfileThread thread = new GetFriendCompactProfileThread(context, userId, null, 
		    Constants.NEW_FRIEND, message);
	    thread.start();
	} else if (intent.getAction().equals(PushNotificationConfig.CURRENT_PKG_NAME + Constants.USER)) {
	    GetFriendCompactProfileThread thread = new GetFriendCompactProfileThread(context, userId, null,
		    Constants.USER, message);
	    thread.start();
	} else if (intent.getAction().equals(PushNotificationConfig.CURRENT_PKG_NAME + Constants.ASSET_LOVED)) {
	    GetFriendCompactProfileThread thread = new GetFriendCompactProfileThread(context, userId, assetId,
		    Constants.ASSET_LOVED, message);
	    thread.start();
	} else if (intent.getAction().equals(PushNotificationConfig.CURRENT_PKG_NAME + Constants.ASSET_COMMENTED)) {
	    GetFriendCompactProfileThread thread = new GetFriendCompactProfileThread(context, userId, assetId,
		    Constants.ASSET_COMMENTED);
	    thread.start();
	} else if (intent.getAction().equals(PushNotificationConfig.CURRENT_PKG_NAME + Constants.NEW_EXPRESSION)) {
	    GetFriendCompactProfileThread thread = new GetFriendCompactProfileThread(context, userId, assetId,
		    Constants.NEW_EXPRESSION);
	    thread.start();
	} else if (intent.getAction().equals(PushNotificationConfig.CURRENT_PKG_NAME + Constants.EXPRESSION)) {
	    // Show a notification with expression details
	    WhatsayNotificationManager.getInstance().sendExpressionNotificationNow(context, assetId, message);
	} else if (intent.getAction().equals(PushNotificationConfig.CURRENT_PKG_NAME + Constants.ANNOUNCEMENT)) {
	    // Show a notification with announcement details
	    WhatsayNotificationManager.getInstance().sendAnnouncementNotificationNow(context, announcementId, message);
	}
    }

}
