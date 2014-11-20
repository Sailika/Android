package com.paradigmcreatives.apspeak.doodleboard.background.googleimages.listeners;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.app.util.Util;
import com.paradigmcreatives.apspeak.app.util.google.image.GoogleImageSearchHelper;
import com.paradigmcreatives.apspeak.doodleboard.background.googleimages.tasks.GoogleImageSearchThread;
import com.paradigmcreatives.apspeak.doodleboard.fragments.BackgroundFragment;
import com.paradigmcreatives.apspeak.logging.Logger;

/**
 * Listener to start the search
 * 
 * @author Soumya Behera | Paradigm Creatives
 * 
 */
public class GoogleImagesEditorListener implements OnEditorActionListener {

	private static final String TAG = "GoogleImagesEditorListener";
	private BackgroundFragment fragment;

	public GoogleImagesEditorListener(BackgroundFragment fragment) {
		super();
		this.fragment = fragment;
	}

	@Override
	public boolean onEditorAction(TextView paramTextView, int paramInt,
			KeyEvent paramKeyEvent) {
		goButtonPressed(paramTextView.getText().toString());
		return false;
	}

	/**
	 * Go button pressed
	 */
	private void goButtonPressed(String searchText) {
		if (fragment != null) {
			if (!TextUtils.isEmpty(searchText)) {
				if (Util.isOnline(fragment.getActivity())) {
					try {
						GoogleImageSearchHelper gish = new GoogleImageSearchHelper(
								searchText);
						gish.init();
						fragment.setSearchHelper(gish);
						fragment.resetAdapter();
						if (Util.isOnline(fragment.getActivity())) {
							(new GoogleImageSearchThread(fragment, gish, true))
									.start();
							fragment.setLoadingMore(true);
						} else {
							Toast.makeText(
									fragment.getActivity(),
									fragment.getActivity().getResources()
											.getString(R.string.no_network),
									Toast.LENGTH_SHORT).show();
						}
					} catch (IllegalThreadStateException itse) {
						Logger.warn(TAG, itse.getLocalizedMessage());
					} catch (Exception e) {
						Logger.warn(TAG, e.getLocalizedMessage());
						e.printStackTrace();
					}
				} else {
					Util.displayToast(fragment.getActivity(),
							fragment.getString(R.string.no_internet));
				}
			} else {
				Logger.warn(TAG, "Search text is empty");
			}
		} else {
			Logger.warn(TAG, "Activity is null");
		}
	}
}