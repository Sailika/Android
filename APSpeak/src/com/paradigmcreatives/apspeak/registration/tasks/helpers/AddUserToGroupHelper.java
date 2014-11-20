package com.paradigmcreatives.apspeak.registration.tasks.helpers;

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
 * Helper to add user to selected group id
 * 
 * @author Dileep | neuv
 * 
 */
public class AddUserToGroupHelper {

    private Context mContext;
    private String mGroupId;
    private String mUserId;

    public AddUserToGroupHelper(Context contex, String groupId, String userId) {
	super();
	this.mContext = contex;
	this.mGroupId = groupId;
	this.mUserId = userId;
    }

    public boolean execute() {
	boolean isUserAdddedToGroup = false;
	if (mContext != null && !TextUtils.isEmpty(mGroupId) && !TextUtils.isEmpty(mUserId)) {
	    HttpPost httpPost = null;
	    HttpClient httpclient = null;
	    try {
		final HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, ServerConstants.CONNECTION_TIMEOUT);
		httpPost = new HttpPost(ServerConstants.NODE_SERVER_URL + ServerConstants.ADD_USER_TO_GROUP);
		NetworkManager.getInstance().register(httpPost);

		// Add session id as request header via Cookie name
		if (Constants.IS_SESSION_ENABLED) {
		    httpPost.addHeader(Constants.REQUEST_COOKIE,
			    JSONConstants.SESSIONID + AppPropertiesUtil.getSessionId(mContext));
		}

		httpclient = new DefaultHttpClient(httpParams);

		String jsonString = null;
		JSONObject addJson = new JSONObject();
		addJson.put(JSONConstants.GROUP_ID, mGroupId);
		addJson.put(JSONConstants.USER_ID, mUserId);
		addJson.put(JSONConstants.EXCLUSIVE, true);

		jsonString = addJson.toString();

		if (!TextUtils.isEmpty(jsonString)) {
		    StringEntity stringEntity = new StringEntity(jsonString);
		    stringEntity.setContentType(ServerConstants.CONTENT_TYPE_JSON);

		    httpPost.setEntity(stringEntity);
		    HttpResponse response = httpclient.execute(httpPost);

		    HttpEntity resEntity = response.getEntity();
		    if (resEntity != null) {
			int statusCode = response.getStatusLine().getStatusCode();
			String result = Util.convertingInputToString(resEntity.getContent());
			JSONObject responseJSON = null;
			switch (statusCode) {
			case HttpStatus.SC_OK:
			    if (!TextUtils.isEmpty(result)) {
				responseJSON = new JSONObject(result);
				if (responseJSON.has(JSONConstants.SUCCESS)
					&& responseJSON.getBoolean(JSONConstants.SUCCESS)) {
				    isUserAdddedToGroup = true;
				}
			    }
			    break;

			case HttpStatus.SC_INTERNAL_SERVER_ERROR:
			    break;

			default:
			    break;
			}
		    }
		}
	    } catch (JSONException je) {
		Logger.logStackTrace(je);
	    } catch (IllegalArgumentException iae) {
		Logger.logStackTrace(iae);
	    } catch (IllegalStateException ise) {
		Logger.logStackTrace(ise);
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
	}
	return isUserAdddedToGroup;
    }
}
