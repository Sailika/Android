package com.paradigmcreatives.apspeak.app.util.server.assets.parsers;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.paradigmcreatives.apspeak.app.util.server.assets.beans.AssetBean;
import com.paradigmcreatives.apspeak.logging.Logger;

/**
 * Class for parsing results of frame cards
 * 
 * @author robin
 * 
 */
public class FrameResultParser implements AssetsParser {

	private static final String TAG = "FramesResultParser";

	@Override
	public ArrayList<AssetBean> parse(JSONArray object) {
		ArrayList<AssetBean> result = null;
		try {
			if (object != null) {
				int length = object.length();
				result = new ArrayList<AssetBean>();
				JSONObject item;
				AssetBean frameItem;
				for (int i = 0; i < length; i++) {
					item = object.getJSONObject(i);
					frameItem = parseGreetingObject(item);
					if (frameItem != null) {
						result.add(frameItem);
					}
				}
			} else {
				Logger.warn(TAG, "Null object supplied to frame parsing");
			}
		} catch (JSONException e) {
			Logger.warn(TAG, "Error while parsing JSON + " + e.getMessage());
			e.printStackTrace();
		}

		return result;
	}

	private AssetBean parseGreetingObject(JSONObject object) {
		AssetBean frameBean = null;

		try {
			if (object != null) {
				frameBean = new AssetBean();
				if (object.has("id")) {
					frameBean.setId(object.getString("id"));
				}

				if (object.has("url")) {
					frameBean.setUrl(object.getString("url"));
				}

				if (object.has("title")) {
					frameBean.setTitle(object.getString("title"));
				}

				if (object.has("thumbnail_url")) {
					frameBean.setThumbnailURL(object.getString("thumbnail_url"));
				}

				if (object.has("auto_id")) {
					frameBean.setAutoID(object.getInt("auto_id"));
				}

				if (object.has("categories")) {
					JSONArray array = object.getJSONArray("categories");
					if (array.length() > 0) {
						ArrayList<String> categories = new ArrayList<String>();
						for (int i = 0; i < array.length(); i++) {
							categories.add(array.getString(i));
						}
						frameBean.setCategory(categories);
					}
				}

			} else {
				Logger.warn(TAG, "Empty object supplied to parse in the frames");
			}
		} catch (JSONException e) {
			Logger.warn(TAG, "Error while parsing frame object " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			Logger.warn(TAG, "Something bad happened while parsing frame result" + e.getMessage());
			e.printStackTrace();
		}

		return frameBean;
	}
}// end of class