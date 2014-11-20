package com.paradigmcreatives.apspeak.feed.tasks.helpers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

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
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.paradigmcreatives.apspeak.app.model.MyFeedBean;
import com.paradigmcreatives.apspeak.app.util.AppPropertiesUtil;
import com.paradigmcreatives.apspeak.app.util.Util;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.app.util.constants.JSONConstants;
import com.paradigmcreatives.apspeak.app.util.constants.ServerConstants;
import com.paradigmcreatives.apspeak.app.util.network.NetworkManager;
import com.paradigmcreatives.apspeak.feed.handlers.MyFeedHandler;
import com.paradigmcreatives.apspeak.feed.parsers.MyFeedParser;
import com.paradigmcreatives.apspeak.logging.Logger;

public class MyFeedHelper {

	private static final String TAG = "MyFeedHelper";

	private Context context;

	private MyFeedHandler handler;

	public MyFeedHelper(final Context context, final MyFeedHandler handler) {
		this.context = context;
		this.handler = handler;
	}

	/**
	 * Fetches my feed details from server
	 * 
	 * @param userId
	 * @param offset
	 * @param count
	 * @return
	 */
	public synchronized ArrayList<MyFeedBean> fetch(final String userId,
			final String offset, final String count) {

		ArrayList<MyFeedBean> myFeedList = null;
		if (context != null && !TextUtils.isEmpty(userId)
				&& !TextUtils.isEmpty(offset) && !TextUtils.isEmpty(count)) {
			HttpGet httpGet = null;
			HttpClient httpclient = null;
			try {
				final HttpParams httpParams = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParams,
						ServerConstants.CONNECTION_TIMEOUT);

				StringBuilder urlBuilder = new StringBuilder(
						ServerConstants.NODE_SERVER_URL
								+ ServerConstants.MYFEED_URL + userId
								+ Constants.URL_SLASH + offset
								+ Constants.URL_SLASH + count);
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
					Logger.info(TAG, "status code is:" + statusCode);
					String result = null;
					JSONObject responseJSON = null;
					switch (statusCode) {
					case HttpStatus.SC_OK:
						result = Util.convertingInputToString(resEntity
								.getContent());

						if (!TextUtils.isEmpty(result)) {

							responseJSON = new JSONObject(result);

							if (responseJSON != null
									&& responseJSON.has(JSONConstants.SUCCESS)
									&& responseJSON
											.getBoolean(JSONConstants.SUCCESS)) {
								if (responseJSON.has(JSONConstants.RESULT)) {
									JSONArray myFeedJSON = responseJSON
											.getJSONArray(JSONConstants.RESULT);
									if (myFeedJSON != null) {
										MyFeedParser parser = new MyFeedParser(
												context);
										myFeedList = parseMyFeedJSONArray(myFeedJSON);
										if (parser != null) {

											if (myFeedList != null
													&& myFeedList.size() != 0) {
												didFetchFeedComplete(myFeedList);
											}else{
												didFetchError();
											}
										}
									} else {
										didFetchError();
										Logger.warn(TAG, "Invalid my feed json");
									}
								}
							} else {
								Logger.warn(TAG, "No my feed json");
							}
						} else {
							Logger.warn(TAG, "Response is null");
						}

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
				NetworkManager.getInstance().unRegister(httpGet);
				if (httpclient != null) {
					httpclient.getConnectionManager().shutdown();
				}
			}

		} else {
			Logger.warn(TAG, "Context or userId or offset or count is null");
		}
		return myFeedList;
	}

	/**
	 * Parses the given feed json object and returns array of MyFeedBean
	 * 
	 * @param myFeedJSONArray
	 * @return myFeedList
	 */
	private ArrayList<MyFeedBean> parseMyFeedJSONArray(JSONArray myFeedJSONArray) {
		ArrayList<MyFeedBean> myFeedList = null;
		if (myFeedJSONArray != null && myFeedJSONArray.length() > 0) {
			myFeedList = new ArrayList<MyFeedBean>();
			MyFeedParser parser = new MyFeedParser(context);
			if (parser != null) {
				for (int i = 0; i < myFeedJSONArray.length(); i++) {
					try {
						MyFeedBean myFeedBean = parser
								.parseMyFeedBeanJSON(myFeedJSONArray
										.getJSONObject(i));
						if (myFeedBean != null) {
							myFeedList.add(myFeedBean);
						}
					} catch (JSONException e) {
						myFeedList = null;
					}
				}
			}
		}
		return myFeedList;
	}

	private void didFetchFeedComplete(ArrayList<MyFeedBean> myFeedBeanList) {
		if (handler != null) {
			handler.didFetchFeedComplete(myFeedBeanList);
		} else {
			Logger.warn(TAG, "Handler is null");
		}
	}

	private void didFetchError(){
		if(handler != null){
			handler.failed(-1, -1, "");
		}
	}
}
