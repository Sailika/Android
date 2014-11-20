package com.paradigmcreatives.apspeak.notification;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.app.alert.AlertReceiver;
import com.paradigmcreatives.apspeak.app.model.Friend;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.gcm.PushNotificationConfig;
import com.paradigmcreatives.apspeak.home.AppNewHomeActivity;
import com.paradigmcreatives.apspeak.logging.Logger;
import com.paradigmcreatives.apspeak.stream.AppChildActivity;
import com.paradigmcreatives.apspeak.user.UserProfileActivity;

/**
 * NotificationManager is the centralized class that handles all the actions related to Notification(s)
 * 
 * @author Dileep | neuv
 * 
 */
public class WhatsayNotificationManager {

    private final String TAG = "NotificationManager";
    private static WhatsayNotificationManager manager = null;

    private PendingIntent unreadCountPendingIntent;

    /**
     * Type of the Notification message(s) related to content/asset(s)
     * 
     * @author Dileep | neuv
     * 
     */
    public enum ContentMessageType {
	ASSET_ADDED, UNREAD_COUNT, NEWDOODLE_FROM, NEWDOODLE_COUNT, DOODLE_DELIVERED
    }

    /**
     * Type of the Notification message(s) related to Social Media
     * 
     * @author Dileep | neuv
     * 
     */
    public enum SocialMessageType {
	FRIEND_JOINED, FRIEND_BIRTHDAY, FRIEND_FOLLOWING_ME
    }

    /**
     * Private Constructor
     */
    private WhatsayNotificationManager() {

    }

    /**
     * Creates(if not available) and returns an instance of NotificationManager
     * 
     * @return NotificationManager, instance
     */
    public static WhatsayNotificationManager getInstance() {
	if (manager == null) {
	    manager = new WhatsayNotificationManager();
	}
	return manager;
    }

    /**
     * Broadcasts notification message, so that, the notification message is displayed on Notification bar
     * 
     * @param context
     *            context to be used for broadcasting
     * @param alertMessage
     *            message to be displayed on the notificatio bar
     */
    /*
     * public void sendNotificationNow(final Context context, String alertMessage) { if (context != null &&
     * !TextUtils.isEmpty(alertMessage)) { Intent notificationIntent = new Intent(context, AlertMessageActivity.class);
     * String CUSTOM_INTENT = PushNotificationConfig.CURRENT_PKG_NAME + Constants.PUSH_NOTIFICATION_ALERT_RECEIVED;
     * Intent intent = new Intent(context, AlertReciever.class); intent.putExtra(Constants.ALERT_MESSAGE, alertMessage);
     * intent.putExtra(Constants.NOTIFICATION_INTENT, notificationIntent); intent.setAction(CUSTOM_INTENT);
     * intent.addFlags(32);// Flag to include stopped packages context.sendBroadcast(intent); } else { Logger.warn(TAG,
     * "invalid values"); } }
     */

    /**
     * Broadcasts notification with passed intent content
     * 
     * @param context
     *            context to be used for broadcasting
     * @param intent
     */
    public void sendNotificationNow(final Context context, Intent intent) {
	if (context != null && intent != null) {
	    context.sendBroadcast(intent);
	} else {
	    Logger.warn(TAG, "invalid values");
	}
    }

    /**
     * Broadcasts notification that says new friend joined
     * 
     * @param context
     *            context to be used for broadcasting
     */
    public void sendNewFriendJoinedNotificationNow(final Context context, final Friend friend, final String messageToShow) {
	if (context != null) {
	    context.sendBroadcast(createIntentWithNewFriendDetialsAndAction(context, friend, messageToShow));
	} else {
	    Logger.warn(TAG, "invalid context");
	}
    }

    /**
     * Broadcasts notification that says user/friend followed i.e. user/friend is following me
     * 
     * @param context
     *            context to be used for broadcasting
     */
    public void sendUserFollowedNotificationNow(final Context context, final Friend friend) {
	if (context != null) {
	    context.sendBroadcast(createIntentWithUserFollowedDetailsAndAction(context, friend));
	} else {
	    Logger.warn(TAG, "invalid context");
	}
    }

