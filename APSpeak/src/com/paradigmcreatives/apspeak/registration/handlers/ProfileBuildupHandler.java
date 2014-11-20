package com.paradigmcreatives.apspeak.registration.handlers;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;

import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.registration.fragments.FacebookConnectAnimationFragment;
import com.paradigmcreatives.apspeak.registration.fragments.FacebookConnectFragment;

/**
 * 
 * @author robin
 * 
 */
public class ProfileBuildupHandler extends Handler {

	private static final int STARTING = 0;
	private static final int SUCCESS = 1;
	private static final int ERROR = 2;

	private Fragment fragment = null;
	private ProgressDialog progressDialog = null;

	public ProfileBuildupHandler(Fragment fragment) {
		this.fragment = fragment;
		if (fragment != null) {
			progressDialog = new ProgressDialog(fragment.getActivity(),
					ProgressDialog.THEME_HOLO_LIGHT);
			progressDialog.setTitle("Just a moment");
			progressDialog.setMessage("Building your profile");
		}

	}

	/**
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
				if (progressDialog != null) {
					progressDialog.show();
				}
				break;

			case SUCCESS:
				dismissProgressDialog();
				if (fragment instanceof FacebookConnectFragment) {
					((FacebookConnectFragment) fragment)
							.processSuccessfulRegistration((String) msg.obj);
				} else if (fragment instanceof FacebookConnectAnimationFragment) {
					((FacebookConnectAnimationFragment) fragment)
							.processSuccessfulRegistration((String) msg.obj);
				}

				break;
			case ERROR:
				if (fragment instanceof FacebookConnectFragment) {
					((FacebookConnectFragment) fragment)
							.showErrorRegistrationDialog(R.string.NoNetworkmsg,
									msg.arg1);
				} else if (fragment instanceof FacebookConnectAnimationFragment) {
					((FacebookConnectAnimationFragment) fragment)
							.showErrorRegistrationDialog(R.string.NoNetworkmsg,
									msg.arg1);
				}
				dismissProgressDialog();
				break;
			}
		}
	}

	public void onStart() {
		sendEmptyMessage(STARTING);
	}

	public void onSuccess(String userID) {
		Message msg = new Message();
		msg.what = SUCCESS;
		msg.obj = userID;
		sendMessage(msg);
	}

	public void onError(String error, int errorCode) {
		Message msg = new Message();
		msg.what = ERROR;
		msg.obj = error;
		msg.arg1 = errorCode;
		sendMessage(msg);
	}

	private void dismissProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}

	}

}
