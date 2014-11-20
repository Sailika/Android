package com.paradigmcreatives.apspeak.app.util.server.assets;

import java.util.ArrayList;

import org.json.JSONArray;

import android.content.Context;
import android.text.TextUtils;

import com.paradigmcreatives.apspeak.app.util.constants.Constants.ExitState;
import com.paradigmcreatives.apspeak.app.util.server.assets.beans.AssetBean;
import com.paradigmcreatives.apspeak.app.util.server.assets.parsers.AssetsParser;
import com.paradigmcreatives.apspeak.app.util.server.assets.parsers.DoodleWorldParser;
import com.paradigmcreatives.apspeak.app.util.server.assets.parsers.EmojiResultParser;
import com.paradigmcreatives.apspeak.app.util.server.assets.parsers.GreetingResultParser;
import com.paradigmcreatives.apspeak.app.util.server.assets.parsers.RageFaceResultParser;
import com.paradigmcreatives.apspeak.logging.Logger;

/**
 * This is the class for downloading various assets that could be stored on Doodly Doo server. It calls parsers
 * according to supplied asset type
 * 
 * @author robin
 * 
 */
public class AssetDownloader {

    private final static String TAG = "AssetDownloader";
    private boolean isResetCalled = false;
    private ExitState exitState = ExitState.NORMAL_EXIT;
    private int limit = 0;

    int currentPageIndex = 0;

    private Context context;
    private ArrayList<String> ids = new ArrayList<String>();

    public enum AssetType {
	GREETINGS, EMOJI, RAGE_FACE, DOODLEWORLD, FRAME
    }

    private AssetType assetType;
    private String category;

    public AssetDownloader(final Context context, AssetType type) {
	this(context, type, null, 0);
    }

    public AssetDownloader(final Context context, AssetType type, int limit) {
	this(context, type, null, limit);
    }

    public AssetDownloader(final Context context, AssetType type, String category) {
	this(context, type, category, 0);
    }

    public AssetDownloader(final Context context, AssetType type, String category, int limit) {
	this.context = context;
	this.assetType = type;
	this.category = category;
	this.limit = limit;
    }

    /**
     * Runs the request for next page of assets
     * 
     * @param cache
     *            Cache the data locally or not
     * @return
     */
    public synchronized ArrayList<?> next(boolean cache) {
	ArrayList<?> result = null;

	// Execute the query
	Query query = null;
	if (currentPageIndex > 0) {
	    if (ids.size() >= currentPageIndex) {
		if (!TextUtils.isEmpty(category)) {
		    query = new Query(context, ids.get(currentPageIndex - 1), assetType, category);
		} else {
		    query = new Query(context, ids.get(currentPageIndex - 1), assetType);
		}
	    } else {
		Logger.warn(TAG, "Invalid current page index");
	    }
	} else {
	    if (!TextUtils.isEmpty(category)) {
		query = new Query(context, assetType, category);
	    } else {
		query = new Query(context, assetType);
	    }
	}

	result = executeQuery(query, cache);

	// If everything is successful then update the pointers for the next request set
	if (result != null) {
	    Object assetBean = result.get(result.size() - 1);

	    if (assetBean instanceof AssetBean) {
		String lastID;
		if (assetType == AssetType.DOODLEWORLD) {
		    lastID = ((AssetBean) assetBean).getAutoID() + "";
		} else {
		    lastID = ((AssetBean) assetBean).getId();
		}
		if (!TextUtils.isEmpty(lastID)) {
		    if (!ids.contains(lastID)) {
			ids.add(lastID);
		    }
		    currentPageIndex++;
		}
	    }
	}

	return result;
    }

    /**
     * Runs the request for previous page of assets
     * 
     * @param cache
     *            Cache the data locally or not
     * @return
     */
    public synchronized ArrayList<?> previous(boolean cache) {
	ArrayList<?> result = null;

	if (currentPageIndex > 0) {
	    currentPageIndex--;
	}

	// Execute the query
	Query query = null;
	if (currentPageIndex > 1) {
	    if (ids.size() >= currentPageIndex) {
		if (!TextUtils.isEmpty(category)) {
		    query = new Query(context, ids.get(currentPageIndex - 2), assetType, category);
		} else {
		    query = new Query(context, ids.get(currentPageIndex - 2), assetType);
		}
	    } else {
		Logger.warn(TAG, "Invalid current page index");
	    }
	} else if (currentPageIndex == 1) {
	    if (!TextUtils.isEmpty(category)) {
		query = new Query(context, assetType, category);
	    } else {
		query = new Query(context, assetType);
	    }
	}

	result = executeQuery(query, cache);

	if (currentPageIndex == 0) {
	    currentPageIndex = 1;
	}

	return result;
    }

    /**
     * 
     * @param query
     * @param cache
     *            Cache the data locally or not
     * @return
     */
    private ArrayList<?> executeQuery(Query query, boolean cache) {
	ArrayList<?> result = null;
	if (query != null) {
	    JSONArray resultJSON = null;
	    if (limit > 0) {
		resultJSON = query.execute(limit);
	    } else {
		resultJSON = query.execute();
	    }
	    exitState = query.getExitState();

	    // Parse the result
	    if (resultJSON != null) {
		AssetsParser parser = null;

		switch (assetType) {
		case GREETINGS:
		    parser = new GreetingResultParser();
		    break;

		case EMOJI:
		    parser = new EmojiResultParser();
		    break;

		case RAGE_FACE:
		    parser = new RageFaceResultParser();
		    break;

		case DOODLEWORLD:
		    parser = new DoodleWorldParser();
		    break;

		default:
		    Logger.warn(TAG, "Invalid asset type given. No parser found for " + assetType);
		    break;
		}

		if (parser != null) {
		    result = parser.parse(resultJSON);
		    if (cache) {
			for (int i = 0; i < result.size(); i++) {

			    //new AssetCacheDAO(context).insertAssetMetadata((AssetBean) result.get(i), assetType,
				//    AssetsTable.DOWNLOADED_ASSETS);

			}
		    }

		}

	    } else {
		Logger.warn(TAG, "Null response received from server for asset query. Asset Type - " + assetType);
	    }
	} else {
	    Logger.warn(TAG, "Could not construct query object for asset downloader");
	}

	return result;
    }

    /**
     * @return the assetType
     */
    public AssetType getAssetType() {
	return assetType;
    }

    /**
     * @param assetType
     *            the assetType to set
     */
    public void setAssetType(AssetType assetType) {
	this.assetType = assetType;
    }

    public String getCategory() {
	return category;
    }

    public void setCategory(String category) {
	this.category = category;
    }

    /**
     * Reset the state of the <code>AssetDownloader</code> so that after offline cache is done displaying its data,
     * asset downloader pings server for more data.<br>
     * The function calling this method should ensure that <code>id</code> supplied in the input argument is correct.
     * This method can be called only once in the lifetime of an <code>AssetDownloader</code> instance. For every other
     * call there won't be any affect on this
     * 
     * @param id
     */
    public void resetAssetDownloaderForCache(String id) {
	if (!isResetCalled) { // Make sure that its called only once per instance lifetime
	    // Update the current index to 1 so that the query uses the lastId to get the next set of thumbnails
	    currentPageIndex = 1;

	    // Update the IDs array
	    ids.clear();
	    ids.trimToSize();
	    ids.add(id);
	    isResetCalled = true;
	}
    }

    /**
     * @return the isResetCalled
     */
    public boolean isResetCalled() {
	return isResetCalled;
    }

    /**
     * @return the exitState
     */
    public ExitState getExitState() {
	return exitState;
    }

}
