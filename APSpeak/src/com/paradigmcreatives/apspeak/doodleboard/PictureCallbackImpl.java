package com.paradigmcreatives.apspeak.doodleboard;

import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.util.Log;

import com.paradigmcreatives.apspeak.doodleboard.fragments.CameraFragment;

public class PictureCallbackImpl implements PictureCallback {
	private ImageSelectionFragmentActivity activity;
	private CameraFragment cameraFragment;

	public PictureCallbackImpl(ImageSelectionFragmentActivity activity,
			CameraFragment cameraFragment) {
		this.activity = activity;
		this.cameraFragment = cameraFragment;
	}

	private static final String TAG = "PictureCallbackImpl";
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;

	@Override
	public void onPictureTaken(byte[] data, Camera camera) {
		if (data != null) {
			boolean isFrontFacing;
			if (cameraFragment.getCameraFacing() == CameraInfo.CAMERA_FACING_FRONT) {
				isFrontFacing = true;
			} else {
				isFrontFacing = false;
			}
			activity.processBitmapAndMoveToFilters(data, isFrontFacing);

			// camera.startPreview();
		} else {
			Log.e(TAG, "Empty data received from camera");
			if(cameraFragment != null){
				cameraFragment.enableDisableCaptureButton(true);
			}
		}
	}
}
