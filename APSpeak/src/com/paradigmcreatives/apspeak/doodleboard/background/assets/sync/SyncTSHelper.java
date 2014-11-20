package com.paradigmcreatives.apspeak.doodleboard.background.assets.sync;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.app.util.server.assets.AssetDownloader.AssetType;
import com.paradigmcreatives.apspeak.logging.Logger;

/**
 * This class contains convenience methods for saving and retrieving the timestamp of various asset sync
 * 
 * @author robin
 * 
 */
public class SyncTSHelper {

    private static final String TAG = "SyncTSHelper";

    /**
     * Saves the current timestamp as the sync time for the given asset
     * 
     * @param context
     * @param assetType
     */
    synchronized public static void saveTimeStamp(Context context, AssetType assetType) {
	if (context != null && assetType != null) {
	    String key = getPrefKey(assetType);
	    if (!TextUtils.isEmpty(key)) {
		SharedPreferences sharedPref = context.getSharedPreferences(key, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putLong(key, System.currentTimeMillis());
		editor.commit();
	    } else {
		Logger.warn(TAG, "Empty key received while saving");
	    }
	} else {
	    Logger.warn(TAG, "Could not save the sync timestamp as context or asset type is empty. Context: " + context
		    + ", AssetType: " + assetType);
	}
    }

    /**
     * Checks if the given asset should be synced or not based on the last sync time
     * 
     * @param context
     * @param assetType
     * @return
     */
    public static boolean shouldSync(Context context, AssetType assetType) {
	if (context != null && assetType != null) {
	    long prevTS = getLastSyncTimeStamp(context, assetType);
	    long currentTS = System.currentTimeMillis();
	    if (prevTS + Constants.ASSET_SYNC_COOLOFF < currentTS) {
		return true;
	    }
	} else {
	    Logger.warn(TAG, "Could not check for sync as context or asset type is empty. Context: " + context
		    + ", Asset Type: " + assetType);
	}

	return false;
    }

    private static long getLastSyncTimeStamp(Context context, AssetType assetType) {
	long ts = 0;
	if (context != null && assetType != null) {
	    String key = getPrefKey(assetType);
	    if (!TextUtils.isEmpty(key)) {
		SharedPreferences sharedPref = context.getSharedPreferences(key, Context.MODE_PRIVATE);
		ts = sharedPref.getLong(key, 0);
	    } else {
		Logger.warn(TAG, "Empty key received while saving");
	    }
	} else {
	    Logger.warn(TAG, "Could not save the sync timestamp as context or asset type is empty. Context: " + context
		    + ", AssetType: " + assetType);
	}

	return ts;

    }

    /**
     * Get the key value based on an asset
     * 
     * @param assetType
     * @return
     */
    private static String getPrefKey(AssetType assetType) {
	String key = null;
	if (assetType != null) {
	    switch (assetType) {
	    case DOODLEWORLD:
		key = Constants.KEY_DOODLEWORLD_SYNC_TS;
		break;
	    case EMOJI:
		key = Constants.KEY_EMOJIS_SYNC_TS;
		break;
	    case GREETINGS:
		key = Constants.KEY_GREETINGS_SYNC_TS;
		break;
	    case RAGE_FACE:
		key = Constants.KEY_RAGE_FACES_SYNC_TS;
		break;

	    }
	}
	return key;
    }

}
