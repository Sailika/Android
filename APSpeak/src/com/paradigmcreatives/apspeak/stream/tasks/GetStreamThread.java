package com.paradigmcreatives.apspeak.stream.tasks;

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

import com.paradigmcreatives.apspeak.app.model.StreamAsset;
import com.paradigmcreatives.apspeak.app.util.AppPropertiesUtil;
import com.paradigmcreatives.apspeak.app.util.Util;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.app.util.constants.JSONConstants;
import com.paradigmcreatives.apspeak.app.util.constants.ServerConstants;
import com.paradigmcreatives.apspeak.app.util.network.NetworkManager;
import com.paradigmcreatives.apspeak.logging.Logger;
import com.paradigmcreatives.apspeak.stream.handlers.GetStreamHandler;
import com.paradigmcreatives.apspeak.stream.parsers.StreamAssetParser;

/**
 * Fetches user's stream that needs to be displayed by default on the AppHomeActivity
 * 
 * @author Dileep | neuv
 * 
 */
public class GetStreamThread extends Thread {
    private static final String TAG = "GetUserStreamThread";

    private Context context;
    private String id;
    private GetStreamHandler handler;
    //private boolean fetchPersonalStream;
    private STREAM_TYPE streamType;
    private int startIndex;
    private int limit;
    
    public enum STREAM_TYPE{
	MAIN_STREAM, PERSONAL_STREAM, COMMENTS_STREAM, COLLEGE, ALLCOLLEGES, FRIENDS, FEATURED_STREAM
    };

    public GetStreamThread(Context context, String id, GetStreamHandler handler, STREAM_TYPE type/*boolean fetchPersonalStream*/) {
	super();
	this.context = context;
	this.id = id;
	this.handler = handler;
	//this.fetchPersonalStream = fetchPersonalStream;
	this.streamType = type;
	this.startIndex = 0;
	this.limit = 25;
    }

