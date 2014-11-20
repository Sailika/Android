package com.paradigmcreatives.apspeak.app.util.facebook;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionDefaultAudience;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.paradigmcreatives.apspeak.app.model.FacebookProfile;
import com.paradigmcreatives.apspeak.app.model.UserProfile.GENDER;
import com.paradigmcreatives.apspeak.app.util.Util;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.app.util.images.ImageUtil;
import com.paradigmcreatives.apspeak.logging.Logger;

/**
 * Utility class for making Open Graph requests from Facebook
 * 
 * @author robin
 * 
 */
public class OpenGraphRequestUtil {

	private static final String TAG = "OpenGraphRequestUtil";

	private Session session = null;
	private Fragment fragment = null;

	/**
	 * Constructor for OpenGraphUtil
	 * 
	 * @param session
	 *            A valid Facebook session
	 * @param activity
	 *            Reference of InitialUserSetupActivity
	 * @param tag
	 *            Tag of the fragment to open after the process if completed
	 */
	public OpenGraphRequestUtil(Fragment fragment) {
		if (fragment != null && fragment instanceof OpenGraphRequestCallback) {
			this.session = ((OpenGraphRequestCallback) fragment).getSession();
		}
		this.fragment = (Fragment) fragment;
	}

	/**
	 * Runs the query for getting the basic Facebook profile of the user
	 * 
	 * @return
	 */
	public void fetchBasicProfile() {

		if (session != null && fragment != null
				&& fragment instanceof OpenGraphRequestCallback) {
			if (((OpenGraphRequestCallback) fragment).getProgressDialog() != null
					&& !((OpenGraphRequestCallback) fragment)
							.getProgressDialog().isShowing()) {
				((OpenGraphRequestCallback) fragment).getProgressDialog()
						.show();
			}

			// Make an API call to get user data and define a
			// new callback to handle the response.
			Request request = Request.newMeRequest(session,
					new Request.GraphUserCallback() {
						@Override
						public void onCompleted(GraphUser user,
								Response response) {
							// If the response is successful
							if (session == Session.getActiveSession()) {
								if (user != null) {

									// TODO: Set Loading Time --- For Google
									// Analytics
									/*
									 * if (fragment != null && fragment
									 * instanceof HomeScreenFragment) {
									 * ((HomeScreenFragment)
									 * fragment).sendLodingTime(); } else if
									 * (fragment != null && fragment instanceof
									 * LoginFragment) { ((LoginFragment)
									 * fragment).sendLodingTime(); }
									 */

									// 1 - Get the basic bio
									FacebookProfile profile = buildProfile(
											session, user);

									// 2 - Get the profile image
									fetchProfilePictureURL(profile);
								}
							}
							if (response.getError() != null) {
								// Handle errors, will do so later.
								((OpenGraphRequestCallback) fragment)
										.showError("Could not perform this operation. Please try again", -1);
							}
						}
					});
			request.executeAsync();
		} else {
			Logger.warn(TAG, "Empty session sent for getting the basic profile");
		}
	}

	private static FacebookProfile buildProfile(Session session, GraphUser user) {
		FacebookProfile profile = new FacebookProfile();
		if (user != null && session != null && profile != null) {
			profile.setBirthday(user.getBirthday());
			profile.setFirstName(user.getFirstName());
			profile.setLastName(user.getLastName());
			profile.setFacebookUserId(user.getId());
			profile.setFacebookAccessToken(session.getAccessToken());
			profile.setUserName(user.getUsername());

			String gender = (String) user.getProperty("gender");
			if (!TextUtils.isEmpty(gender)) {
				if (gender.equalsIgnoreCase("male")) {
					profile.setGender(GENDER.MALE);
				} else if (gender.equalsIgnoreCase("female")) {
					profile.setGender(GENDER.FEMALE);
				}
			}
			Logger.info(TAG, profile + "");

		}
		return profile;
	}

	/**
	 * The FacebookProfile data collected so far
	 * 
	 * @param profile
	 */
	private void fetchProfilePictureURL(final FacebookProfile profile) {
		Bundle bundle = new Bundle();
		bundle.putString("redirect", "false");
		bundle.putString("type", "square");
		bundle.putString("height", "200");
		bundle.putString("width", "200");

		Request request = new Request(session, "me/picture", bundle, null,
				new Request.Callback() {

					@Override
					public void onCompleted(Response response) {
						if (response.getError() != null) {
							// TODO Handle error here
						} else {
							GraphObject graphObject = response.getGraphObject();
							if (graphObject != null) {
								GraphObject data = graphObject.getPropertyAs(
										"data", GraphObject.class);
								if (data != null) {
									boolean isSilhouette = (Boolean) data
											.getProperty("is_silhouette");
									if (!isSilhouette) {
										String url = (String) data
												.getProperty("url");
										profile.setProfilePicUrl(url);

										// Start the thread for getting the
										// profile
										// picture
										new Thread(new ImageDownloader(profile))
												.start();

										// TODO Get the cover image

									} else {
										notifyFetchComplete(profile);
									}
								}
							}
						}
					}
				});
		request.executeAsync();

	}

