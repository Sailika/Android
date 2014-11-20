package com.paradigmcreatives.apspeak.app.util.server.assets;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;

import com.paradigmcreatives.apspeak.app.util.AppPropertiesUtil;
import com.paradigmcreatives.apspeak.app.util.Util;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.app.util.constants.JSONConstants;
import com.paradigmcreatives.apspeak.app.util.constants.ServerConstants;
import com.paradigmcreatives.apspeak.app.util.network.NetworkManager;
import com.paradigmcreatives.apspeak.app.util.server.assets.AssetDownloader.AssetType;
import com.paradigmcreatives.apspeak.app.util.server.assets.beans.CategoryBean;
import com.paradigmcreatives.apspeak.app.util.server.assets.parsers.CategoryParser;
import com.paradigmcreatives.apspeak.logging.Logger;

/**
 * Queries the server to get the categories from the server and the number of assets in each category
 * 
 * @author robin
 * 
 */
public class QueryCategories {

    private static final String TAG = "QueryCategories";

    private AssetType assetType;
    HashMap<AssetType, String> assetURLMapping = new HashMap<AssetType, String>();

    public QueryCategories(AssetType assetType) {
	this.assetType = assetType;
	initAssetURLMapping();
    }

    public ArrayList<CategoryBean> query(final Context context) {
	ArrayList<CategoryBean> result = null;

	JSONArray jsonResponse = executeWebQuery(context);

	if (jsonResponse != null) {
	    result = new CategoryParser().parse(jsonResponse);
	}

	return result;
    }

    private void initAssetURLMapping() {
	assetURLMapping.put(AssetType.GREETINGS, ServerConstants.GREETINGS_CATEGORIES_URL);
	assetURLMapping.put(AssetType.RAGE_FACE, ServerConstants.RAGE_FACE_CATEGORIES_URL);
	assetURLMapping.put(AssetType.EMOJI, ServerConstants.EMOJI_CATEGORIES_URL);
	assetURLMapping.put(AssetType.DOODLEWORLD, ServerConstants.DOODLEWORLD_CATEGORIES_URL);
	
    }

    /**
     * Performs the query on the server and returns the back the response in JSON form
     * 
     * @return
     */
    private JSONArray executeWebQuery(Context context) {
	JSONArray result = null;
	HttpGet httpGet = null;
	HttpClient httpClient = null;

	try {
	    if (assetType != null) {
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, ServerConstants.CONNECTION_TIMEOUT);
		String url = ServerConstants.SERVER_URL + assetURLMapping.get(assetType);
		httpGet = new HttpGet(url);
		NetworkManager.getInstance().register(httpGet);

		// Add session id as request header via Cookie name
		if (Constants.IS_SESSION_ENABLED) {
		    httpGet.addHeader(Constants.REQUEST_COOKIE, JSONConstants.SESSIONID + AppPropertiesUtil.getSessionId(context));
		}

		httpClient = new DefaultHttpClient(httpParams);  

		HttpResponse response = httpClient.execute(httpGet);

		HttpEntity resEntity = response.getEntity();

		if (resEntity != null) {
		    int statusCode = response.getStatusLine().getStatusCode();
		    switch (statusCode) {
		    case HttpStatus.SC_OK:
			String resultString = Util.convertingInputToString(resEntity.getContent());
			result = new JSONArray(resultString);
			break;
		    default:
			Logger.warn(TAG,
				"Some wrong response returned from the server while getting the greetings' category. Here is the response entity"
					+ resEntity.toString());
			break;

		    }

		}
	    } else {
		Logger.warn(TAG, "Empty asset type sent");
	    }

	} catch (UnsupportedEncodingException e) {
	    Logger.warn(TAG, e.getLocalizedMessage());
	    e.printStackTrace();
	} catch (ClientProtocolException e) {
	    Logger.warn(TAG, e.getLocalizedMessage());
	    e.printStackTrace();
	} catch (IOException e) {
	    Logger.warn(TAG, e.getLocalizedMessage());
	    e.printStackTrace();
	} catch (JSONException e) {
	    Logger.warn(TAG, e.getLocalizedMessage());
	    e.printStackTrace();
	} finally {
	    NetworkManager.getInstance().unRegister(httpGet);
	    if (httpClient != null) {
		httpClient.getConnectionManager().shutdown();
	    }

	}

	return result;
    }

}
