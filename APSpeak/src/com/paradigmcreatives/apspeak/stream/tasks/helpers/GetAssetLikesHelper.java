package com.paradigmcreatives.apspeak.stream.tasks.helpers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.paradigmcreatives.apspeak.app.model.ASSOCIATION_TYPE;
import com.paradigmcreatives.apspeak.app.model.Friend;
import com.paradigmcreatives.apspeak.app.util.AppPropertiesUtil;
import com.paradigmcreatives.apspeak.app.util.Util;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.app.util.constants.JSONConstants;
import com.paradigmcreatives.apspeak.app.util.constants.ServerConstants;
import com.paradigmcreatives.apspeak.app.util.network.NetworkManager;
import com.paradigmcreatives.apspeak.logging.Logger;
import com.paradigmcreatives.apspeak.stream.parsers.StreamAssetParser;

/**
 * Helper class used to fetch comments of an Asset
 * 
 * @author Dileep | neuv
 */
public class GetAssetLikesHelper {

    private final String TAG = "GetAssetLikesHelper";

    private Context context;
    private String assetId;
    private int startIndex;
    private int limit;

    public GetAssetLikesHelper(Context contxt, String assetId, int startIndex, int limit) {
	this.context = contxt;
	this.assetId = assetId;
	this.startIndex = startIndex;
	this.limit = limit;
    }

    public HashMap<ASSOCIATION_TYPE, ArrayList<Friend>> execute() {
	HashMap<ASSOCIATION_TYPE, ArrayList<Friend>> associations = null;
	if (!TextUtils.isEmpty(assetId)) {
	    HttpGet httpGet = null;
	    HttpClient httpclient = null;
	    try {
		final HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, ServerConstants.CONNECTION_TIMEOUT);
		StringBuilder urlBuilder = new StringBuilder(ServerConstants.NODE_SERVER_URL
			+ ServerConstants.USERASSET + Constants.URL_SLASH + assetId + ServerConstants.ASSOCIATIONS
			+ Constants.URL_SLASH + startIndex + Constants.URL_SLASH + limit);

		httpGet = new HttpGet(urlBuilder.toString());
		NetworkManager.getInstance().register(httpGet);

		// Add session id as request header via Cookie name
		if (Constants.IS_SESSION_ENABLED) {
		    httpGet.addHeader(Constants.REQUEST_COOKIE,
			    JSONConstants.SESSIONID + AppPropertiesUtil.getSessionId(context));
		}

		httpclient = new DefaultHttpClient(httpParams);

		HttpResponse response = httpclient.execute(httpGet);

		HttpEntity resEntity = response.getEntity();
		if (resEntity != null) {
		    int statusCode = response.getStatusLine().getStatusCode();
		    Logger.info(TAG, "status code is:" + statusCode);
		    String result = Util.convertingInputToString(resEntity.getContent());
		    JSONObject responseJSON = null;
		    switch (statusCode) {
		    case HttpStatus.SC_OK:
			if (!TextUtils.isEmpty(result)) {
			    responseJSON = new JSONObject(result);
			    if (responseJSON != null && responseJSON.has(JSONConstants.SUCCESS)
				    && responseJSON.getBoolean(JSONConstants.SUCCESS)) {
				if (responseJSON.has(JSONConstants.RESULT)) {
				    JSONObject resultObject = responseJSON.getJSONObject(JSONConstants.RESULT);
				    if (resultObject != null && resultObject.has(JSONConstants.ASSOCIATIONS)) {
					JSONArray associationsJSONArray = resultObject
						.getJSONArray(JSONConstants.ASSOCIATIONS);
					StreamAssetParser parser = new StreamAssetParser(context);
					associations = parser.parseUsersInAssociation(associationsJSONArray);
				    }
				}
			    } else {
				Logger.warn(TAG, "No assets in stream response");
			    }
			} else {
			    Logger.warn(TAG, "Response is null");
			}

			break;

		    case HttpStatus.SC_INTERNAL_SERVER_ERROR:
			Logger.warn(TAG, "Error in response:" + response.getStatusLine().getReasonPhrase());
			break;

		    default:
			Logger.warn(TAG, "Error in response:" + response.getStatusLine().getReasonPhrase());
			break;
		    }
		} else {
		    Logger.warn(TAG, "result entity is null");
		}
	    } catch (JSONException je) {
		Logger.logStackTrace(je);
	    } catch (IllegalArgumentException iae) {
		Logger.logStackTrace(iae);
	    } catch (IllegalStateException e) {
		Logger.logStackTrace(e);
	    } catch (UnsupportedEncodingException e) {
		Logger.logStackTrace(e);
	    } catch (IOException e) {
		Logger.logStackTrace(e);
	    } catch (Exception e) {
		Logger.logStackTrace(e);
	    } finally {
		NetworkManager.getInstance().unRegister(httpGet);
		if (httpclient != null) {
		    httpclient.getConnectionManager().shutdown();
		}
	    }
	} else {
	    Logger.warn(TAG, "assetId is null");
	}
	return associations;
    }
}
