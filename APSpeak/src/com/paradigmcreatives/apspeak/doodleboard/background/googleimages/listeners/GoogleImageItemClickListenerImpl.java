package com.paradigmcreatives.apspeak.doodleboard.background.googleimages.listeners;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;

import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.app.util.google.image.beans.ImageResultsBean;
import com.paradigmcreatives.apspeak.doodleboard.fragments.BackgroundFragment;
import com.paradigmcreatives.apspeak.logging.Logger;

/**
 * Implements the google image grid item click listener
 * 
 * @author Soumya Behera | Paradigm Creatives
 * 
 */
public class GoogleImageItemClickListenerImpl implements OnItemClickListener {

	private static final String TAG = "GoogleImageItemClickListenerImpl";

	private BackgroundFragment fragment;

	public GoogleImageItemClickListenerImpl(BackgroundFragment fragment) {
		super();
		this.fragment = fragment;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget
	 * .AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (fragment != null) {
			ImageResultsBean imageBean = (ImageResultsBean) (parent
					.getItemAtPosition(position));
			if (imageBean == null) {
				Logger.warn(TAG, "Image bean is null");
				return;
			}
			if (view != null) {
				final View gridItem = view;
				final ImageView gridItemImageView = (ImageView) gridItem
						.findViewById(R.id.image);
				if (gridItemImageView != null) {
					try {

						fragment.saveImage(imageBean.getURL());
					} catch (Exception e) {
						Logger.warn(TAG, "Google image thumbnail not created");
					}

				}
			} else {
				Logger.warn(TAG, "Grid item view is null");
			}
		} else {
			Logger.warn(TAG, "Activity is null");
		}
	}

}
