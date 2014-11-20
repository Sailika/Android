package com.paradigmcreatives.apspeak.discovery.tasks.helpers;

import java.io.IOException;
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

/**
 * Convenience class to make the find friends call to the server
 * 
 * @author robin
 * 
 */
public class FindFriendsHelper {

    private static final String TAG = "FindFriendsHelper";
    private Context context = null;
    private String userID = null;

    /**
     * Find friends for the user ID signed into the device
     * 
     * @param context
     */
    public FindFriendsHelper(Context context) {
	this.context = context;
    }

    /**
     * Find the friends for the given user ID
     * 
     * @param context
     * @param userID
     */
    public FindFriendsHelper(Context context, String userID) {
	this.context = context;
	this.userID = userID;
    }

    public HashMap<UserNetwork, Collection<Friend>> execute() {
	HashMap<UserNetwork, Collection<Friend>> friends = null;

	if (context != null) {
	    if (TextUtils.isEmpty(userID)) {
		userID = AppPropertiesUtil.getUserID(context);
	    }

	    HttpGet httpGet = null;
	    HttpClient httpclient = null;
	    try {
		final HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, ServerConstants.CONNECTION_TIMEOUT);
		StringBuilder urlBuilder = new StringBuilder(ServerConstants.NODE_SERVER_URL
			+ ServerConstants.USER_PROFILE_NETWORK_FETCH + userID);
		httpGet = new HttpGet(urlBuilder.toString());
		NetworkManager.getInstance().register(httpGet);

		// Add session id as request header via Cookie name
		if (Constants.IS_SESSION_ENABLED) {
		    httpGet.addHeader(Constants.REQUEST_COOKIE, JSONConstants.SESSIONID + AppPropertiesUtil.getSessionId(context));
		}

		httpclient = new DefaultHttpClient(httpParams);

		HttpResponse response = httpclient.execute(httpGet);

		HttpEntity resEntity = response.getEntity();
		if (resEntity != null) {
		    int statusCode = response.getStatusLine().getStatusCode();
		    Logger.info(TAG, "status code is:" + statusCode);
		    String result = null;
		    switch (statusCode) {
		    case HttpStatus.SC_OK:
			result = Util.convertingInputToString(resEntity.getContent());
			if (!TextUtils.isEmpty(result)) {
			    friends = new HashMap<UserNetwork, Collection<Friend>>();
			    
			    UserNetworkParser parser = new UserNetworkParser(result, UserNetwork.FRIENDS);
			    friends.put(UserNetwork.FRIENDS, parser.parse());
			    parser.setUserNetwork(UserNetwork.SUGGESTED_FRIENDS);
			    friends.put(UserNetwork.SUGGESTED_FRIENDS, parser.parse());
			} else {
			    Logger.warn(TAG, "Response is null");
			}

			break;

		    case HttpStatus.SC_INTERNAL_SERVER_ERROR:
			Logger.warn(TAG, "Error in response:" + response.getStatusLine().getReasonPhrase());
			break;

		    default:
			Logger.warn(TAG, "Error in response:" + response.getStatusLine().getReasonPhrase());
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

	} else {
	    Logger.warn(TAG, "Empty context supplied while trying to fetch the friends");
	}

	return friends;
    }

}
