package com.paradigmcreatives.apspeak.assets.handlers;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;

import com.paradigmcreatives.apspeak.globalstream.fragments.GlobalStreamsFragment;
import com.paradigmcreatives.apspeak.logging.Logger;
import com.paradigmcreatives.apspeak.stream.fragments.AssetDetailsWithCommentsFragment;
import com.paradigmcreatives.apspeak.stream.fragments.UserStreamFragment;

public class InappropriateFlagHandler extends Handler {
	private static final String TAG = "InappropriateFlagHandler";

	private static final int PRE_EXECUTE = 1;
	private static final int SUCCESS = 2;
	private static final int FAILURE = 3;

	private Fragment fragment;

	public InappropriateFlagHandler(Fragment fragment) {
		super();
		this.fragment = fragment;
	}

	public void willStartTask() {
		sendEmptyMessage(PRE_EXECUTE);
	}

	public void success() {
		Message msg = new Message();
		msg.what = SUCCESS;

		sendMessage(msg);
	}

	public void failed(int statusCode, int errorCode) {
		Message msg = new Message();
		msg.what = FAILURE;
		msg.arg1 = statusCode;
		msg.arg2 = errorCode;

		sendMessage(msg);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.Handler#handleMessage(android.os.Message)
	 */
	@Override
	public void handleMessage(Message msg) {
		if (fragment != null) {
			switch (msg.what) {
			case PRE_EXECUTE:
				// do nothing
				break;

			case SUCCESS:
				if (fragment != null) {
					if (fragment instanceof UserStreamFragment) {
						( (UserStreamFragment)fragment).inappropriateSuccess();
					}else if (fragment instanceof AssetDetailsWithCommentsFragment) {
						((AssetDetailsWithCommentsFragment)fragment).inappropriateSuccess();
					}else if(fragment instanceof GlobalStreamsFragment){
						((GlobalStreamsFragment)fragment).inappropriateSuccess();
					}
				}
				break;

			case FAILURE:
				if (fragment != null) {
					if (fragment != null) {
						if (fragment instanceof UserStreamFragment) {
							( (UserStreamFragment)fragment).inappropriateFailure();
						}else if (fragment instanceof AssetDetailsWithCommentsFragment) {
							((AssetDetailsWithCommentsFragment)fragment).inappropriateFailure();
						}else if(fragment instanceof GlobalStreamsFragment){
							((GlobalStreamsFragment)fragment).inappropriateFailure();
						}
					}
				}
				break;
			default:
				break;
			}
		} else {
			Logger.warn(TAG, "Context is null");
		}
		super.handleMessage(msg);
	}

}
