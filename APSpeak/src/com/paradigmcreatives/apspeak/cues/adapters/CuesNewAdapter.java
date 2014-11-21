package com.paradigmcreatives.apspeak.cues.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.paradigmcreatives.apspeak.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.paradigmcreatives.apspeak.app.model.Campaigns;
import com.paradigmcreatives.apspeak.app.model.StreamAsset;
import com.paradigmcreatives.apspeak.app.util.customcontrols.FullWidthImageView;
import com.paradigmcreatives.apspeak.app.util.dialogs.ProgressWheel;
import com.paradigmcreatives.apspeak.cues.fragments.CuesFragment;

/**
 * Adapter to hold Whatsay Cues
 * 
 * @author Dileep | neuv
 * 
 */
public class CuesNewAdapter extends BaseAdapter {

	private Fragment mFragment;
	// private ArrayList<CueBean> mCues;
	private HashMap<Integer, ArrayList<Campaigns>> mCues;
	private Random mRandom;
	private DisplayImageOptions options;

	public CuesNewAdapter(final Fragment fragment,
			HashMap<Integer, ArrayList<Campaigns>> cues) {
		this.mFragment = fragment;
		this.mCues = cues;
		this.mRandom = new Random();
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
					R.layout.cue_item_v1, null);
		}
		view = convertView;
		if (view != null) {
			// Initialize view with Cue bean content
			int bgcolor = -1;
			ArrayList<Campaigns> cues = null;
			try {
				cues = (ArrayList<Campaigns>) getItem(position);
			} catch (Exception e) {

			}

			if (cues != null && !cues.isEmpty()) {
				if (cues.size() > 1 || cues.get(0).getWidth() == 1) {
					// Display GridView
					GridView gridView = (GridView) view
							.findViewById(R.id.cues_grid_view);
					CuesAdapter adapter = new CuesAdapter(mFragment, cues);
					gridView.setAdapter(adapter);
					adapter.notifyDataSetChanged();
					gridView.setVisibility(View.VISIBLE);
					ImageView fullWidthImage = (ImageView) view
							.findViewById(R.id.cue_thumbnail);
					fullWidthImage.setVisibility(View.INVISIBLE);
				} else {
					final Campaigns cueBean = cues.get(0);
					try {
						bgcolor = Color.parseColor((cueBean
								.getBackgroundColor().startsWith("#") ? cueBean
								.getBackgroundColor() : ("#" + cueBean
								.getBackgroundColor())));
					} catch (Exception e) {
						bgcolor = Color.argb(255, mRandom.nextInt(256),
								mRandom.nextInt(256), mRandom.nextInt(256));
					}
					view.setBackgroundColor(bgcolor);
					cueBean.setBackgroundColor(String.format("#%06X",
							0xFFFFFF & bgcolor));

					// Display the single cue so that it occupies entire row
					// ImageView fullWidthImage = (ImageView)
					// view.findViewById(R.id.cue_thumbnail);
					final FullWidthImageView fullWidthImage = (FullWidthImageView) view
							.findViewById(R.id.cue_thumbnail);
					ImageView roundedFrame = (ImageView) view
							.findViewById(R.id.rounded_rectangle_frame);
					StreamAsset dummyAsset = new StreamAsset();
					dummyAsset.setAssetSnapURL(cueBean.getBackgroundURL());
					setAssetImage(dummyAsset, fullWidthImage, roundedFrame,
							view, null);
					GridView gridView = (GridView) view
							.findViewById(R.id.cues_grid_view);
					gridView.setVisibility(View.INVISIBLE);

					fullWidthImage.setVisibility(View.VISIBLE);
					fullWidthImage
							.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View arg0) {
									if (mFragment != null) {
										if (mFragment instanceof CuesFragment) {
											if (fullWidthImage != null) {
												Animation bounceInAnim = AnimationUtils.loadAnimation(
														mFragment.getActivity(),
														R.anim.bounce_in);
												fullWidthImage
														.startAnimation(bounceInAnim);
												bounceInAnim
														.setAnimationListener(new AnimationListener() {
															public void onAnimationStart(
																	Animation animation) {
															}

															public void onAnimationRepeat(
																	Animation animation) {
															}

															public void onAnimationEnd(
																	Animation animation) {
																Animation bounceOutAnim = AnimationUtils
																		.loadAnimation(
																				mFragment
																						.getActivity(),
																				R.anim.bounce_out_anim);
																fullWidthImage
																		.startAnimation(bounceOutAnim);
																bounceOutAnim
																		.setAnimationListener(new AnimationListener() {
																			public void onAnimationStart(
																					Animation animation) {
																			}

																			public void onAnimationRepeat(
																					Animation animation) {
																			}

																			public void onAnimationEnd(
																					Animation animation) {
																				((CuesFragment) mFragment)
																						.launchCueStream(cueBean);
																			}
																		});
															}
														});

											}

										}
									}
								}
							});
				}
			}
		}
		return view;
	}

	@Override
	public int getCount() {
		if (mCues != null) {
			return mCues.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (mCues != null && position >= 0 && position < mCues.size()) {
			return mCues.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		if (mCues != null && position >= 0 && position < mCues.size()) {
			return position;
		}
		return -1;
	}

	/**
	 * Sets asset's image
	 * 
	 * @param position
	 * @param assetImage
	 */
	public void setAssetImage(final StreamAsset asset,
			final ImageView assetImage, final ImageView roundedFrame,
			final View viewLayout,  final ProgressWheel progresswheel) {
		if (assetImage == null) {
			return;
		}
		if (asset != null) {
			assetImage.setVisibility(View.INVISIBLE);
			if (roundedFrame != null) {
				roundedFrame.setVisibility(View.INVISIBLE);
			}
			ImageLoader.getInstance().displayImage(asset.getAssetSnapURL(),
					assetImage, options, new ImageLoadingListener() {

						@Override
						public void onLoadingStarted(String imageUri, View view) {

							if (progresswheel != null) {
								progresswheel.incrementProgress(0);
								progresswheel.setVisibility(view.VISIBLE);
								
								RelativeLayout layout = new RelativeLayout(null);
								RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100,100);
								params.addRule(RelativeLayout.CENTER_IN_PARENT);
								layout.addView(progresswheel,params);
							
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
							if (viewLayout != null) {
								viewLayout
										.setBackgroundColor(Color.TRANSPARENT);
							}
							if (roundedFrame != null) {
								roundedFrame.setVisibility(View.VISIBLE);
							}
							assetImage.setVisibility(View.VISIBLE);
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
