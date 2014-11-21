package com.paradigmcreatives.apspeak.doodleboard.fragments;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.app.util.Util;
import com.paradigmcreatives.apspeak.app.util.google.image.GoogleImageSearchHelper;
import com.paradigmcreatives.apspeak.app.util.google.image.beans.ImageResultsBean;
import com.paradigmcreatives.apspeak.cues.handlers.GetCueBackgroundsHandler;
import com.paradigmcreatives.apspeak.cues.tasks.GetCueBackgroundsThread;
import com.paradigmcreatives.apspeak.doodleboard.ImageSelectionFragmentActivity;
import com.paradigmcreatives.apspeak.doodleboard.background.googleimages.GoogleImageAdapter;
import com.paradigmcreatives.apspeak.doodleboard.background.googleimages.listeners.GoogleImageItemClickListenerImpl;
import com.paradigmcreatives.apspeak.doodleboard.background.googleimages.listeners.GoogleImagesEditorListener;
import com.paradigmcreatives.apspeak.logging.Logger;

public class BackgroundFragment extends Fragment {
	private EditText searchBar;
	private GridView gridView;
	private EditText writeHere;
	private GoogleImageSearchHelper searchHelper;
	private DisplayImageOptions options;
	private String TAG = "BackgroundFragment";
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private boolean isLoadingMore = true;
	private ArrayList<ImageResultsBean> cueBackgroundsList;
	private String cueId;
	private ImageSelectionFragmentActivity activity;
	private ProgressBar progressBar;

	public BackgroundFragment(ImageSelectionFragmentActivity activity,
			String cueId) {
		this.cueId = cueId;
		this.activity = activity;
	}

	public BackgroundFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.background_google_images,
				container, false);

		options = new DisplayImageOptions.Builder().cacheInMemory(false)
				.bitmapConfig(Bitmap.Config.ARGB_8888)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.showImageForEmptyUri(R.drawable.removed)
				.showImageOnLoading(R.drawable.refresh)
				.showImageOnFail(R.drawable.removed).build();
		writeHere=(EditText)view.findViewById(R.id.tv_create_own_idea_write);
		searchBar = (EditText) view.findViewById(R.id.search_bar);
		gridView = (GridView) view.findViewById(R.id.grid_view);
		progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
		searchBar
				.setOnEditorActionListener(new GoogleImagesEditorListener(this));

		gridView.setOnItemClickListener(new GoogleImageItemClickListenerImpl(
				this));
		GetCueBackgroundsHandler handler = new GetCueBackgroundsHandler(this,
				progressBar);
		if (cueId == null) {
			cueId = "10980abe-7f1e-4ffc-be00-02754b830a55";
		}
		if (Util.isOnline(getActivity())) {
			GetCueBackgroundsThread thread = new GetCueBackgroundsThread(
					this.getActivity(), handler, cueId);
			thread.start();

		} else {
			Toast.makeText(getActivity(),
					getResources().getString(R.string.no_network),
					Toast.LENGTH_SHORT).show();
		}
		if (activity != null) {
			activity.stopProgress();
		}
		return view;
	}

	public void updateAdapter(ArrayList<ImageResultsBean> results) {
		GoogleImageAdapter adapter = (GoogleImageAdapter) gridView.getAdapter();
		if (adapter != null) {
			adapter.addImageResults(results);
		} else {
			adapter = new GoogleImageAdapter(this, results);
			gridView.setAdapter(adapter);
		}

		adapter.notifyDataSetChanged();
	}

	public void resetAdapter() {
		GoogleImageAdapter adapter = (GoogleImageAdapter) gridView.getAdapter();
		adapter = new GoogleImageAdapter(this,
				new ArrayList<ImageResultsBean>());
		gridView.setAdapter(adapter);

		adapter.notifyDataSetChanged();
	}

	public void saveImage(String urlString) {
		// BackgroundImageDownloadTask task = new BackgroundImageDownloadTask(
		// this.getActivity());
		// task.execute(urlString);
		if (imageLoader != null) {
			imageLoader.loadImage(urlString, new ImageLoadingListener() {

				@Override
				public void onLoadingStarted(String imageUri, View view) {
					progressBar.setVisibility(View.VISIBLE);
					progressBar.bringToFront();
				}

				@Override
				public void onLoadingFailed(String imageUri, View view,
						FailReason failReason) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onLoadingComplete(String imageUri, View view,
						Bitmap loadedImage) {
					activity.notifyImageBitmap(loadedImage);

				}

				@Override
				public void onLoadingCancelled(String imageUri, View view) {
					// TODO Auto-generated method stub

				}
			});
		}

	}

	/**
	 * Returns the current search text
	 * 
	 * @return
	 */
	public String getSearchText() {
		String result = null;
		if (searchBar != null) {
			result = searchBar.getText().toString();
		} else {
			Logger.warn(TAG, "Search bar is not initialized");
		}

		return result;
	}

	/**
	 * @return the searchHelper
	 */
	public GoogleImageSearchHelper getSearchHelper() {
		return searchHelper;
	}

	/**
	 * @param searchHelper
	 *            the searchHelper to set
	 */
	public void setSearchHelper(GoogleImageSearchHelper searchHelper) {
		this.searchHelper = searchHelper;
	}

	/**
	 * @return the options
	 */
	public DisplayImageOptions getDisplayImageOptions() {
		return options;
	}

	/**
	 * @param options
	 *            the options to set
	 */
	public void setDisplayImageOptions(DisplayImageOptions options) {
		this.options = options;
	}

	/**
	 * @return the imageLoader
	 */
	public ImageLoader getImageLoader() {
		return imageLoader;
	}

	/**
	 * @param imageLoader
	 *            the imageLoader to set
	 */
	public void setImageLoader(ImageLoader imageLoader) {
		this.imageLoader = imageLoader;
	}

	/**
	 * @return the isLoadingMore
	 */
	public boolean isLoadingMore() {
		return isLoadingMore;
	}

	/**
	 * @param isLoadingMore
	 *            the isLoadingMore to set
	 */
	public void setLoadingMore(boolean isLoadingMore) {
		this.isLoadingMore = isLoadingMore;
	}

	public ArrayList<ImageResultsBean> getCueBackgroundsList() {
		return cueBackgroundsList;
	}

	public void setCueBackgroundsList(
			ArrayList<ImageResultsBean> cueBackgroundsList) {
		this.cueBackgroundsList = cueBackgroundsList;
	}

	public void setAdapter() {
		if (cueBackgroundsList != null && cueBackgroundsList.size() > 0) {
			GoogleImageAdapter adapter = new GoogleImageAdapter(this,
					cueBackgroundsList);
			gridView.setAdapter(adapter);
		}
	}
}