	private void notifyFetchComplete(FacebookProfile profile) {

		if (fragment != null && fragment instanceof OpenGraphRequestCallback) {
			((OpenGraphRequestCallback) fragment)
					.onBasicFacebookProfileFetchComplete(profile);
		}

	}

	/**
	 * Listener for <code>OpenGraphRequest</code>
	 * 
	 * @author robin
	 * 
	 */
	public interface OpenGraphRequestCallback {
		public void onBasicFacebookProfileFetchComplete(FacebookProfile profile);

		public ProgressDialog getProgressDialog();

		public void showError(String error, int errorCode);

		public void moveToFragment(String tag, boolean resetStack,
				boolean addToBackStack);

		public Session getSession();

		public void sendLodingTime();

	}

    class ImageDownloader implements Runnable {
	private FacebookProfile profile = null;

	ImageDownloader(FacebookProfile profile) {
	    this.profile = profile;
	}

	@Override
	public void run() {
	    Bitmap picture = Util.getBitmapFromURL(profile.getProfilePictureUrl());
	    if (picture != null) {
		if (fragment != null) {
		    if (fragment instanceof Fragment) {
			try {
			    Activity activity = ((Fragment) fragment).getActivity();
			    if (activity != null) {
				try{
					int radius = ImageUtil.getBubbleRadius(activity.getApplicationContext());
					picture = ImageUtil.getCircularBitmapResizeTo(activity.getApplicationContext(),
						picture, radius, radius);
					profile.setProfileBitmap(picture);
				}catch(Exception e){
				    
				}
			    }
			} catch (Exception e) {

			}
		    }
		}
	    }
	    notifyFetchComplete(profile);
	}

    }

	/**
	 * Requests other permissions related to publish
	 */
	public void requestNewPublishPermissions() {
		Session session = Session.getActiveSession();

		List<String> permissions = session.getPermissions();
		if (!permissions.contains("publish_actions")
				&& fragment instanceof Fragment) {
			Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(
					((Fragment) fragment).getActivity(),
					Arrays.asList("publish_actions"))
					.setDefaultAudience(SessionDefaultAudience.FRIENDS);
			session.requestNewPublishPermissions(newPermissionsRequest);
		}
	}

	/**
	 * Invites Facebook Friends
	 */
	public void inviteFBFriends() {
		if (fragment == null || !(fragment instanceof Fragment)) {
			return;
		}
		Bundle params = new Bundle();
		params.putString("type", "facebook_whatsay:whatsay");
		params.putString("url", "http://samples.ogp.me/1447942302118538");
		params.putString("title", "Whatsay Request");
		params.putString("description", "");
		params.putString("app_id", Constants.FACEBOOK_APPID);

		params.putString(
				"message",
				"has invited you to try out "
						+ "Whatsay, a unique way to express your emotions.  Download now on your device!");

		WebDialog requestsDialog = (new WebDialog.RequestsDialogBuilder(
				((Fragment) fragment).getActivity(),
				Session.getActiveSession(), params)).setOnCompleteListener(
				new OnCompleteListener() {
					@Override
					public void onComplete(Bundle values,
							FacebookException error) {
						if (error != null) {
							if (error instanceof FacebookOperationCanceledException) {
								Toast.makeText(
										((Fragment) fragment).getActivity()
												.getApplicationContext(),
										"Request cancelled", Toast.LENGTH_SHORT)
										.show();
							} else {
								Toast.makeText(
										((Fragment) fragment).getActivity()
												.getApplicationContext(),
										"Network Error", Toast.LENGTH_SHORT)
										.show();
							}
						} else {
							final String requestId = values
									.getString("request");
							if (requestId != null) {
								Toast.makeText(
										((Fragment) fragment).getActivity()
												.getApplicationContext(),
										"Request sent", Toast.LENGTH_SHORT)
										.show();
							} else {
								Toast.makeText(
										((Fragment) fragment).getActivity()
												.getApplicationContext(),
										"Request cancelled", Toast.LENGTH_SHORT)
										.show();
							}
						}
					}
				}).build();
		requestsDialog.show();
	}

}
