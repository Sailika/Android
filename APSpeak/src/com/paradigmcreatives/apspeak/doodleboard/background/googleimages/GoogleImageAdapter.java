package com.paradigmcreatives.apspeak.doodleboard.background.googleimages;

import java.util.ArrayList;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.paradigmcreatives.apspeak.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.paradigmcreatives.apspeak.app.util.google.image.beans.ImageResultsBean;
import com.paradigmcreatives.apspeak.doodleboard.fragments.BackgroundFragment;
import com.paradigmcreatives.apspeak.logging.Logger;

/**
 * Adapter to load the google images
 * 
 * @author Soumya Behera | Paradigm Creatives
 * 
 */
public class GoogleImageAdapter extends BaseAdapter {

	private static final String TAG = "GoogleImageAdapter";
	private final BackgroundFragment fragment;
	private ArrayList<ImageResultsBean> imageResults;
	private HashMap<Integer, Boolean> viewEnablers;

	public GoogleImageAdapter(BackgroundFragment fragment,
			ArrayList<ImageResultsBean> imageResults) {
		this.fragment = fragment;
		this.imageResults = imageResults;
		this.viewEnablers = new HashMap<Integer, Boolean>();
		ImageLoader.getInstance().init(
				ImageLoaderConfiguration.createDefault(fragment.getActivity()));
	}

	/**
	 * Updates the image list with new data
	 * 
	 * @param newImageResults
	 * @return
	 */
	public boolean addImageResults(ArrayList<ImageResultsBean> newImageResults) {
		boolean result = false;
		if (newImageResults != null && newImageResults.size() > 0) {
			if (imageResults == null) {
				imageResults = new ArrayList<ImageResultsBean>();
			}
			result = imageResults.addAll(newImageResults);
		} else {
			Logger.info(TAG, "trying to add empty image list");
		}

		return result;
	}

	@Override
	public int getCount() {
		if (imageResults != null) {
			return imageResults.size();
		}
		return 0;
	}

	@Override
	public ImageResultsBean getItem(int position) {
		if (imageResults != null && position >= 0
				&& position < imageResults.size()) {
			return imageResults.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final View view;
		final ImageView imageView;
		final int itemPosition = position;
		if (fragment == null) {
			return convertView;
		}
		if (convertView == null) {
			view = fragment.getActivity().getLayoutInflater()
					.inflate(R.layout.grid_view_image_item, parent, false);
		} else {
			view = convertView;
		}

		if (view != null) {
			ImageResultsBean bean = imageResults.get(position);
			if (bean != null) {
				imageView = (ImageView) view.findViewById(R.id.image);

				String thumbnail = bean.getThumbnailURL();
				if (TextUtils.isEmpty(thumbnail)) {
					thumbnail = bean.getURL();
				}
				if (fragment.getImageLoader() != null
						&& fragment.getDisplayImageOptions() != null) {
					fragment.getImageLoader().displayImage(thumbnail,
							imageView, fragment.getDisplayImageOptions(),
							new SimpleImageLoadingListener() {

								@Override
								public void onLoadingStarted(String imageUri,
										View view) {
									if (view instanceof ImageView) {
										((ImageView) view)
												.setScaleType(ScaleType.CENTER);
									}
									super.onLoadingStarted(imageUri, view);
								}

								@Override
								public void onLoadingComplete(String imageUri,
										View view, Bitmap loadedImage) {
									if (view instanceof ImageView) {
										((ImageView) view)
												.setScaleType(ScaleType.FIT_XY);
									}

									if (viewEnablers != null) {
										viewEnablers.put(itemPosition, true);
									}
									super.onLoadingComplete(imageUri, view,
											loadedImage);
								}

							});
				}
			}
		}

		return view;
	}

	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	@Override
	public boolean isEnabled(int position) {
		if (viewEnablers != null && viewEnablers.containsKey(position)) {
			return viewEnablers.get(position);
		}
		return false;
	}
}