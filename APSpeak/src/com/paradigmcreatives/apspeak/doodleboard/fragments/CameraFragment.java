package com.paradigmcreatives.apspeak.doodleboard.fragments;

import android.content.pm.PackageManager;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.app.util.AppPropertiesUtil;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.doodleboard.CameraGridView;
import com.paradigmcreatives.apspeak.doodleboard.CameraPreview;
import com.paradigmcreatives.apspeak.doodleboard.ImageSelectionFragmentActivity;
import com.paradigmcreatives.apspeak.doodleboard.PictureCallbackImpl;
import com.paradigmcreatives.apspeak.doodleboard.ShuttercallbackImpl;
import com.paradigmcreatives.apspeak.doodleboard.listeners.CameraOptionsOnClickListener;

public class CameraFragment extends Fragment {

	private ImageView mCapture;
	private ImageView mFlipCamera;
	private ImageView mCameraFlash;
	private static int WIDTH = 500;
	private CameraPreview mPreview;
	private static Camera mCamera;
	private FrameLayout mPreviewLayout;
	private ProgressBar progressBar;
	private ImageView mCameraGridButton;
	private int flashMode;

	private ImageSelectionFragmentActivity activity;
	private static String TAG = "CameraFragment";
	private LinearLayout mOptionsLayout;
	private View view;
	private CameraGridView mCameraGridView;
	private int cameraFacing = Camera.CameraInfo.CAMERA_FACING_BACK;

	public CameraFragment(ImageSelectionFragmentActivity activity) {
		this.activity = activity;
	}

