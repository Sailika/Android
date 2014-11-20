package com.paradigmcreatives.apspeak.app.util.server.assets.parsers;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.paradigmcreatives.apspeak.app.util.server.assets.beans.CategoryBean;
import com.paradigmcreatives.apspeak.logging.Logger;

/**
 * Parser for category
 * 
 * @author robin
 * 
 */
public class CategoryParser {

    private static final String TAG = "CategoryParser";

    public ArrayList<CategoryBean>  parse(JSONArray object) {
	ArrayList<CategoryBean> result = null;

	try {
	    if (object != null) {
		int length = object.length(); 
		if (length > 0) {
		    result = new ArrayList<CategoryBean>();
		    CategoryBean categoryBean = null;
		    JSONObject categoryItem;
		    for (int i = 0; i < length; i++) {
			categoryBean = new CategoryBean();
			categoryItem = object.getJSONObject(i);
			
			if (categoryItem.has("category")) {
			    categoryBean.setName(categoryItem.getString("category"));
			}

			if (categoryItem.has("count")) {
			    categoryBean.setCardsCount(categoryItem.getInt("count"));
			}

			result.add(categoryBean);
		    }
		}
	    }
	} catch (JSONException e) {
	    Logger.warn(TAG, "Error while parsing category object " + e.getMessage());
	    e.printStackTrace();
	} catch (Exception e) {
	    Logger.warn(TAG, "Something bad happened while parsing category object" + e.getMessage());
	    e.printStackTrace();
	}

	return result;
    }

}
