package com.paradigmcreatives.apspeak.assets.tasks;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.text.TextUtils;

import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.app.util.server.assets.AssetDownloader.AssetType;
import com.paradigmcreatives.apspeak.assets.WhatsayAssetsDownloadManager;
import com.paradigmcreatives.apspeak.assets.tasks.helpers.GetAssetCategoriesHelper;
import com.paradigmcreatives.apspeak.assets.tasks.helpers.GetAssetsListHelper;
import com.paradigmcreatives.apspeak.logging.Logger;

/**
 * Thread that initiates fetching of Categories for different asset types
 * 
 * @author Dileep | neuv
 * 
 */
public class GetAssetsTask extends Thread {

    private Context context;

    public GetAssetsTask(final Context context) {
	super();
	this.context = context;
    }

    public void run() {
	if (context != null) {
	    GetAssetCategoriesHelper helper = new GetAssetCategoriesHelper(context);
	    // Fetch EMOJI categories
	    String result = helper.execute(AssetType.EMOJI);
	    // Save json to a text in app's private files directory with name emoji_categories.txt
	    saveJSONToPrivateFilesDir(context, result, Constants.EMOJI_CATEGORIES_FILENAME);

	    // Fetch FRAME categories
	    result = helper.execute(AssetType.FRAME);
	    // Save json to a text in app's private files directory with name frame_categories.txt
	    saveJSONToPrivateFilesDir(context, result, Constants.FRAME_CATEGORIES_FILENAME);

	    GetAssetsListHelper listHelper = new GetAssetsListHelper(context);
	    // Fetch EMOJI assets list
	    result = listHelper.execute(AssetType.EMOJI);
	    // Save json to a text in app's private files directory with name emoji_list.txt
	    saveJSONToPrivateFilesDir(context, result, Constants.EMOJI_LIST_FILENAME);

	    // Fetch FRAME assets list
	    result = listHelper.execute(AssetType.FRAME);
	    // Save json to a text in app's private files directory with name frame_list.txt
	    saveJSONToPrivateFilesDir(context, result, Constants.FRAME_LIST_FILENAME);
	    
	    // Start downloading assets
	    WhatsayAssetsDownloadManager downloadManager = new WhatsayAssetsDownloadManager(context);
	    downloadManager.startAssetsDownloading();
	}
    }

    /**
     * Creates a file in application's private files directory with passed json content
     * 
     * @param context
     * @param jsonString
     * @param destFileName
     * @return
     */
    private boolean saveJSONToPrivateFilesDir(Context context, String jsonArray, String destFileName) {
	boolean isSaved = false;
	OutputStreamWriter writer = null;
	if (context != null && !TextUtils.isEmpty(jsonArray) && !TextUtils.isEmpty(destFileName)) {
	    try {
		JSONArray resultJSON = new JSONArray(jsonArray);
		if (resultJSON != null) {
		    String appPrivateFilesDirectory = context.getFilesDir().getAbsolutePath();
		    File filesDir = new File(appPrivateFilesDirectory);
		    if (!filesDir.exists()) {
			filesDir.mkdirs();
		    }

		    File destFile = new File(filesDir, destFileName);

		    writer = new OutputStreamWriter(new FileOutputStream(destFile));
		    writer.write(resultJSON.toString());
		    writer.close();
		    isSaved = true;
		}
	    } catch (JSONException jse) {
		Logger.logStackTrace(jse);
	    } catch (FileNotFoundException fnfe) {
		Logger.logStackTrace(fnfe);
	    } catch (Exception e) {
		Logger.logStackTrace(e);
	    } finally {
		if (writer != null) {
		    try {
			writer.close();
		    } catch (IOException ioe) {
			Logger.logStackTrace(ioe);
		    }
		}
	    }
	}
	return isSaved;
    }
}
