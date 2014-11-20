package com.paradigmcreatives.apspeak.app.util.server.assets.parsers;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.paradigmcreatives.apspeak.app.util.server.assets.beans.AssetBean;
import com.paradigmcreatives.apspeak.logging.Logger;

/**
 * Class for parsing results of Sticker cards
 * 
 * @author robin
 * 
 */
public class StickerResultParser implements AssetsParser {

	private static final String TAG = "StickersResultParser";

	@Override
	public ArrayList<AssetBean> parse(JSONArray object) {
		ArrayList<AssetBean> result = null;
		try {
			if (object != null) {
				int length = object.length();
				result = new ArrayList<AssetBean>();
				JSONObject item;
				AssetBean StickerItem;
				for (int i = 0; i < length; i++) {
					item = object.getJSONObject(i);
					StickerItem = parseStickerObject(item);
					if (StickerItem != null) {
						result.add(StickerItem);
					}
				}
			} else {
				Logger.warn(TAG, "Null object supplied to Sticker parsing");
			}
		} catch (JSONException e) {
			Logger.warn(TAG, "Error while parsing JSON + " + e.getMessage());
			e.printStackTrace();
		}

		return result;
	}

	private AssetBean parseStickerObject(JSONObject object) {
		AssetBean stickerBean = null;

		try {
			if (object != null) {
				stickerBean = new AssetBean();
				if (object.has("id")) {
					stickerBean.setId(object.getString("id"));
				}

				if (object.has("url")) {
					stickerBean.setUrl(object.getString("url"));
				}

				if (object.has("title")) {
					stickerBean.setTitle(object.getString("title"));
				}

				if (object.has("thumbnail_url")) {
					stickerBean.setThumbnailURL(object.getString("thumbnail_url"));
				}

				if (object.has("auto_id")) {
					stickerBean.setAutoID(object.getInt("auto_id"));
				}

				if (object.has("categories")) {
					JSONArray array = object.getJSONArray("categories");
					if (array.length() > 0) {
						ArrayList<String> categories = new ArrayList<String>();
						for (int i = 0; i < array.length(); i++) {
							categories.add(array.getString(i));
						}
						stickerBean.setCategory(categories);
					}
				}

			} else {
				Logger.warn(TAG, "Empty object supplied to parse in the Stickers");
			}
		} catch (JSONException e) {
			Logger.warn(TAG, "Error while parsing Sticker object " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			Logger.warn(TAG, "Something bad happened while parsing Sticker result" + e.getMessage());
			e.printStackTrace();
		}

		return stickerBean;
	}
}// end of class