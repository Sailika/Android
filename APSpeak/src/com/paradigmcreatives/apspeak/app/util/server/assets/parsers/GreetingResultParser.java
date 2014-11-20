package com.paradigmcreatives.apspeak.app.util.server.assets.parsers;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.paradigmcreatives.apspeak.app.util.server.assets.beans.AssetBean;
import com.paradigmcreatives.apspeak.logging.Logger;

/**
 * Class for parsing results of greeting cards
 * 
 * @author robin
 * 
 */
public class GreetingResultParser implements AssetsParser {

	private static final String TAG = "GreetingsResultParser";

	@Override
	public ArrayList<AssetBean> parse(JSONArray object) {
		ArrayList<AssetBean> result = null;
		try {
			if (object != null) {
				int length = object.length();
				result = new ArrayList<AssetBean>();
				JSONObject item;
				AssetBean greetingItem;
				for (int i = 0; i < length; i++) {
					item = object.getJSONObject(i);
					greetingItem = parseGreetingObject(item);
					if (greetingItem != null) {
						result.add(greetingItem);
					}
				}
			} else {
				Logger.warn(TAG, "Null object supplied to greeting parsing");
			}
		} catch (JSONException e) {
			Logger.warn(TAG, "Error while parsing JSON + " + e.getMessage());
			e.printStackTrace();
		}

		return result;
	}

	private AssetBean parseGreetingObject(JSONObject object) {
		AssetBean greetingBean = null;

		try {
			if (object != null) {
				greetingBean = new AssetBean();
				if (object.has("id")) {
					greetingBean.setId(object.getString("id"));
				}

				if (object.has("url")) {
					greetingBean.setUrl(object.getString("url"));
				}

				if (object.has("title")) {
					greetingBean.setTitle(object.getString("title"));
				}

				if (object.has("thumbnail_url")) {
					greetingBean.setThumbnailURL(object.getString("thumbnail_url"));
				}

				if (object.has("auto_id")) {
					greetingBean.setAutoID(object.getInt("auto_id"));
				}

				if (object.has("categories")) {
					JSONArray array = object.getJSONArray("categories");
					if (array.length() > 0) {
						ArrayList<String> categories = new ArrayList<String>();
						for (int i = 0; i < array.length(); i++) {
							categories.add(array.getString(i));
						}
						greetingBean.setCategory(categories);
					}
				}

			} else {
				Logger.warn(TAG, "Empty object supplied to parse in the greetings");
			}
		} catch (JSONException e) {
			Logger.warn(TAG, "Error while parsing greeting object " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			Logger.warn(TAG, "Something bad happened while parsing greeting card result" + e.getMessage());
			e.printStackTrace();
		}

		return greetingBean;
	}
}// end of class