    /**
     * Broadcasts notification that says about a user
     * 
     * @param context
     *            context to be used for broadcasting
     */
    public void sendUserNotificationNow(final Context context, final Friend friend, String message) {
	if (context != null) {
	    context.sendBroadcast(createIntentWithUserDetailsAndAction(context, friend, message));
	} else {
	    Logger.warn(TAG, "invalid context");
	}
    }

    /**
     * Broadcasts notification that says user/friend loved your Whatsay expression
     * 
     * @param context
     *            context to be used for broadcasting
     */
    public void sendAssetLovedNotificationNow(final Context context, final Friend friend, final String assetId, final String messageToShow) {
	if (context != null) {
	    context.sendBroadcast(createIntentWithAssetLovedDetailsAndAction(context, friend, assetId, messageToShow));
	} else {
	    Logger.warn(TAG, "invalid context");
	}
    }

    /**
     * Broadcasts notification that says user/friend commented your Whatsay expression
     * 
     * @param context
     *            context to be used for broadcasting
     */
    public void sendAssetCommentedNotificationNow(final Context context, final Friend friend, final String assetId) {
	if (context != null) {
	    context.sendBroadcast(createIntentWithAssetCommentedDetailsAndAction(context, friend, assetId));
	} else {
	    Logger.warn(TAG, "invalid context");
	}
    }

    /**
     * Broadcasts notification that says user/friend posted new Whatsay asset/expression
     * 
     * @param context
     *            context to be used for broadcasting
     */
    public void sendNewAssetNotificationNow(final Context context, final Friend friend, final String assetId) {
	if (context != null) {
	    context.sendBroadcast(createIntentWithNewAssetDetailsAndAction(context, friend, assetId));
	} else {
	    Logger.warn(TAG, "invalid context");
	}
    }

    /**
     * Broadcasts notification that shows announcement
     * 
     * @param context
     *            context to be used for broadcasting
     */
    public void sendAnnouncementNotificationNow(final Context context, final String announcementId,
	    final String announcementMsg) {
	if (context != null) {
	    context.sendBroadcast(createIntentWithAnnouncementDetailsAndAction(context, announcementId, announcementMsg));
	} else {
	    Logger.warn(TAG, "invalid context");
	}
    }

    /**
     * Broadcasts notification that shows expression
     * 
     * @param context
     *            context to be used for broadcasting
     */
    public void sendExpressionNotificationNow(final Context context, final String expressionId,
	    final String announcementMsg) {
	if (context != null) {
	    context.sendBroadcast(createIntentWithExpressionDetailsAndAction(context, expressionId, announcementMsg));
	} else {
	    Logger.warn(TAG, "invalid context");
	}
    }

    /**
     * Broadcasts notification that says user/friend reposted your doodlydoo asset
     * 
     * @param context
     *            context to be used for broadcasting
     */
    /*
     * public void sendAssetRepostedNotificationNow(final Context context, final Friend friend, final String assetId) {
     * if (context != null) { context.sendBroadcast(createIntentWithAssetRepostedDetailsAndAction(context, friend,
     * assetId)); } else { Logger.warn(TAG, "invalid context"); } }
     */

    /**
     * Stops the periodic notifications of Unread Doodles Count
     * 
     * @param context
     */
    /*
     * public void stopPeriodicUnreadCountNotifications(final Context context) { if (context != null &&
     * unreadCountPendingIntent != null) { AlarmManager am = (AlarmManager)
     * context.getSystemService(Context.ALARM_SERVICE); am.cancel(unreadCountPendingIntent); unreadCountPendingIntent =
     * null; setPeriodicUnreadStartedPrefValue(context, false); } }
     */

