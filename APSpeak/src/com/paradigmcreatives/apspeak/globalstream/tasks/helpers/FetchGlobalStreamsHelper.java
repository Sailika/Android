package com.paradigmcreatives.apspeak.globalstream.tasks.helpers;

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

import com.paradigmcreatives.apspeak.app.model.StreamAsset;
import com.paradigmcreatives.apspeak.app.util.AppPropertiesUtil;
import com.paradigmcreatives.apspeak.app.util.Util;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.app.util.constants.JSONConstants;
import com.paradigmcreatives.apspeak.app.util.constants.ServerConstants;
import com.paradigmcreatives.apspeak.app.util.network.NetworkManager;
import com.paradigmcreatives.apspeak.logging.Logger;
import com.paradigmcreatives.apspeak.stream.parsers.StreamAssetParser;
import com.paradigmcreatives.apspeak.stream.tasks.GetStreamThread.STREAM_TYPE;

/**
 * Helper class that fetches stream by connecting to Whatsay server
 * 
 * @author Dileep | neuv
 * 
 */
public class FetchGlobalStreamsHelper {

    private Context mContext;
    private String mUserId;
    private String mGroupId;
    private String mCueId;
    private STREAM_TYPE mStreamType;
    private int mOffset;
    private int mCount;

    private final String TAG = "FetchGlobalStreamsHelper";

    public FetchGlobalStreamsHelper(Context context, String userId, String groupId, String cueId,
	    STREAM_TYPE streamType, int startIndex, int limit) {
	super();
	this.mContext = context;
	this.mUserId = userId;
	this.mGroupId = groupId;
	this.mCueId = cueId;
	this.mStreamType = streamType;
	this.mOffset = startIndex;
	this.mCount = limit;
    }

    public ArrayList<StreamAsset> execute() {
	ArrayList<StreamAsset> streamAssets = null;
	if (mContext != null && !TextUtils.isEmpty(mUserId) && mStreamType != null) {
	    HttpPost httpPost = null;
	    HttpClient httpclient = null;
	    try {
		final HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, ServerConstants.CONNECTION_TIMEOUT);
		StringBuilder urlBuilder = new StringBuilder(ServerConstants.NODE_SERVER_URL
			+ ServerConstants.USER_STREAM_FETCH_SUFFIX);

		httpPost = new HttpPost(urlBuilder.toString());
		NetworkManager.getInstance().register(httpPost);

		// Add session id as request header via Cookie name
		if (Constants.IS_SESSION_ENABLED) {
		    httpPost.addHeader(Constants.REQUEST_COOKIE,
			    JSONConstants.SESSIONID + AppPropertiesUtil.getSessionId(mContext));
		}

		httpclient = new DefaultHttpClient(httpParams);

		JSONObject jObject = createRequestEntity();
		if (jObject != null) {
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
					JSONArray streamJSONArray = responseJSON.getJSONArray(JSONConstants.RESULT);
					if (streamJSONArray != null && streamJSONArray.length() > 0) {
					    StreamAssetParser parser = new StreamAssetParser(mContext);
					    if (parser != null) {
						streamAssets = parser.parseStreamJSONArray(streamJSONArray);
					    }
					}
				    }
				}
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
		NetworkManager.getInstance().unRegister(httpPost);
		if (httpclient != null) {
		    httpclient.getConnectionManager().shutdown();
		}
	    }
	} else {
	    Logger.warn(TAG, "One of the passed parameters are empty");
	}
	return streamAssets;
    }

    /**
     * Creates request entity based on Stream Type
     * 
     * @return
     */
    private JSONObject createRequestEntity() {
	JSONObject jsonObject = null;
	try {
	    if (mStreamType != null) {
		if (mStreamType == STREAM_TYPE.PERSONAL_STREAM) {
		    if (!TextUtils.isEmpty(mUserId)) {
			jsonObject = new JSONObject();
			String myId = AppPropertiesUtil.getUserID(mContext);
			if(!TextUtils.isEmpty(myId) && !myId.equals(mUserId)){
				// Its not currently logged in user's personal stream
				// Hence add my_id parameter too
				jsonObject.put(JSONConstants.MY_ID, myId);
			}
			jsonObject.put(JSONConstants.LABEL, JSONConstants.IDEA);
			jsonObject.put(JSONConstants.USER_ID, mUserId);
			jsonObject.put(JSONConstants.TYPE, JSONConstants.PERSONAL);
			jsonObject.put(JSONConstants.OFFSET, mOffset);
			jsonObject.put(JSONConstants.COUNT, mCount);
		    }
		} else if (mStreamType == STREAM_TYPE.COLLEGE) {
		    if (!TextUtils.isEmpty(mUserId) && !TextUtils.isEmpty(mGroupId) && !TextUtils.isEmpty(mCueId)) {
			jsonObject = new JSONObject();
			jsonObject.put(JSONConstants.USER_ID, mUserId);
			jsonObject.put(JSONConstants.LABEL, JSONConstants.IDEA);

			jsonObject.put(JSONConstants.GROUP_ID, mGroupId);
			jsonObject.put(JSONConstants.CUE_ID, mCueId);
			jsonObject.put(JSONConstants.TYPE, JSONConstants.GENERAL);
			jsonObject.put(JSONConstants.OFFSET, mOffset);
			jsonObject.put(JSONConstants.COUNT, mCount);
		    }
		} else if (mStreamType == STREAM_TYPE.ALLCOLLEGES) {
		    if (!TextUtils.isEmpty(mUserId) && !TextUtils.isEmpty(mCueId)) {
			jsonObject = new JSONObject();
			jsonObject.put(JSONConstants.LABEL, JSONConstants.IDEA);

			jsonObject.put(JSONConstants.USER_ID, mUserId);
			jsonObject.put(JSONConstants.CUE_ID, mCueId);
			jsonObject.put(JSONConstants.TYPE, JSONConstants.GENERAL);
			jsonObject.put(JSONConstants.COVERAGE, JSONConstants.GROUP);
			jsonObject.put(JSONConstants.OFFSET, mOffset);
			jsonObject.put(JSONConstants.COUNT, mCount);
		    }
		} else if (mStreamType == STREAM_TYPE.FRIENDS) {
		    if (!TextUtils.isEmpty(mUserId) && !TextUtils.isEmpty(mCueId)) {
			jsonObject = new JSONObject();
			jsonObject.put(JSONConstants.LABEL, JSONConstants.IDEA);

			jsonObject.put(JSONConstants.USER_ID, mUserId);
			jsonObject.put(JSONConstants.CUE_ID, mCueId);
			jsonObject.put(JSONConstants.TYPE, JSONConstants.GENERAL);
			jsonObject.put(JSONConstants.COVERAGE, JSONConstants.FRIENDS);
			jsonObject.put(JSONConstants.OFFSET, mOffset);
			jsonObject.put(JSONConstants.COUNT, mCount);
		    }
		} else if (mStreamType == STREAM_TYPE.FEATURED_STREAM) {
		    if (!TextUtils.isEmpty(mUserId)) {
			jsonObject = new JSONObject();
			jsonObject.put(JSONConstants.USER_ID, mUserId);
			jsonObject.put(JSONConstants.LABEL, JSONConstants.IDEA);
			jsonObject.put(JSONConstants.TYPE, JSONConstants.GENERAL);
			jsonObject.put(JSONConstants.COVERAGE, JSONConstants.FEATURED);
			jsonObject.put(JSONConstants.OFFSET, mOffset);
			jsonObject.put(JSONConstants.COUNT, mCount);
		    }
		}
	    }
	} catch (Exception e) {

	}
	return jsonObject;
    }
}
