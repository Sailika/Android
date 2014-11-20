package com.paradigmcreatives.apspeak.assets.handlers;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.paradigmcreatives.apspeak.app.util.constants.JSONConstants;
import com.paradigmcreatives.apspeak.globalstream.fragments.GlobalStreamsFragment;
import com.paradigmcreatives.apspeak.logging.Logger;
import com.paradigmcreatives.apspeak.stream.fragments.AssetDetailsWithCommentsFragment;
import com.paradigmcreatives.apspeak.stream.fragments.UserStreamFragment;

public class AssetDeleteHandler extends Handler {
	private static final String TAG = "AssetDeleteHandler";

	private static final int PRE_EXECUTE = 1;
	private static final int SUCCESS = 2;
	private static final int FAILURE = 3;

	public static final String LIKE_VIEW = "like_view";

	private Fragment fragment;

	public AssetDeleteHandler(Fragment fragment) {
		super();
		this.fragment = fragment;
	}

	public void willStartTask() {
		sendEmptyMessage(PRE_EXECUTE);
	}

	public void success(String assetId) {
		Message msg = new Message();
		msg.what = SUCCESS;
		if (!TextUtils.isEmpty(assetId)) {
			Bundle data = new Bundle();
			data.putString(JSONConstants.ASSET_ID, assetId);
			msg.setData(data);
		}
		sendMessage(msg);
	}

	public void failed(int statusCode, int errorCode, String assetId) {
		Message msg = new Message();
		msg.what = FAILURE;
		msg.arg1 = statusCode;
		msg.arg2 = errorCode;
		if (!TextUtils.isEmpty(assetId)) {
			Bundle data = new Bundle();
			data.putString(JSONConstants.ASSET_ID, assetId);
			msg.setData(data);
		}
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
					Bundle data = msg.getData();
					if (fragment != null) {
						if (fragment instanceof UserStreamFragment) {
							((UserStreamFragment) fragment)
									.assetDeleteSuccess(data
											.getString(JSONConstants.ASSET_ID));
						} else if (fragment instanceof AssetDetailsWithCommentsFragment) {
							((AssetDetailsWithCommentsFragment) fragment)
									.assetDeleteSuccess(data
											.getString(JSONConstants.ASSET_ID));
						} else if (fragment instanceof GlobalStreamsFragment) {
							((GlobalStreamsFragment) fragment)
									.assetDeleteSuccess(data
											.getString(JSONConstants.ASSET_ID));
						}
					}
				}
				break;

			case FAILURE:
				if (fragment != null) {
					Bundle data = msg.getData();
					if (fragment != null) {
						if (fragment instanceof UserStreamFragment) {
							((UserStreamFragment) fragment)
									.assetDeleteFailed(data
											.getString(JSONConstants.ASSET_ID));
						} else if (fragment instanceof AssetDetailsWithCommentsFragment) {
							((AssetDetailsWithCommentsFragment) fragment)
									.assetDeleteFailed(data
											.getString(JSONConstants.ASSET_ID));
						} else if (fragment instanceof GlobalStreamsFragment) {
							((GlobalStreamsFragment) fragment)
									.assetDeleteFailed(data
											.getString(JSONConstants.ASSET_ID));
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
