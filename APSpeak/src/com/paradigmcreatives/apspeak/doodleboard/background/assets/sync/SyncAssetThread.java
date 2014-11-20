package com.paradigmcreatives.apspeak.doodleboard.background.assets.sync;

import java.util.ArrayList;

import android.content.Context;
import android.text.TextUtils;

import com.paradigmcreatives.apspeak.app.util.constants.Constants.State;
import com.paradigmcreatives.apspeak.app.util.server.assets.AssetDownloader.AssetType;
import com.paradigmcreatives.apspeak.app.util.server.assets.beans.AssetBean;

/**
 * Singleton thread class to perform the sync process of assets. It checks the server for the given assets and get the
 * entire data from there. If needed be then it flushes the current cache and put together the new cache (which ensures
 * that the order in which the server wants the assets to be displayed is honored). <br>
 * <br>
 * Since this processes can be quite heavy therefore this class ensures that at a time only one of its instance is in
 * execution, hence its Singleton.
 * 
 * 
 * @author robin
 * 
 */
public class SyncAssetThread implements Runnable {
    private static final String TAG = "SyncAssetThread";
    private Context context;
    private AssetType assetType;
    private String category;
    private static SyncAssetThread syncAssetThread = null;

    private State state;

    /**
     * Gets the instance of this thread class if its not busy
     * 
     * @param context
     * @param assetType
     * @param category
     * @return Instance of <code>SyncAssetThread</code> if its not busy else returns <code>null</code>
     */
    public static SyncAssetThread getInstance(Context context, AssetType assetType, String category) {

	// If its not initialized till now then initialize it and return the value
	if (syncAssetThread == null) {
	    syncAssetThread = new SyncAssetThread(context, assetType, category);
	    return syncAssetThread;
	} else if (syncAssetThread.getState() == State.STOPPED) {
	    syncAssetThread.setActivity(context);
	    syncAssetThread.setAssetType(assetType);
	    syncAssetThread.setCategory(category);
	    return syncAssetThread;
	} else {
	    return null;
	}

    }

    /**
     * Variation of getting instance with lesser number of arguments
     * 
     * @see #getInstance(Context context, AssetType assetType, String category)
     * 
     * @param context
     * @param assetType
     * @return
     */
    public static SyncAssetThread getInstance(Context context, AssetType assetType) {
	return getInstance(context, assetType, null);
    }

    private SyncAssetThread(Context context, AssetType assetType) {
	this(context, assetType, null);
    }

    private SyncAssetThread(Context context, AssetType assetType, String category) {
	this.context = context;
	this.assetType = assetType;
	this.category = category;
    }

    @Override
    public void run() {
	state = State.RUNNING;
	GetAllAssets getAllAssets = initAssetsHelper();
	ArrayList<AssetBean> assets = (ArrayList<AssetBean>) getAllAssets.execute();
	if (assets != null && !assets.isEmpty()) {
	    /*
	    CacheAssets cacheAssets = new CacheAssets(context.getApplicationContext(), assetType, assets);
	    boolean result = cacheAssets.execute();
	    if (result) {
		Logger.info(TAG, "Sync completed");
	    } else {
		Logger.warn(TAG, "Sync of assets failed");
	    }
	    */
	}

	// Save the sync timestamp
	SyncTSHelper.saveTimeStamp(context, assetType);

	state = State.STOPPED;
    }

    /**
     * Initializes an instance of <code>GetAllAssets</code> class based on the state of the variables
     * 
     * @return
     */
    private GetAllAssets initAssetsHelper() {
	if (TextUtils.isEmpty(category)) {
	    return new GetAllAssets(context, assetType);
	} else {
	    return new GetAllAssets(context, assetType, category);
	}

    }

    /**
     * @return the state
     */
    public State getState() {
	return state;
    }

    /**
     * @param context
     *            The context to set
     */
    private void setActivity(Context context) {
	this.context = context;
    }

    /**
     * @param assetType
     *            the assetType to set
     */
    private void setAssetType(AssetType assetType) {
	this.assetType = assetType;
    }

    /**
     * @param category
     *            the category to set
     */
    private void setCategory(String category) {
	this.category = category;
    }

}
