package com.paradigmcreatives.apspeak.settings.tasks.helpers;

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

import com.paradigmcreatives.apspeak.app.util.AppPropertiesUtil;
import com.paradigmcreatives.apspeak.app.util.Util;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.app.util.constants.JSONConstants;
import com.paradigmcreatives.apspeak.app.util.constants.ServerConstants;
import com.paradigmcreatives.apspeak.app.util.network.NetworkManager;
import com.paradigmcreatives.apspeak.logging.Logger;

/**
 * Helper class to set requireFollowPermission setting with Whatsay server
 * 
 * @author robin
 * 
 */
public class RequireFollowPermissionHelper {
    private static final String TAG = "RequireFollowPermissionHelper";
    private Context context = null;
    private String userId = null;
    private boolean requireFollowPermission = false;

    public RequireFollowPermissionHelper(Context context, String userId, boolean requireFollowPermission) {
	this.context = context;
	this.userId = userId;
	this.requireFollowPermission = requireFollowPermission;
    }

    public boolean execute() {
	boolean isSuccess = false;
	if (context != null && !TextUtils.isEmpty(userId)) {

	    HttpGet httpGet = null;
	    HttpClient httpclient = null;
	    try {
		final HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, ServerConstants.CONNECTION_TIMEOUT);
		httpGet = new HttpGet(ServerConstants.NODE_SERVER_URL + ServerConstants.SETTINGS
			+ ServerConstants.USER_STREAM_FETCH_PREFIX + userId + ServerConstants.REQUIRE_FOLLOW_PERMISSION
			+ requireFollowPermission);

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
		    String resultString = Util.convertingInputToString(resEntity.getContent());

		    switch (statusCode) {
		    case HttpStatus.SC_OK:
			isSuccess = true;
			break;

		    case HttpStatus.SC_INTERNAL_SERVER_ERROR:
			Logger.warn(TAG, "Server not responding");
			break;

		    default:
			Logger.warn(TAG, "Error in response: " + response.getStatusLine().getReasonPhrase());
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
	    Logger.warn(TAG, "One of the requried entry is empty");
	}
	return isSuccess;
    }
}
