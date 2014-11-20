package com.paradigmcreatives.apspeak.registration.tasks.helpers;

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
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.paradigmcreatives.apspeak.app.model.HandleAvailabilityBean;
import com.paradigmcreatives.apspeak.app.util.AppPropertiesUtil;
import com.paradigmcreatives.apspeak.app.util.Util;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.app.util.constants.JSONConstants;
import com.paradigmcreatives.apspeak.app.util.constants.ServerConstants;
import com.paradigmcreatives.apspeak.app.util.network.NetworkManager;
import com.paradigmcreatives.apspeak.logging.Logger;

public class HandleAvailabilityHelper {

    private static final String TAG = "HandleAvailabilityHelper";
    private Context context;
    private String handle;

    // Requests/response attributes
    private static final String RESULT = "result";
    private static final String TRUE = "true";

    /**
     * @param context
     * @param handle
     */
    public HandleAvailabilityHelper(Context context, String handle) {
	this.context = context;
	this.handle = handle;
    }

    public HandleAvailabilityBean execute() {
	HandleAvailabilityBean handleCheckBean = new HandleAvailabilityBean(false);
	if (context != null) {
	    if (!TextUtils.isEmpty(handle)) {
		HttpGet httpGet = null;
		HttpClient httpclient = null;
		try {
		    final HttpParams httpParams = new BasicHttpParams();
		    HttpConnectionParams.setConnectionTimeout(httpParams, ServerConstants.CONNECTION_TIMEOUT);
		    StringBuilder urlBuilder = new StringBuilder(ServerConstants.SERVER_URL
			    + ServerConstants.USER_PROFILE_HANDLE + handle);
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
			JSONObject responseJSON = null;
			switch (statusCode) {
			case HttpStatus.SC_OK:
			    result = Util.convertingInputToString(resEntity.getContent());
			    if (!TextUtils.isEmpty(result)) {
				responseJSON = new JSONObject(result);
				if (responseJSON.has(RESULT) && TextUtils.equals(responseJSON.getString(RESULT), TRUE)) {
				    handleCheckBean.setAvailability(true);
				    updateAppStateForHandle();
				}
			    } else {
				onFailed();
				Logger.warn(TAG, "Response is null");
			    }

			    break;

			case HttpStatus.SC_INTERNAL_SERVER_ERROR:
			    onFailed();
			    Logger.warn(TAG, "Error in response:" + response.getStatusLine().getReasonPhrase());
			    break;

			default:
			    onFailed();
			    Logger.warn(TAG, "Error in response:" + response.getStatusLine().getReasonPhrase());
			    break;
			}
		    } else {
			onFailed();
			Logger.warn(TAG, "result entity is null");
		    }
		} catch (JSONException je) {
		    onFailed();
		    Logger.logStackTrace(je);
		} catch (IllegalArgumentException iae) {
		    onFailed();
		    Logger.logStackTrace(iae);
		} catch (IllegalStateException e) {
		    onFailed();
		    Logger.logStackTrace(e);
		} catch (UnsupportedEncodingException e) {
		    onFailed();
		    Logger.logStackTrace(e);
		} catch (IOException e) {
		    onFailed();
		    Logger.logStackTrace(e);
		} catch (Exception e) {
		    onFailed();
		    Logger.logStackTrace(e);
		} finally {
		    NetworkManager.getInstance().unRegister(httpGet);
		    if (httpclient != null) {
			httpclient.getConnectionManager().shutdown();
		    }
		}
	    } else {
		onFailed();
		Logger.warn(TAG, "User's unique handle is null or empty");
	    }
	} else {
	    onFailed();
	    Logger.warn(TAG, "Context is null");
	}

	return handleCheckBean;
    }

    private void updateAppStateForHandle() {
    	
	AppPropertiesUtil.setSelectedUniqueHandle(context, handle);
	AppPropertiesUtil.setUniqueHandleCreatedStatus(context, true);

    }
    
    /**
     * Update the states if handle selection fails
     */
    private void onFailed() {
	AppPropertiesUtil.setSelectedUniqueHandle(context, null);
	AppPropertiesUtil.setSelectedUniqueHandlePassword(context, null);
	AppPropertiesUtil.setUniqueHandleCreatedStatus(context, false);
    }

}
