package com.paradigmcreatives.apspeak.registration.handlers;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import com.paradigmcreatives.apspeak.app.util.facebook.OpenGraphRequestUtil.OpenGraphRequestCallback;
import com.paradigmcreatives.apspeak.registration.LoginActivity;
import com.paradigmcreatives.apspeak.registration.fragments.FacebookConnectAnimationFragment;
import com.paradigmcreatives.apspeak.registration.fragments.FacebookConnectFragment;

public class SigninHandler extends Handler {
	private static final String TAG = "SigninHandler";
	private static final int CREATE_PROFILE = 1;
	private static final int SHOW_FRIENDS_NETWORK = 2;
	private static final int SHOW_NEW_HOME_ACTIVITY = 3;
	private static final int ERROR = 4;

	private OpenGraphRequestCallback fragment = null;

	public SigninHandler(OpenGraphRequestCallback fragment) {
		this.fragment = fragment;
	}

	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		if (fragment.getProgressDialog() != null
				&& fragment.getProgressDialog().isShowing()) {
			fragment.getProgressDialog().dismiss();
		}

		if (msg != null && fragment != null) {
			switch (msg.what) {
			case CREATE_PROFILE:
				// fragment.moveToFragment(Constants.HANDLE_SELECTION_TAG,
				// false, true);
				if (fragment instanceof FacebookConnectFragment) {
					((FacebookConnectFragment) fragment)
							.makeProfileCreateRequest();
				} else if (fragment instanceof FacebookConnectAnimationFragment) {
					((FacebookConnectAnimationFragment) fragment)
							.makeProfileCreateRequest();
				}
				break;
			case SHOW_FRIENDS_NETWORK:
				// fragment.moveToFragment(Constants.FRIENDS_NETWORK_FRAGMENT_TAG,
				// true, false);
				break;
			case SHOW_NEW_HOME_ACTIVITY:
				Activity activity = null;
				if (fragment instanceof FacebookConnectFragment) {
					activity = ((FacebookConnectFragment) fragment)
							.getActivity();
				} else if (fragment instanceof FacebookConnectAnimationFragment) {
					activity = ((FacebookConnectAnimationFragment) fragment)
							.getActivity();
				}
				if (activity != null && activity instanceof LoginActivity) {
					((LoginActivity) activity).launchAppNewHomeActivity();
				}
				break;
			case ERROR:
				fragment.showError(
						"Could not perform this operation. Please try again",
						msg.arg1);
				break;
			}
		}

	}

	public void requestProfileCreation() {
		Message msg = new Message();
		msg.what = CREATE_PROFILE;
		sendMessage(msg);
	}

	public void launchNewHomeActivity() {
		Message msg = new Message();
		msg.what = SHOW_NEW_HOME_ACTIVITY;
		sendMessage(msg);
	}

	public void showFriendsNetwork() {
		Message msg = new Message();
		msg.what = SHOW_FRIENDS_NETWORK;
		sendMessage(msg);
	}

	public void showError(int errorCode) {
		Message msg = new Message();
		msg.what = ERROR;
		msg.arg1 = errorCode;
		sendMessage(msg);
	}
}
