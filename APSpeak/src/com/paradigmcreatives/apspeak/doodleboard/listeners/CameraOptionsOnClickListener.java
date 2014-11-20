package com.paradigmcreatives.apspeak.doodleboard.listeners;

import android.view.View;
import android.view.View.OnClickListener;

import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.doodleboard.fragments.CameraFragment;

public class CameraOptionsOnClickListener implements OnClickListener {
	private CameraFragment fragment;

	public CameraOptionsOnClickListener(CameraFragment fragment) {
		this.fragment = fragment;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.camera_capture:
			if (fragment != null) {
				fragment.takePicture();
			}
			break;
		case R.id.camera_flash:
			if (fragment != null) {
				fragment.handleFlashButtonClick();
			}
			break;
		case R.id.grid_camera:
			if (fragment != null) {
				fragment.handleGridButtonClick();
			}
			break;
		}

	}

}