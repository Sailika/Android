package com.paradigmcreatives.apspeak.assets.tasks.helpers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.paradigmcreatives.apspeak.app.util.AppPropertiesUtil;
import com.paradigmcreatives.apspeak.app.util.Util;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.app.util.constants.JSONConstants;
import com.paradigmcreatives.apspeak.app.util.constants.ServerConstants;
import com.paradigmcreatives.apspeak.app.util.network.NetworkManager;
import com.paradigmcreatives.apspeak.app.util.server.assets.AssetDownloader.AssetType;
import com.paradigmcreatives.apspeak.logging.Logger;

/**
 * Gets list of assets from server for the given Asset type
 * 
 * @author Dileep | neuv
 * 
 */
public class GetAssetsListHelper {

    private Context context;
    private final int LIMIT_VALUE = 200;
    private final String TAG = "GetAssetsListHelper";

    public GetAssetsListHelper(final Context context) {
	super();
	this.context = context;
    }

    public String execute(AssetType assetType) {
	String result = null;
	if (context != null && assetType != null) {
	    HttpPost httpPost = null;
	    HttpClient httpclient = null;
	    try {
		final HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, ServerConstants.CONNECTION_TIMEOUT);
		if (assetType == AssetType.EMOJI) {
		    httpPost = new HttpPost(ServerConstants.SERVER_URL + ServerConstants.EMOJI_ASSET_URL);
		} else if (assetType == AssetType.FRAME) {
		    httpPost = new HttpPost(ServerConstants.SERVER_URL + ServerConstants.FRAME_ASSETSLIST_URL);
		}
		NetworkManager.getInstance().register(httpPost);

		// Add session id as request header via Cookie name
		if (Constants.IS_SESSION_ENABLED) {
		    httpPost.addHeader(Constants.REQUEST_COOKIE,
			    JSONConstants.SESSIONID + AppPropertiesUtil.getSessionId(context));
		}

		httpclient = new DefaultHttpClient(httpParams);

		String jsonString = null;
		JSONObject requestJSON = new JSONObject();
		requestJSON.put(JSONConstants.LIMIT, LIMIT_VALUE);
		jsonString = requestJSON.toString();

		if (!TextUtils.isEmpty(jsonString)) {
		    StringEntity stringEntity = new StringEntity(jsonString);
		    Logger.info(TAG, "json string:" + jsonString);
		    stringEntity.setContentType(ServerConstants.CONTENT_TYPE_JSON);

		    httpPost.setEntity(stringEntity);
		    HttpResponse response = httpclient.execute(httpPost);

		    HttpEntity resEntity = response.getEntity();
		    if (resEntity != null) {
			int statusCode = response.getStatusLine().getStatusCode();
			Logger.info(TAG, "status code is:" + statusCode);
			switch (statusCode) {
			case HttpStatus.SC_OK:
			    result = Util.convertingInputToString(resEntity.getContent());
			    break;
			default:
			    Logger.warn(TAG, "Error in response:" + response.getStatusLine().getReasonPhrase());
			    break;
			}
		    } else {
			Logger.warn(TAG, "result entity is null");
		    }
		} else {
		    Logger.warn(TAG, "json string is null");
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
		NetworkManager.getInstance().unRegister(httpPost);
		if (httpclient != null) {
		    httpclient.getConnectionManager().shutdown();
		}
	    }
	}
	return result;
    }
}
