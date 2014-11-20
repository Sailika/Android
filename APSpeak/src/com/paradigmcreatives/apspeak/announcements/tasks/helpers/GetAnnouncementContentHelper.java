package com.paradigmcreatives.apspeak.announcements.tasks.helpers;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.paradigmcreatives.apspeak.app.util.Util;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.app.util.constants.JSONConstants;
import com.paradigmcreatives.apspeak.app.util.constants.ServerConstants;
import com.paradigmcreatives.apspeak.app.util.network.NetworkManager;
import com.paradigmcreatives.apspeak.logging.Logger;

/**
 * Helper class that connects to Whatsay server and fetches Announcement Content for the given AnnouncementId
 * 
 * @author Dileep | neuv
 * 
 */
public class GetAnnouncementContentHelper {

    private String mAnnouncementId;

    public GetAnnouncementContentHelper(String announcementId) {
	super();
	this.mAnnouncementId = announcementId;
    }

    public String execute() {
	String announcementContent = null;

	if (!TextUtils.isEmpty(mAnnouncementId)) {
	    HttpGet httpGet = null;
	    HttpClient httpclient = null;
	    try {
		final HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, ServerConstants.CONNECTION_TIMEOUT);
		StringBuilder urlBuilder = new StringBuilder(ServerConstants.NODE_SERVER_URL
			+ ServerConstants.GET_ANNOUCEMENT_LIST + Constants.URL_SLASH + mAnnouncementId);

		httpGet = new HttpGet(urlBuilder.toString());
		NetworkManager.getInstance().register(httpGet);
		httpclient = new DefaultHttpClient(httpParams);
		HttpResponse response = httpclient.execute(httpGet);

		HttpEntity resEntity = response.getEntity();
		if (resEntity != null) {
		    int statusCode = response.getStatusLine().getStatusCode();
		    String result = Util.convertingInputToString(resEntity.getContent());
		    JSONArray responseJSONArray = null;
		    switch (statusCode) {
		    case HttpStatus.SC_OK:
			if (!TextUtils.isEmpty(result)) {
			    responseJSONArray = new JSONArray(result);
			    if (responseJSONArray != null && responseJSONArray.length() > 0) {
				for (int i = 0; i < responseJSONArray.length(); i++) {
				    JSONObject announcement = responseJSONArray.getJSONObject(i);
				    if (announcement != null && announcement.has(JSONConstants.ID)
					    && announcement.has(JSONConstants.CONTENT)) {
					// Parse announcement content for the given announcement id only
					String id = announcement.getString(JSONConstants.ID);
					if (!TextUtils.isEmpty(id) && id.equals(mAnnouncementId)) {
					    announcementContent = announcement.getString(JSONConstants.CONTENT);
					}
					break;
				    }
				}
			    }
			}
			break;

		    case HttpStatus.SC_INTERNAL_SERVER_ERROR:
			break;

		    default:
			break;
		    }
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
	}
	return announcementContent;
    }
}
