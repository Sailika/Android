package com.paradigmcreatives.apspeak.cues;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import android.util.Log;

import com.paradigmcreatives.apspeak.app.util.google.image.beans.ImageResultsBean;

public class CueBackgroundsResultParser {
	private static String TAG = "CuesResultParser";

	/**
	 * Parses the results object
	 * 
	 * @param imageResultsArray
	 * @return
	 */
	public static ArrayList<ImageResultsBean> parse(JSONArray imageResultsArray) {
		ArrayList<ImageResultsBean> result = null;

		try {
			if (imageResultsArray != null) {
				result = new ArrayList<ImageResultsBean>();

				int length = imageResultsArray.length();
				JSONObject item;
				ImageResultsBean imageResultsBeanItem;
				for (int i = 0; i < length; i++) {
					item = imageResultsArray.getJSONObject(i);
					imageResultsBeanItem = new ImageResultsBean();

					if (item.has("thumbnail_url")) {
						imageResultsBeanItem.setThumbnailURL(item
								.getString("thumbnail_url"));
					}

					if (item.has("url")) {
						String url = item.getString("url");
						if (!TextUtils.isEmpty(url)) {
							/*
							 * String IMAGE_PATTERN =
							 * "([^\\s]+(\\.(?i)(jpg|png|gif|bmp))$)"; if
							 * (!Pattern.matches(IMAGE_PATTERN, url)) { url =
							 * !TextUtils.isEmpty(imageResultsBeanItem
							 * .getThumbnailURL()) ? new String(
							 * imageResultsBeanItem.getThumbnailURL()) : null; }
							 * } else { url =
							 * !TextUtils.isEmpty(imageResultsBeanItem
							 * .getThumbnailURL()) ? new String(
							 * imageResultsBeanItem.getThumbnailURL()) : null; }
							 */
							imageResultsBeanItem.setURL(url);
						}
					}

					result.add(imageResultsBeanItem);

				}
			}
		} catch (JSONException e) {
			Log.w(TAG,
					"Error while parsing results attribute " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			Log.w(TAG, "Something bad happened " + e.getMessage());
			e.printStackTrace();
		}

		return result;
	}
}
