package com.paradigmcreatives.apspeak.user.tasks.helpers;

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
import android.text.TextUtils;

import com.paradigmcreatives.apspeak.app.model.User;
import com.paradigmcreatives.apspeak.app.util.AppPropertiesUtil;
import com.paradigmcreatives.apspeak.app.util.Util;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.app.util.constants.JSONConstants;
import com.paradigmcreatives.apspeak.app.util.constants.ServerConstants;
import com.paradigmcreatives.apspeak.app.util.network.NetworkManager;
import com.paradigmcreatives.apspeak.logging.Logger;
import com.paradigmcreatives.apspeak.user.parsers.FullProfileParser;

public class FullProfileHelper {

    private static final String TAG = "FullProfileHelper";

    private String userID = null;
    private Context context = null;

    public FullProfileHelper(Context context, String userID) {
	this.context = context;
	this.userID = userID;
    }

    public User execute() {
	User user = null;
	if (context != null) {
	    if (TextUtils.isEmpty(userID)) {
		userID = AppPropertiesUtil.getUserID(context);
	    }

	    HttpGet httpGet = null;
	    HttpClient httpclient = null;
	    try {
		final HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, ServerConstants.CONNECTION_TIMEOUT);
		StringBuilder urlBuilder = new StringBuilder(ServerConstants.NODE_SERVER_URL
			+ ServerConstants.USER_PROFILE_NETWORK_FETCH + userID);
		httpGet = new HttpGet(urlBuilder.toString());
		NetworkManager.getInstance().register(httpGet);

		// Add session id as request header via Cookie name
		if (Constants.IS_SESSION_ENABLED) {
		    httpGet.addHeader(Constants.REQUEST_COOKIE, JSONConstants.SESSIONID + AppPropertiesUtil.getSessionId(context));
		}

		httpclient = new DefaultHttpClient(httpParams);

		HttpResponse response = httpclient.execute(httpGet);

		HttpEntity resEntity = response.getEntity();
		if (resEntity != null) {
		    int statusCode = response.getStatusLine().getStatusCode();
		    Logger.info(TAG, "status code is:" + statusCode);
		    String result = null;
		    switch (statusCode) {
		    case HttpStatus.SC_OK:
			result = Util.convertingInputToString(resEntity.getContent());
			if (!TextUtils.isEmpty(result)) {
			    FullProfileParser parser = new FullProfileParser(result);
			    user = parser.parse();
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
	    Logger.warn(TAG, "Empty context supplied while trying to fetch the friends");
	}

	return user;
    }
}
