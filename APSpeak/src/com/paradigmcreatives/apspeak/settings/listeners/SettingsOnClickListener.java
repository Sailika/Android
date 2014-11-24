package com.paradigmcreatives.apspeak.settings.listeners;

import java.io.File;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.facebook.Session;
import com.facebook.android.Facebook;
import com.google.android.gcm.GCMRegistrar;
import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.ShutDownListener;
import com.paradigmcreatives.apspeak.app.analytics.GoogleAnalyticsHelper;
import com.paradigmcreatives.apspeak.app.database.WhatSayDBHandler;
import com.paradigmcreatives.apspeak.app.util.AppPropertiesUtil;
import com.paradigmcreatives.apspeak.app.util.Util;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.app.util.constants.GoogleAnalyticsConstants;
import com.paradigmcreatives.apspeak.discovery.fragments.SettingsFragment;
import com.paradigmcreatives.apspeak.logging.Logger;
import com.paradigmcreatives.apspeak.registration.LoginActivity;
import com.paradigmcreatives.apspeak.settings.AboutUsActivity;
import com.paradigmcreatives.apspeak.settings.FeedbackActivity;
import com.paradigmcreatives.apspeak.settings.HelpActivity;
import com.paradigmcreatives.apspeak.settings.PrivacyPolicyActivity;

public class SettingsOnClickListener implements OnClickListener {

	private static final String TAG = "HandleSelectionClickListener";
	private SettingsFragment fragment;

	public SettingsOnClickListener(SettingsFragment fragment) {
		this.fragment = fragment;
	}

	@Override
	public void onClick(View v) {
		if (fragment != null) {
			switch (v.getId()) {
			case R.id.about_us_text:
				GoogleAnalyticsHelper.sendEventToGA(fragment.getActivity(),
						GoogleAnalyticsConstants.SETTINGS_SCREEN,
						GoogleAnalyticsConstants.ACTION_BUTTON,
						GoogleAnalyticsConstants.SETTINGS_ABOUT_US_BUTTON);
				aboutUsAction();
				break;
			case R.id.help_text:
				GoogleAnalyticsHelper.sendEventToGA(fragment.getActivity(),
						GoogleAnalyticsConstants.SETTINGS_SCREEN,
						GoogleAnalyticsConstants.ACTION_BUTTON,
						GoogleAnalyticsConstants.SETTINGS_HELP_BUTTON);
				helpAction();
				break;
			case R.id.privacy_policy_text:
				GoogleAnalyticsHelper
						.sendEventToGA(
								fragment.getActivity(),
								GoogleAnalyticsConstants.SETTINGS_SCREEN,
								GoogleAnalyticsConstants.ACTION_BUTTON,
								GoogleAnalyticsConstants.SETTINGS_PRIVACY_POLICY_BUTTON);
				privacyPolicyAction();
				break;
			case R.id.feedback_text:
				feedbackAction();
				break;
			case R.id.app_logout:
				GoogleAnalyticsHelper.sendEventToGA(fragment.getActivity(),
						GoogleAnalyticsConstants.SETTINGS_SCREEN,
						GoogleAnalyticsConstants.ACTION_BUTTON,
						GoogleAnalyticsConstants.SETTINGS_LOGOUT_BUTTON);
				//showLogoutDialog();
				break;
			case R.id.change_college_text:
				if (fragment != null) {
					fragment.fetchGroupsListFromServer();
				}
				break;
			}
		}
	}

	/**
	 * Performs logout from the application. Clears Facebook session, deletes
	 * app data which includes database, sd card files, shared preference file
	 */
	public static void performLogout(Context context) {
		// Unregister from GCM
		GCMRegistrar.unregister(context);
		// Close and clear Facebook Session
		callFacebookLogout(context);
		// Clear files from SD card which includes app Database
		cleanDevice(context);
	}

