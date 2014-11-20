package com.paradigmcreatives.apspeak.assets;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.app.util.constants.JSONConstants;
import com.paradigmcreatives.apspeak.app.util.server.assets.AssetDownloader.AssetType;
import com.paradigmcreatives.apspeak.logging.Logger;

/**
 * 
 * Class used to manage all downloads related to assets(such as emoji/sticker/smiley, frame/layout, etc). It uses
 * ExecutorService with a thread pool of 5. Parses files such as emoji_list.txt, frame_list.txt(located in app's private
 * files directory) to get all assets download urls, starts downloading each asset which is not already available. Then,
 * each downloaded asset is saved to a file in app's private files directory.
 * 
 * @author Dileep | neuv
 * 
 */
public class WhatsayAssetsDownloadManager {

    private final int THREAD_POOL_SIZE = 5;

    private Context context;
    private ExecutorService executorService;
    
    private HashMap<String, List<String>> emojiAssetsHashMap;
    private HashMap<String, List<String>> frameAssetsHashMap;

    public WhatsayAssetsDownloadManager(final Context context) {
	super();
	this.context = context;
	this.executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    }

    /**
     * Starts assets downloading
     */
    public void startAssetsDownloading() {
	emojiAssetsHashMap = parseAssetsList(context, AssetType.EMOJI);
	frameAssetsHashMap= parseAssetsList(context, AssetType.FRAME);
	
	startQueueing();
    }

    /**
     * Stops assets downloading
     */
    public void stopAssetsDownloading() {
	executorService.shutdownNow();
    }

    /**
     * Starts adding all the asset(not already existing) download requests to queue
     */
    private void startQueueing(){
	if(executorService == null){
	    return;
	}
	// for Emoji assets
	if(emojiAssetsHashMap != null && emojiAssetsHashMap.size() > 0){
	    // Get all the categories
	    ArrayList<String> categoriesList = new ArrayList<String>();
	    categoriesList.addAll(emojiAssetsHashMap.keySet());
	    for(int i=0; i<categoriesList.size(); i++){
		// Get list of assets for each category
		ArrayList<String> assets = new ArrayList<String>();
		assets.addAll(emojiAssetsHashMap.get(categoriesList.get(i)));
		for(int j=0; j<assets.size(); j++){
		    String assetDownloadURL = assets.get(j);
		    if(!isAssetAlreadyExist(context, assetDownloadURL)){
			// Asset not exists, so add to Queue for downloading
			executorService.submit(new WhatsayAssetDownloadRunnable(context, assetDownloadURL));
		    }
		}
	    }
	}

	// for Frame assets
	if(frameAssetsHashMap != null && frameAssetsHashMap.size() > 0){
	    // Get all the categories
	    ArrayList<String> categoriesList = new ArrayList<String>();
	    categoriesList.addAll(frameAssetsHashMap.keySet());
	    for(int i=0; i<categoriesList.size(); i++){
		// Get list of assets for each category
		ArrayList<String> assets = new ArrayList<String>();
		assets.addAll(frameAssetsHashMap.get(categoriesList.get(i)));
		for(int j=0; j<assets.size(); j++){
		    String assetDownloadURL = assets.get(j);
		    if(!isAssetAlreadyExist(context, assetDownloadURL)){
			// Asset not exists, so add to Queue for downloading
			executorService.submit(new WhatsayAssetDownloadRunnable(context, assetDownloadURL));
		    }
		}
	    }
	}
    }
    
    /**
     * Checks whether asset already download and available in app's private files directory or not
     * 
     * @return
     */
    public static boolean isAssetAlreadyExist(Context context, String assetDownloadURL) {
	boolean isAvailable = false;
	if (context != null && !TextUtils.isEmpty(assetDownloadURL)) {
	    String assetFileName = assetDownloadURL.substring(assetDownloadURL.lastIndexOf('/') + 1);
	    if (!TextUtils.isEmpty(assetFileName)) {
		File assetFile = new File(context.getFilesDir(), assetFileName);
		if (assetFile.exists()) {
		    isAvailable = true;
		}
	    }
	}
	return isAvailable;
    }
    
    /**
     * Parses assets list from the <asset>_list.txt file, which is saved in app's private files directory
     * 
     * @param assetType
     */
    public static HashMap<String, List<String>> parseAssetsList(Context context, AssetType assetType) {
	HashMap<String, List<String>> assetsList = null;
	if (context != null && assetType != null) {
	    // Read assets list content from file
	    File file = null;
	    if (assetType == AssetType.EMOJI) {
		file = new File(context.getFilesDir(), Constants.EMOJI_LIST_FILENAME);
	    } else if (assetType == AssetType.FRAME) {
		file = new File(context.getFilesDir(), Constants.FRAME_LIST_FILENAME);
	    }
	    String assetsListAsString = getFileContentAsString(file);
	    // Convert string content to JSON, parse and get assets list as Map
	    assetsList = convertToJSONAndParse(assetsListAsString);
	}
	return assetsList;
    }

    /**
     * Reads content from the given file and returns the content in string format
     * 
     * @return
     */
    public static String getFileContentAsString(File file) {
	String fileContent = null;
	if (file != null && file.exists()) {
	    BufferedReader reader = null;
	    try {
		reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
		    sb.append(line).append("\n");
		}
		reader.close();
		fileContent = sb.toString();
	    } catch (FileNotFoundException fnfe) {
		Logger.logStackTrace(fnfe);
	    } catch (Exception e) {
		Logger.logStackTrace(e);
	    } finally {
		try {
		    reader.close();
		} catch (IOException ioe) {
		    Logger.logStackTrace(ioe);
		}
	    }
	}
	return fileContent;
    }

    /**
     * Converts the passed string to JSON and returns in a form of Map
     * 
     * @param assetsListAsString
     * @return
     */
    public static HashMap<String, List<String>> convertToJSONAndParse(String assetsListAsString) {
	HashMap<String, List<String>> map = null;
	if (!TextUtils.isEmpty(assetsListAsString)) {
	    try {
		JSONArray jsonArray = new JSONArray(assetsListAsString);
		if (jsonArray != null && jsonArray.length() > 0) {
		    map = new HashMap<String, List<String>>();
		    for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			if (jsonObject != null) {
			    String category = null;
			    if (jsonObject.has(JSONConstants.CATEGORIES)) {
				try {
				    JSONArray categories = jsonObject.getJSONArray(JSONConstants.CATEGORIES);
				    if (categories != null && categories.length() > 0) {
					category = categories.getString(0);
				    }
				} catch (JSONException e) {
				    Logger.logStackTrace(e);
				}

				if (!TextUtils.isEmpty(category) && jsonObject.has(JSONConstants.THUMBNAIL_URL)) {
				    if (map.containsKey(category)) {
					// category key already present in HashMap
					map.get(category).add(jsonObject.getString(JSONConstants.THUMBNAIL_URL));
				    } else {
					// category key not present in HashMap
					ArrayList<String> list = new ArrayList<String>();
					list.add(jsonObject.getString(JSONConstants.THUMBNAIL_URL));
					map.put(category, list);
				    }
				}
			    }
			}
		    }
		}
	    } catch (JSONException jse) {
		Logger.logStackTrace(jse);
	    }
	}
	return map;
    }
}
