package com.paradigmcreatives.apspeak;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.paradigmcreatives.apspeak.app.util.AppPropertiesUtil;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.gcm.GCMUtilClient;
import com.paradigmcreatives.apspeak.gcm.PushNotificationConfig;
import com.paradigmcreatives.apspeak.logging.Logger;
import com.paradigmcreatives.apspeak.notification.IncomingNotificationReceiver;
import com.paradigmcreatives.apspeak.registration.receivers.GCMRegistrationReceiver;

/**
 * This class is used as GCM listener for events like registration with GCM server, receiving a message and similar
 * other events.
 * 
 * @author Vishal | Paradigm Creatives
 */
public class GCMIntentService extends GCMBaseIntentService {
    private static final String TAG = "GCMIntentService";

    public GCMIntentService() {
	super(GCMUtilClient.SENDER_ID);
    }

    /**
     * Called when the device tries to register or unregister, but GCM returned an error.
     * 
     * @param context
     *            calling context.
     * @param errorID
     *            error id returned by the GCM service.
     */
    @Override
    protected void onError(Context context, String errorID) {
	Log.d(TAG, "Error occurred while registering to the gcm server" + errorID);
	Logger.fatal(TAG, " Error occurred while registering to the gcm server " + errorID);
	Intent onErrorIntent = new Intent("com.google.ctp.UPDATE_UI");
	onErrorIntent.addFlags(32);// Flag to include stopped packages
	context.sendBroadcast(onErrorIntent);
    }// end of onError()

    /**
     * Called when a cloud message has been received.
     * 
     * @param context
     *            calling context.
     * @param intent
     *            intent containing the message payload as extras.
     */
    @Override
    protected void onMessage(Context context, Intent intent) {
	Logger.info(TAG, "Received a message.");
	Bundle extras = intent.getExtras();
	if (extras != null) {
	    /*
	     * According to our conventions ... we will receive message in the form of pkg_name:<message> so checking
	     * for package name.
	     */
	    String message = extras.containsKey("message") ? extras.getString("message") : null;
	    if (message != null && message.contains(":")) {
		Logger.info(TAG, "Message received: " + message);
		if (message.startsWith(PushNotificationConfig.CURRENT_PKG_NAME)) {
		    int columnPosition = message.indexOf(":");
		    if (message.length() > columnPosition + 1) {
			String dataFromServer = null;
			try {
			    dataFromServer = message.substring(columnPosition + 1);
			    if (dataFromServer.startsWith(PushNotificationConfig.PROTO)) {
				int updateStringIndex = dataFromServer.indexOf(":");
				if (dataFromServer.length() > updateStringIndex + 1) {
				    String updateString = dataFromServer.substring(dataFromServer.indexOf(":") + 1);
				    String[] data = updateString.split(",");
				    if (data != null && data.length >= 2) {
					String notificationType = data[0];
					String notificationValue = data[1];
					if (!TextUtils.isEmpty(notificationType)
						&& !TextUtils.isEmpty(notificationValue)) {
					    if (TextUtils.equals(notificationType, Constants.ASSET_LOVED)) {
						if (data.length >= 4) {
						    String assetId = data[2];
						    String messageToShow = data[3];
						    sendNotificationReceivedBroadcast(context, notificationType,
							    notificationValue, assetId, null, messageToShow);
						}
					    } else if (TextUtils.equals(notificationType, Constants.ASSET_COMMENTED)) {
						if (data.length >= 3) {
						    String assetId = data[2];
						    sendNotificationReceivedBroadcast(context, notificationType,
							    notificationValue, assetId, null, null);
						}
					    } else if (TextUtils.equals(notificationType, Constants.NEW_EXPRESSION)) {
						if (data.length >= 4) {
						    String expressionId = notificationValue;
						    String userId = data[2];
						    String messageToShow = data[3];
						    sendNotificationReceivedBroadcast(context, notificationType,
							    userId, expressionId, null, messageToShow);
						}
					    } else if(TextUtils.equals(notificationType, Constants.EXPRESSION)){
						if (data.length >= 3) {
						    String expressionId = notificationValue;
						    String messageToShow = data[2];
						    sendNotificationReceivedBroadcast(context, notificationType,
							    null, expressionId, null, messageToShow);
						}
					    } else if (TextUtils.equals(notificationType, Constants.USER_FOLLOWED)) {
						sendNotificationReceivedBroadcast(context, notificationType,
							notificationValue, null, null, null);
					    } else if (TextUtils.equals(notificationType, Constants.NEW_FRIEND)) {
					    	int index = data.length;
					    	String messageToShow = data[index-1];
						sendNotificationReceivedBroadcast(context, notificationType,
							notificationValue, null, null, messageToShow);
					    } else if (TextUtils.equals(notificationType, Constants.ANNOUNCEMENT)) {
						if (data.length >= 3) {
						    String announcementId = notificationValue;
						    String messageToShow = data[2];
						    sendNotificationReceivedBroadcast(context, notificationType,
							    notificationValue, null, announcementId,
							    messageToShow);
						}
					    } else if(TextUtils.equals(notificationType, Constants.USER)){
						if (data.length >= 3) {
						    String messageToShow = data[2];
						    sendNotificationReceivedBroadcast(context, notificationType,
							    notificationValue, null, null,
							    messageToShow);
						}
					    } else if(TextUtils.equals(notificationType, Constants.SHOW_FEATURED)) {
						boolean isFeaturedEnabled = Boolean.getBoolean(notificationValue);
						AppPropertiesUtil.setFeaturedFlag(context, isFeaturedEnabled);
					    } else {
						Logger.warn(TAG, "Unknown notification type");
					    }
					} else {
					    Logger.warn(TAG, "Invalid doodle status: " + notificationType
						    + " or uuid: " + notificationValue + " in push message");
					}
				    } else {
					Logger.warn(TAG, " Invalid Push Message: " + message);
				    }
				} else {
				    Logger.warn(TAG, "Invalid Push update message: " + dataFromServer
					    + ". Length is less than required.");
				}
			    }
			} catch (IndexOutOfBoundsException ioobe) {
			    Logger.fatal(TAG, "Error in server message: " + dataFromServer + ". Exception: " + ioobe);
			}
		    } else {
			Logger.warn(TAG, "Invalid Push message: " + message + ". Length is less than required.");
		    }
		} else {
		    Logger.warn(TAG, "Invalid Push message: " + message + ". Doesn't belongs to app.");
		}
	    } else {
		Logger.warn(TAG, "Invalid Push message: " + message + ". Message is null or doesn't contains \":\"");
	    }
	} else {
	    Logger.warn(TAG, "Empty intent bundle.");
	}
    }

