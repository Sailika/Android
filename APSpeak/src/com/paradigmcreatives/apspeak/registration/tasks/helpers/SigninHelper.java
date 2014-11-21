package com.paradigmcreatives.apspeak.registration.tasks.helpers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.Header;
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
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.paradigmcreatives.apspeak.app.database.SettingsDBUtil;
import com.paradigmcreatives.apspeak.app.model.SigninBean;
import com.paradigmcreatives.apspeak.app.util.AppPropertiesUtil;
import com.paradigmcreatives.apspeak.app.util.Util;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.app.util.constants.JSONConstants;
import com.paradigmcreatives.apspeak.app.util.constants.ServerConstants;
import com.paradigmcreatives.apspeak.app.util.network.NetworkManager;
import com.paradigmcreatives.apspeak.logging.Logger;
import com.paradigmcreatives.apspeak.registration.RegistrationUtil;

public class SigninHelper {

	private static final String TAG = "SigninHelper";
	private Context context = null;

	// Attributes for web-service request/response
	private static final String EXISTS = "exists";
	private static final String USERID = "user_id";
	private static final String HANDLE = "handle";

	public SigninHelper(Context context) {
		this.context = context;
	}

	public SigninBean execute() {
		HttpPost httpPost = null;
		HttpClient httpclient = null;
		SigninBean signinBean = null;
		try {
			final HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams,
					ServerConstants.CONNECTION_TIMEOUT);
			httpPost = new HttpPost(ServerConstants.SERVER_URL
					+ ServerConstants.USER_SIGNIN);

			NetworkManager.getInstance().register(httpPost);
			httpclient = new DefaultHttpClient(httpParams);

			// Read Facebook details from SettingsDBUtil
			SettingsDBUtil settingsDBUtil = new SettingsDBUtil(context);
			String facebookId = null;
			String access_token = null;
			if (settingsDBUtil.isKeyPresent(Constants.KEY_FACEBOOK_PROFILE_ID)) {
				facebookId = settingsDBUtil
						.getValue(Constants.KEY_FACEBOOK_PROFILE_ID);
			}
			if (settingsDBUtil
					.isKeyPresent(Constants.KEY_FACEBOOK_PROFILE_ACCESSTOKEN)) {
				access_token = settingsDBUtil
						.getValue(Constants.KEY_FACEBOOK_PROFILE_ACCESSTOKEN);
			}
			if (!TextUtils.isEmpty(facebookId)) {
				JSONObject jObject = new JSONObject();
				jObject.put("provider", "FACEBOOK");
				jObject.put("platform", "ANDROID");
				jObject.put("uid", facebookId);
				jObject.put(JSONConstants.ACCESS_TOKEN, access_token);
				String gcmId = AppPropertiesUtil.getGCMID(context);
				if (!TextUtils.isEmpty(gcmId)) {
					jObject.put("push_id", gcmId);
				}
				String deviceUniqueId = RegistrationUtil
						.getDeviceUniqueId(context);
				if (!TextUtils.isEmpty(deviceUniqueId)) {
					jObject.put("device_id", deviceUniqueId);
				}

				StringEntity stringEntity = new StringEntity(jObject.toString());
				Logger.info(TAG, httpPost.getURI().toString() + "json string:"
						+ jObject.toString());
				// stringEntity.setContentType(ServerConstants.CONTENT_TYPE_JSON);
				httpPost.setHeader(HTTP.CONTENT_TYPE,
						ServerConstants.CONTENT_TYPE_JSON);
				httpPost.setHeader("J290EeGRFyIYRdXES7outLUbZKr",
						"l0FQ5cmpRcADmREyUY4DKwH3CnxejQtpb1cM");
				httpPost.setEntity(stringEntity);
				HttpResponse response = httpclient.execute(httpPost);

				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					int statusCode = response.getStatusLine().getStatusCode();
					// Retrieve session id
					if (Constants.IS_SESSION_ENABLED) {
						Header[] responseHeaders = response.getAllHeaders();
						if (responseHeaders != null) {
							for (Header header : responseHeaders) {
								if (header != null
										&& header.getName().equals(
												Constants.RESPONSE_SET_COOKIE)) {
									// The format of the header value is:
									// sessionId=<session id>;Version=<value>
									// Now parse the session id from the above
									// format
									String headerValue = header.getValue();
									try {
										String sessionId = headerValue
												.substring(headerValue
														.indexOf('=') + 1,
														headerValue
																.indexOf(';'));
										AppPropertiesUtil.setSessionID(context,
												sessionId);
									} catch (IndexOutOfBoundsException e) {
										// handle exception
									}
									break;
								}
							}
						}
					}

					String result = Util.convertingInputToString(resEntity
							.getContent());
					JSONObject responseJSON = new JSONObject(result);
					switch (statusCode) {
					case HttpStatus.SC_OK:
					case HttpStatus.SC_NO_CONTENT:
						// if (responseJSON.getBoolean(EXISTS)) {
						Logger.info(TAG,
								"Current Facebook account linked with UserHandle");
						return parseResponseAndStoreData(responseJSON);
						// } else {
						// Logger.info(TAG,
						// "Current Facebook account not linked with any userHandle");
						// }
						// break;

					case HttpStatus.SC_BAD_REQUEST:
						Logger.warn(TAG, "Error in response:"
								+ response.getStatusLine().getReasonPhrase());
						return parseResponseAndStoreData(responseJSON);
					default:
						Logger.warn(TAG, "Error in response:"
								+ response.getStatusLine().getReasonPhrase());
						break;
					}
				} else {
					Logger.warn(TAG, "result entity is null");
				}
			} else {
				Logger.warn(TAG, "required values are  null");
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
		return signinBean;
	}

	/**
	 * Parses the server response and stores the retrieved values
	 * 
	 * @param response
	 */
	private SigninBean parseResponseAndStoreData(JSONObject response) {
		SigninBean signinBean = new SigninBean();
		if (response != null) {
			try {
				if (response.has(USERID)) {
					String user_id = response.getString(USERID);
					if (!TextUtils.isEmpty(user_id)) {
						AppPropertiesUtil.setUserID(context, user_id);
						signinBean.setUserID(user_id);

						// Ensure profile created status is set to true
						AppPropertiesUtil.setUserProfileComplete(context, true);
						AppPropertiesUtil.setAppLaunchedBeforeToTrue(context);
					}
				}
				/*
				 * Not dealing with handle at the moment if
				 * (response.has(HANDLE)) { String handle =
				 * response.getString(HANDLE); if (!TextUtils.isEmpty(handle)) {
				 * AppPropertiesUtil.setSelectedUniqueHandle(context, handle);
				 * AppPropertiesUtil.setUniqueHandleCreatedStatus(context,
				 * true); signinBean.setHandle(handle); } }
				 */

			} catch (JSONException e) {
				Logger.warn(TAG, "Error while parsing the JSON. " + e);
			} catch (Exception e) {
				Logger.warn(TAG,
						"Unknown error while parsing the sign in response: "
								+ e);
			}

		}
		return signinBean;
	}

}
