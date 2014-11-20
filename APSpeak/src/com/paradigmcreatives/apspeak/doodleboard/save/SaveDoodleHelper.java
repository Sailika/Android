package com.paradigmcreatives.apspeak.doodleboard.save;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;

import com.paradigmcreatives.apspeak.doodleboard.DoodleViewProperties;
import com.paradigmcreatives.apspeak.logging.Logger;

/**
 * Helper class for saving the doodle in the SD card
 * 
 * @author robin
 * 
 */
public class SaveDoodleHelper {
	private static final String TAG = "SaveDoodleHelper";

	public String saveDoodle(Context context, ArrayList<Object> doodlePoints, Bitmap thumbnail,
			DoodleViewProperties doodleViewProperties) {
		String doodleSavedPath = null;

		if (context != null) {
			doodleSavedPath = SaveUtil.createEmptyDoodleFolder(context);
			SaveUtil saveUtil = new SaveUtil();

			if (doodleSavedPath != null) {
				if (saveUtil.saveDoodlePoints(context, doodleSavedPath, doodlePoints)
						&& saveUtil.saveDoodleSettings(context, doodleSavedPath, doodleViewProperties)
						&& saveUtil.saveDoodleThumbnail(context, doodleSavedPath, thumbnail)) {
					if (doodleViewProperties.getBackgroundBitmap() != null
							|| (doodleViewProperties.getBitmapLayers() != null && doodleViewProperties
									.getBitmapLayers().size() > 0)) {
						saveUtil.saveDoodleBackgroundImage(context, doodleSavedPath, doodleViewProperties);

					} else {
						Logger.info(TAG, "No background to save");
					}
				} else {
					Logger.warn(TAG, "Could not save the doodle points or settings or thumbnail");
				}
			} else {
				Logger.warn(TAG, "Could not create empty folder to save the doodle");
			}
		} else {
			Logger.warn(TAG, "Context is null while saving");
		}
		return doodleSavedPath;

	}
}