    public GetStreamThread(Context context, String id, GetStreamHandler handler,
	    STREAM_TYPE type/*boolean fetchPersonalStream*/, int startIndex, int limit) {
	super();
	this.context = context;
	this.id = id;
	this.handler = handler;
	//this.fetchPersonalStream = fetchPersonalStream;
	this.streamType = type;
	this.startIndex = startIndex;
	this.limit = limit;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Thread#run()
     */
    @Override
    public void run() {
	if (context != null) {
	    if (!TextUtils.isEmpty(id) && streamType != null) {
		willStartTask();
		HttpGet httpGet = null;
		HttpClient httpclient = null;
		try {
			final HttpParams httpParams = new BasicHttpParams();
		    HttpConnectionParams.setConnectionTimeout(httpParams, ServerConstants.CONNECTION_TIMEOUT);
		    StringBuilder urlBuilder = null;
		    if(streamType == STREAM_TYPE.MAIN_STREAM){
			    urlBuilder = new StringBuilder(ServerConstants.NODE_SERVER_URL
				    + ServerConstants.USER_STREAM_FETCH_PREFIX + id
				    + ServerConstants.USER_STREAM_5_FETCH_SUFFIX + startIndex + Constants.URL_SLASH
					+ limit);
		    }else if(streamType == STREAM_TYPE.PERSONAL_STREAM){
			urlBuilder = new StringBuilder(ServerConstants.NODE_SERVER_URL
				+ ServerConstants.USER_STREAM_FETCH_PREFIX + id
				+ ServerConstants.USER_STREAM_FETCH_SUFFIX
				+ ServerConstants.PERSONAL_STREAM_FETCH_SUFFIX + startIndex + Constants.URL_SLASH
				+ limit);
		    }else if(streamType == STREAM_TYPE.COMMENTS_STREAM){
			    urlBuilder = new StringBuilder(ServerConstants.NODE_SERVER_URL
				    + ServerConstants.ASSET + id
				    + ServerConstants.COMMENTS + startIndex + Constants.URL_SLASH
					+ limit);
		    }
		    
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
			String result = Util.convertingInputToString(resEntity.getContent());
			JSONObject responseJSON = null;
			switch (statusCode) {
			case HttpStatus.SC_OK:
			    // result =
			    // "{\"success\": true,\"result\": [{\"asset_id\": \"29bf8966-b27f-4bda-b376-8da73cd9f981\",\"snap_url\": \"https://doodlydoo-stage.s3.amazonaws.com/server/doodle/2014-2-11/2bf04b35-945d-47e6-89ec-e4004bb2cffc_thumbnail.jpeg\",\"url\": \"https://s3.amazonaws.com/doodlydoo-stage/server/doodle/2013-11-12/741a9298-0348-4398-a2b1-5d6aad797502.zip\",\"owner\": {\"user_id\": \"391eb3d1-29ae-4361-a5b9-7ef49dd6d39f\",\"profile_picture\": \"https://doodlydoo-stage.s3.amazonaws.com/server/profile/2014-3-20/8b1eb058-267f-4a01-87da-e27e346c6045_image.png\",\"handle\": \"srivastava.robin\",\"name\": \"rRoobin Srivastava\"},\"created_at\": 1409232639,\"type\": \"DOODLE\",\"associations\": [] }, {\"asset_id\": \"29bf8966-b27f-4bda-b376-8da73cd9f982\",\"snap_url\": \"https://doodlydoo-stage.s3.amazonaws.com/server/doodle/2014-2-11/2bf04b35-945d-47e6-89ec-e4004bb2cffc_thumbnail.jpeg\",\"url\": \"https://s3.amazonaws.com/doodlydoo-stage/server/doodle/2013-11-12/741a9298-0348-4398-a2b1-5d6aad797502.zip\",\"owner\": {\"user_id\": \"391eb3d1-29ae-4361-a5b9-7ef49dd6d39f\",\"profile_picture\": \"https://doodlydoo-stage.s3.amazonaws.com/server/profile/2014-3-20/8b1eb058-267f-4a01-87da-e27e346c6045_image.png\",\"handle\": \"srivastava.robin\",\"name\": \"Robin Srivastava\"},\"created_at\": 1409232603,\"type\": \"DOODLE\",\"associations\": [] },{\"asset_id\": \"29bf8966-b27f-4bda-b376-8da73cd9f983\",\"snap_url\": \"https://doodlydoo-stage.s3.amazonaws.com/server/doodle/2014-2-11/2bf04b35-945d-47e6-89ec-e4004bb2cffc_thumbnail.jpeg\",\"url\": \"https://s3.amazonaws.com/doodlydoo-stage/server/doodle/2013-11-12/741a9298-0348-4398-a2b1-5d6aad797502.zip\",\"owner\": {\"user_id\": \"391eb3d1-29ae-4361-a5b9-7ef49dd6d39f\",\"profile_picture\": \"https://doodlydoo-stage.s3.amazonaws.com/server/profile/2014-3-20/8b1eb058-267f-4a01-87da-e27e346c6045_image.png\",\"handle\": \"srivastava.robin\",\"name\": \"rRoobin Srivastava\"},\"created_at\": 1409232639,\"type\": \"DOODLE\",\"associations\": [] }, {\"asset_id\": \"29bf8966-b27f-4bda-b376-8da73cd9f984\",\"snap_url\": \"https://doodlydoo-stage.s3.amazonaws.com/server/doodle/2014-2-11/2bf04b35-945d-47e6-89ec-e4004bb2cffc_thumbnail.jpeg\",\"url\": \"https://s3.amazonaws.com/doodlydoo-stage/server/doodle/2013-11-12/741a9298-0348-4398-a2b1-5d6aad797502.zip\",\"owner\": {\"user_id\": \"391eb3d1-29ae-4361-a5b9-7ef49dd6d39f\",\"profile_picture\": \"https://doodlydoo-stage.s3.amazonaws.com/server/profile/2014-3-20/8b1eb058-267f-4a01-87da-e27e346c6045_image.png\",\"handle\": \"srivastava.robin\",\"name\": \"Robin Srivastava\"},\"created_at\": 1409232603,\"type\": \"DOODLE\",\"associations\": [] },{\"asset_id\": \"29bf8966-b27f-4bda-b376-8da73cd9f985\",\"snap_url\": \"https://doodlydoo-stage.s3.amazonaws.com/server/doodle/2014-2-11/2bf04b35-945d-47e6-89ec-e4004bb2cffc_thumbnail.jpeg\",\"url\": \"https://s3.amazonaws.com/doodlydoo-stage/server/doodle/2013-11-12/741a9298-0348-4398-a2b1-5d6aad797502.zip\",\"owner\": {\"user_id\": \"391eb3d1-29ae-4361-a5b9-7ef49dd6d39f\",\"profile_picture\": \"https://doodlydoo-stage.s3.amazonaws.com/server/profile/2014-3-20/8b1eb058-267f-4a01-87da-e27e346c6045_image.png\",\"handle\": \"srivastava.robin\",\"name\": \"rRoobin Srivastava\"},\"created_at\": 1409232639,\"type\": \"DOODLE\",\"associations\": [] }, {\"asset_id\": \"29bf8966-b27f-4bda-b376-8da73cd9f986\",\"snap_url\": \"https://doodlydoo-stage.s3.amazonaws.com/server/doodle/2014-2-11/2bf04b35-945d-47e6-89ec-e4004bb2cffc_thumbnail.jpeg\",\"url\": \"https://s3.amazonaws.com/doodlydoo-stage/server/doodle/2013-11-12/741a9298-0348-4398-a2b1-5d6aad797502.zip\",\"owner\": {\"user_id\": \"391eb3d1-29ae-4361-a5b9-7ef49dd6d39f\",\"profile_picture\": \"https://doodlydoo-stage.s3.amazonaws.com/server/profile/2014-3-20/8b1eb058-267f-4a01-87da-e27e346c6045_image.png\",\"handle\": \"srivastava.robin\",\"name\": \"Robin Srivastava\"},\"created_at\": 1409232603,\"type\": \"DOODLE\",\"associations\": [] },{\"asset_id\": \"29bf8966-b27f-4bda-b376-8da73cd9f987\",\"snap_url\": \"https://doodlydoo-stage.s3.amazonaws.com/server/doodle/2014-2-11/2bf04b35-945d-47e6-89ec-e4004bb2cffc_thumbnail.jpeg\",\"url\": \"https://s3.amazonaws.com/doodlydoo-stage/server/doodle/2013-11-12/741a9298-0348-4398-a2b1-5d6aad797502.zip\",\"owner\": {\"user_id\": \"391eb3d1-29ae-4361-a5b9-7ef49dd6d39f\",\"profile_picture\": \"https://doodlydoo-stage.s3.amazonaws.com/server/profile/2014-3-20/8b1eb058-267f-4a01-87da-e27e346c6045_image.png\",\"handle\": \"srivastava.robin\",\"name\": \"rRoobin Srivastava\"},\"created_at\": 1409232639,\"type\": \"DOODLE\",\"associations\": [] }, {\"asset_id\": \"29bf8966-b27f-4bda-b376-8da73cd9f988\",\"snap_url\": \"https://doodlydoo-stage.s3.amazonaws.com/server/doodle/2014-2-11/2bf04b35-945d-47e6-89ec-e4004bb2cffc_thumbnail.jpeg\",\"url\": \"https://s3.amazonaws.com/doodlydoo-stage/server/doodle/2013-11-12/741a9298-0348-4398-a2b1-5d6aad797502.zip\",\"owner\": {\"user_id\": \"391eb3d1-29ae-4361-a5b9-7ef49dd6d39f\",\"profile_picture\": \"https://doodlydoo-stage.s3.amazonaws.com/server/profile/2014-3-20/8b1eb058-267f-4a01-87da-e27e346c6045_image.png\",\"handle\": \"srivastava.robin\",\"name\": \"Robin Srivastava\"},\"created_at\": 1409232603,\"type\": \"DOODLE\",\"associations\": [] }	]}";
			    if (!TextUtils.isEmpty(result)) {
				responseJSON = new JSONObject(result);
				if (responseJSON != null && responseJSON.has(JSONConstants.SUCCESS)
					&& responseJSON.getBoolean(JSONConstants.SUCCESS)) {
				    if (responseJSON.has(JSONConstants.RESULT)) {
					JSONArray streamJSONArray = responseJSON.getJSONArray(JSONConstants.RESULT);
					if (streamJSONArray != null && streamJSONArray.length() > 0) {
					    ArrayList<StreamAsset> streamAssets = parseStreamJSONArray(streamJSONArray);
					    if (streamAssets != null && streamAssets.size() > 0) {
						if (startIndex != -1 && limit != -1) {
						    didBatchFetchComplete(streamAssets, startIndex, limit, streamType);
						} else {
						    didFetchComplete(streamAssets, streamType);
						}
					    } else {
						/*
						 * In the case of batched assets fetching, do not delegate error if the
						 * startIndex is greater than 0 (Because, there might be some assets
						 * already available)
						 */
						if(streamType == STREAM_TYPE.PERSONAL_STREAM && startIndex > 0){
						    // do nothing
						}else{
						    failed(-1, -1,
							    "No stream to show.  Please start following your friends or create your own content.");
						}
						Logger.warn(TAG, "No assets in stream response");
					    }
					} else {
					    /*
					     * In the case of batched assets fetching, do not delegate error if the
					     * startIndex is greater than 0 (Because, there might be some assets already
					     * available)
					     */
					    if (streamType == STREAM_TYPE.PERSONAL_STREAM && startIndex > 0) {
						// do nothing
					    } else {
						failed(-1, -1,
							"No stream to show.  Please start following your friends or create your own content.");
					    }
					    Logger.warn(TAG, "No assets in stream response");
					}
				    }
				} else {
				    failed(-1, -1, "Failed to load content from server.");
				    Logger.warn(TAG, "No assets in stream response");
				}
			    } else {
				failed(-1, -1, "Failed to load content from server.");
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
			failed(-1, -1, "Failed to load content from server.");
			Logger.warn(TAG, "result entity is null");
		    }
		} catch (JSONException je) {
		    Logger.logStackTrace(je);
		    failed(-1, -1, "Failed to load content from server.");
		} catch (IllegalArgumentException iae) {
		    Logger.logStackTrace(iae);
		    failed(-1, -1, "Failed to load content from server.");
		} catch (IllegalStateException e) {
		    Logger.logStackTrace(e);
		    failed(-1, -1, "Failed to load content from server.");
		} catch (UnsupportedEncodingException e) {
		    Logger.logStackTrace(e);
		    failed(-1, -1, "Failed to load content from server.");
		} catch (IOException e) {
		    Logger.logStackTrace(e);
		    failed(-1, -1, "Failed to load content from server.");
		} catch (Exception e) {
		    Logger.logStackTrace(e);
		    failed(-1, -1, "Failed to load content from server.");
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

    private void didFetchComplete(ArrayList<StreamAsset> streamAssets, STREAM_TYPE streamType) {
	if (handler != null) {
	    handler.didFetchComplete(streamAssets, streamType);
	} else {
	    Logger.warn(TAG, "Handler is null");
	}
    }

    private void didBatchFetchComplete(ArrayList<StreamAsset> streamAssets, int startIndex, int limit, STREAM_TYPE streamType) {
	if (handler != null) {
	    handler.didBatchFetchComplete(streamAssets, startIndex, limit, streamType);
	} else {
	    Logger.warn(TAG, "Handler is null");
	}
    }

    /**
     * Parses the given Stream json object and returns array of Stream Assets
     * 
     * @param streamJSONArray
     * @return StreamAsset
     */
    private ArrayList<StreamAsset> parseStreamJSONArray(JSONArray streamJSONArray) {
	ArrayList<StreamAsset> streamAssets = null;
	if (streamJSONArray != null && streamJSONArray.length() > 0) {
	    streamAssets = new ArrayList<StreamAsset>();
	    StreamAssetParser parser = new StreamAssetParser(context);
	    if (parser != null) {
		for (int i = 0; i < streamJSONArray.length(); i++) {
		    try {
			StreamAsset asset = parser.parseAssetJSON(streamJSONArray.getJSONObject(i));
			if (asset != null) {
			    streamAssets.add(asset);
			}
		    } catch (JSONException e) {
			streamAssets = null;
		    }
		}
	    }
	}
	return streamAssets;
    }

}
