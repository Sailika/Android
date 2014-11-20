package com.paradigmcreatives.apspeak.user.tasks;

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
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.paradigmcreatives.apspeak.app.model.Friend;
import com.paradigmcreatives.apspeak.app.model.Friend.FRIEND_TYPE;
import com.paradigmcreatives.apspeak.app.util.AppPropertiesUtil;
import com.paradigmcreatives.apspeak.app.util.Util;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.app.util.constants.JSONConstants;
import com.paradigmcreatives.apspeak.app.util.constants.ServerConstants;
import com.paradigmcreatives.apspeak.app.util.network.NetworkManager;
import com.paradigmcreatives.apspeak.logging.Logger;
import com.paradigmcreatives.apspeak.user.handlers.GetFriendCompactProfileHandler;

/**
 * Fetches compact profile details of a Friend
 * 
 * @author Dileep | neuv
 * 
 */
public class GetFriendCompactProfileThread extends Thread {
    private static final String TAG = "GetFriendCompactProfileThread";

    private Context context;
    private String userId;
    private String assetId;
    private String message;
    private GetFriendCompactProfileHandler handler;
    private Friend friend;

    public GetFriendCompactProfileThread(Context context, String userId, String notificationType) {
	super();
	this.context = context;
	this.userId = userId;
	this.handler = new GetFriendCompactProfileHandler(context, notificationType);
    }

    public GetFriendCompactProfileThread(Context context, String userId, String assetId, String notificationType) {
	super();
	this.context = context;
	this.userId = userId;
	this.handler = new GetFriendCompactProfileHandler(context, assetId, notificationType);
    }

    public GetFriendCompactProfileThread(Context context, String userId, String assetId, String notificationType,
	    String message) {
	super();
	this.context = context;
	this.userId = userId;
	this.message = message;
	this.handler = new GetFriendCompactProfileHandler(context, assetId, notificationType, message);
    }

    public GetFriendCompactProfileThread(Context context, String userId, Fragment fragment) {
	super();
	this.context = context;
	this.userId = userId;
	this.handler = new GetFriendCompactProfileHandler(context, fragment);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Thread#run()
     */
    @Override
    public void run() {
	if (context != null) {
	    if (!TextUtils.isEmpty(userId)) {
		willStartTask();
		HttpGet httpGet = null;
		HttpClient httpclient = null;
		try {
		    final HttpParams httpParams = new BasicHttpParams();
		    HttpConnectionParams.setConnectionTimeout(httpParams, ServerConstants.CONNECTION_TIMEOUT);
		    StringBuilder urlBuilder = new StringBuilder(ServerConstants.NODE_SERVER_URL
			    + ServerConstants.USER_PROFILE_NETWORK_FETCH + userId
			    + ServerConstants.USER_COMPACTPROFILE_FETCH);
		    httpGet = new HttpGet(urlBuilder.toString());
		    NetworkManager.getInstance().register(httpGet);

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
			String result = null;
			JSONObject responseJSON = null;
			switch (statusCode) {
			case HttpStatus.SC_OK:
			    result = Util.convertingInputToString(resEntity.getContent());
			    if (!TextUtils.isEmpty(result)) {
				responseJSON = new JSONObject(result);
				if (responseJSON.has(JSONConstants.SUCCESS)
					&& TextUtils.equals(responseJSON.getString(JSONConstants.SUCCESS),
						JSONConstants.TRUE)) {
				    if (responseJSON.has(JSONConstants.RESULT)) {
					friend = parseFriendJSON(responseJSON.getJSONObject(JSONConstants.RESULT),
						FRIEND_TYPE.CONTACT, true);
					if (friend != null) {
					    didFetchComplete();
					}
				    }
				}
			    } else {
				Logger.warn(TAG, "Response is null");
			    }

			    break;

			case HttpStatus.SC_INTERNAL_SERVER_ERROR:
			    failed(statusCode, -1, response.getStatusLine().getReasonPhrase());
			    Logger.warn(TAG, "Error in response:" + response.getStatusLine().getReasonPhrase());
			    break;

			default:
			    failed(statusCode, -1, response.getStatusLine().getReasonPhrase());
			    Logger.warn(TAG, "Error in response:" + response.getStatusLine().getReasonPhrase());
			    break;
			}
		    } else {
			Logger.warn(TAG, "result entity is null");
		    }
		} catch (JSONException je) {
		    Logger.logStackTrace(je);
		    failed(-1, -1, null);
		} catch (IllegalArgumentException iae) {
		    Logger.logStackTrace(iae);
		    failed(-1, -1, null);
		} catch (IllegalStateException e) {
		    Logger.logStackTrace(e);
		    failed(-1, -1, null);
		} catch (UnsupportedEncodingException e) {
		    Logger.logStackTrace(e);
		    failed(-1, -1, null);
		} catch (IOException e) {
		    Logger.logStackTrace(e);
		    failed(-1, -1, null);
		} catch (Exception e) {
		    Logger.logStackTrace(e);
		    failed(-1, -1, null);
		} finally {
		    NetworkManager.getInstance().unRegister(httpGet);
		    if (httpclient != null) {
			httpclient.getConnectionManager().shutdown();
		    }
		}
	    } else {
		Logger.warn(TAG, "UserId is null");
	    }
	} else {
	    Logger.warn(TAG, "Context is null");
	}

	super.run();
    }

    private void willStartTask() {
	if (handler != null) {
	    handler.willStartTask();
	} else {
	    Logger.warn(TAG, "Handler is null");
	}
    }

    private void failed(int statusCode, int errorCode, String reasonPhrase) {
	if (handler != null) {
	    handler.failed(statusCode, errorCode, reasonPhrase);
	} else {
	    Logger.warn(TAG, "Handler is null");
	}
    }

    /**
     * Performs tasks after fetching Friend's compact profile
     * 
     * @param friend
     */
    private void didFetchComplete() {
	if (handler != null) {
	    handler.didFetchComplete(friend);
	} else {
	    Logger.warn(TAG, "Handler is null");
	}
    }

    /**
     * Parses the given friend json object and returns respective Friend object
     * 
     * @param friendJSON
     * @param type
     *            Friend type, such as, CONTACT, FACEBOOK, TWITTER, EMAIL
     * @param followingStatus
     * @return Friend
     */
    private Friend parseFriendJSON(JSONObject friendJSON, FRIEND_TYPE type, boolean followingStatus) {
	Friend friend = null;
	if (friendJSON != null) {
	    try {
		friend = new Friend();
		if (friendJSON.has(JSONConstants.USER_ID)) {
		    friend.setUserId(friendJSON.getString(JSONConstants.USER_ID));
		}
		if (friendJSON.has(JSONConstants.NAME)) {
		    friend.setName(friendJSON.getString(JSONConstants.NAME));
		}
		if (friendJSON.has(JSONConstants.HANDLE)) {
		    friend.setUniqueHandle(friendJSON.getString(JSONConstants.HANDLE));
		}
		if (friendJSON.has(JSONConstants.PROFILE_PICTURE)) {
		    friend.setProfilePicURL(friendJSON.getString(JSONConstants.PROFILE_PICTURE));
		}
		if (friendJSON.has(JSONConstants.COVER_IMAGE)) {
		    friend.setCoverImageURL(friendJSON.getString(JSONConstants.COVER_IMAGE));
		}
		friend.setFollowStatus(followingStatus);
		friend.setFriendType(type);
	    } catch (JSONException e) {
		friend = null;
	    } catch (Exception e) {
		friend = null;
	    }
	}
	return friend;
    }

}