    /**
     * Verifies whether preference value that represents periodic unread doodles count notification has started or not
     * 
     * @param context
     * @return
     */
    /*
     * private boolean isPeriodicUnreadStarted(final Context context) { boolean isStarted = false; if (context != null)
     * { SharedPreferences prefs = context.getSharedPreferences(Constants.APP_NAME, Context.MODE_PRIVATE); if (prefs !=
     * null && prefs.contains(Constants.PREF_NOTIFICATION_UNREAD)) { isStarted =
     * prefs.getBoolean(Constants.PREF_NOTIFICATION_UNREAD, false); } } return isStarted; }
     */

    /**
     * Sets the periodic unread doodles count value in shared preferences
     * 
     * @param context
     * @param value
     */
    /*
     * private void setPeriodicUnreadStartedPrefValue(final Context context, boolean value) { if (context != null) {
     * SharedPreferences prefs = context.getSharedPreferences(Constants.APP_NAME, Context.MODE_PRIVATE); if (prefs !=
     * null) { SharedPreferences.Editor editor = prefs.edit(); editor.putBoolean(Constants.PREF_NOTIFICATION_UNREAD,
     * value); } } }
     */

    /**
     * Creates and intent with new friend joined details, respective alert message and action to be performed
     * 
     * @param friend
     *            object of Friend
     * @return Intent, Intent object with message, action
     */
    private Intent createIntentWithNewFriendDetialsAndAction(Context context, Friend friend, String messageToShow) {
	Intent intent = null;
	if (context != null && friend != null) {
	    String CUSTOM_INTENT = PushNotificationConfig.CURRENT_PKG_NAME + Constants.NEW_FRIEND;
	    Intent notificationIntent = new Intent(context, UserProfileActivity.class);
	    notificationIntent.putExtra(Constants.USERID, friend.getUserId());

	    String alertMessage = messageToShow;
	    if(TextUtils.isEmpty(alertMessage)){
		    String friendName = friend.getName();
		    if (friendName != null && friendName.length() > 0) {
			alertMessage = friendName + " " + context.getResources().getString(R.string.joined);
		    } else {
			alertMessage = context.getResources().getString(R.string.new_friend_joined);
		    }
	    }
	    intent = new Intent(context, AlertReceiver.class);
	    intent.putExtra(Constants.ALERT_MESSAGE, alertMessage);
	    Bitmap bmp = friend.getProfilePicBitmap();
	    if (bmp != null) {
		intent.putExtra(Constants.NOTIFICATION_LARGE_ICON, bmp);
	    } else {
		bmp = friend.getCoverImageBitmap();
		if (bmp != null) {
		    intent.putExtra(Constants.NOTIFICATION_LARGE_ICON, bmp);
		}
	    }
	    intent.putExtra(Constants.NOTIFICATION_INTENT, notificationIntent);
	    intent.setAction(CUSTOM_INTENT);
	    intent.addFlags(32);// Flag to include stopped packages
	}
	return intent;
    }

    /**
     * Creates and intent with user/friend following(i.e. user/friend is following me) details, respective alert message
     * and action to be performed
     * 
     * @param friend
     *            object of Friend
     * @return Intent, Intent object with message, action
     */
    private Intent createIntentWithUserFollowedDetailsAndAction(Context context, Friend friend) {
	Intent intent = null;
	if (context != null && friend != null) {
	    String CUSTOM_INTENT = PushNotificationConfig.CURRENT_PKG_NAME + Constants.USER_FOLLOWED;
	    Intent notificationIntent = new Intent(context, UserProfileActivity.class);
	    notificationIntent.putExtra(Constants.USERID, friend.getUserId());

	    String alertMessage = null;
	    String friendName = friend.getName();
	    if (friendName != null && friendName.length() > 0) {
		alertMessage = friendName + " " + context.getResources().getString(R.string.following);
	    } else {
		alertMessage = context.getResources().getString(R.string.friend_following);
	    }
	    intent = new Intent(context, AlertReceiver.class);
	    intent.putExtra(Constants.ALERT_MESSAGE, alertMessage);
	    Bitmap bmp = friend.getProfilePicBitmap();
	    if (bmp != null) {
		intent.putExtra(Constants.NOTIFICATION_LARGE_ICON, bmp);
	    } else {
		bmp = friend.getCoverImageBitmap();
		if (bmp != null) {
		    intent.putExtra(Constants.NOTIFICATION_LARGE_ICON, bmp);
		}
	    }
	    intent.putExtra(Constants.NOTIFICATION_INTENT, notificationIntent);
	    intent.setAction(CUSTOM_INTENT);
	    intent.addFlags(32);// Flag to include stopped packages
	}
	return intent;
    }

