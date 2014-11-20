package com.paradigmcreatives.apspeak.globalstream.listeners;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.paradigmcreatives.apspeak.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.paradigmcreatives.apspeak.app.model.StreamAsset;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.app.util.dialogs.ProgressWheel;
import com.paradigmcreatives.apspeak.app.util.images.ImageUtil;
import com.paradigmcreatives.apspeak.stream.listeners.UserStreamGestureListener;
import com.paradigmcreatives.apspeak.stream.listeners.UserStreamOnTouchListener;

/**
 * Adapter to hold Stream content
 * 
 * @author Dileep | neuv
 * 
 */
public class GlobalStreamsAdapter extends BaseAdapter {

	private Fragment mFragment;
	private ArrayList<StreamAsset> mAssetsList;
	private DisplayImageOptions options;

	public GlobalStreamsAdapter(final Fragment fragment,
			ArrayList<StreamAsset> assets) {
		this.mFragment = fragment;
		this.mAssetsList = assets;
		ImageLoader.getInstance().init(
				ImageLoaderConfiguration.createDefault(this.mFragment
						.getActivity()));
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).displayer(new FadeInBitmapDisplayer(250))
				.build();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		if (mFragment == null) {
			return null;
		}
		if (convertView == null) {
			convertView = LayoutInflater.from(mFragment.getActivity()).inflate(
					R.layout.global_streams_asset_item, null);
		}
		view = convertView;
		if (view != null) {
			// Initialize view with stream asset
			StreamAsset asset = (StreamAsset) getItem(position);
			if (asset != null) {
				ImageView assetImage = (ImageView) convertView
						.findViewById(R.id.global_streams_asset_image);
				ProgressWheel progressWheel = (ProgressWheel) convertView
						.findViewById(R.id.progressBarwheel);
				ImageView assetLoveAnimationImage = (ImageView) convertView
						.findViewById(R.id.asset_love_animation_image);
				ImageView assetLove = (ImageView) convertView
						.findViewById(R.id.love_image);
				ImageView profileImageView = (ImageView) convertView
						.findViewById(R.id.profile_picture);
				TextView name = (TextView) convertView
						.findViewById(R.id.user_name);
				TextView groupNameView = (TextView) convertView
						.findViewById(R.id.group_name);

				ImageLoader.getInstance().displayImage(
						asset.getAssetOwner().getProfilePicURL(),
						profileImageView, options, new ImageLoadingListener() {

							@Override
							public void onLoadingStarted(String imageUri,
									View view) {

							}

							@Override
							public void onLoadingFailed(String imageUri,
									View view, FailReason failReason) {

							}

							@Override
							public void onLoadingComplete(String imageUri,
									View view, Bitmap loadedImage) {
								try {
									if (view instanceof ImageView) {
										loadedImage = ImageUtil.getCircularBitmapResizeTo(
												mFragment.getActivity(),
												loadedImage,
												Constants.BUBBLE_IMAGE_SIZE,
												Constants.BUBBLE_IMAGE_SIZE);
										((ImageView) view)
												.setImageBitmap(loadedImage);
									}
								} catch (Exception e) {

								}

							}

							@Override
							public void onLoadingCancelled(String imageUri,
									View view) {

							}
						});
				if (mAssetsList.get(position).getAssetOwner()
						.getProfilePicBitmap() != null) {
					profileImageView.setImageBitmap(mAssetsList.get(position)
							.getAssetOwner().getProfilePicBitmap());
				}
				name.setText(mAssetsList.get(position).getAssetOwner()
						.getName());
				if (mAssetsList.get(position).getAssetOwner()
						.getUserGroupNames() != null
						&& mAssetsList.get(position).getAssetOwner()
								.getUserGroupNames().size() > 0) {
					String groupName = mAssetsList.get(position)
							.getAssetOwner().getUserGroupNames().get(0);
					if (!TextUtils.isEmpty(groupName)) {
						groupNameView.setText(groupName);
					}
				}

				setAssetImage(asset, assetImage, assetLove, progressWheel);

				UserStreamGestureListener gestureListener = new UserStreamGestureListener(
						mFragment, asset);
				gestureListener
						.setAssetAnimationIconHolder(assetLoveAnimationImage);
				GestureDetector gd = new GestureDetector(mFragment
						.getActivity().getApplicationContext(), gestureListener);
				UserStreamOnTouchListener touchListener = new UserStreamOnTouchListener(
						gd, mFragment);
				assetImage.setOnTouchListener(touchListener);
			}
		}
		return view;
	}

	@Override
	public int getCount() {
		if (mAssetsList != null) {
			return mAssetsList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (mAssetsList != null && position >= 0
				&& position < mAssetsList.size()) {
			return mAssetsList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		if (mAssetsList != null && position >= 0
				&& position < mAssetsList.size()) {
			return position;
		}
		return -1;
	}

	/**
	 * Appends next batch of assets to existing list
	 * 
	 * @param nextBatchAssets
	 */
	/*
	 * public void appendNextBatchAssets(ArrayList<StreamAsset> nextBatchAssets)
	 * { if (nextBatchAssets != null && nextBatchAssets.size() > 0) {
	 * this.mAssetsList.addAll(nextBatchAssets); notifyDataSetChanged(); } }
	 */

	/**
	 * Clears all items from the adapter
	 */
	public void clearAll() {
		if (mAssetsList != null) {
			mAssetsList.clear();
			notifyDataSetChanged();
		}
	}

	/**
	 * Sets asset's image
	 * 
	 * @param position
	 * @param assetImage
	 */
	public void setAssetImage(final StreamAsset asset,
			final ImageView assetImage, final ImageView assetLove,
			final ProgressWheel progresswheel) {
		if (assetImage == null) {
			return;
		}
		// assetImage.setImageResource(R.drawable.doodle);
		if (asset != null) {
			assetImage.setVisibility(View.INVISIBLE);
			String url = asset.getAssetThumbnailURL();
			if (TextUtils.isEmpty(url)) {
				url = asset.getAssetSnapURL();
			}
			ImageLoader.getInstance().displayImage(url, assetImage, options,
					new ImageLoadingListener() {

						@Override
						public void onLoadingStarted(String imageUri, View view) {

							if (progresswheel != null) {
								progresswheel.incrementProgress(0);
								progresswheel.setVisibility(view.VISIBLE);
							}
						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
							// TODO Auto-generated method stub
							if (progresswheel != null)
								progresswheel.setVisibility(view.GONE);
						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							if (progresswheel != null)
								progresswheel.setVisibility(view.GONE);
							assetImage.setVisibility(View.VISIBLE);
							if (asset.getAssetIsLoved()) {
								assetLove.setSelected(true);
								assetLove.setVisibility(View.VISIBLE);
							} else {
								assetLove.setSelected(false);
								assetLove.setVisibility(View.INVISIBLE);
							}
						}

						@Override
						public void onLoadingCancelled(String imageUri,
								View view) {
							// TODO Auto-generated method stub
							if (progresswheel != null)
								progresswheel.setVisibility(view.GONE);
						}
					}, new ImageLoadingProgressListener() {

						@Override
						public void onProgressUpdate(String imageUri,
								View view, int current, int total) {
							if (progresswheel != null)
								progresswheel.incrementProgress((current * 360)
										/ total);
						}
					});

		}
	}
}
