package com.paradigmcreatives.apspeak.stream.tasks.helpers;

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

/**
 * Helper class to make server request for to remove asset relationship
 * 
 * @author Dileep | neuv
 * 
 */
public class RemoveAssetRelationshipHelper {

	private static final String TAG = "RemoveAssetRelationshipHelper";
	private Context context;

	/**
	 * @param context
	 */
	public RemoveAssetRelationshipHelper(Context context) {
		this.context = context;
	}

	public boolean execute(JSONObject requestJSON) {
		boolean isRemoved = false;
		if (context != null && requestJSON != null) {
			HttpPost httpPost = null;
			HttpClient httpclient = null;
			try {
				final HttpParams httpParams = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParams,
						ServerConstants.CONNECTION_TIMEOUT);
				httpPost = new HttpPost(ServerConstants.NODE_SERVER_URL
						+ ServerConstants.DELETE_ASSET_RELATIONSHIP);
				NetworkManager.getInstance().register(httpPost);

				// Add session id as request header via Cookie name
				if (Constants.IS_SESSION_ENABLED) {
				    httpPost.addHeader(Constants.REQUEST_COOKIE, JSONConstants.SESSIONID + AppPropertiesUtil.getSessionId(context));
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
						String result = null;
						JSONObject responseJSON = null;
						switch (statusCode) {
						case HttpStatus.SC_OK:
						case HttpStatus.SC_CREATED:
							result = Util.convertingInputToString(resEntity
									.getContent());
							if (!TextUtils.isEmpty(result)) {
								responseJSON = new JSONObject(result);
								if (responseJSON.has(JSONConstants.SUCCESS)
										&& responseJSON
												.getBoolean(JSONConstants.SUCCESS)) {
									if (responseJSON.has(JSONConstants.RESULT)
											&& responseJSON
													.getJSONObject(JSONConstants.RESULT) != null) {
										isRemoved = true;
									}
								}
							} else {
								Logger.warn(TAG, "Response is null");
							}

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
		} else {
			Logger.warn(TAG, "Context is null");
		}

		return isRemoved;
	}

}

