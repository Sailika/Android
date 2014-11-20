package com.paradigmcreatives.apspeak.settings.listeners;

import java.io.File;

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
import com.google.android.gcm.GCMRegistrar;
import com.paradigmcreatives.apspeak.R;
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
				showLogoutDialog();
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
	private void performLogout() {
		// Unregister from GCM
		GCMRegistrar.unregister(fragment.getActivity().getApplicationContext());
		// Close and clear Facebook Session
		callFacebookLogout(fragment.getActivity());
		// Clear files from SD card which includes app Database
		cleanDevice();
	}

	/**
	 * Cleans the device and db
	 */
	private void cleanDevice() {
		if (fragment != null) {
			Util.deleteFile(new File(AppPropertiesUtil.getAppDirectory(fragment
					.getActivity())));

			if ((new WhatSayDBHandler(fragment.getActivity()))
					.deleteDB(fragment.getActivity())) {
				Logger.info(TAG, "Deleted entire Whatsay data on mobile");
			} else {
				Logger.warn(TAG, "Failed to delete Whatsay data");
			}

			// Clear shared preferences file
			SharedPreferences prefs = fragment.getActivity()
					.getSharedPreferences(Constants.KEY_APP_PREFERENCE_NAME,
							Context.MODE_PRIVATE);
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
	private void launchLoginScreen() {
		if (fragment != null) {
			// Launch LoginActivity
			Intent loginActivity = new Intent(fragment.getActivity(),
					LoginActivity.class);
			loginActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_CLEAR_TASK);
			fragment.startActivity(loginActivity);
			fragment.getActivity().finish();
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

	private void showLogoutDialog() {
		LayoutInflater inflater = (LayoutInflater) fragment.getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View eulaDialog = inflater.inflate(R.layout.logout_dialog, null);
		final Dialog dialog = new Dialog(fragment.getActivity(),
				android.R.style.Theme_Translucent_NoTitleBar);
		Button yesButton = (Button) eulaDialog.findViewById(R.id.yes_button);
		Button noButton = (Button) eulaDialog.findViewById(R.id.no_button);
		noButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				dialog.dismiss();

			}

		});

		yesButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				dialog.dismiss();
				performLogout();
				launchLoginScreen();

			}

		});
		dialog.setContentView(eulaDialog);
		dialog.show();

	}
}
