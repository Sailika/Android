package com.paradigmcreatives.apspeak.discovery.tasks.helpers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

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

import com.paradigmcreatives.apspeak.app.util.AppPropertiesUtil;
import com.paradigmcreatives.apspeak.app.util.Util;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.app.util.constants.JSONConstants;
import com.paradigmcreatives.apspeak.app.util.constants.ServerConstants;
import com.paradigmcreatives.apspeak.app.util.network.NetworkManager;
import com.paradigmcreatives.apspeak.logging.Logger;

/**
 * Convenience class for following/unfollowing friends
 * 
 * @author robin
 * 
 */
public class FollowFriendsHelper {

    private static final String TAG = "FollowFriendsHelper";
    private Context context;
    private List<String> ids;
    private String userID = null;

    public enum Type {
	FOLLOW, UNFOLLOW, INVITE
    };

    private Type type = Type.FOLLOW;

    /**
     * @param context
     * @param ids
     */
    public FollowFriendsHelper(Context context, List<String> ids, Type type) {
	this.context = context;
	this.ids = ids;
	this.type = type;
    }

    public FollowFriendsHelper(Context context, String userID, List<String> ids, Type type) {
	this.context = context;
	this.userID = userID;
	this.ids = ids;
	this.type = type;
    }

    public boolean execute() {
	boolean success = false;
	if (context != null) {
	    JSONObject requestJson = getRequestJson(ids);
	    if (requestJson != null && !TextUtils.isEmpty(requestJson.toString())) {
		HttpPost httpPost = null;
		HttpClient httpclient = null;
		try {
		    final HttpParams httpParams = new BasicHttpParams();
		    HttpConnectionParams.setConnectionTimeout(httpParams, ServerConstants.CONNECTION_TIMEOUT);
		    if (type != null && type == Type.UNFOLLOW) {
			httpPost = new HttpPost(ServerConstants.NODE_SERVER_URL + ServerConstants.UNFOLLOW_USER);
		    } else if (type != null && type == Type.INVITE) {
			httpPost = new HttpPost(ServerConstants.NODE_SERVER_URL + ServerConstants.INVITE_USER);
		    } else {
			httpPost = new HttpPost(ServerConstants.NODE_SERVER_URL + ServerConstants.FOLLOW_USER);
		    }

		    NetworkManager.getInstance().register(httpPost);

		    // Add session id as request header via Cookie name
		    if (Constants.IS_SESSION_ENABLED) {
			httpPost.addHeader(Constants.REQUEST_COOKIE,
				JSONConstants.SESSIONID + AppPropertiesUtil.getSessionId(context));
		    }

		    httpclient = new DefaultHttpClient(httpParams);

		    StringEntity stringEntity = new StringEntity(requestJson.toString());
		    Logger.info(TAG, "json string:" + requestJson.toString());
		    stringEntity.setContentType(ServerConstants.CONTENT_TYPE_JSON);

		    httpPost.setEntity(stringEntity);
		    HttpResponse response = httpclient.execute(httpPost);

		    HttpEntity resEntity = response.getEntity();
		    if (resEntity != null) {
			int statusCode = response.getStatusLine().getStatusCode();
			Logger.info(TAG, "status code is:" + statusCode);
			String resultString = Util.convertingInputToString(resEntity.getContent());
			JSONObject responseJSON = null;

			switch (statusCode) {
			case HttpStatus.SC_OK:
			    if (type == Type.INVITE) {
				success = true;
			    } else {
				responseJSON = new JSONObject(resultString);
				if (responseJSON.has(JSONConstants.SUCCESS)) {
				    if (responseJSON.getBoolean(JSONConstants.SUCCESS)) {
					success = true;
				    }
				}
			    }
			    break;

			case HttpStatus.SC_INTERNAL_SERVER_ERROR:
			    Logger.warn(TAG, "Server not responding");
			    break;

			default:
			    Logger.warn(TAG, "Error in response: " + response.getStatusLine().getReasonPhrase());
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
		    NetworkManager.getInstance().unRegister(httpPost);
		    if (httpclient != null) {
			httpclient.getConnectionManager().shutdown();
		    }
		}
	    } else {
		Logger.warn(TAG, "Request json is null");
	    }

	} else {
	    Logger.warn(TAG, "context is null");
	}

	return success;
    }

    private JSONObject getRequestJson(List<String> userIds) {
	JSONObject json = null;
	if (context != null && userIds != null && userIds.size() > 0) {
	    try {
		JSONArray userIdsList = getFriendsArray(userIds);
		json = new JSONObject();
		if (TextUtils.isEmpty(userID)) {
		    userID = AppPropertiesUtil.getUserID(context);
		}
		json.put(JSONConstants.USER_ID, userID);

		// Remove self ID if its there
		userIds.remove(userID);

		if (type != null && type == Type.UNFOLLOW) {
		    json.put(JSONConstants.UNFOLLOW, userIdsList);
		} else if (type != null && type == Type.INVITE) {
		    json.put(JSONConstants.USER_IDS, userIdsList);
		} else {
		    json.put(JSONConstants.FOLLOW, userIdsList);
		}
	    } catch (JSONException je) {
		Logger.warn(TAG, je.getLocalizedMessage());
		je.printStackTrace();
	    } catch (Exception e) {
		Logger.warn(TAG, e.getLocalizedMessage());
		e.printStackTrace();
	    }
	} else {
	    Logger.warn(TAG, "Context is null or Empty userIds");
	}
	return json;
    }

    /**
     * Returns a json array of friends to be followed/invited.
     * 
     * @param userIds
     * @return
     */
    private JSONArray getFriendsArray(List<String> userIds) {
	JSONArray array = new JSONArray();
	for (String string : userIds) {
	    array.put(string);
	}
	return array;
    }

}