	/**
	 * Cleans the device and db
	 */
	private static void cleanDevice(Context context) {
		if (context != null) {
			Util.deleteFile(new File(AppPropertiesUtil.getAppDirectory(context)));

			if ((new WhatSayDBHandler(context)).deleteDB(context)) {
				Logger.info(TAG, "Deleted entire Whatsay data on mobile");
			} else {
				Logger.warn(TAG, "Failed to delete Whatsay data");
			}

			// Clear shared preferences file
			SharedPreferences prefs = context.getSharedPreferences(
					Constants.KEY_APP_PREFERENCE_NAME, Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = prefs.edit();
			editor.clear();
			editor.commit();
		}
	}

	/**
	 * Logout From Facebook
	 */
	public static void callFacebookLogout(Context context) {
		Session session = Session.getActiveSession();
		if (session != null) {
			if (!session.isClosed()) {
				session.closeAndClearTokenInformation();
				// clear your preferences if saved
			}
		} else {
			session = new Session(context);
			Session.setActiveSession(session);
			session.closeAndClearTokenInformation();
			// clear your preferences if saved
		}
	}

	/**
	 * Launches Login Activity and kills the current activity
	 */
	private static void launchLoginScreen(Context context) {
		if (context != null) {
			// Launch LoginActivity
			Intent loginActivity = new Intent(context, LoginActivity.class);
			loginActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_CLEAR_TASK);
			context.startActivity(loginActivity);
			if (context instanceof Activity) {
				((Activity) context).finish();
			}
		}
	}

	private void aboutUsAction() {

		try {

			Intent aboutUsIntent = new Intent(fragment.getActivity(),
					AboutUsActivity.class);
			fragment.getActivity().startActivityForResult(aboutUsIntent,
					Constants.SETTINGS_OPTIONS_REQUESTCODE);

		} catch (ActivityNotFoundException anfe) {
			Logger.warn(TAG,
					"Activity not found : " + anfe.getLocalizedMessage());
		} catch (Exception e) {
			Logger.warn(TAG, "Unknown Exception : " + e.getLocalizedMessage());
		}

	}

	private void helpAction() {

		try {
			Intent helpIntent = new Intent(fragment.getActivity(),
					HelpActivity.class);
			fragment.getActivity().startActivityForResult(helpIntent,
					Constants.SETTINGS_OPTIONS_REQUESTCODE);
		} catch (ActivityNotFoundException anfe) {
			Logger.warn(TAG,
					"Activity not found : " + anfe.getLocalizedMessage());
		} catch (Exception e) {
			Logger.warn(TAG, "Unknown Exception : " + e.getLocalizedMessage());
		}

	}

	private void privacyPolicyAction() {

		try {
			Intent policyIntent = new Intent(fragment.getActivity(),
					PrivacyPolicyActivity.class);
			fragment.getActivity().startActivityForResult(policyIntent,
					Constants.SETTINGS_OPTIONS_REQUESTCODE);
		} catch (ActivityNotFoundException anfe) {
			Logger.warn(TAG,
					"Activity not found : " + anfe.getLocalizedMessage());
		} catch (Exception e) {
			Logger.warn(TAG, "Unknown Exception : " + e.getLocalizedMessage());
		}

	}

	private void feedbackAction() {

		try {
			Intent feedbackIntent = new Intent(fragment.getActivity(),
					FeedbackActivity.class);
			fragment.getActivity().startActivityForResult(feedbackIntent,
					Constants.SETTINGS_OPTIONS_REQUESTCODE);
		} catch (ActivityNotFoundException anfe) {
			Logger.warn(TAG,
					"Activity not found : " + anfe.getLocalizedMessage());
		} catch (Exception e) {
			Logger.warn(TAG, "Unknown Exception : " + e.getLocalizedMessage());
		}

	}

	public static void showLogoutDialog(final Context context,final ShutDownListener listener) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View eulaDialog = inflater.inflate(R.layout.logout_dialog, null);
		final Dialog dialog = new Dialog(context,
				android.R.style.Theme_Translucent_NoTitleBar);
		Button yesButton = (Button) eulaDialog.findViewById(R.id.yes_button);
		Button noButton = (Button) eulaDialog.findViewById(R.id.no_button);
		noButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				dialog.dismiss();
				listener.completed();

			}

		});

		yesButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.completed();
				dialog.dismiss();
				performLogout(context);
				launchLoginScreen(context);

			}

		});
		dialog.setContentView(eulaDialog);
		dialog.show();

	}
}
