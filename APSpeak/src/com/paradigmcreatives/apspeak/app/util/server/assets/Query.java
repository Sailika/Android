package com.paradigmcreatives.apspeak.app.util.server.assets;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
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
import android.graphics.Point;
import android.text.TextUtils;

import com.paradigmcreatives.apspeak.app.util.AppPropertiesUtil;
import com.paradigmcreatives.apspeak.app.util.DeviceInfoUtil;
import com.paradigmcreatives.apspeak.app.util.Util;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.app.util.constants.JSONConstants;
import com.paradigmcreatives.apspeak.app.util.constants.ServerConstants;
import com.paradigmcreatives.apspeak.app.util.constants.Constants.ExitState;
import com.paradigmcreatives.apspeak.app.util.network.NetworkManager;
import com.paradigmcreatives.apspeak.app.util.server.assets.AssetDownloader.AssetType;
import com.paradigmcreatives.apspeak.logging.Logger;

public class Query {

    private Context context;
    private String id;
    private AssetType assetType;
    private String category;
    HashMap<AssetType, String> assetURLMapping = new HashMap<AssetType, String>();
    private static final String TAG = "assets.Query";
    private static final String LAST_VIEWED_ID = "last_viewed_id";
    private static final String LAST_VIEWED_AUTO_ID = "last_viewed_auto_id";
    private static final String HEIGHT = "height";
    private static final String WIDTH = "width";
    private static final String CATEGORY = "category";
    private static final String LIMIT = "limit";
    private ExitState exitState = ExitState.NORMAL_EXIT;

    public Query(Context context, AssetType assetType) {
	this.context = context;
	this.assetType = assetType;
	initAssetURLMapping();
    }

    public Query(Context context, AssetType assetType, String category) {
	this.context = context;
	this.assetType = assetType;
	this.category = category;
	initAssetURLMapping();
    }

    public Query(Context context, String id, AssetType assetType) {
	this.context = context;
	this.id = id;
	this.assetType = assetType;
	initAssetURLMapping();
    }

    public Query(Context context, String id, AssetType assetType, String category) {
	this.context = context;
	this.id = id;
	this.assetType = assetType;
	this.category = category;
	initAssetURLMapping();
    }

    private void initAssetURLMapping() {
	assetURLMapping.put(AssetType.GREETINGS, ServerConstants.GREETINGS_ASSET_URL);
	assetURLMapping.put(AssetType.RAGE_FACE, ServerConstants.RAGE_FACE_ASSET_URL);
	assetURLMapping.put(AssetType.EMOJI, ServerConstants.EMOJI_ASSET_URL);
	assetURLMapping.put(AssetType.DOODLEWORLD, ServerConstants.DOODLEWORLD_ASSET_URL);
    }

    /**
     * Perform execute with the given limit value, i.e., only the specified number of results would be returned
     * 
     * @param limit
     * @return
     */
    public JSONArray execute(int limit) {
	JSONArray result = null;

	HttpPost httpPost = null;
	HttpClient httpClient = null;

	try {
	    if (assetType != null) {
		String url = ServerConstants.SERVER_URL + assetURLMapping.get(assetType);
		final HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, ServerConstants.CONNECTION_TIMEOUT);
		httpPost = new HttpPost(url);
		NetworkManager.getInstance().register(httpPost);

		// Add session id as request header via Cookie name
		if (Constants.IS_SESSION_ENABLED) {
		    httpPost.addHeader(Constants.REQUEST_COOKIE, JSONConstants.SESSIONID + AppPropertiesUtil.getSessionId(context));
		}

		httpClient = new DefaultHttpClient(httpParams);

		String jsonString = null;
		JSONObject assetArguments = new JSONObject();

		Point screenDimens = null;
		if (context != null) {
		    screenDimens = DeviceInfoUtil.getScreenSize(context);
		}
		if (screenDimens == null) {
		    screenDimens = new Point(500, 600);
		}
		assetArguments.put(WIDTH, screenDimens.x);
		assetArguments.put(HEIGHT, screenDimens.y);

		if (limit > 0) {
		    assetArguments.put(LIMIT, limit);
		}

		if (!TextUtils.isEmpty(id)) {
		    if (assetType == AssetType.DOODLEWORLD) {
			assetArguments.put(LAST_VIEWED_AUTO_ID, Integer.parseInt(id));
		    } else {
			assetArguments.put(LAST_VIEWED_ID, id);
		    }
		}

		if (assetType == AssetType.DOODLEWORLD && TextUtils.isEmpty(id)) {
		    assetArguments.put(LAST_VIEWED_AUTO_ID, 0); // Passing the number zero here means that send from the
								// beginning
		}

		if (!TextUtils.isEmpty(category)) {
		    assetArguments.put(CATEGORY, category);
		}

		jsonString = assetArguments.toString();
		StringEntity stringEntity = new StringEntity(jsonString);
		stringEntity.setContentType(ServerConstants.CONTENT_TYPE_JSON);

		httpPost.setEntity(stringEntity);
		HttpResponse response = httpClient.execute(httpPost);

		HttpEntity resEntity = response.getEntity();

		if (resEntity != null) {
		    int statusCode = response.getStatusLine().getStatusCode();
		    String resultString = Util.convertingInputToString(resEntity.getContent());
		    switch (statusCode) {
		    case HttpStatus.SC_OK:
			
			result = new JSONArray(resultString);
			break;

		    default:
			Logger.warn(TAG,
				"Some wrong response returned from the server while getting the greetings. Here is the response entity"
					+ resEntity.toString());
			break;
		    }
		}
	    }
	} catch (JSONException e) {
	    Logger.warn(TAG, e.getLocalizedMessage());
	    exitState = ExitState.PARSING_FAILED;
	    e.printStackTrace();
	} catch (UnsupportedEncodingException e) {
	    Logger.warn(TAG, e.getLocalizedMessage());
	    exitState = ExitState.WRONG_ENCODING;
	    e.printStackTrace();
	} catch (ClientProtocolException e) {
	    Logger.warn(TAG, e.getLocalizedMessage());
	    exitState = ExitState.WRONG_CLIENT_PROTOCOL;
	    e.printStackTrace();
	} catch (IOException e) {
	    exitState = ExitState.CONNECTION_INTERRUPTED;
	    Logger.warn(TAG, e.getLocalizedMessage());
	    e.printStackTrace();
	} catch (Exception e) {
	    exitState = ExitState.UNKNOWN;
	    Logger.warn(TAG, e.getLocalizedMessage());
	    e.printStackTrace();
	} finally {
	    NetworkManager.getInstance().unRegister(httpPost);
	    if (httpClient != null) {
		httpClient.getConnectionManager().shutdown();
	    }

	}

	return result;

    }

    /**
     * Perform execute with the default value of limit
     * 
     * @return
     */
    public JSONArray execute() {
	return execute(0);
    }

    /**
     * @return the exitState
     */
    public ExitState getExitState() {
	return exitState;
    }

}
