package com.paradigmcreatives.apspeak.discovery.tasks.helpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.text.TextUtils;

import com.paradigmcreatives.apspeak.app.model.Friend;
import com.paradigmcreatives.apspeak.app.util.AppPropertiesUtil;
import com.paradigmcreatives.apspeak.app.util.Util;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.app.util.constants.JSONConstants;
import com.paradigmcreatives.apspeak.app.util.constants.ServerConstants;
import com.paradigmcreatives.apspeak.app.util.network.NetworkManager;
import com.paradigmcreatives.apspeak.discovery.fragments.UserNetworkFragment.UserNetwork;
import com.paradigmcreatives.apspeak.discovery.tasks.parsers.UserNetworkParser;
import com.paradigmcreatives.apspeak.logging.Logger;

public class NetworkListHelper {
	private static final String TAG = "NetworkListHelper";
	private Context context = null;
	private UserNetwork network = null;
	private String userID = null;
	private int offSet = 0;
	private int limit = Constants.BATCH_FETCHLIMIT;

	public NetworkListHelper(Context context, UserNetwork network) {
		this.context = context;
		this.network = network;
	}

	public NetworkListHelper(Context context, UserNetwork network, String userID) {
		this(context, network);
		this.userID = userID;
	}

	public NetworkListHelper(Context context, UserNetwork network,
			String userId, int offSet, int limit) {
		this(context, network, userId);
		this.offSet = offSet;
		this.limit = limit;
	}

	public HashMap<UserNetwork, Collection<Friend>> execute() {
		HashMap<UserNetwork, Collection<Friend>> friends = null;

		if (context != null && network != null) {
			if (TextUtils.isEmpty(userID)) {
				userID = AppPropertiesUtil.getUserID(context);
			}

			HttpGet httpGet = null;
			HttpClient httpclient = null;
			try {
				final HttpParams httpParams = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParams,
						ServerConstants.CONNECTION_TIMEOUT);
				StringBuilder urlBuilder = null;

				if (network == UserNetwork.FOLLOWING) {
					urlBuilder = new StringBuilder(
							ServerConstants.NODE_SERVER_URL
									+ ServerConstants.FOLLOWING + userID);
				} else if (network == UserNetwork.FOLLOWERS) {
					urlBuilder = new StringBuilder(
							ServerConstants.NODE_SERVER_URL
									+ ServerConstants.FOLLOWERS + userID);
				} else if (network == UserNetwork.FACEBOOK_FRIENDS) {
					urlBuilder = new StringBuilder(
							ServerConstants.NODE_SERVER_URL
									+ ServerConstants.INVITE_FB_LIST + userID
									+ Constants.URL_SLASH + offSet
									+ Constants.URL_SLASH + limit);
				} else { // Take default as followers
					urlBuilder = new StringBuilder(
							ServerConstants.NODE_SERVER_URL
									+ ServerConstants.FOLLOWERS + userID);
				}
				httpGet = new HttpGet(urlBuilder.toString());
				NetworkManager.getInstance().register(httpGet);

				// Add session id as request header via Cookie name
				if (Constants.IS_SESSION_ENABLED) {
					httpGet.addHeader(
							Constants.REQUEST_COOKIE,
							JSONConstants.SESSIONID
									+ AppPropertiesUtil.getSessionId(context));
				}

				httpclient = new DefaultHttpClient(httpParams);

				HttpResponse response = httpclient.execute(httpGet);

				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					int statusCode = response.getStatusLine().getStatusCode();
					String result = null;
					switch (statusCode) {
					case HttpStatus.SC_OK:
						result = Util.convertingInputToString(resEntity
								.getContent());
						if (limit == Constants.FRIENDS_FETCHLIMIT
								&& (!TextUtils.isEmpty(result))&&network == UserNetwork.FACEBOOK_FRIENDS) {
							saveJSONToPrivateFilesDir(context, result,
									Constants.FRIENDS_LIST_FILENAME);
						}
						friends = new HashMap<UserNetwork, Collection<Friend>>();
						UserNetworkParser parser = new UserNetworkParser(
								result, network);
						friends.put(this.network, parser.parse());
						break;

					case HttpStatus.SC_INTERNAL_SERVER_ERROR:
						Logger.warn(TAG, "Error in response:"
								+ response.getStatusLine().getReasonPhrase());
						break;

					default:
						Logger.warn(TAG, "Error in response:"
								+ response.getStatusLine().getReasonPhrase());
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
		}

		return friends;
	}

	private boolean saveJSONToPrivateFilesDir(Context context,
			String jsonArray, String destFileName) {
		boolean isSaved = false;
		OutputStreamWriter writer = null;
		if (context != null && !TextUtils.isEmpty(jsonArray)
				&& !TextUtils.isEmpty(destFileName)) {
			try {
				JSONArray resultJSON = new JSONArray(jsonArray);
				if (resultJSON != null) {
					String appPrivateFilesDirectory = context.getFilesDir()
							.getAbsolutePath();
					File filesDir = new File(appPrivateFilesDirectory);
					if (!filesDir.exists()) {
						filesDir.mkdirs();
					}

					File destFile = new File(filesDir, destFileName);

					writer = new OutputStreamWriter(new FileOutputStream(
							destFile));
					writer.write(resultJSON.toString());
					writer.close();
					isSaved = true;
				}
			} catch (JSONException jse) {
				Logger.logStackTrace(jse);
			} catch (FileNotFoundException fnfe) {
				Logger.logStackTrace(fnfe);
			} catch (Exception e) {
				Logger.logStackTrace(e);
			} finally {
				if (writer != null) {
					try {
						writer.close();
					} catch (IOException ioe) {
						Logger.logStackTrace(ioe);
					}
				}
			}
		}
		return isSaved;
	}
}
