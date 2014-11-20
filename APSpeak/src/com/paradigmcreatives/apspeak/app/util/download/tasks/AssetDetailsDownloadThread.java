package com.paradigmcreatives.apspeak.app.util.download.tasks;

import android.content.Context;
import android.text.TextUtils;

import com.paradigmcreatives.apspeak.app.model.StreamAsset;
import com.paradigmcreatives.apspeak.app.util.download.handlers.AssetDetailsDownloadHandler;
import com.paradigmcreatives.apspeak.app.util.download.tasks.helpers.AssetDetailsDownloadHelper;
import com.paradigmcreatives.apspeak.logging.Logger;

/**
 * Fetches asset details from server
 * 
 * @author Dileep | neuv
 * 
 */
public class AssetDetailsDownloadThread extends Thread {
	private static final String TAG = "AssetDetailsDownloadThread";

	private Context context;
	private String assetId;
	private AssetDetailsDownloadHandler handler;

	public AssetDetailsDownloadThread(Context context, String assetId, AssetDetailsDownloadHandler handler) {
		super();
		this.assetId = assetId;
		this.context = context;
		this.handler = handler;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		if (context != null) {
			if (!TextUtils.isEmpty(assetId)) {
				willStartTask();

				AssetDetailsDownloadHelper helper = new AssetDetailsDownloadHelper(context);
				StreamAsset asset = helper.fetch(assetId);
				didDownloadComplete(asset);
			} else {
				Logger.warn(TAG, "Doodle Download URl or Doodle Download Path is null");
			}
		} else {
			Logger.warn(TAG, "Context is null");
		}

		super.run();
	}

	private void willStartTask() {
		if (handler != null) {
			handler.willStartTask();
		} else {
			Logger.warn(TAG, "Handler is null");
		}
	}

	private void didDownloadComplete(StreamAsset asset) {
		if (handler != null) {
			handler.didDownloadComplete(asset);
		} else {
			Logger.warn(TAG, "Handler is null");
		}
	}

}
