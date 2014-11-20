package com.paradigmcreatives.apspeak.doodleboard.background.assets.sync;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;

import com.paradigmcreatives.apspeak.app.util.Util;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.app.util.constants.Constants.ExitState;
import com.paradigmcreatives.apspeak.app.util.server.assets.AssetDownloader;
import com.paradigmcreatives.apspeak.app.util.server.assets.AssetDownloader.AssetType;
import com.paradigmcreatives.apspeak.app.util.server.assets.beans.AssetBean;
import com.paradigmcreatives.apspeak.logging.Logger;

/**
 * Helper class for getting all the assets of a given type from the server in the order of server
 * 
 * @author robin
 * 
 */
public class GetAllAssets {

    private static final String TAG = "GetAllAssets";
    private AssetType assetType;
    private String category;
    private Context context;

    /**
     * Initialization with the Asset Type
     * 
     * @param assetType
     */
    public GetAllAssets(Context context, AssetType assetType) {
	this(context, assetType, null);
    }

    /**
     * Initialization with Asset Type and Category
     * 
     * @param assetType
     * @param category
     */
    public GetAllAssets(Context context, AssetType assetType, String category) {
	this.context = context;
	this.assetType = assetType;
	this.category = category;
    }

    /**
     * Pings the server and gets all the assets
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<AssetBean> execute() {
	if (context != null && !isInSync()) {
	    List<AssetBean> assetList = null;
	    List<AssetBean> temporaryResult = null;
	    ExitState exitState = null;

	    AssetDownloader assetDownloader = initAssetDownloader(Constants.MAX_ASSETS_FETCH_LIMIT);

	    if (assetDownloader != null) {
		assetList = new ArrayList<AssetBean>();

		do {
		    // If the activity is destroyed while the operation is still being performed then halt this
		    if (context != null && Util.isOnline(context)) {
			temporaryResult = (ArrayList<AssetBean>) assetDownloader.next(false);
			if (temporaryResult != null && !temporaryResult.isEmpty()) {
			    assetList.addAll(temporaryResult);
			} else {
			    exitState = ExitState.NORMAL_EXIT;
			    break;
			}
		    } else {
			exitState = ExitState.CONNECTION_INTERRUPTED;
			break;
		    }
		} while (true);

		// Send the asset list only if getting of assets exited normally, i.e., it was able to complete all the
		// requests and get the results successfully
		if (assetDownloader.getExitState() == ExitState.NORMAL_EXIT && exitState == ExitState.NORMAL_EXIT) {
		    return assetList;
		}

	    }
	} else {
	    Logger.warn(TAG, "Empty activity during execution of get all assets or everything in sync");
	}

	return null;

    }

    /**
     * Checks if the latest cached and the latest on the server are same or not
     * 
     * @return
     */
    private boolean isInSync() {
	// Check for the order of assets
	//AssetBean latestFromCache = getLatestAsset(new AssetCacheDAO(context));
	//AssetBean latestFromServer = getLatestAssetFromServer();

	// Check if number of cards given in the new cache and the downloaded cache is equal to as given by the server
	// or not
	//if(latestFromCache != null && latestFromServer != null) {
	//	return latestFromCache.equals(latestFromServer) && countTotalAssetsInCache() == countTotalAssetsOnServer();
	//} else {
	    return false;
	//}
    }

    /**
     * Returns the total number of asset count locally
     * 
     * @return
     */
	/*
    private int countTotalAssetsInCache() {
	AssetCacheDAO assetCacheDAO = new AssetCacheDAO(context);
	ArrayList<AssetBean> downloadedAssets = assetCacheDAO.getAssetsFromDB(assetType.ordinal(), null,
		AssetsTable.DOWNLOADED_ASSETS);
	ArrayList<AssetBean> newCachedAssets = assetCacheDAO.getAssetsFromDB(assetType.ordinal(), null,
		AssetsTable.NEW_ASSETS);
	int count = 0;

	if (downloadedAssets != null && !downloadedAssets.isEmpty()) {
	    count += downloadedAssets.size();
	}

	if (newCachedAssets != null && !newCachedAssets.isEmpty()) {
	    count += newCachedAssets.size();
	}

	return count;
    }
	*/

    /**
     * Get total number of assets on the server
     * 
     * @return
     */
    /*
    private int countTotalAssetsOnServer() {
	QueryCategories queryCategories = new QueryCategories(assetType);
	ArrayList<CategoryBean> categories = queryCategories.query();
	int count = 0;
	Iterator<CategoryBean> iterator = categories.iterator();
	CategoryBean item;
	while (iterator.hasNext()) {
	    item = iterator.next();
	    if (item != null) {
		count += item.getCardsCount();
	    }
	}

	return count;

    }
    */

    /**
     * Gets the latest asset from the server
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    private AssetBean getLatestAssetFromServer() {
	AssetBean asset = null;
	AssetDownloader assetDownloader = initAssetDownloader(1);
	ArrayList<AssetBean> assets = (ArrayList<AssetBean>) assetDownloader.next(false);
	if (assets != null && !assets.isEmpty()) {
	    asset = assets.get(0);
	}

	return asset;
    }

    /**
     * Returns an appropriate asset downloader based on the state of various arguments
     * 
     * @return
     */
    private AssetDownloader initAssetDownloader(int limit) {
	if (context == null || assetType == null) {
	    return null;
	}

	// Initialize the assetDownloader
	if (TextUtils.isEmpty(category)) {
	    return new AssetDownloader(context, assetType, limit);
	} else {
	    return new AssetDownloader(context, assetType, category, limit);
	}

    }

    /**
     * Get the latest asset from the cached DB
     * 
     * @param assetCacheDAO
     * @return
     */
    /*
    private AssetBean getLatestAsset(AssetCacheDAO assetCacheDAO) {
	ArrayList<AssetBean> assetsFromCache = assetCacheDAO.getAssetsFromDB(assetType.ordinal(), null,
		AssetsTable.DOWNLOADED_ASSETS, 1);
	if (assetsFromCache != null && !assetsFromCache.isEmpty()) {
	    return assetsFromCache.get(0);
	}
	return null;
    }
    */

}
