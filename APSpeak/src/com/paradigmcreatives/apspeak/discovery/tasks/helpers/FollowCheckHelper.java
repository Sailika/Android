package com.paradigmcreatives.apspeak.discovery.tasks.helpers;

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

import com.paradigmcreatives.apspeak.app.model.FollowCheckBean;
import com.paradigmcreatives.apspeak.app.util.AppPropertiesUtil;
import com.paradigmcreatives.apspeak.app.util.Util;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.app.util.constants.JSONConstants;
import com.paradigmcreatives.apspeak.app.util.constants.ServerConstants;
import com.paradigmcreatives.apspeak.app.util.network.NetworkManager;
import com.paradigmcreatives.apspeak.logging.Logger;

/**
 * Helper class to check if a userA follows another userB or not
 * 
 * @author robin
 * 
 */
public class FollowCheckHelper {
	private static final String TAG = "FollowCheckHelper";
	private Context context = null;
	private String userA = null;
	private String userB = null;

	public FollowCheckHelper(Context context, String userA, String userB) {
		this.context = context;
		this.userA = userA;
		this.userB = userB;
	}

	public FollowCheckBean execute() {
		FollowCheckBean follows = null;
		if (context != null && !TextUtils.isEmpty(userA) && !TextUtils.isEmpty(userB)) {

			HttpGet httpGet = null;
			HttpClient httpclient = null;
			try {
				final HttpParams httpParams = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParams, ServerConstants.CONNECTION_TIMEOUT);
				httpGet = new HttpGet(ServerConstants.NODE_SERVER_URL + ServerConstants.DOES_FOLLOW_PREFIX + userA
						+ ServerConstants.DOES_FOLLOW_BETWEEN + userB);

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
					String resultString = Util.convertingInputToString(resEntity.getContent());

					switch (statusCode) {
					case HttpStatus.SC_OK:
						follows = parseResponse(resultString);
						break;

					case HttpStatus.SC_INTERNAL_SERVER_ERROR:
						Logger.warn(TAG, "Server not responding");
						follows = new FollowCheckBean(userA, userB, false,
								"Ooopss!! Server misbehaved. Please give it another shot!", null);
						break;

					default:
						follows = new FollowCheckBean(userA, userB, false, response.getStatusLine().getReasonPhrase(), null);
						Logger.warn(TAG, "Error in response: " + response.getStatusLine().getReasonPhrase());
						break;
					}
				} else {
					follows = new FollowCheckBean(userA, userB, false, "Empty response received from server", null);
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
			Logger.warn(TAG, "One of the requried entry is empty. Context: " + context + ", userA: " + userA
					+ ", userB: " + userB);
		}

		return follows;
	}

	private FollowCheckBean parseResponse(String resultString) {
		FollowCheckBean result = null;
		try {
			if (!TextUtils.isEmpty(resultString)) {
				JSONObject obj = new JSONObject(resultString);
				if (obj.has(JSONConstants.SUCCESS)) {
					if (obj.getBoolean(JSONConstants.SUCCESS)) {
						if (obj.has(JSONConstants.RESULT)) {
							JSONObject resObj = obj.getJSONObject(JSONConstants.RESULT);
							    String status = null;
							    if(resObj.has(JSONConstants.STATUS)){
								status = resObj.getString(JSONConstants.STATUS);
							    }
							if (resObj.has(JSONConstants.FOLLOWS)) {
								if (resObj.getBoolean(JSONConstants.FOLLOWS)) {
									result = new FollowCheckBean(userA, userB, true, null, status);
								} else {
									result = new FollowCheckBean(userA, userB, false, null, status);
								}
							}
						}
					} else {
						result = new FollowCheckBean(userA, userB, false,
								"Server returned a -ve response for query execution", null);
					}
				}
			}
		} catch (JSONException e) {
			Logger.warn(TAG, "JSON Exception while parsing the response of follow check. Error: " + e);
		} catch (Exception e) {
			Logger.warn(TAG, "Error while parsing the response of follow check. Error: " + e);
		}

		return result;

	}
}
