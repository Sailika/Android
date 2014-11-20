package com.paradigmcreatives.apspeak.doodleboard.listeners;
/*package com.paradigmcreatives.apspeak.doodleboard.listeners;

import android.view.View;
import android.view.View.OnClickListener;

import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.app.analytics.GoogleAnalyticsHelper;
import com.paradigmcreatives.apspeak.app.util.constants.GoogleAnalyticsConstants;
import com.paradigmcreatives.apspeak.doodleboard.DoodlyDooCameraActivity;

*//**
 * Listener class to handle DoodlyDoo Camera options clicks
 * 
 * @author Dileep | neuv
 * 
 *//*
public class CameraOptionsClickListener implements OnClickListener {

    private DoodlyDooCameraActivity mCameraActivity;

    public CameraOptionsClickListener(DoodlyDooCameraActivity activity) {
	mCameraActivity = activity;
    }

    @Override
    public void onClick(View v) {
	switch (v.getId()) {
	case R.id.camera_capture:
	    if (mCameraActivity != null) {
		GoogleAnalyticsHelper.sendEventToGA(mCameraActivity, GoogleAnalyticsConstants.CAMERA_SCREEN,
			GoogleAnalyticsConstants.ACTION_BUTTON, GoogleAnalyticsConstants.CAMERA_CAPTURE_BUTTON);
		mCameraActivity.takePicture();
	    }
	    break;

	case R.id.camera_grid:
	    if (mCameraActivity != null) {
		mCameraActivity.showStickers();
	    }
	    break;

	case R.id.camera_flash_toggle:
	    if (mCameraActivity != null) {
		mCameraActivity.changeCameraFlashMode();
	    }
	    break;

	case R.id.flip_camera:
	case R.id.camera_front:
	    if (mCameraActivity != null) {
		mCameraActivity.flipCamera();
	    }
	    break;

	case R.id.gallery_launch:
	    if (mCameraActivity != null) {
		GoogleAnalyticsHelper.sendEventToGA(mCameraActivity, GoogleAnalyticsConstants.CAMERA_SCREEN,
			GoogleAnalyticsConstants.ACTION_BUTTON, GoogleAnalyticsConstants.CAMERA_GALLERY_BUTTON);
		mCameraActivity.launchGallery();
	    }
	    break;
	case R.id.explore_launch:
	    if(mCameraActivity != null){
		mCameraActivity.launchGoogleSearch();
	    }
	    break;
	}

    }
}
*/