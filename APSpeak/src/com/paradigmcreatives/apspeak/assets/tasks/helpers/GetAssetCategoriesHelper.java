package com.paradigmcreatives.apspeak.assets.tasks.helpers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;

import com.paradigmcreatives.apspeak.app.util.AppPropertiesUtil;
import com.paradigmcreatives.apspeak.app.util.Util;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.app.util.constants.JSONConstants;
import com.paradigmcreatives.apspeak.app.util.constants.ServerConstants;
import com.paradigmcreatives.apspeak.app.util.network.NetworkManager;
import com.paradigmcreatives.apspeak.app.util.server.assets.AssetDownloader.AssetType;
import com.paradigmcreatives.apspeak.logging.Logger;

/**
 * Gets Categories list of a given Asset type
 * 
 * @author Dileep | neuv
 * 
 */
public class GetAssetCategoriesHelper {

    private Context context = null;
    private static final String TAG = "GetAssetCategoriesHelper";

    public GetAssetCategoriesHelper(final Context context) {
	super();
	this.context = context;
    }

    public String execute(AssetType assetType) {
	String result = null;
	if (context != null && assetType != null) {
	    HttpGet httpGet = null;
	    HttpClient httpclient = null;
	    try {
		final HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, ServerConstants.CONNECTION_TIMEOUT);
		StringBuilder urlBuilder = null;

		if (assetType == AssetType.EMOJI) {
		    urlBuilder = new StringBuilder(ServerConstants.SERVER_URL + ServerConstants.EMOJI_CATEGORIES_URL);
		} else if (assetType == AssetType.FRAME) {
		    urlBuilder = new StringBuilder(ServerConstants.SERVER_URL + ServerConstants.FRAME_CATEGORIES_URL);
		}

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
		    switch (statusCode) {
		    case HttpStatus.SC_OK:
			result = Util.convertingInputToString(resEntity.getContent());
			break;

		    default:
			Logger.warn(TAG, "Error in response:" + response.getStatusLine().getReasonPhrase());
			break;
		    }
		}
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
	}
	return result;
    }
}
