package com.paradigmcreatives.apspeak.cues.tasks.helpers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.paradigmcreatives.apspeak.app.model.Campaigns;
import com.paradigmcreatives.apspeak.app.util.AppPropertiesUtil;
import com.paradigmcreatives.apspeak.app.util.Util;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.app.util.constants.JSONConstants;
import com.paradigmcreatives.apspeak.app.util.constants.ServerConstants;
import com.paradigmcreatives.apspeak.app.util.network.NetworkManager;
import com.paradigmcreatives.apspeak.logging.Logger;

/**
 * Helper class to fetch Cues from Whatsay Server
 * 
 * @author Dileep | neuv
 * 
 */
public class GetCuesHelper {

    private Context context;
    private String userId;
    private int currentHour;

    private String TAG = "GetCuesHelper";

    public GetCuesHelper(Context context, String userId, int currentHour) {
	this.context = context;
	this.userId = userId;
	this.currentHour = currentHour;
    }

    public ArrayList<Campaigns> execute() {
	ArrayList<Campaigns> cuesList = null;
	if (context != null && !TextUtils.isEmpty(userId) && currentHour >= 0 && currentHour <= 23) {
	    HttpPost httpPost = null;
	    HttpClient httpclient = null;
	    try {
		final HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, ServerConstants.CONNECTION_TIMEOUT);
		StringBuilder urlBuilder = null;
		urlBuilder = new StringBuilder(ServerConstants.NODE_SERVER_URL + ServerConstants.GET_CUE_LIST);

		httpPost = new HttpPost(urlBuilder.toString());
		NetworkManager.getInstance().register(httpPost);

		// Add session id as request header via Cookie name
		if (Constants.IS_SESSION_ENABLED) {
		    httpPost.addHeader(Constants.REQUEST_COOKIE,
			    JSONConstants.SESSIONID + AppPropertiesUtil.getSessionId(context));
		}

		httpclient = new DefaultHttpClient(httpParams);

		JSONObject jObject = new JSONObject();
		jObject.put(JSONConstants.USER_ID, userId);
		jObject.put(JSONConstants.TIME, currentHour);

		StringEntity stringEntity = new StringEntity(jObject.toString());
		Logger.info(TAG, "json string:" + jObject.toString());
		stringEntity.setContentType(ServerConstants.CONTENT_TYPE_JSON);

		httpPost.setEntity(stringEntity);
		HttpResponse response = httpclient.execute(httpPost);

		HttpEntity resEntity = response.getEntity();
		if (resEntity != null) {
		    int statusCode = response.getStatusLine().getStatusCode();
		    Logger.info(TAG, "status code is:" + statusCode);
		    String result = Util.convertingInputToString(resEntity.getContent());
		    JSONObject responseJSON = null;
		    switch (statusCode) {
		    case HttpStatus.SC_OK:
			if (!TextUtils.isEmpty(result)) {
			    responseJSON = new JSONObject(result);
			    if (responseJSON != null && responseJSON.has(JSONConstants.SUCCESS)
				    && responseJSON.getBoolean(JSONConstants.SUCCESS)) {
				if (responseJSON.has(JSONConstants.RESULT)) {
				    JSONArray cuesJSONArray = responseJSON.getJSONArray(JSONConstants.RESULT);
				    if (cuesJSONArray != null && cuesJSONArray.length() > 0) {
					cuesList = parseCuesJSONArray(cuesJSONArray);
				    }
				}
			    }
			}
			break;

		    case HttpStatus.SC_INTERNAL_SERVER_ERROR:
			cuesList = null;
			Logger.warn(TAG, "Error in response:" + response.getStatusLine().getReasonPhrase());
			break;

		    default:
			cuesList = null;
			Logger.warn(TAG, "Error in response:" + response.getStatusLine().getReasonPhrase());
			break;
		    }
		} else {
		    cuesList = null;
		    Logger.warn(TAG, "result entity is null");
		}
	    } catch (JSONException je) {
		Logger.logStackTrace(je);
		cuesList = null;
	    } catch (IllegalArgumentException iae) {
		Logger.logStackTrace(iae);
		cuesList = null;
	    } catch (IllegalStateException e) {
		Logger.logStackTrace(e);
		cuesList = null;
	    } catch (UnsupportedEncodingException e) {
		Logger.logStackTrace(e);
		cuesList = null;
	    } catch (IOException e) {
		Logger.logStackTrace(e);
		cuesList = null;
	    } catch (Exception e) {
		Logger.logStackTrace(e);
		cuesList = null;
	    } finally {
		NetworkManager.getInstance().unRegister(httpPost);
		if (httpclient != null) {
		    httpclient.getConnectionManager().shutdown();
		}
	    }
	} else {
	    Logger.warn(TAG, "Context or userId is null");
	}

