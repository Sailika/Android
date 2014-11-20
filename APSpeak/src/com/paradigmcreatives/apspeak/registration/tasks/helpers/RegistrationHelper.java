package com.paradigmcreatives.apspeak.registration.tasks.helpers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.Header;
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

import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.paradigmcreatives.apspeak.app.database.SettingsDBUtil;
import com.paradigmcreatives.apspeak.app.database.settingsdb.SettingsDAO;
import com.paradigmcreatives.apspeak.app.model.FacebookProfile;
import com.paradigmcreatives.apspeak.app.model.RegisterBean;
import com.paradigmcreatives.apspeak.app.util.AppPropertiesUtil;
import com.paradigmcreatives.apspeak.app.util.Util;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.app.util.constants.ServerConstants;
import com.paradigmcreatives.apspeak.app.util.images.ImageUtil;
import com.paradigmcreatives.apspeak.app.util.network.NetworkManager;
import com.paradigmcreatives.apspeak.logging.Logger;
import com.paradigmcreatives.apspeak.registration.LoginActivity;
import com.paradigmcreatives.apspeak.registration.RegistrationUtil;

public class RegistrationHelper {
    private static final String TAG = "RegistrationHelper";
    private Fragment fragment = null;

    // Request/response attributes
    private static final String RESULT = "result";
    private static final String TRUE = "true";

    public RegistrationHelper(Fragment fragment) {
	this.fragment = fragment;
    }