    /**
     * Creates and intent with user details, respective alert message
     * and action to be performed
     * 
     * @param friend
     *            object of Friend
     * @return Intent, Intent object with message, action
     */
    private Intent createIntentWithUserDetailsAndAction(Context context, Friend friend, String message) {
	Intent intent = null;
	if (context != null && friend != null) {
	    String CUSTOM_INTENT = PushNotificationConfig.CURRENT_PKG_NAME + Constants.USER;
	    Intent notificationIntent = new Intent(context, UserProfileActivity.class);
	    notificationIntent.putExtra(Constants.USERID, friend.getUserId());

	    String alertMessage = message;
	    intent = new Intent(context, AlertReceiver.class);
	    intent.putExtra(Constants.ALERT_MESSAGE, alertMessage);
	    Bitmap bmp = friend.getProfilePicBitmap();
	    if (bmp != null) {
		intent.putExtra(Constants.NOTIFICATION_LARGE_ICON, bmp);
	    } else {
		bmp = friend.getCoverImageBitmap();
		if (bmp != null) {
		    intent.putExtra(Constants.NOTIFICATION_LARGE_ICON, bmp);
		}
	    }
	    intent.putExtra(Constants.NOTIFICATION_INTENT, notificationIntent);
	    intent.setAction(CUSTOM_INTENT);
	    intent.addFlags(32);// Flag to include stopped packages
	}
	return intent;
    }

