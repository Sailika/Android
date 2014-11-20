package com.paradigmcreatives.apspeak.assets.tasks;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;

import com.paradigmcreatives.apspeak.app.util.AppPropertiesUtil;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.app.util.constants.JSONConstants;
import com.paradigmcreatives.apspeak.app.util.constants.ServerConstants;
import com.paradigmcreatives.apspeak.app.util.network.NetworkManager;
import com.paradigmcreatives.apspeak.assets.handlers.InappropriateFlagHandler;
import com.paradigmcreatives.apspeak.logging.Logger;

public class AssetInappropriateThread extends Thread {
	private static final String TAG = "AssetDeleteThread";

	private Context context;
	private String assetId;
	private String userId;
	private InappropriateFlagHandler handler;

	public AssetInappropriateThread(Context context, String assetId,
			String userId,InappropriateFlagHandler handler) {
		super();
		this.context = context;
		this.assetId = assetId;
		this.userId = userId;
		this.handler=handler;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		willStartTask();
		if (context != null && assetId != null && userId != null) {
			HttpGet httpGet = null;
			HttpClient httpclient = null;
			try {
				final HttpParams httpParams = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParams,
						ServerConstants.CONNECTION_TIMEOUT);
				httpGet = new HttpGet(ServerConstants.NODE_SERVER_URL
						+ ServerConstants.USER_STREAM_FETCH_PREFIX + userId
						+ ServerConstants.FLAG_ASSET_URL + assetId+ServerConstants.INAPPROPRIATE_URL);
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
					Logger.info(TAG, "status code is:" + statusCode);
					switch (statusCode) {
					case HttpStatus.SC_OK:
						success();
						break;
					case HttpStatus.SC_INTERNAL_SERVER_ERROR:
						Logger.warn(TAG, "Error in response:"
								+ response.getStatusLine().getReasonPhrase());
						failed(-1,-1);
						break;

					default:
						Logger.warn(TAG, "Error in response:"
								+ response.getStatusLine().getReasonPhrase());
						break;
					}
				} else {
					Logger.warn(TAG, "result entity is null");
				}
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
			Logger.warn(TAG, "Context is null");
		}

	}
	private void willStartTask() {
		if (handler != null) {
			handler.willStartTask();
		} else {
			Logger.warn(TAG, "Handler is null");
		}
	}

	private void failed(int statusCode, int errorCode) {
		if (handler != null) {
			handler.failed(statusCode, errorCode);
		} else {
			Logger.warn(TAG, "Handler is null");
		}
	}

	private void success() {
		if (handler != null) {
			handler.success();
		} else {
			Logger.warn(TAG, "Handler is null");
		}
	}

}
