package com.paradigmcreatives.apspeak.app.util.server.assets.parsers;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.paradigmcreatives.apspeak.app.util.server.assets.beans.AssetBean;
import com.paradigmcreatives.apspeak.logging.Logger;

public class DoodleWorldParser implements AssetsParser {

    private static final String TAG = "DoodleWorldParser";

    @Override
    public ArrayList<AssetBean> parse(JSONArray object) {
	ArrayList<AssetBean> result = null;
	try {
	    if (object != null) {
		int length = object.length();
		result = new ArrayList<AssetBean>();
		JSONObject item;
		AssetBean doodleItem;
		for (int i = 0; i < length; i++) {
		    item = object.getJSONObject(i);
		    doodleItem = parseDoodleWorldObject(item);
		    if (doodleItem != null) {
			result.add(doodleItem);
		    }
		}
	    } else {
		Logger.warn(TAG, "Null object supplied to doodle world parsing");
	    }
	} catch (JSONException e) {
	    Logger.warn(TAG, "Error while parsing JSON + " + e.getMessage());
	    e.printStackTrace();
	}

	return result;
    }

    private AssetBean parseDoodleWorldObject(JSONObject object) {
	AssetBean doodleBean = null;

	try {
	    if (object != null) {
		doodleBean = new AssetBean();
		if (object.has("id")) {
		    doodleBean.setId(object.getString("id"));
		}

		if (object.has("url")) {
		    doodleBean.setUrl(object.getString("url"));
		}

		if (object.has("title")) {
		    doodleBean.setTitle(object.getString("title"));
		}

		if (object.has("thumbnail_url")) {
		    doodleBean.setThumbnailURL(object.getString("thumbnail_url"));
		}

		if (object.has("auto_id")) {
		    doodleBean.setAutoID(object.getInt("auto_id"));
		}

		if (object.has("categories")) {
		    JSONArray array = object.getJSONArray("categories");
		    if (array.length() > 0) {
			ArrayList<String> categories = new ArrayList<String>();
			for (int i = 0; i < array.length(); i++) {
			    categories.add(array.getString(i));
			}
			doodleBean.setCategory(categories);
		    }
		}

	    } else {
		Logger.warn(TAG, "Empty object supplied to parse in the doodle world");
	    }
	} catch (JSONException e) {
	    Logger.warn(TAG, "Error while parsing doodle object " + e.getMessage());
	    e.printStackTrace();
	} catch (Exception e) {
	    Logger.warn(TAG, "Something bad happened while parsing doodle result" + e.getMessage());
	    e.printStackTrace();
	}

	return doodleBean;
    }

}