	public CameraFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.camera_fragment_layout, container,
				false);
		mCapture = (ImageView) view.findViewById(R.id.camera_capture);
		mFlipCamera = (ImageView) view.findViewById(R.id.flip_camera);
		mCameraFlash = (ImageView) view.findViewById(R.id.camera_flash);
		mOptionsLayout = (LinearLayout) view.findViewById(R.id.asset_options);

		mCameraGridButton = (ImageView) view.findViewById(R.id.grid_camera);

		flashMode = AppPropertiesUtil.getCameraFlashMode(this.getActivity());
		cameraFacing = AppPropertiesUtil.getCameraFacing(this.getActivity());
		mCameraGridView = new CameraGridView(this.getActivity());
		mCameraGridButton.setSelected(false);

		mPreviewLayout = (FrameLayout) view.findViewById(R.id.camera_layout);
		progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
		CameraOptionsOnClickListener listener = new CameraOptionsOnClickListener(
				this);
		mCapture.setClickable(true);
		mCapture.setOnClickListener(listener);
		mFlipCamera.setOnClickListener(new CameraFlipListener());
		mCameraFlash.setOnClickListener(listener);
		mCameraGridButton.setOnClickListener(listener);
		mCamera = getCameraInstance(cameraFacing);

		if (mCamera != null) {
			Log.i(TAG, mCamera + "");
			mPreview = new CameraPreview(this.getActivity(), mCamera);
			Display display = this.getActivity().getWindowManager()
					.getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);
			WIDTH = size.x;
			mPreviewLayout.addView(mPreview, WIDTH, WIDTH);
			mOptionsLayout.bringToFront();
		}
		if (CameraFragment.this.getActivity().getPackageManager()
				.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
			mCameraFlash.setVisibility(View.VISIBLE);
		} else {
			mCameraFlash.setVisibility(View.GONE);
		}
		if (!haveFrontFacingCamera()) {
			mFlipCamera.setVisibility(View.GONE);
		} else {
			mFlipCamera.setVisibility(View.VISIBLE);
		}
		setCameraFacingFrontParameters();
		setCameraFlashParameters();

		return (view);
	}

	private boolean haveFrontFacingCamera() {
		int cameraCount = 0;
		Camera.CameraInfo info = new Camera.CameraInfo();
		cameraCount = Camera.getNumberOfCameras();
		for (int idx = 0; idx < cameraCount; idx++) {
			Camera.getCameraInfo(idx, info);
			if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void onPause() {
		super.onPause();
		releaseCamera(); // release the camera immediately on pause event
	}

	@Override
	public void onResume() {
		super.onResume();
		progressBar.setVisibility(View.VISIBLE);
		progressBar.bringToFront();
		if (mCamera == null) {
			mCamera = getCameraInstance(cameraFacing);
			if (mCamera != null) {
				mPreview = new CameraPreview(this.getActivity(), mCamera);
				mPreviewLayout.addView(mPreview, WIDTH, WIDTH);
				// mCamera.startPreview();
			}
		} else {
			try {
				mCamera.startPreview();
			} catch (Exception e) {
				Log.w(TAG, e + "");
			}
		}
		progressBar.setVisibility(View.GONE);
		if(mCapture != null){
			mCapture.setClickable(true);
		}
		mCapture.bringToFront();
	}

	private void releaseCamera() {
		if (mCamera != null) {
			mCamera.release(); // release the camera for other applications
			mCamera = null;
		}
	}

	public static Camera getCameraInstance(int facing) {
		Camera c = null;
		if (facing == CameraInfo.CAMERA_FACING_FRONT) {

			int cameraCount = 0;
			Camera.CameraInfo info = new Camera.CameraInfo();
			cameraCount = Camera.getNumberOfCameras();
			for (int idx = 0; idx < cameraCount; idx++) {
				Camera.getCameraInfo(idx, info);
				if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
					try {
						c = Camera.open(idx); // attempt to get a Camera
												// instance
					} catch (Exception e) {
						// Camera is not available (in use or does not exist)
					}
				}
			}
		} else {
			try {
				c = Camera.open(); // attempt to get a Camera instance
			} catch (Exception e) {
				// Camera is not available (in use or does not exist)
			}

		}
		return c; // returns null if camera is unavailable
	}

	public void showProgress() {
		if (progressBar != null) {
			progressBar.setVisibility(View.VISIBLE);
			progressBar.bringToFront();
		}
	}

	public void removeCamera() {
		if (mPreviewLayout != null && mPreview != null) {
			mPreviewLayout.removeView(mPreview);

		}
	}

	public void takePicture() {
		if(mCapture != null){
			mCapture.setClickable(false);
		}
		if (mCamera != null) {
			mCamera.takePicture(new ShuttercallbackImpl(this), null,
					new PictureCallbackImpl(activity, this));
		}
	}

	public void handleFlashButtonClick() {
		if (cameraFacing == CameraInfo.CAMERA_FACING_BACK) {
			if (flashMode == Constants.FLASH_MODE_ON) {
				flashMode = Constants.FLASH_MODE_OFF;
			} else if (flashMode == Constants.FLASH_MODE_OFF) {
				flashMode = Constants.FLASH_MODE_AUTOMATIC;
			} else if (flashMode == Constants.FLASH_MODE_AUTOMATIC) {
				flashMode = Constants.FLASH_MODE_ON;
			}
			setCameraFlashParameters();
		}
	}

	private void setCameraFlashParameters() {
		if (mCamera != null && mCameraFlash != null && cameraFacing == CameraInfo.CAMERA_FACING_BACK) {
			Parameters params = mCamera.getParameters();
			if (flashMode == Constants.FLASH_MODE_ON) {
				params.setFlashMode(Parameters.FLASH_MODE_ON);
				AppPropertiesUtil.setCameraFlashMode(this.getActivity(),
						Constants.FLASH_MODE_ON);
				mCameraFlash.setImageResource(R.drawable.flash_selected_icon);
			} else if (flashMode == Constants.FLASH_MODE_OFF) {
				params.setFlashMode(Parameters.FLASH_MODE_OFF);
				AppPropertiesUtil.setCameraFlashMode(this.getActivity(),
						Constants.FLASH_MODE_OFF);
				mCameraFlash.setImageResource(R.drawable.flash_disabled_icon);
			} else {
				params.setFlashMode(Parameters.FLASH_MODE_AUTO);
				AppPropertiesUtil.setCameraFlashMode(this.getActivity(),
						Constants.FLASH_MODE_AUTOMATIC);
				mCameraFlash.setImageResource(R.drawable.flash_auto_icon);
			}
			mCamera.setParameters(params);
		}

	}

	private void setCameraFacingFrontParameters() {

		if (cameraFacing == CameraInfo.CAMERA_FACING_FRONT) {
			AppPropertiesUtil.setCameraFacing(this.getActivity(),
					CameraInfo.CAMERA_FACING_FRONT);
			Parameters params = mCamera.getParameters();
			params.setFlashMode(Parameters.FLASH_MODE_OFF);
			mCamera.setParameters(params);
			mCameraFlash.setImageResource(R.drawable.flash_disabled_icon);
		}
	}

	public void handleGridButtonClick() {

		if (mCameraGridButton.isSelected()) {
			mCameraGridButton.setSelected(false);
			mCameraGridView.setVisibility(View.INVISIBLE);
			mPreviewLayout.removeView(mCameraGridView);

		} else {
			mCameraGridButton.setSelected(true);
			mPreviewLayout.addView(mCameraGridView);
			mCameraGridView.setVisibility(View.VISIBLE);
			mOptionsLayout.bringToFront();
		}
	}

	public int getCameraFacing() {
		return cameraFacing;
	}

	class CameraFlipListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			releaseCamera();
			if (getCameraFacing() == CameraInfo.CAMERA_FACING_BACK) {
				cameraFacing = CameraInfo.CAMERA_FACING_FRONT;
			} else {
				cameraFacing = CameraInfo.CAMERA_FACING_BACK;
			}

			// Remove the current preview view
			if (mPreview != null) {
				mPreviewLayout.removeView(mPreview);
			}

			progressBar.setVisibility(View.VISIBLE);
			progressBar.bringToFront();

			mCamera = getCameraInstance(getCameraFacing());
			if (mCamera != null) {
				if (cameraFacing == CameraInfo.CAMERA_FACING_FRONT) {
					setCameraFacingFrontParameters();
				} else if (cameraFacing == CameraInfo.CAMERA_FACING_BACK) {

					AppPropertiesUtil.setCameraFacing(
							CameraFragment.this.getActivity(),
							CameraInfo.CAMERA_FACING_BACK);
					Parameters params = mCamera.getParameters();
					if (flashMode == Constants.FLASH_MODE_ON) {
						params.setFlashMode(Parameters.FLASH_MODE_ON);
						mCameraFlash
								.setImageResource(R.drawable.flash_selected_icon);
					} else if (flashMode == Constants.FLASH_MODE_OFF) {
						params.setFlashMode(Parameters.FLASH_MODE_OFF);
						mCameraFlash
								.setImageResource(R.drawable.flash_disabled_icon);
					} else {
						params.setFlashMode(Parameters.FLASH_MODE_AUTO);
						mCameraFlash
								.setImageResource(R.drawable.flash_auto_icon);
					}
					mCamera.setParameters(params);

				}
				mPreview = new CameraPreview(CameraFragment.this.getActivity(),
						mCamera);
				mPreviewLayout.addView(mPreview, WIDTH, WIDTH);
				mCamera.startPreview();
			}
			progressBar.setVisibility(View.GONE);
			mOptionsLayout.bringToFront();

		}

	}

	public void enableDisableCaptureButton(boolean enableDisable){
		if(mCapture != null){
			mCapture.setClickable(enableDisable);
		}
	}
}
