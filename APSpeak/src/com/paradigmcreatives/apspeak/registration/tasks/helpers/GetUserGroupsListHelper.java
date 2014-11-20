package com.paradigmcreatives.apspeak.registration.tasks.helpers;

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

import com.paradigmcreatives.apspeak.app.model.GroupBean;
import com.paradigmcreatives.apspeak.app.util.Util;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.app.util.constants.JSONConstants;
import com.paradigmcreatives.apspeak.app.util.constants.ServerConstants;
import com.paradigmcreatives.apspeak.app.util.network.NetworkManager;
import com.paradigmcreatives.apspeak.logging.Logger;
import com.paradigmcreatives.apspeak.registration.tasks.parsers.GroupBeanParser;

/**
 * Helper class that connects to Whatsay server and fetches current user's Groups List
 * 
 * @author Dileep | neuv
 * 
 */
public class GetUserGroupsListHelper {

    public Context mContext;
    public String mUserId;

    public GetUserGroupsListHelper(final Context context, String userId) {
	super();
	this.mContext = context;
	this.mUserId = userId;
    }

    public ArrayList<GroupBean> execute() {
	ArrayList<GroupBean> groupsList = null;
	if (mContext != null && !TextUtils.isEmpty(mUserId)) {
	    HttpGet httpGet = null;
	    HttpClient httpclient = null;
	    try {
		final HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, ServerConstants.CONNECTION_TIMEOUT);
		StringBuilder urlBuilder = new StringBuilder(ServerConstants.NODE_SERVER_URL
			+ ServerConstants.GET_USER_GROUPS_LIST + Constants.URL_SLASH + mUserId + ServerConstants.LIST);

		httpGet = new HttpGet(urlBuilder.toString());
		NetworkManager.getInstance().register(httpGet);
		httpclient = new DefaultHttpClient(httpParams);
		HttpResponse response = httpclient.execute(httpGet);

		HttpEntity resEntity = response.getEntity();
		if (resEntity != null) {
		    int statusCode = response.getStatusLine().getStatusCode();
		    String result = Util.convertingInputToString(resEntity.getContent());
		    JSONObject responseJSON = null;
		    switch (statusCode) {
		    case HttpStatus.SC_OK:
			if (!TextUtils.isEmpty(result)) {
			    responseJSON = new JSONObject(result);
			    if (responseJSON != null && responseJSON.has(JSONConstants.SUCCESS)
				    && responseJSON.getBoolean(JSONConstants.SUCCESS)) {
				if (responseJSON.has(JSONConstants.RESULT)) {
				    JSONArray groupsJSONArray = responseJSON.getJSONArray(JSONConstants.RESULT);
				    if (groupsJSONArray != null && groupsJSONArray.length() > 0) {
					GroupBeanParser parser = new GroupBeanParser();
					if (parser != null) {
					    groupsList = parser.parseGroupBeanJSONArray(groupsJSONArray);
					}
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
	return groupsList;
    }
}
