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
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.paradigmcreatives.apspeak.app.util.AppPropertiesUtil;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.app.util.constants.JSONConstants;
import com.paradigmcreatives.apspeak.app.util.constants.ServerConstants;
import com.paradigmcreatives.apspeak.app.util.network.NetworkManager;
import com.paradigmcreatives.apspeak.logging.Logger;

public class UserInappropriateHelper {

	private static final String TAG = "UserInappropriateHelper";
	private Context context;

	/**
	 * @param context
	 */
	public UserInappropriateHelper(Context context) {
		this.context = context;
	}

	public boolean execute(JSONObject requestJSON) {
		boolean isFlagged = false;
		if (context != null && requestJSON != null) {
			HttpPost httpPost = null;
			HttpClient httpclient = null;
			try {
				final HttpParams httpParams = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParams,
						ServerConstants.CONNECTION_TIMEOUT);
				httpPost = new HttpPost(ServerConstants.NODE_SERVER_URL
						+ ServerConstants.FLAG_USER_URL);
				NetworkManager.getInstance().register(httpPost);

				// Add session id as request header via Cookie name
				if (Constants.IS_SESSION_ENABLED) {
					httpPost.addHeader(
							Constants.REQUEST_COOKIE,
							JSONConstants.SESSIONID
									+ AppPropertiesUtil.getSessionId(context));
				}

				httpclient = new DefaultHttpClient(httpParams);

				String jsonString = null;
				JSONObject updateJson = requestJSON;

				jsonString = updateJson.toString();

				if (!TextUtils.isEmpty(jsonString)) {
					StringEntity stringEntity = new StringEntity(jsonString);
					Logger.info(TAG, "json string:" + jsonString);
					stringEntity
							.setContentType(ServerConstants.CONTENT_TYPE_JSON);

					httpPost.setEntity(stringEntity);
					HttpResponse response = httpclient.execute(httpPost);

					HttpEntity resEntity = response.getEntity();
					if (resEntity != null) {
						int statusCode = response.getStatusLine()
								.getStatusCode();
						Logger.info(TAG, "status code is:" + statusCode);
						switch (statusCode) {
						case HttpStatus.SC_OK:
							isFlagged = true;
							break;
						case HttpStatus.SC_INTERNAL_SERVER_ERROR:
							Logger.warn(TAG, "Error in response:"
									+ response.getStatusLine()
											.getReasonPhrase());
							break;

						default:
							Logger.warn(TAG, "Error in response:"
									+ response.getStatusLine()
											.getReasonPhrase());
							break;
						}
					} else {
						Logger.warn(TAG, "result entity is null");
					}
				} else {
					Logger.warn(TAG, "json string is null");
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
				NetworkManager.getInstance().unRegister(httpPost);
				if (httpclient != null) {
					httpclient.getConnectionManager().shutdown();
				}
			}
		} else {
			Logger.warn(TAG, "Context is null");
		}

		return isFlagged;
	}

}

