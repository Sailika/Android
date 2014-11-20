package com.paradigmcreatives.apspeak.registration.tasks;

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
import com.paradigmcreatives.apspeak.logging.Logger;
import com.paradigmcreatives.apspeak.registration.handlers.UpdateGCMIdHandler;

/**
 * Updates the latest GCM id in the server
 * 
 * @author Soumya Behera | Paradigm Creatives
 * 
 */
public class UpdateGCMIdThread extends Thread {
    private static final String TAG = "UpdateGCMIdThread";

    private Context context;
    private UpdateGCMIdHandler handler;

    private static final String PUSH_ID = "push_id";
    private static final String PLATFORM = "platform";

    private static final String RESULT = "result";
    private static final String TRUE = "true";

    public UpdateGCMIdThread(Context context) {
	super();
	this.context = context;
    }

    private void willStartTask() {
	if (handler != null) {
	    handler.willStartTask();
	} else {
	    Logger.warn(TAG, "Handler is null");
	}
    }

    private void failed(int statusCode, int errorCode, String reasonPhrase) {
	if (handler != null) {
	    handler.failed(statusCode, errorCode, reasonPhrase);
	} else {
	    Logger.warn(TAG, "Handler is null");
	}
    }

    private void didUpdate() {
	if (handler != null) {
	    handler.didUpdate();
	} else {
	    Logger.warn(TAG, "Handler is null");
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Thread#run()
     */
    @Override
    public void run() {
	if (context != null) {
	    // String phoneNumber = AppPropertiesUtil.getPhoneNumber(context);
	    // String countryCode = AppPropertiesUtil.getCountryCodeFromSharedPreferences(context);
	    String gcmId = AppPropertiesUtil.getGCMID(context);
	    if (/* !TextUtils.isEmpty(phoneNumber) && !TextUtils.isEmpty(countryCode) && */!TextUtils.isEmpty(gcmId)) {
		willStartTask();
		HttpPost httpPost = null;
		HttpClient httpclient = null;
		try {
		    final HttpParams httpParams = new BasicHttpParams();
		    HttpConnectionParams.setConnectionTimeout(httpParams, ServerConstants.CONNECTION_TIMEOUT);
		    httpPost = new HttpPost(ServerConstants.SERVER_URL + ServerConstants.REGISTER_USER);
		    NetworkManager.getInstance().register(httpPost);

		    // Add session id as request header via Cookie name
		    if (Constants.IS_SESSION_ENABLED) {
			httpPost.addHeader(Constants.REQUEST_COOKIE,
				JSONConstants.SESSIONID + AppPropertiesUtil.getSessionId(context));
		    }

		    httpclient = new DefaultHttpClient(httpParams);

		    String jsonString = null;
		    JSONObject updateJson = new JSONObject();
		    // updateJson.put(UNIQUE_ID, phoneNumber);
		    // updateJson.put(COUNTRY_CODE, countryCode);
		    updateJson.put(PUSH_ID, gcmId);
		    updateJson.put(PLATFORM, Constants.PLATFORM_VALUE);

		    jsonString = updateJson.toString();

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
			    String result = null;
			    JSONObject responseJSON = null;
			    switch (statusCode) {
			    case HttpStatus.SC_OK:
				result = Util.convertingInputToString(resEntity.getContent());
				if (!TextUtils.isEmpty(result)) {
				    responseJSON = new JSONObject(result);
				    if (responseJSON.has(RESULT)
					    && TextUtils.equals(responseJSON.getString(RESULT), TRUE)) {
					didUpdate();
				    }
				} else {
				    Logger.warn(TAG, "Response is null");
				}

				break;

			    case HttpStatus.SC_INTERNAL_SERVER_ERROR:
				failed(statusCode, -1, response.getStatusLine().getReasonPhrase());
				Logger.warn(TAG, "Error in response:" + response.getStatusLine().getReasonPhrase());
				break;

			    default:
				failed(statusCode, -1, response.getStatusLine().getReasonPhrase());
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
		    failed(-1, -1, null);
		} catch (IllegalArgumentException iae) {
		    Logger.logStackTrace(iae);
		    failed(-1, -1, null);
		} catch (IllegalStateException e) {
		    Logger.logStackTrace(e);
		    failed(-1, -1, null);
		} catch (UnsupportedEncodingException e) {
		    Logger.logStackTrace(e);
		    failed(-1, -1, null);
		} catch (IOException e) {
		    Logger.logStackTrace(e);
		    failed(-1, -1, null);
		} catch (Exception e) {
		    Logger.logStackTrace(e);
		    failed(-1, -1, null);
		} finally {
		    NetworkManager.getInstance().unRegister(httpPost);
		    if (httpclient != null) {
			httpclient.getConnectionManager().shutdown();
		    }
		}
	    } else {
		Logger.warn(TAG, "GCM id or user unique number or code is empty");
	    }
	} else {
	    Logger.warn(TAG, "Context is null");
	}

	super.run();
    }
}
