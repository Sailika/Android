package com.paradigmcreatives.apspeak.doodleboard.send;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.text.TextUtils;

import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.app.model.ExpressionSubmitQueueBean;
import com.paradigmcreatives.apspeak.app.model.SubmitResultBean;
import com.paradigmcreatives.apspeak.app.util.AppPropertiesUtil;
import com.paradigmcreatives.apspeak.app.util.DeviceInfoUtil;
import com.paradigmcreatives.apspeak.app.util.Util;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.app.util.constants.JSONConstants;
import com.paradigmcreatives.apspeak.app.util.constants.ServerConstants;
import com.paradigmcreatives.apspeak.app.util.constants.Constants.ExitState;
import com.paradigmcreatives.apspeak.app.util.network.NetworkManager;
import com.paradigmcreatives.apspeak.logging.Logger;

/**
 * Convenience class for performing send of the doodles
 * 
 * @author robin
 * 
 */
public class AssetSubmitHelper {
	private static final String TAG = "AssetSubmitHelper";

	public enum DoodleStatus {
		SENT, FAILED, SENDING
	}

	public enum AssetType {
		DOODLE, IMAGE
	}

	// Class vars
	private Context context = null;
	private ExitState exitState = null;
	//private File doodleZipFile = null;
	private ExpressionSubmitQueueBean mExpression = null;

	private String mGroupId = null;
	private String mUserId = null;
	private String mCueId = null;
	private String mRootAssetId = null;
	private String mDescription = null;
	private String mAssetType = null;
	private String mFilePath = null;

	/**
	 * 
	 * @param assetPath
	 * @param context
	 * @param description
	 */
	public AssetSubmitHelper(Context context,
			ExpressionSubmitQueueBean expression) {
		this.context = context;
		this.mExpression = expression;
		initialize();
	}

	private void initialize() {
		if (this.mExpression != null) {
			this.mGroupId = this.mExpression.getGroupId();
			this.mUserId = this.mExpression.getUserId();
			this.mCueId = this.mExpression.getCueId();
			this.mRootAssetId = this.mExpression.getRootAssetId();
			this.mDescription = this.mExpression.getDescription();
			this.mAssetType = this.mExpression.getType();
			this.mFilePath = this.mExpression.getFilePath();
		}
	}

	/**
	 * Performs the send operation based on the values initialized during this
	 * class' object construction
	 * 
	 * @return
	 */
	public SubmitResultBean submitDoodle() {
		SubmitResultBean result = null;
		HttpPost httpPost = null;
		HttpClient httpClient = null;
		HttpResponse httpResponse = null;

		try {
			if (validateInputs()) {

				// 1 - Initialize the HTTP related objects
				httpClient = new DefaultHttpClient();
				httpPost = new HttpPost(ServerConstants.SERVER_URL
						+ ServerConstants.ASSET_SUBMIT);
				if(mExpression != null && !TextUtils.isEmpty(mExpression.getRootAssetId())){
					// Expression submission as a COMMENT
					httpPost = new HttpPost(ServerConstants.SERVER_URL
							+ ServerConstants.COMMENT_CREATE);
				}

				// Add session id as request header via Cookie name
				if (Constants.IS_SESSION_ENABLED) {
					httpPost.addHeader(
							Constants.REQUEST_COOKIE,
							JSONConstants.SESSIONID
									+ AppPropertiesUtil.getSessionId(context));
				}

				// 2 - Intimate the network manager for the impending network
				// operation
				NetworkManager.getInstance().register(httpPost);

				// 3 - Call the server
				httpPost.setEntity(createRequestEntity());
				httpResponse = httpClient.execute(httpPost);

				// 4 - Parse the response
				result = parseSendResponse(httpResponse);

				// 5 - Update the exit state
				setExitState(ExitState.NORMAL_EXIT);

			} else {
				Logger.warn(TAG,
						"Could not perform send. One of the critical value is null");
			}
		} catch (UnsupportedEncodingException e) {
			result = new SubmitResultBean(null, "Unsupported encoding", -1,
					false);
			Logger.warn(TAG, "Error during send. " + e.getMessage());
		} catch (ClientProtocolException e) {
			result = new SubmitResultBean(null, "Client protocol exception",
					-1, false);
			Logger.warn(TAG, "Error during send. " + e.getMessage());
		} catch (IOException e) {
			setExitState(ExitState.CONNECTION_INTERRUPTED);
			result = new SubmitResultBean(null, "Connection interrupted", -1,
					false);
			Logger.warn(TAG, "Error during send. " + e.getMessage());
		} catch (IllegalStateException e) {
			result = new SubmitResultBean(null, "Illegal state", -1, false);
			Logger.warn(TAG, "Error during send. " + e.getMessage());
		} catch (JSONException e) {
			result = new SubmitResultBean(null, "Exception while parsing JSON",
					-1, false);
			Logger.warn(TAG, "Error during send. " + e.getMessage());
		} finally {
			try {
				// 1 - Delete the temporary file
				//deleteDoodleZIPfile();

				// 2 - Unregister from the network manager
				NetworkManager.getInstance().unRegister(httpPost);
				if (httpClient != null) {
					httpClient.getConnectionManager().shutdown();
				}
			} catch (Exception e) {
				Logger.logStackTrace(e);
			}
		}

		return result;
	}

