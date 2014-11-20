package com.paradigmcreatives.apspeak.user.handlers;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;

import com.paradigmcreatives.apspeak.app.model.User;
import com.paradigmcreatives.apspeak.user.fragments.ProfileFragment;

public class UserProfileHandler extends Handler {
	private static final int STARTING = 0;
	private static final int SUCCESS = 1;
	private static final int ERROR = 2;

	private ProgressDialog progressDialog = null;
	private ProfileFragment fragment = null;
	private boolean showProgressDialog = true;

	public UserProfileHandler(ProfileFragment fragment, boolean showProgressDialog) {
		this.fragment = fragment;
		this.showProgressDialog = showProgressDialog;
		if (fragment != null && showProgressDialog) {
			progressDialog = new ProgressDialog(fragment.getActivity(), ProgressDialog.THEME_HOLO_LIGHT);
			progressDialog.setTitle("Just a moment!!");
			progressDialog.setMessage("Please wait...");
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.Handler#handleMessage(android.os.Message)
	 */
	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		if (msg != null) {
			switch (msg.what) {
			case STARTING:
				if (progressDialog != null) {
					progressDialog.show();
				}
				break;
			case SUCCESS:
				fragment.onSuccessfulProfileFetch((User) msg.obj);
				dismissProgressDialog();
				break;
			case ERROR:
				fragment.onError((String) msg.obj);
				dismissProgressDialog();
				break;
			}
		}
	}

	private void dismissProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}

	public void onStarting() {
		sendEmptyMessage(STARTING);
	}

	public void onSuccess(User user) {
		Message msg = new Message();
		msg.what = SUCCESS;
		msg.obj = user;
		sendMessage(msg);
	}

	public void onError(String error) {
		Message msg = new Message();
		msg.what = ERROR;
		msg.obj = error;
		sendMessage(msg);
	}

}