    /**
     * Called after a device has been registered.
     * 
     * @param context
     *            calling context.
     * @param registrationId
     *            the registration id returned by the GCM service.
     */
    @Override
    protected void onRegistered(Context context, String registrationID) {
	Logger.info(TAG, "Registered successfully. Registration ID: " + registrationID);
	SharedPreferences appPref = context.getSharedPreferences(Constants.KEY_APP_PREFERENCE_NAME,
		Context.MODE_PRIVATE);
	SharedPreferences.Editor editor = appPref.edit();

	editor.putString(Constants.KEY_GCM_ID, registrationID);
	editor.commit();
	broadcastRegistrationSuccess(context);
    }// end of onRegistered()

    /**
     * Called after a device has been unregistered.
     * 
     * @param context
     *            calling context.
     * @param registrationId
     *            the registration id that was previously registered.
     */
    @Override
    protected void onUnregistered(Context context, String registrationID) {
	Logger.info(TAG, "UNRegistered ID: " + registrationID + " with the GCM server successfully.");

	if (PushNotificationConfig.AUTO_RE_REGISTER_WHEN_UNREGISTERED) {
	    GCMUtilClient.register(context);
	}
    }// end of onUnregistered()

    /**
     * Gets the Sender IDs.
     */
    @Override
    protected String[] getSenderIds(Context context) {
	return super.getSenderIds(context);
    }// end of getSenderIds()

    /**
     * Called when Registration is successful.
     * 
     * @param context
     *            calling context.
     */
    private void broadcastRegistrationSuccess(Context context) {
	if (context != null) {
	    String CUSTOM_INTENT = PushNotificationConfig.CURRENT_PKG_NAME + Constants.GCM_REGISTRATION_NOTIFICATION;
	    Log.d(TAG, "Broadcasting to PushManager");
	    Intent intent = new Intent(context, GCMRegistrationReceiver.class);
	    intent.setAction(CUSTOM_INTENT);
	    intent.addFlags(32);// Flag to include stopped packages
	    context.sendBroadcast(intent);
	} else {
	    Logger.warn(TAG, "Context is null");
	}
    }// end of broadcastRegistrationSuccess()

    /**
     * Sends a broadcast saying Notification has received
     * 
     * @param context
     * @param notificationType
     * @param userId
     */
    private void sendNotificationReceivedBroadcast(Context context, String notificationType, String userId,
	    String assetId, String announcementId, String message) {
	if (context != null && !TextUtils.isEmpty(notificationType)) {
		// Update Notifications Count value
		int notificationsCount = AppPropertiesUtil.getNotificationsCount(context);
		AppPropertiesUtil.setNotificationsCount(context, ++notificationsCount);
		
			// Send Broadcast of notifications count update
			Intent notificationCountBroadcast = new Intent();
			notificationCountBroadcast.setAction(Constants.NOTIFICATIONSCOUNT_BROADCAST_ACTION);
			context.sendBroadcast(notificationCountBroadcast);
		
	    Intent notificationIntent = new Intent(context, IncomingNotificationReceiver.class);
	    String CUSTOM_INTENT = PushNotificationConfig.CURRENT_PKG_NAME + notificationType;
	    notificationIntent.putExtra(Constants.USERID, userId);
	    if (!TextUtils.isEmpty(assetId)) {
		notificationIntent.putExtra(Constants.ASSETID, assetId);
	    }
	    if (!TextUtils.isEmpty(announcementId)) {
		notificationIntent.putExtra(Constants.ANNOUNCEMENTID, announcementId);
	    }
	    if (!TextUtils.isEmpty(message)) {
		notificationIntent.putExtra(Constants.ANNOUNCEMNETMESSAGE, message);
	    }
	    notificationIntent.setAction(CUSTOM_INTENT);
	    notificationIntent.addFlags(32);// Flag to include stopped packages
	    context.sendBroadcast(notificationIntent);
	} else {
	    Logger.warn(TAG, "invalid values");
	}
    }

}// end of class