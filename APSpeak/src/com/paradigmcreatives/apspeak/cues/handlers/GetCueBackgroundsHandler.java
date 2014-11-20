package com.paradigmcreatives.apspeak.cues.handlers;

import java.util.ArrayList;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ProgressBar;

import com.paradigmcreatives.apspeak.app.util.google.image.beans.ImageResultsBean;
import com.paradigmcreatives.apspeak.doodleboard.fragments.BackgroundFragment;

public class GetCueBackgroundsHandler extends Handler {

	private static final int SUCCESS = 1;
	private static final int FAILURE = 2;
	private Fragment fragment;
	private ProgressBar progressBar;

	public GetCueBackgroundsHandler(Fragment fragment, ProgressBar progressBar) {
		super();
		this.fragment = fragment;
		this.progressBar = progressBar;
	}

	@Override
	public void handleMessage(Message msg) {
		progressBar.setVisibility(View.GONE);
		switch (msg.what) {
		case SUCCESS:
			if (fragment != null) {
				if (fragment instanceof BackgroundFragment) {
					try {
						BackgroundFragment backgroundFragment = ((BackgroundFragment) fragment);
						if (msg.obj != null) {
							backgroundFragment
									.setCueBackgroundsList((ArrayList<ImageResultsBean>) msg.obj);
							backgroundFragment.setAdapter();
						}
					} catch (Exception e) {

					}
				}
			}
			break;

		case FAILURE:

			break;

		default:
			break;
		}
		super.handleMessage(msg);
	}

	public void didFetchComplete(ArrayList<ImageResultsBean> cueBackgroundsList) {
		Message msg = new Message();
		msg.what = SUCCESS;
		msg.obj = cueBackgroundsList;
		sendMessage(msg);
	}

	public void didFail() {
		Message msg = new Message();
		msg.what = FAILURE;
		sendMessage(msg);
	}
}
