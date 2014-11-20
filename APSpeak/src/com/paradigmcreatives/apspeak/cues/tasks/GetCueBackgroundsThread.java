package com.paradigmcreatives.apspeak.cues.tasks;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.paradigmcreatives.apspeak.app.util.AppPropertiesUtil;
import com.paradigmcreatives.apspeak.app.util.Util;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.app.util.constants.JSONConstants;
import com.paradigmcreatives.apspeak.app.util.constants.ServerConstants;
import com.paradigmcreatives.apspeak.app.util.google.image.beans.ImageResultsBean;
import com.paradigmcreatives.apspeak.app.util.network.NetworkManager;
import com.paradigmcreatives.apspeak.cues.CueBackgroundsResultParser;
import com.paradigmcreatives.apspeak.cues.handlers.GetCueBackgroundsHandler;
import com.paradigmcreatives.apspeak.logging.Logger;

public class GetCueBackgroundsThread extends Thread {

	private Context context;
	private GetCueBackgroundsHandler handler;
	private String cueId;
	private String TAG = "GetCueBackgroundsThread";

	public GetCueBackgroundsThread(Context context,
			GetCueBackgroundsHandler handler, String cueId) {
		super();
		this.context = context;
		this.handler = handler;
		this.cueId = cueId;
	}

	@Override
	public void run() {
		ArrayList<ImageResultsBean> cueBackgrounsList = null;
		if (context != null && !TextUtils.isEmpty(cueId)) {
			HttpGet httpGet = null;
			HttpClient httpclient = null;
			try {
				final HttpParams httpParams = new BasicHttpParams();
			    HttpConnectionParams.setConnectionTimeout(httpParams, ServerConstants.CONNECTION_TIMEOUT);
			    StringBuilder urlBuilder = null;
				urlBuilder = new StringBuilder(ServerConstants.NODE_SERVER_URL
						+ ServerConstants.GET_CUE_BACKGROUNDS + cueId
						+ ServerConstants.GET_CUE_BACKGROUNDS_SIMPLIFY);

				 httpGet = new HttpGet(urlBuilder.toString());
				    NetworkManager.getInstance().register(httpGet);

				// Add session id as request header via Cookie name
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
					Logger.info(TAG, "status code is:" + statusCode);
					String result = Util.convertingInputToString(resEntity
							.getContent());
					JSONObject responseJSON = null;
					switch (statusCode) {
					case HttpStatus.SC_OK:
						if (!TextUtils.isEmpty(result)) {
							responseJSON = new JSONObject(result);
							if (responseJSON != null
									&& responseJSON.has(JSONConstants.SUCCESS)
									&& responseJSON
											.getBoolean(JSONConstants.SUCCESS)) {
								if (responseJSON.has(JSONConstants.RESULT)) {
									JSONArray cuesJSONArray = responseJSON
											.getJSONArray(JSONConstants.RESULT);
									if (cuesJSONArray != null
											&& cuesJSONArray.length() > 0) {
										cueBackgrounsList = CueBackgroundsResultParser
												.parse(cuesJSONArray);
									}
								}
							}
						}
						break;

					case HttpStatus.SC_INTERNAL_SERVER_ERROR:
						cueBackgrounsList = null;
						Logger.warn(TAG, "Error in response:"
								+ response.getStatusLine().getReasonPhrase());
						break;

					default:
						cueBackgrounsList = null;
						Logger.warn(TAG, "Error in response:"
								+ response.getStatusLine().getReasonPhrase());
						break;
					}
				} else {
					cueBackgrounsList = null;
					Logger.warn(TAG, "result entity is null");
				}
			} catch (JSONException je) {
				Logger.logStackTrace(je);
				cueBackgrounsList = null;
			} catch (IllegalArgumentException iae) {
				Logger.logStackTrace(iae);
				cueBackgrounsList = null;
			} catch (IllegalStateException e) {
				Logger.logStackTrace(e);
				cueBackgrounsList = null;
			} catch (UnsupportedEncodingException e) {
				Logger.logStackTrace(e);
				cueBackgrounsList = null;
			} catch (IOException e) {
				Logger.logStackTrace(e);
				cueBackgrounsList = null;
			} catch (Exception e) {
				Logger.logStackTrace(e);
				cueBackgrounsList = null;
			} finally {
				NetworkManager.getInstance().unRegister(httpGet);
				if (httpclient != null) {
					httpclient.getConnectionManager().shutdown();
				}
			}
		} else {
			Logger.warn(TAG, "Context or userId is null");
		}
		if (handler != null) {
			if (cueBackgrounsList != null && cueBackgrounsList.size() > 0) {
				handler.didFetchComplete(cueBackgrounsList);
			} else {
				handler.didFail();
			}
		}
		super.run();
	}
}
