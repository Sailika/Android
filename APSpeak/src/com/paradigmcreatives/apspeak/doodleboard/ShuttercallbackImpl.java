package com.paradigmcreatives.apspeak.doodleboard;

import android.hardware.Camera.ShutterCallback;

import com.paradigmcreatives.apspeak.doodleboard.fragments.CameraFragment;

public class ShuttercallbackImpl implements ShutterCallback {
	private CameraFragment fragment;

	public ShuttercallbackImpl(CameraFragment fragment) {
		this.fragment = fragment;
	}

	@Override
	public void onShutter() {

		if (fragment != null) {
			fragment.showProgress();
		}

	}

}
