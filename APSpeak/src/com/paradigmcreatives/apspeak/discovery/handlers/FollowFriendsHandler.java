package com.paradigmcreatives.apspeak.discovery.handlers;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;

import com.paradigmcreatives.apspeak.discovery.fragments.UserNetworkFragment;

/**
 * 
 * @author robin
 * 
 */

public class FollowFriendsHandler extends Handler {

	private static final int STARTING = 0;
	private static final int SUCCESS = 1;
	private static final int ERROR = 2;
	private UserNetworkFragment fragment = null;
	private ProgressDialog progressDialog = null;

	/**
	 * @param fragment
	 */
	public FollowFriendsHandler(UserNetworkFragment fragment) {
		this.fragment = fragment;
		/*
		if (fragment != null) {
			progressDialog = new ProgressDialog(fragment.getActivity(), ProgressDialog.THEME_HOLO_LIGHT);
			progressDialog.setTitle("Just a moment");
			progressDialog.setMessage("Attempting to follow your friends");
		}
		*/
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.Handler#handleMessage(android.os.Message)
	 */
	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		if (msg != null && fragment != null) {
			switch (msg.what) {
			case STARTING:
			    /*
				if (progressDialog != null) {
					progressDialog.show();
				}
				*/
				break;
			case SUCCESS:
				//dismissProgressDialog();
				// Response to FOLLOW ALL request
				fragment.processSuccessfulFollowAll();
				break;
			case ERROR:
				//dismissProgressDialog();
				fragment.processFailureFollowAll();
				fragment.showError((String) msg.obj);
				break;
			}
		}
	}

	public void onStart() {
		sendEmptyMessage(STARTING);
	}

	public void onFollowAllSuccess() {
		sendEmptyMessage(SUCCESS);
	}

	public void onError(String error) {
		Message msg = new Message();
		msg.what = ERROR;
		msg.obj = error;
		sendMessage(msg);
	}

	private void dismissProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}

}
