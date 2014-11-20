package com.paradigmcreatives.apspeak.app.util.google.image;

import java.util.ArrayList;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import android.util.Log;

import com.paradigmcreatives.apspeak.app.util.google.image.beans.CursorBean;
import com.paradigmcreatives.apspeak.app.util.google.image.beans.ImageResultsBean;
import com.paradigmcreatives.apspeak.app.util.google.image.beans.PagesBean;
import com.paradigmcreatives.apspeak.app.util.google.image.beans.SearchResultsBean;

/**
 * Contains convenience methods for parsing the JSON result and return the result bean
 * 
 * @author robin
 * 
 */
public class ResultParser {

    private static final String TAG = "ResultParser";
    JSONObject object;

    public ResultParser(JSONObject object) {
	this.object = object;
    }

    public SearchResultsBean parse() {
	SearchResultsBean result = null;

	if (object != null) {
	    result = new SearchResultsBean();
	    try {
		if (object.has("responseStatus")) {
		    String responseStatus = object.getString("responseStatus");
		    result.setResponseStatus(Integer.parseInt(responseStatus));

		    if (object.has("responseDetails")) {
			result.setResponseDetails(object.getString("responseDetails"));
		    }

		    if (!TextUtils.isEmpty(responseStatus) && responseStatus.equals("200")) {

			if (object.has("responseData")) {
			    JSONObject responseData = object.getJSONObject("responseData");
			    if (responseData != null) {
				if (responseData.has("results")) {
				    JSONArray imageResults = responseData.getJSONArray("results");
				    result.setImageResults(parseImageResults(imageResults));
				}

				if (responseData.has("cursor")) {
				    JSONObject cursor = responseData.getJSONObject("cursor");
				    result.setCursor(parseCursor(cursor));
				}
			    }
			}
		    }
		}
	    } catch (JSONException e) {
		Log.w(TAG, "Error parsing the JSON - " + e.getMessage());
		e.printStackTrace();
	    } catch (Exception e) {
		Log.w(TAG, "Something bad happened - " + e.getMessage());
		e.printStackTrace();
	    }

	}

	return result;
    }

    /**
     * Parses the results object
     * 
     * @param imageResultsArray
     * @return
     */
    private ArrayList<ImageResultsBean> parseImageResults(JSONArray imageResultsArray) {
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

		    if (item.has("width")) {
			imageResultsBeanItem.setWidth(item.getInt("width"));
		    }

		    if (item.has("height")) {
			imageResultsBeanItem.setHeight(item.getInt("width"));
		    }

		    if (item.has("tbWidth")) {
			imageResultsBeanItem.setThumbnailWidth(item.getInt("tbWidth"));
		    }

		    if (item.has("tbHeight")) {
			imageResultsBeanItem.setThumbnailHeight(item.getInt("tbHeight"));
		    }

		    if (item.has("tbUrl")) {
			imageResultsBeanItem.setThumbnailURL(item.getString("tbUrl"));
		    }

		    if (item.has("url")) {
			String url = item.getString("url");
			if (!TextUtils.isEmpty(url)) {
			/*    String IMAGE_PATTERN = "([^\\s]+(\\.(?i)(jpg|png|gif|bmp))$)";
			    if (!Pattern.matches(IMAGE_PATTERN, url)) {
				url = !TextUtils.isEmpty(imageResultsBeanItem.getThumbnailURL()) ? new String(
					imageResultsBeanItem.getThumbnailURL()) : null;
			    }
			} else {
			    url = !TextUtils.isEmpty(imageResultsBeanItem.getThumbnailURL()) ? new String(
				    imageResultsBeanItem.getThumbnailURL()) : null;
			}*/
			imageResultsBeanItem.setURL(url);
			}
		    }

		    if (item.has("titleNoFormatting")) {
			imageResultsBeanItem.setTitle(item.getString("titleNoFormatting"));
		    }

		    result.add(imageResultsBeanItem);

		}
	    }
	} catch (JSONException e) {
	    Log.w(TAG, "Error while parsing results attribute " + e.getMessage());
	    e.printStackTrace();
	} catch (Exception e) {
	    Log.w(TAG, "Something bad happened " + e.getMessage());
	    e.printStackTrace();
	}

	return result;
    }

    /**
     * Parses the cursor object
     * 
     * @param object
     * @return
     */
    private CursorBean parseCursor(JSONObject cursor) {
	CursorBean result = null;
	try {
	    if (cursor != null) {
		result = new CursorBean();

		if (cursor != null) {
		    if (cursor.has("pages")) {
			JSONArray pages = cursor.getJSONArray("pages");
			result.setPages(parsePages(pages));
		    }

		    if (cursor.has("estimatedResultCount")) {
			result.setResultCount(cursor.getInt("estimatedResultCount"));
		    }

		    if (cursor.has("currentPageIndex")) {
			result.setCurrentPageIndex(cursor.getInt("currentPageIndex"));
		    }

		    if (cursor.has("moreResultsUrl")) {
			result.setMoreResultsUrl(cursor.getString("moreResultsUrl"));
		    }

		    if (cursor.has("searchResultTime")) {
			result.setSerachResultTime((float) cursor.getDouble("searchResultTime"));
		    }

		}
	    }
	} catch (JSONException e) {
	    Log.w(TAG, "Error while parsing the cursor attribute" + e.getMessage());
	    e.printStackTrace();
	} catch (Exception e) {
	    Log.w(TAG, "Something bad happened while parsing the cursror - " + e.getMessage());
	    e.printStackTrace();
	}

	return result;
    }

    /**
     * Parses the pages attribute of the result
     * 
     * @param pages
     * @return
     */
    private ArrayList<PagesBean> parsePages(JSONArray pages) {
	ArrayList<PagesBean> result = null;
	try {
	    if (pages != null) {
		result = new ArrayList<PagesBean>();
		int length = pages.length();
		JSONObject item;
		PagesBean pagesBeanItem;
		for (int i = 0; i < length; i++) {
		    item = pages.getJSONObject(i);
		    pagesBeanItem = new PagesBean();
		    if (item.has("start")) {
			pagesBeanItem.setStart(item.getInt("start"));
		    }

		    if (item.has("label")) {
			pagesBeanItem.setLabel(item.getInt("label"));
		    }
		    result.add(pagesBeanItem);
		}
	    }
	} catch (JSONException e) {
	    Log.w(TAG, "Error while parsing pages  " + e.getMessage());
	    e.printStackTrace();
	} catch (Exception e) {
	    Log.w(TAG, "Something bad happened while parsing pages - " + e.getMessage());
	    e.printStackTrace();
	}

	return result;
    }

}
