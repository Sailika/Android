package com.paradigmcreatives.apspeak.user.handlers;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.logging.Logger;

/**
 * Handles the messaging during image download
 * 
 * @author Dileep | neuv
 * 
 */
public class ImageDownloadHandler extends Handler {
	private static final String TAG = "ImageDownloadHandler";

	private static final int PRE_EXECUTE = 1;
	private static final int SUCCESS = 2;
	private static final int FAILURE = 3;

	private Handler handler;

	/**
	 * Constructor that accepts Handler
	 * 
	 * @param handler
	 */
	public ImageDownloadHandler(Handler handler) {
		super();
		this.handler = handler;
	}

	public void willStartTask() {
		sendEmptyMessage(PRE_EXECUTE);
	}

	public void didUpdate(Bitmap bmp, String imageUrl) {
		Message msg = new Message();
		msg.what = SUCCESS;
		if (bmp != null) {
			Bundle bmpData = new Bundle();
			bmpData.putString(Constants.PROFILE_PICTURE_URL, imageUrl);
			bmpData.putParcelable(Constants.FACEBOOK_PROFILE_PICTURE, bmp);
			msg.setData(bmpData);
		}
		sendMessage(msg);
	}

	public void failed() {
		Message msg = new Message();
		msg.what = FAILURE;
		sendMessage(msg);
	}

	@Override
	public void handleMessage(Message msg) {
		if (msg.what == PRE_EXECUTE) {
		} else if (msg.what == SUCCESS) {
			Logger.info(TAG, "partial registration is done");
			Bundle bmpData = msg.getData();
			if (bmpData != null && bmpData.containsKey(Constants.FACEBOOK_PROFILE_PICTURE)) {
				Bitmap bmp = bmpData.getParcelable(Constants.FACEBOOK_PROFILE_PICTURE);
				if (handler != null) {
					if (handler instanceof GetFriendCompactProfileHandler) {
						String imageURL = null;
						if (bmpData.containsKey(Constants.PROFILE_PICTURE_URL)) {
							imageURL = bmpData.getString(Constants.PROFILE_PICTURE_URL);
						}
						((GetFriendCompactProfileHandler) handler).setFriendBitmap(bmp, imageURL);
					}
				}
			}
		} else if (msg.what == FAILURE) {
			Logger.fatal(TAG, "partial registration not done");
			if (handler != null) {
				if (handler instanceof GetFriendCompactProfileHandler) {
					((GetFriendCompactProfileHandler) handler).friendBitmapFailed();
				}
			}
		}
		super.handleMessage(msg);
	}
}