	return cuesList;
    }

    /**
     * Parses cues json array and returns list of cues
     * 
     * @param cuesJsonArray
     * @return
     */
    public ArrayList<Campaigns> parseCuesJSONArray(JSONArray cuesJsonArray) {
	ArrayList<Campaigns> cuesList = null;
	if (cuesJsonArray != null && cuesJsonArray.length() > 0) {
	    cuesList = new ArrayList<Campaigns>();
	    for (int i = 0; i < cuesJsonArray.length(); i++) {
		try {
		    Campaigns cue = parseCueJSON(cuesJsonArray.getJSONObject(i));
		    if (cue != null) {
			cuesList.add(cue);
		    }
		} catch (JSONException e) {

		}

	    }
	}
	return cuesList;
    }

    /**
     * Parses cue json and returns an instance of CueBean
     * 
     * @param cueJSON
     * @return
     */
    public Campaigns parseCueJSON(JSONObject cueJSON) {
	Campaigns cue = null;
	if (cueJSON != null) {
	    try {
		cue = new Campaigns();
		if (cueJSON.has(JSONConstants.ID) && !cueJSON.isNull(JSONConstants.ID)) {
		    cue.setCueId(cueJSON.getString(JSONConstants.ID));
		}
		if (cueJSON.has(JSONConstants.TEXT) && !cueJSON.isNull(JSONConstants.TEXT)) {
		    cue.setCueMessage(cueJSON.getString(JSONConstants.TEXT));
		}
		if (cueJSON.has(JSONConstants.BACKGROUND_URL) && !cueJSON.isNull(JSONConstants.BACKGROUND_URL)) {
		    cue.setBackgroundURL(cueJSON.getString(JSONConstants.BACKGROUND_URL));
		}
		if (cueJSON.has(JSONConstants.BACKGROUND_URL_WIDE) && !cueJSON.isNull(JSONConstants.BACKGROUND_URL_WIDE)) {
		    cue.setBackgroundURLWide(cueJSON.getString(JSONConstants.BACKGROUND_URL_WIDE));
		}
		if (cueJSON.has(JSONConstants.BGCOLOR) && !cueJSON.isNull(JSONConstants.BGCOLOR)) {
		    cue.setBackgroundColor(cueJSON.getString(JSONConstants.BGCOLOR));
		}
		if (cueJSON.has(JSONConstants.FCOLOR) && !cueJSON.isNull(JSONConstants.FCOLOR)) {
		    cue.setForegroundColor(cueJSON.getString(JSONConstants.FCOLOR));
		}
		if (cueJSON.has(JSONConstants.ICON_URL) && !cueJSON.isNull(JSONConstants.ICON_URL)) {
		    cue.setIconURL(cueJSON.getString(JSONConstants.ICON_URL));
		}
		if (cueJSON.has(JSONConstants.WIDTH) && !cueJSON.isNull(JSONConstants.WIDTH)) {
		    cue.setWidth(cueJSON.getInt(JSONConstants.WIDTH));
		} else {
		    cue.setWidth(1);
		}
	    } catch (JSONException e) {

	    }
	}
	return cue;
    }
}
