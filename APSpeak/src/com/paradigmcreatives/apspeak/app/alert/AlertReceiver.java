package com.paradigmcreatives.apspeak.app.alert;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;

import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.app.util.AppPropertiesUtil;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.gcm.PushNotificationConfig;
import com.paradigmcreatives.apspeak.logging.Logger;

/**
 * Handles the alert message received from server
 * 
 * @author Soumya Behera | Paradigm Creatives
 * 
 */
public class AlertReceiver extends BroadcastReceiver {

	private static final String TAG = "AlertReciever";

	private Context context;

	@Override
	public void onReceive(Context context, Intent intent) {
		this.context = context;
		String alertMessage = null;
		Bitmap largeIcon = null;
		Intent notificationIntent = null;

		if (intent.hasExtra(Constants.ALERT_MESSAGE)) {
			alertMessage = intent.getStringExtra(Constants.ALERT_MESSAGE);
		}
		if (intent.hasExtra(Constants.NOTIFICATION_LARGE_ICON)) {
			largeIcon = intent.getParcelableExtra(Constants.NOTIFICATION_LARGE_ICON);
		}
		if (intent.hasExtra(Constants.NOTIFICATION_INTENT)) {
			notificationIntent = intent.getParcelableExtra(Constants.NOTIFICATION_INTENT);
		}
		int notificationId = AppPropertiesUtil.getLastUsedNotificationId(context);
		AppPropertiesUtil.setLastUsedNotificationId(context, ++notificationId);
		if (intent.getAction().equals(PushNotificationConfig.CURRENT_PKG_NAME + Constants.NEW_FRIEND)) {
			if(notificationIntent != null){
				notificationIntent.setAction(PushNotificationConfig.CURRENT_PKG_NAME + Constants.NEW_FRIEND + notificationId);
			}
			raiseAlertNotification(alertMessage, largeIcon, notificationIntent,
					/*Constants.WHATSAY_NEW_FRIEND_JOINED_ID*/ notificationId);
		} else if (intent.getAction().equals(PushNotificationConfig.CURRENT_PKG_NAME + Constants.USER_FOLLOWED)) {
			if(notificationIntent != null){
				notificationIntent.setAction(PushNotificationConfig.CURRENT_PKG_NAME + Constants.USER_FOLLOWED + notificationId);
			}
			raiseAlertNotification(alertMessage, largeIcon, notificationIntent, /*Constants.WHATSAY_USER_FOLLOWED_ID*/ notificationId);
		} else if (intent.getAction().equals(PushNotificationConfig.CURRENT_PKG_NAME + Constants.USER)) {
			if(notificationIntent != null){
				notificationIntent.setAction(PushNotificationConfig.CURRENT_PKG_NAME + Constants.USER + notificationId);
			}
			raiseAlertNotification(alertMessage, largeIcon, notificationIntent, /*Constants.WHATSAY_USER_FOLLOWED_ID*/ notificationId);
		} else if (intent.getAction().equals(PushNotificationConfig.CURRENT_PKG_NAME + Constants.ASSET_LOVED)) {
			raiseAlertNotification(alertMessage, largeIcon, notificationIntent, Constants.WHATSAY_ASSET_LOVED);
		} else if (intent.getAction().equals(PushNotificationConfig.CURRENT_PKG_NAME + Constants.ASSET_COMMENTED)) {
			if(notificationIntent != null){
				notificationIntent.setAction(PushNotificationConfig.CURRENT_PKG_NAME + Constants.ASSET_COMMENTED + notificationId);
			}
			raiseAlertNotification(alertMessage, largeIcon, notificationIntent, /*Constants.WHATSAY_ASSET_COMMENTED*/ notificationId);
		} else if (intent.getAction().equals(PushNotificationConfig.CURRENT_PKG_NAME + Constants.NEW_EXPRESSION)) {
			if(notificationIntent != null){
				notificationIntent.setAction(PushNotificationConfig.CURRENT_PKG_NAME + Constants.NEW_EXPRESSION + notificationId);
			}
			raiseAlertNotification(alertMessage, largeIcon, notificationIntent, /*Constants.WHATSAY_NEW_EXPRESSION*/ notificationId);
		} else if (intent.getAction().equals(PushNotificationConfig.CURRENT_PKG_NAME + Constants.EXPRESSION)) {
			if(notificationIntent != null){
				notificationIntent.setAction(PushNotificationConfig.CURRENT_PKG_NAME + Constants.EXPRESSION + notificationId);
			}
			raiseAlertNotification(alertMessage, largeIcon, notificationIntent, /*Constants.WHATSAY_NEW_EXPRESSION*/ notificationId);
		} else if(intent.getAction().equals(PushNotificationConfig.CURRENT_PKG_NAME + Constants.ANNOUNCEMENT)){
			if(notificationIntent != null){
				notificationIntent.setAction(PushNotificationConfig.CURRENT_PKG_NAME + Constants.ANNOUNCEMENT + notificationId);
			}
		    raiseAlertNotification(alertMessage, largeIcon, notificationIntent, /*Constants.WHATSAY_ANNOUNCEMENT*/ notificationId);
		} else {
			Logger.warn(TAG, "Intent filter action value doesn't match");
		}
	}

	private void raiseAlertNotification(final String alertMessage, final Bitmap largeIcon,
			final Intent notificationIntent, final int alertId) {
		(new Handler()).post(new Runnable() {

			@Override
			public void run() {
				if (context != null) {
					try {
						NotificationManager manager = (NotificationManager) context
								.getSystemService(Context.NOTIFICATION_SERVICE);
						NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
						int icon = PushNotificationConfig.NOTIFICATION_ICON;
						CharSequence tickerText = context.getResources().getString(R.string.notification_ticker_text)
								+ " " + PushNotificationConfig.APP_NAME;
						long when = System.currentTimeMillis();

						builder.setSmallIcon(icon).setTicker(tickerText).setWhen(when)
								.setContentTitle(context.getResources().getString(R.string.app_name));

						if (largeIcon != null) {
							builder.setLargeIcon(largeIcon);
						}

						if (alertMessage != null) {
							builder.setContentText(alertMessage);
						}

						if (notificationIntent != null) {
							PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,
									PendingIntent.FLAG_UPDATE_CURRENT);
							builder.setContentIntent(contentIntent);
						}

						if (PushNotificationConfig.NOTIFICATION_SOUND) {
							builder.setDefaults(Notification.DEFAULT_SOUND);
						}
						if (PushNotificationConfig.NOTIFICATION_VIBRATE) {
							builder.setDefaults(Notification.DEFAULT_VIBRATE);
						}

						// Gets auto canceled when user clicks on Notification
						// message
						builder.setAutoCancel(true);

						manager.notify(alertId, builder.build());
					} catch (Exception e) {
						Logger.logStackTrace(e);
					}
				} else {
					Logger.warn(TAG, "Context is null");
				}
			}
		});
	}

}
