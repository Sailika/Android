package com.paradigmcreatives.apspeak.doodleboard.tasks;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.app.util.AppPropertiesUtil;
import com.paradigmcreatives.apspeak.app.util.DeviceInfoUtil;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.doodleboard.ImageSelectionFragmentActivity;
import com.paradigmcreatives.apspeak.logging.Logger;

public class BackgroundImageDownloadTask extends
		AsyncTask<String, Void, String> {
	private Activity activity;
	private String TAG = "BackgroundImageDownloadTask";

	public BackgroundImageDownloadTask(Activity activity) {
		this.activity = activity;
	}

	@Override
	protected String doInBackground(String... urlString) {
		try {
			java.net.URL url = new java.net.URL(urlString[0]);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap bitmap = BitmapFactory.decodeStream(input);
			String imagePath = saveImageAndGetPath(bitmap);
			return imagePath;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(String imagePath) {
		if (activity != null) {
			if (activity instanceof ImageSelectionFragmentActivity) {
				((ImageSelectionFragmentActivity) activity).notifyImagePath(
						imagePath, true);
			}

		}

	}

	public String saveImageAndGetPath(Bitmap bitmap) {
		boolean saved = false;
		try {
			if (bitmap != null) {
				if (DeviceInfoUtil.mediaWritable()) {
					String appRoot = AppPropertiesUtil
							.getAppDirectory(activity);
					String tempFolder = activity.getResources().getString(
							R.string.temp_folder);
					File tempFile = new File(appRoot, tempFolder);
					if (!tempFile.exists()) {
						tempFile.mkdir();
					}

					String tempFolderPath = tempFile.getAbsolutePath();
					File imageFile = new File(tempFolderPath, activity
							.getResources().getString(
									R.string.temp_google_image_snap));
					FileOutputStream out = new FileOutputStream(imageFile);

					saved = bitmap.compress(CompressFormat.PNG,
							Constants.COMPRESSION_QUALITY_HIGH, out);
					if (saved) {
						String filePath = imageFile.getAbsolutePath();
						return filePath;
					}
				} else {
					Logger.warn(TAG,
							"Can't save bitmap to file. No memory card");
				}

			} else {
				Logger.warn(TAG,
						"Either context is null or the supplied bitmap is null. Conext - "
								+ " Bitmap - " + bitmap);
			}
		} catch (Exception e) {
			Logger.warn(TAG, e.getMessage());
		}
		return null;

	}
}
