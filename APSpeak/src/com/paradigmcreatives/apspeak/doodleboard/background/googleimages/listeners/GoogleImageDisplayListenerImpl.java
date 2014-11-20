package com.paradigmcreatives.apspeak.doodleboard.background.googleimages.listeners;

import android.app.Activity;
import android.graphics.Bitmap;

import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.app.util.Util;
import com.paradigmcreatives.apspeak.doodleboard.background.googleimages.GoogleImageActivity;
import com.paradigmcreatives.apspeak.logging.Logger;

public class GoogleImageDisplayListenerImpl implements ImageDisplayListener {
    private static final String TAG = "GoogleImageDisplayListenerImpl";

    private final GoogleImageActivity activity;
    private Bitmap displayedBitmap;

    public GoogleImageDisplayListenerImpl(GoogleImageActivity activity) {
	super();
	this.activity = activity;
    }

    @Override
    public void selectedImage(Bitmap selectedImage) {
	if (activity != null && selectedImage != null) {
	    if (activity.saveTheGoogleImage(selectedImage)) {
		activity.setResult(Activity.RESULT_OK);
		activity.finish();
	    } else {
		Logger.warn(TAG, "Failed to save image");
		Util.displayToast(activity, activity.getString(R.string.failed_to_save_temp_image));
	    }
	} else {
	    Logger.warn(TAG, "Activity is null or empty image");
	}
    }

    @Override
    public void displayedImage(Bitmap image) {
	if (image != null) {
	    displayedBitmap = image;
	    if (activity != null) {
		// Do nothing
		Logger.warn(TAG, "Displayed image");
	    } else {
		Logger.warn(TAG, "Activity is null");
	    }
	} else {
	    Logger.warn(TAG, "Displayed image is null");
	}
    }

    @Override
    public void selectedImage() {
	// Do nothing
    }

    /**
     * @return the displayedImage
     */
    public Bitmap getDisplayedBitmap() {
	return displayedBitmap;
    }

}