    public RegisterBean execute() {
	RegisterBean registerBean = null;

	if (fragment != null) {
	    HttpPost httpPost = null;
	    HttpClient httpclient = null;
	    try {
		final HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, ServerConstants.CONNECTION_TIMEOUT);
		httpPost = new HttpPost(ServerConstants.SERVER_URL + ServerConstants.REGISTER_USER);
		NetworkManager.getInstance().register(httpPost);
		httpclient = new DefaultHttpClient(httpParams);

		String jsonString = null;
		JSONObject updateJson = buildRequestJSON();

		jsonString = updateJson.toString();

		if (!TextUtils.isEmpty(jsonString)) {
		    StringEntity stringEntity = new StringEntity(jsonString);
		    Logger.info(TAG, "json string:" + jsonString);
		    stringEntity.setContentType(ServerConstants.CONTENT_TYPE_JSON);

		    httpPost.setEntity(stringEntity);
		    HttpResponse response = httpclient.execute(httpPost);

		    HttpEntity resEntity = response.getEntity();
		    if (resEntity != null) {
			int statusCode = response.getStatusLine().getStatusCode();
			Logger.info(TAG, "status code is:" + statusCode);
			
			// Retrieve session id
			if (Constants.IS_SESSION_ENABLED) {
			    Header[] responseHeaders = response.getAllHeaders();
			    if (responseHeaders != null) {
				for (Header header : responseHeaders) {
				    if (header != null && header.getName().equals(Constants.RESPONSE_SET_COOKIE)) {
					// The format of the header value is: sessionId=<session id>;Version=<value>
					// Now parse the session id from the above format
					String headerValue = header.getValue();
					try {
					    String sessionId = headerValue.substring(headerValue.indexOf('=') + 1,
						    headerValue.indexOf(';'));
					    AppPropertiesUtil.setSessionID(fragment.getActivity(), sessionId);
					} catch (IndexOutOfBoundsException e) {
					    // handle exception
					}
					break;
				    }
				}
			    }
			}

			String result = null;
			JSONObject responseJSON = null;
			result = Util.convertingInputToString(resEntity.getContent());
			switch (statusCode) {
			case HttpStatus.SC_OK:
			    if (!TextUtils.isEmpty(result)) {
				responseJSON = new JSONObject(result);
				if (responseJSON.has(RESULT) && TextUtils.equals(responseJSON.getString(RESULT), TRUE)) {
				    registerBean = new RegisterBean("", true);
				    onSuccessfulRegistration(registerBean);
				}
			    } else {
				Logger.warn(TAG, "Response is null");
			    }

			    break;
			case HttpStatus.SC_CREATED:
			    if (!TextUtils.isEmpty(result)) {
				responseJSON = new JSONObject(result);
				String userId = responseJSON.getString("user_id");
				registerBean = new RegisterBean(userId, true);
				onSuccessfulRegistration(registerBean);
			    } else {
				Logger.warn(TAG, "Response is null");
			    }

			    break;
			case HttpStatus.SC_INTERNAL_SERVER_ERROR:
			    Logger.warn(TAG, "Error in response:" + response.getStatusLine().getReasonPhrase());
			    onFailedRegistration();
			    break;

			default:
			    Logger.warn(TAG, "Error in response:" + response.getStatusLine().getReasonPhrase());
			    onFailedRegistration();
			    break;
			}
		    } else {
			Logger.warn(TAG, "result entity is null");
		    }
		} else {
		    Logger.warn(TAG, "json string is null");
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
	    Logger.warn(TAG, "Context is null");
	}

	return registerBean;
    }

    /**
     * Builds the JSON meant to be sent to the server. The data for this JSON is collected from the
     * InitialUserSetupActivity's <code>profile</code> data member
     * 
     * @return
     */
    private JSONObject buildRequestJSON() {
	JSONObject requestJSON = null;
	if (fragment != null && fragment.getActivity() instanceof LoginActivity) {
	    FacebookProfile profile = getFacebookProfile();
	    requestJSON = new JSONObject();

	    try {
		String fullName = !TextUtils.isEmpty(profile.getFirstName()) ? profile.getFirstName() : "";
		fullName = fullName + " " + (!TextUtils.isEmpty(profile.getLastName()) ? profile.getLastName() : "");

		if (!TextUtils.isEmpty(fullName)) {
		    requestJSON.put("name", fullName);
		}

		/*
		String handle = AppPropertiesUtil.getUserUniqueHandle(fragment.getActivity());
		if (!TextUtils.isEmpty(handle)) {
		    requestJSON.put("handle", handle);
		}
		*/

		if (!TextUtils.isEmpty(profile.getPassword())) {
		    requestJSON.put("password", profile.getPassword());
		}

		if (!TextUtils.isEmpty(profile.getEmail())) {
		    requestJSON.put("email", profile.getEmail());
		}

		if (profile.getGender() != null) {
		    requestJSON.put("gender", profile.getGender().name());
		}

		String userId = AppPropertiesUtil.getUserID(fragment.getActivity());
		if (!TextUtils.isEmpty(userId)) {
		    requestJSON.put("user_id", userId);
		}

		String pushId = AppPropertiesUtil.getGCMID(fragment.getActivity());
		if (pushId != null && pushId.length() > 0) {
		    requestJSON.put("push_id", pushId);
		}
		requestJSON.put("platform", Constants.PLATFORM_VALUE);
		RegistrationUtil registrationUtil = new RegistrationUtil(fragment.getActivity());
		String deviceMacId = registrationUtil.getDeviceMacId();
		if (!TextUtils.isEmpty(deviceMacId)) {
		    requestJSON.put("mac_id", deviceMacId);
		}
		String deviceUniqueId = RegistrationUtil.getDeviceUniqueId(fragment.getActivity());
		if (!TextUtils.isEmpty(deviceUniqueId)) {
		    requestJSON.put("device_id", deviceUniqueId);
		}

		if (!TextUtils.isEmpty(profile.getProfilePictureUrl())) {
		    requestJSON.put("photo_url", profile.getProfilePictureUrl());
		}

		JSONArray accountsArray = new JSONArray();
		JSONObject facebookAccount = new JSONObject();
		facebookAccount.put("provider", "FACEBOOK");
		SettingsDBUtil settingsUtil = new SettingsDBUtil(fragment.getActivity());
		if (settingsUtil.isKeyPresent(Constants.KEY_FACEBOOK_PROFILE_ID)) {
		    String uid = settingsUtil.getValue(Constants.KEY_FACEBOOK_PROFILE_ID);
		    if (!TextUtils.isEmpty(uid)) {
			facebookAccount.put("uid", uid);
		    }
		}
		if (settingsUtil.isKeyPresent(Constants.KEY_FACEBOOK_PROFILE_ACCESSTOKEN)) {
		    String accessToken = settingsUtil.getValue(Constants.KEY_FACEBOOK_PROFILE_ACCESSTOKEN);
		    if (!TextUtils.isEmpty(accessToken)) {
			facebookAccount.put("access_token", accessToken);
		    }
		}
		if (!TextUtils.isEmpty(profile.getBiography())) {
		    facebookAccount.put("about_me", profile.getBiography());
		}
		accountsArray.put(facebookAccount);

		requestJSON.put("accounts", accountsArray);
	    } catch (JSONException e) {
		Logger.warn(TAG, "Exception while creating the request JSON for registration. " + e);
	    } catch (Exception e) {
		Logger.warn(TAG, "Unknown exception while creating the request JSON for registration. " + e);
	    }

	}
	return requestJSON;
    }

    /**
     * Get the reference to the <code>FacebookProfile</code> from <code>InitialUserSetupActivity</code>
     * 
     * @return
     */
    private FacebookProfile getFacebookProfile() {
	if (fragment != null && fragment.getActivity() instanceof LoginActivity) {
	    FacebookProfile profile = ((LoginActivity) fragment.getActivity()).getFacebookProfile();
	    return profile;
	}
	return null;

    }

    /**
     * Save various states upon successful registration
     * 
     * @param registerBean
     */
    private void onSuccessfulRegistration(RegisterBean registerBean) {
	AppPropertiesUtil.setUserID(fragment.getActivity(), registerBean.getUserID());
	// Set Profile Creation completed status
	AppPropertiesUtil.setUserProfileComplete(fragment.getActivity(), true);
	// Set the flag that says app is already launched
	AppPropertiesUtil.setAppLaunchedBeforeToTrue(fragment.getActivity());

	FacebookProfile profile = getFacebookProfile();
	// Set Profile picture
	if (profile.getProfileBitmap() != null) {
	    ImageUtil.saveProfileImage(fragment.getActivity(), profile.getProfileBitmap());
	}

	SettingsDAO settingsDAO = null;
	if (fragment != null) {
	    if (settingsDAO == null) {
		settingsDAO = new SettingsDAO(fragment.getActivity());
	    }

	    if (profile.getEmail() != null) {
		settingsDAO.insertOrReplace(Constants.KEY_USER_PROFILE_EMAIL, profile.getEmail());
	    }
	    if (profile.getGender() != null) {
		settingsDAO.insertOrReplace(Constants.KEY_USER_PROFILE_GENDER, profile.getGender().name());
	    }

	}

    }

    /**
     * Update the states if registration fails
     */
    private void onFailedRegistration() {
	AppPropertiesUtil.setSelectedUniqueHandle(fragment.getActivity(), null);
	AppPropertiesUtil.setSelectedUniqueHandlePassword(fragment.getActivity(), null);
	AppPropertiesUtil.setUniqueHandleCreatedStatus(fragment.getActivity(), false);
    }

}