    /**
     * Creates and intent with asset loved details, respective alert message and action to be performed
     * 
     * @param friend
     *            object of Friend
     * @param assetId
     *            asset id
     * @return Intent, Intent object with message, action
     */
    private Intent createIntentWithAssetLovedDetailsAndAction(Context context, Friend friend, String assetId, String messageToShow) {
	Intent intent = null;
	if (context != null && friend != null) {
	    String CUSTOM_INTENT = PushNotificationConfig.CURRENT_PKG_NAME + Constants.ASSET_LOVED;
	    Intent notificationIntent = new Intent(context, AppChildActivity.class);
	    notificationIntent.putExtra(Constants.LAUNCH_ASSET_DETAILS, true);
	    notificationIntent.putExtra(Constants.USERID, friend.getUserId());
	    notificationIntent.putExtra(Constants.ASSETID, assetId);

	    String alertMessage = messageToShow;
	    if(TextUtils.isEmpty(alertMessage)){
		    String friendName = friend.getName();
		    if (friendName != null && friendName.length() > 0) {
			alertMessage = friendName + " " + context.getResources().getString(R.string.loved_your_asset);
		    } else {
			alertMessage = context.getResources().getString(R.string.friend_loved_your_asset);
		    }
	    }
	    intent = new Intent(context, AlertReceiver.class);
	    intent.putExtra(Constants.ALERT_MESSAGE, alertMessage);
	    try{
		    Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.like_selected);
		    if (bmp != null) {
			intent.putExtra(Constants.NOTIFICATION_LARGE_ICON, bmp);
		    }
	    }catch(Exception e){
	    	
	    }
	    intent.putExtra(Constants.NOTIFICATION_INTENT, notificationIntent);
	    intent.setAction(CUSTOM_INTENT);
	    intent.addFlags(32);// Flag to include stopped packages
	}
	return intent;
    }

    /**
     * Creates and intent with asset commented details, respective alert message and action to be performed
     * 
     * @param friend
     *            object of Friend
     * @param assetId
     *            asset id
     * @return Intent, Intent object with message, action
     */
    private Intent createIntentWithAssetCommentedDetailsAndAction(Context context, Friend friend, String assetId) {
	Intent intent = null;
	if (context != null && friend != null) {
	    String CUSTOM_INTENT = PushNotificationConfig.CURRENT_PKG_NAME + Constants.ASSET_COMMENTED;
	    Intent notificationIntent = new Intent(context, AppChildActivity.class);
	    notificationIntent.putExtra(Constants.LAUNCH_ASSET_DETAILS, true);
	    notificationIntent.putExtra(Constants.USERID, friend.getUserId());
	    notificationIntent.putExtra(Constants.ASSETID, assetId);

	    String alertMessage = null;
	    String friendName = friend.getName();
	    if (friendName != null && friendName.length() > 0) {
		alertMessage = friendName + " " + context.getResources().getString(R.string.commented_your_asset);
	    } else {
		alertMessage = context.getResources().getString(R.string.friend_commented_your_asset);
	    }
	    intent = new Intent(context, AlertReceiver.class);
	    intent.putExtra(Constants.ALERT_MESSAGE, alertMessage);
	    Bitmap bmp = friend.getProfilePicBitmap();
	    if (bmp != null) {
		intent.putExtra(Constants.NOTIFICATION_LARGE_ICON, bmp);
	    } else {
		bmp = friend.getCoverImageBitmap();
		if (bmp != null) {
		    intent.putExtra(Constants.NOTIFICATION_LARGE_ICON, bmp);
		}
	    }
	    intent.putExtra(Constants.NOTIFICATION_INTENT, notificationIntent);
	    intent.setAction(CUSTOM_INTENT);
	    intent.addFlags(32);// Flag to include stopped packages
	}
	return intent;
    }

    /**
     * Creates and intent with new asset details, respective alert message and action to be performed
     * 
     * @param friend
     *            object of Friend
     * @param assetId
     *            asset id
     * @return Intent, Intent object with message, action
     */
    private Intent createIntentWithNewAssetDetailsAndAction(Context context, Friend friend, String assetId) {
	Intent intent = null;
	if (context != null && friend != null) {
	    String CUSTOM_INTENT = PushNotificationConfig.CURRENT_PKG_NAME + Constants.NEW_EXPRESSION;
	    Intent notificationIntent = new Intent(context, AppChildActivity.class);
	    notificationIntent.putExtra(Constants.LAUNCH_ASSET_DETAILS, true);
	    notificationIntent.putExtra(Constants.USERID, friend.getUserId());
	    notificationIntent.putExtra(Constants.ASSETID, assetId);

	    String alertMessage = null;
	    String friendName = friend.getName();
	    if (friendName != null && friendName.length() > 0) {
		alertMessage = friendName + " " + context.getResources().getString(R.string.new_expression_posted);
	    } else {
		alertMessage = context.getResources().getString(R.string.friend_new_expression_posted);
	    }
	    intent = new Intent(context, AlertReceiver.class);
	    intent.putExtra(Constants.ALERT_MESSAGE, alertMessage);
	    Bitmap bmp = friend.getProfilePicBitmap();
	    if (bmp != null) {
		intent.putExtra(Constants.NOTIFICATION_LARGE_ICON, bmp);
	    } else {
		bmp = friend.getCoverImageBitmap();
		if (bmp != null) {
		    intent.putExtra(Constants.NOTIFICATION_LARGE_ICON, bmp);
		}
	    }
	    intent.putExtra(Constants.NOTIFICATION_INTENT, notificationIntent);
	    intent.setAction(CUSTOM_INTENT);
	    intent.addFlags(32);// Flag to include stopped packages
	}
	return intent;
    }

    /**
     * Creates an intent with announcement details and action to be performed
     * 
     * @param announcementId
     *            announcement id
     * @return Intent, Intent object with message, action
     */
    private Intent createIntentWithAnnouncementDetailsAndAction(Context context, String announcementId,
	    String announementMsg) {
	Intent intent = null;
	if (context != null) {
	    String CUSTOM_INTENT = PushNotificationConfig.CURRENT_PKG_NAME + Constants.ANNOUNCEMENT;
	    Intent notificationIntent = new Intent(context, AppNewHomeActivity.class);
	    notificationIntent.putExtra(Constants.LAUNCH_NOTIFICATIONS_FEED_SCREEN, true);
	    notificationIntent.putExtra(Constants.ANNOUNCEMENTID, announcementId);
	    notificationIntent.putExtra(Constants.ANNOUNCEMNETMESSAGE, announementMsg);

	    String alertMessage = announementMsg;
	    intent = new Intent(context, AlertReceiver.class);
	    intent.putExtra(Constants.ALERT_MESSAGE, alertMessage);
	    Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.announcement);
	    if (bmp != null) {
		intent.putExtra(Constants.NOTIFICATION_LARGE_ICON, bmp);
	    }
	    intent.putExtra(Constants.NOTIFICATION_INTENT, notificationIntent);
	    intent.setAction(CUSTOM_INTENT);
	    intent.addFlags(32);// Flag to include stopped packages
	}
	return intent;
    }

    /**
     * Creates and intent with expression details, respective alert message and action to be performed
     * 
     * @param friend
     *            object of Friend
     * @param assetId
     *            asset id
     * @return Intent, Intent object with message, action
     */
    private Intent createIntentWithExpressionDetailsAndAction(Context context, String assetId, String message) {
	Intent intent = null;
	if (context != null) {
	    String CUSTOM_INTENT = PushNotificationConfig.CURRENT_PKG_NAME + Constants.EXPRESSION;
	    Intent notificationIntent = new Intent(context, AppChildActivity.class);
	    notificationIntent.putExtra(Constants.LAUNCH_ASSET_DETAILS, true);
	    notificationIntent.putExtra(Constants.ASSETID, assetId);

	    String alertMessage = message;
	    intent = new Intent(context, AlertReceiver.class);
	    intent.putExtra(Constants.ALERT_MESSAGE, alertMessage);
	    //intent.putExtra(Constants.NOTIFICATION_LARGE_ICON, bmp);
	    intent.putExtra(Constants.NOTIFICATION_INTENT, notificationIntent);
	    intent.setAction(CUSTOM_INTENT);
	    intent.addFlags(32);// Flag to include stopped packages
	}
	return intent;
    }

    /**
     * Creates and intent with asset reposted details, respective alert message and action to be performed
     * 
     * @param friend
     *            object of Friend
     * @param assetId
     *            asset id
     * @return Intent, Intent object with message, action
     */
    /*
     * private Intent createIntentWithAssetRepostedDetailsAndAction(Context context, Friend friend, String assetId) {
     * Intent intent = null; if (context != null && friend != null) { String CUSTOM_INTENT =
     * PushNotificationConfig.CURRENT_PKG_NAME + Constants.ASSET_REPOSTED; Intent notificationIntent = new
     * Intent(context, AppChildActivity.class); notificationIntent.putExtra(Constants.LAUNCH_ASSET_DETAILS, true);
     * notificationIntent.putExtra(Constants.USERID, friend.getUserId()); notificationIntent.putExtra(Constants.ASSETID,
     * assetId);
     * 
     * String alertMessage = null; String friendName = friend.getName(); if (friendName != null && friendName.length() >
     * 0) { alertMessage = friendName + " " + context.getResources().getString(R.string.loved_your_asset); } else {
     * alertMessage = context.getResources().getString(R.string.friend_loved_your_asset); } intent = new Intent(context,
     * AlertReciever.class); intent.putExtra(Constants.ALERT_MESSAGE, alertMessage); Bitmap bmp =
     * friend.getProfilePicBitmap(); if (bmp != null) { intent.putExtra(Constants.NOTIFICATION_LARGE_ICON, bmp); } else
     * { bmp = friend.getCoverImageBitmap(); if (bmp != null) { intent.putExtra(Constants.NOTIFICATION_LARGE_ICON, bmp);
     * } } intent.putExtra(Constants.NOTIFICATION_INTENT, notificationIntent); intent.setAction(CUSTOM_INTENT);
     * intent.addFlags(32);// Flag to include stopped packages } return intent; }
     */
}