	/**
	 * Parses the server's response of send service
	 * 
	 * @param httpResponse
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 * @throws JSONException
	 */
	private SubmitResultBean parseSendResponse(HttpResponse httpResponse)
			throws IllegalStateException, IOException, JSONException {
		SubmitResultBean result = null;
		if (httpResponse != null) {
			HttpEntity responseEntity = httpResponse.getEntity();
			if (responseEntity != null) {
				String responseString = Util
						.convertingInputToString(responseEntity.getContent());
				int statusCode = httpResponse.getStatusLine().getStatusCode();

				// Try to extract the result from the response

				switch (statusCode) {
				case HttpStatus.SC_OK:
					JSONObject responseJSON = new JSONObject(responseString);
					if (responseJSON != null) {
						String assetID = null;
						if (responseJSON.has(JSONConstants.ASSET_ID)) {
							assetID = responseJSON
									.getString(JSONConstants.ASSET_ID);
						} else if (responseJSON
								.has(JSONConstants.COMMENT_ASSET_ID)) {
							assetID = responseJSON
									.getString(JSONConstants.COMMENT_ASSET_ID);
						}
						if (!TextUtils.isEmpty(assetID)) {
							result = new SubmitResultBean(assetID, null, -1,
									true);
						} else {
							result = new SubmitResultBean(
									null,
									"No ID sent even though its a positive response",
									-1, true);
						}
					}
					break;
				case HttpStatus.SC_BAD_REQUEST:
					result = new SubmitResultBean(null,
							"Wrong Inputs Sent for request",
							HttpStatus.SC_BAD_REQUEST, false);
					break;
				case HttpStatus.SC_INTERNAL_SERVER_ERROR:
					result = new SubmitResultBean(null,
							"Server couldn't respond properly",
							HttpStatus.SC_INTERNAL_SERVER_ERROR, false);
					break;
				case HttpStatus.SC_FORBIDDEN:
					JSONObject responseJSONObject = new JSONObject(
							responseString);
					String message = "";
					if (responseJSONObject != null
							&& responseJSONObject.has(JSONConstants.ERROR)) {
						message = responseJSONObject
								.getString(JSONConstants.ERROR);
					}
					result = new SubmitResultBean(null, message,
							HttpStatus.SC_FORBIDDEN, false);
					break;
				default:
					result = new SubmitResultBean(null, "Unexpected response",
							-1, false);
					break;
				}
			} else {
				result = new SubmitResultBean(null, "Empty response sent", -1,
						false);
				Logger.warn(TAG, "Empty response entity in the send response");
			}
		} else {
			result = new SubmitResultBean(null, "No response sent", -1, false);
			Logger.warn(TAG, "Empty response entity in the send response");
			Logger.warn(TAG, "Empty response received during send");
		}
		return result;
	}

