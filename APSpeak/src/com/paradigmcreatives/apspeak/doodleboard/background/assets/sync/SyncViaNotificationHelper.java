package com.paradigmcreatives.apspeak.doodleboard.background.assets.sync;

import java.util.ArrayList;

import android.content.Context;

import com.paradigmcreatives.apspeak.app.util.server.assets.AssetDownloader.AssetType;
import com.paradigmcreatives.apspeak.app.util.server.assets.beans.AssetBean;
import com.paradigmcreatives.apspeak.logging.Logger;

/**
 * Performs the sync for a particular <code>AssetType</code> upon reception of
 * notification. Once sync is complete it puts a notification message in the
 * standard notification bar.
 * 
 * @author robin
 * 
 */
public class SyncViaNotificationHelper {

	private static final String TAG = "SyncViaNotificationHelper";
	private Context context;
	private AssetType assetType;
	private String message;

	public SyncViaNotificationHelper(Context context, AssetType assetType,
			String message) {
		this.context = context;
		this.assetType = assetType;
		this.message = message;
	}

	public void sync() {
		if (context != null && assetType != null) {
			GetAllAssets getAllAssets = new GetAllAssets(context, assetType);
			ArrayList<AssetBean> assets = (ArrayList<AssetBean>) getAllAssets
					.execute();
			if (assets != null && !assets.isEmpty()) {
				/*
				 * CacheAssets cacheAssets = new
				 * CacheAssets(context.getApplicationContext(), assetType,
				 * assets); boolean result = cacheAssets.execute(); if (result)
				 * { Logger.info(TAG, "Sync completed for " + assetType); if
				 * (cacheAssets.getNumberOfNewAssets() > 0) { Intent intent =
				 * createIntentWithCountAndAction
				 * (cacheAssets.getNumberOfNewAssets()); //
				 * DoodlyDooNotificationManager
				 * .getInstance().sendNotificationNow(context, intent); }
				 * 
				 * } else { Logger.warn(TAG, "Sync of assets failed"); }
				 */
			}
		} else {
			Logger.warn(TAG, "Context or asset type is null. Context: "
					+ context + ", AssetType: " + assetType);
		}

	}

	/*
	 * private Intent createIntentWithCountAndAction(int newAssetCount) { Intent
	 * intent = null; Intent notificationIntent = null; String CUSTOM_INTENT;
	 * String alertMessage;
	 * 
	 * if (assetType == AssetType.DOODLEWORLD) { // CUSTOM_INTENT =
	 * PushNotificationConfig.CURRENT_PKG_NAME +
	 * Constants.ALERT_NEW_DOODLEWORLD; alertMessage =
	 * "FREE new DOODLES to use!"; // notificationIntent = new Intent(context,
	 * AssetsActivity.class); //
	 * notificationIntent.putExtra(Constants.ASSET_TYPE,
	 * AssetType.DOODLEWORLD.name()); } else { alertMessage =
	 * "FREE new STUFF to use!"; // CUSTOM_INTENT =
	 * PushNotificationConfig.CURRENT_PKG_NAME + Constants.ALERT_NEW_ASSETS;
	 * notificationIntent = new Intent(context, CanvasFragmentActivity.class);
	 * notificationIntent.putExtra(Constants.NEED_PHONE_NUMBER, true); }
	 * 
	 * intent = new Intent(context, AlertReciever.class); if
	 * (!TextUtils.isEmpty(message)) { intent.putExtra(Constants.ALERT_MESSAGE,
	 * message); } else { intent.putExtra(Constants.ALERT_MESSAGE,
	 * alertMessage); }
	 * 
	 * intent.putExtra(Constants.NOTIFICATION_INTENT, notificationIntent); //
	 * intent.setAction(CUSTOM_INTENT); intent.addFlags(32);// Flag to include
	 * stopped packages return intent; }
	 */

}
