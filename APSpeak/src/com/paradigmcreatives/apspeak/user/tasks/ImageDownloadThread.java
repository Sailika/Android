package com.paradigmcreatives.apspeak.user.tasks;

import android.content.Context;
import android.graphics.Bitmap;

import com.paradigmcreatives.apspeak.app.util.Util;
import com.paradigmcreatives.apspeak.logging.Logger;
import com.paradigmcreatives.apspeak.user.handlers.ImageDownloadHandler;

public class ImageDownloadThread extends Thread {

	private static final String TAG = "ImageDownloadThread";

	private Context context;
	private String imageURL;
	private ImageDownloadHandler handler;

	public ImageDownloadThread(Context context, String url, ImageDownloadHandler handler) {
		super();
		this.context = context;
		this.imageURL = url;
		this.handler = handler;
	}

	private void willStartTask() {
		if (handler != null) {
			handler.willStartTask();
		} else {
			Logger.warn(TAG, "Handler is null");
		}
	}

	private void failed() {
		if (handler != null) {
			handler.failed();
		} else {
			Logger.warn(TAG, "Handler is null");
		}
	}

	private void didUpdate(Bitmap bmp) {

		if (handler != null) {
			handler.didUpdate(bmp, imageURL);
		} else {
			Logger.warn(TAG, "Handler is null");
		}
	}

	@Override
	public void run() {
		willStartTask();
		Bitmap picture = Util.getBitmap(imageURL, context);
		if (picture != null) {
			didUpdate(picture);
		} else {
			failed();
		}
		super.run();
	}
}