	/**
	 * Creates the request entity for the send request
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private MultipartEntity createRequestEntity()
			throws UnsupportedEncodingException {

		MultipartEntity reqEntity = new MultipartEntity();

		reqEntity.addPart(JSONConstants.PLATFORM, new StringBody(
				Constants.PLATFORM_VALUE));
		if (mGroupId != null) {
			reqEntity.addPart(JSONConstants.GROUP_ID, new StringBody(mGroupId));
		}
		if (!TextUtils.isEmpty(mUserId)) {
			reqEntity.addPart(JSONConstants.USER_ID, new StringBody(mUserId));
		}
		if (mCueId != null) {
			reqEntity.addPart(JSONConstants.CUE_ID, new StringBody(mCueId));
		}
		if (!TextUtils.isEmpty(mRootAssetId)) {
			reqEntity.addPart(JSONConstants.ROOT_ASSET_ID, new StringBody(
					mRootAssetId));
		}
		if (!TextUtils.isEmpty(mDescription)) {
			reqEntity.addPart(JSONConstants.DESCRIPTION, new StringBody(
					mDescription));
		}
		if (mAssetType != null && mAssetType.equals(AssetType.IMAGE.name())) {
			reqEntity.addPart(JSONConstants.TYPE, new StringBody("IMAGE"));
			if (!TextUtils.isEmpty(mFilePath)) {
				File doodleZipFile = new File(mFilePath);
				FileBody binary = new FileBody(doodleZipFile);
				reqEntity.addPart(JSONConstants.CONTENT, binary);
			}
		}

		return reqEntity;
	}

	/**
	 * Validate the inputs to check if send can happen or not
	 * 
	 * @return
	 */
	private boolean validateInputs() {
		return (context != null) && (mAssetType != null);
	}

	/**
	 * Deletes temporary doodle ZIP file if it exists
	 */
	private void deleteDoodleZIPfile() {
		/*
		if (doodleZipFile != null) {
			doodleZipFile.delete();
		}
		*/
	}

	public ExitState getExitState() {
		return exitState;
	}

	private void setExitState(ExitState exitState) {
		this.exitState = exitState;
	}

	/**
	 * Saves bitmap in whatsay folder as tempImage.jpeg The image will be
	 * deleted after submitting the expression to stream
	 * 
	 * @param context
	 * @param bitmap
	 * @return
	 */
	public static boolean saveBitmapToTempLocation(Context context,
			Bitmap bitmap) {
		boolean saved = false;
		try {
			if (context != null && bitmap != null) {
				if (DeviceInfoUtil.mediaWritable()) {
					String appRoot = AppPropertiesUtil.getAppDirectory(context);
					String tempFolder = context.getResources().getString(
							R.string.whatsay_folder);
					File tempFile = new File(appRoot, tempFolder);
					if (!tempFile.exists()) {
						tempFile.mkdir();
					}

					String tempFolderPath = tempFile.getAbsolutePath();
					File imageFile = new File(tempFolderPath, context
							.getResources().getString(R.string.temp_image_name));
					FileOutputStream out = new FileOutputStream(imageFile);

					saved = bitmap.compress(CompressFormat.PNG,
							Constants.COMPRESSION_QUALITY_HIGH, out);
				} else {
					Logger.warn(TAG,
							"Can't save bitmap to file. No memory card");
				}

			} else {
				Logger.warn(TAG,
						"Either context is null or the supplied bitmap is null. Conext - "
								+ context + " Bitmap - " + bitmap);
			}
		} catch (Exception e) {
			Logger.warn(TAG, e.getMessage());
		}
		return saved;
	}

	/**
	 * Saves bitmap in whatsay private folder as timestamp.jpeg The image will
	 * be deleted after submitting the expression to stream
	 * 
	 * @param context
	 * @param bitmap
	 * @return
	 */
	public static String saveBitmapToAppPrivateFolder(Context context,
			Bitmap bitmap) {
		String savedImagePath = null;
		try {
			if (context != null && bitmap != null) {
				if (DeviceInfoUtil.mediaWritable()) {
					File tempFile = new File(context.getFilesDir()
							.getAbsolutePath());
					if (!tempFile.exists()) {
						tempFile.mkdir();
					}

					String tempFolderPath = tempFile.getAbsolutePath();
					File imageFile = new File(tempFolderPath,
							System.currentTimeMillis() + ".jpeg");
					FileOutputStream out = new FileOutputStream(imageFile);

					boolean saved = bitmap.compress(CompressFormat.PNG,
							Constants.COMPRESSION_QUALITY_HIGH, out);
					if (saved) {
						savedImagePath = imageFile.getAbsolutePath();
					}
				} else {
					Logger.warn(TAG,
							"Can't save bitmap to file. No memory card");
				}

			} else {
				Logger.warn(TAG,
						"Either context is null or the supplied bitmap is null. Conext - "
								+ context + " Bitmap - " + bitmap);
			}
		} catch (Exception e) {
			Logger.warn(TAG, e.getMessage());
		}
		return savedImagePath;
	}
}
