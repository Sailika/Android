package com.paradigmcreatives.apspeak.doodleboard.background.googleimages.listeners;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.app.util.Util;
import com.paradigmcreatives.apspeak.app.util.google.image.GoogleImageSearchHelper;
import com.paradigmcreatives.apspeak.doodleboard.background.googleimages.tasks.GoogleImageSearchThread;
import com.paradigmcreatives.apspeak.doodleboard.fragments.BackgroundFragment;
import com.paradigmcreatives.apspeak.logging.Logger;

/**
 * The button clicks listener
 * 
 * @author Soumya Behera | Paradigm Creatives
 * 
 */
public class GoogleImagesViewOnClickListener implements OnClickListener {

	private static final String TAG = "GoogleImagesViewOnClickListener";
	private BackgroundFragment fragment;

	public GoogleImagesViewOnClickListener(BackgroundFragment fragment) {
		super();
		this.fragment = fragment;
	}

	@Override
	public void onClick(View paramView) {
		// switch (paramView.getId()) {
		// case R.id.forward_button:
		// navigationForwardButtonPressed();
		// break;
		//
		// case R.id.back_button:
		// navigationBackButtonPressed();
		// break;
		//
		// case R.id.refresh_button:
		// refreshButtonPressed();
		// break;
		//
		// default:
		// break;
		// }
	}

	/**
	 * Back button press action
	 */
	private void navigationBackButtonPressed() {
		if (fragment != null) {
			if (fragment.getSearchHelper() != null) {
				if (Util.isOnline(fragment.getActivity())) {
					(new GoogleImageSearchThread(fragment,
							fragment.getSearchHelper(), false)).start();
				} else {
					Util.displayToast(fragment.getActivity(),
							fragment.getString(R.string.no_internet));
				}
			} else {
				Logger.warn(TAG, "Search helper is null");
			}
		} else {
			Logger.warn(TAG, "Activity is null");
		}
	}

	/**
	 * Refresh button press action
	 */
	private void refreshButtonPressed() {
		if (fragment != null) {
			if (TextUtils.isEmpty(fragment.getSearchText())) {
				Logger.warn(TAG, "Empty search text");
				return;
			}
			if (Util.isOnline(fragment.getActivity())) {
				GoogleImageSearchHelper gish = new GoogleImageSearchHelper(
						fragment.getSearchText());
				gish.init();
				fragment.setSearchHelper(gish);
				(new GoogleImageSearchThread(fragment, gish, true)).start();
			} else {
				Util.displayToast(fragment.getActivity(),
						fragment.getString(R.string.no_internet));
			}
		} else {
			Logger.warn(TAG, "Activity is null");
		}
	}

	/**
	 * Forward button press action
	 */
	private void navigationForwardButtonPressed() {
		if (fragment != null) {
			if (fragment.getSearchHelper() != null) {
				if (Util.isOnline(fragment.getActivity())) {
					(new GoogleImageSearchThread(fragment,
							fragment.getSearchHelper(), true)).start();
				} else {
					Util.displayToast(fragment.getActivity(),
							fragment.getString(R.string.no_internet));
				}
			} else {
				Logger.warn(TAG, "Search helper is null");
			}
		} else {
			Logger.warn(TAG, "Activity is null");
		}
	}

}