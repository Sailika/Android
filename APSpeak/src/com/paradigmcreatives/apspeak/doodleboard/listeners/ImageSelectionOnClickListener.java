package com.paradigmcreatives.apspeak.doodleboard.listeners;

import android.view.View;
import android.view.View.OnClickListener;

import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.doodleboard.fragments.ImageChooserFragment;

public class ImageSelectionOnClickListener implements OnClickListener {
	private ImageChooserFragment fragment;
	

	public ImageSelectionOnClickListener(ImageChooserFragment fragment) {
		this.fragment = fragment;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.camera_button_layout:
			if (fragment != null) {
				fragment.showCameraFragment();
			}
			break;

		case R.id.gallery_button_layout:
			if (fragment != null) {
				fragment.showGallery();
			}
			break;

		case R.id.write_button_layout:
			if (fragment != null) {
				fragment.showBackgroundFragment();
			}
			break;

		}

	}

}
