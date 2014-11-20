package com.paradigmcreatives.apspeak.cues.adapters;

import java.util.ArrayList;
import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
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
import com.paradigmcreatives.apspeak.app.model.Campaigns;
import com.paradigmcreatives.apspeak.app.model.StreamAsset;
import com.paradigmcreatives.apspeak.app.util.dialogs.ProgressWheel;
import com.paradigmcreatives.apspeak.cues.fragments.CuesFragment;

/**
 * Adapter to hold Whatsay Cues
 * 
 * @author Dileep | neuv
 * 
 */
public class CuesAdapter extends BaseAdapter {

	private Fragment mFragment;
	private ArrayList<Campaigns> mCues;
	private Random mRandom;
	private DisplayImageOptions options;

	public CuesAdapter(final Fragment fragment, ArrayList<Campaigns> cues) {
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
					R.layout.cue_item, null);
		}
		view = convertView;
		if (view != null) {
			// Initialize view with Cue bean content
			int bgcolor = -1;
			int fcolor = -1;
			final Campaigns cueBean = (Campaigns) getItem(position);
			TextView cueMessage = (TextView) view
					.findViewById(R.id.cue_message);
			convertView.setOnClickListener(new ViewOnClickListener(cueBean));
			if (cueBean != null) {
				try {
					bgcolor = Color.parseColor((cueBean.getBackgroundColor()
							.startsWith("#") ? cueBean.getBackgroundColor()
							: ("#" + cueBean.getBackgroundColor())));
				} catch (Exception e) {
					bgcolor = Color.argb(255, mRandom.nextInt(256),
							mRandom.nextInt(256), mRandom.nextInt(256));
				}
				try {
					fcolor = Color.parseColor((cueBean.getForegroundColor()
							.startsWith("#") ? cueBean.getForegroundColor()
							: ("#" + cueBean.getForegroundColor())));
				} catch (Exception e) {
					fcolor = Color.BLACK;
				}

				cueMessage
						.setText((!TextUtils.isEmpty(cueBean.getCueMessage()) ? cueBean
								.getCueMessage() : ""));
				cueMessage.setTextColor(fcolor);
				view.setBackgroundColor(bgcolor);

				ImageView cueThumbnail = (ImageView) view
						.findViewById(R.id.cue_thumbnail);
				ImageView cueRoundedFrame = (ImageView) view
						.findViewById(R.id.rounded_rectangle_frame);
				StreamAsset dummyAsset = new StreamAsset();
				dummyAsset.setAssetSnapURL(cueBean.getBackgroundURL());
				setAssetImage(dummyAsset, cueThumbnail, cueRoundedFrame, view,
						null);

				cueBean.setBackgroundColor(String.format("#%06X",
						0xFFFFFF & bgcolor));
				cueBean.setForegroundColor(String.format("#%06X",
						0xFFFFFF & fcolor));
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
			final View viewLayout, final ProgressWheel progresswheel) {
		if (assetImage == null) {
			return;
		}
		// assetImage.setImageResource(R.drawable.doodle);
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

	public class ViewOnClickListener implements OnClickListener {

		private Campaigns cueBean;

		private ViewOnClickListener(Campaigns cueBean) {
			this.cueBean = cueBean;

		}

		@Override
		public void onClick(final View v) {
			if (mFragment != null) {
				if (mFragment instanceof CuesFragment) {
					Animation bounceInAnim = AnimationUtils.loadAnimation(
							mFragment.getActivity(), R.anim.bounce_in);
					v.startAnimation(bounceInAnim);
					bounceInAnim.setAnimationListener(new AnimationListener() {
						public void onAnimationStart(Animation animation) {
						}

						public void onAnimationRepeat(Animation animation) {
						}

						public void onAnimationEnd(Animation animation) {
							Animation bounceOutAnim = AnimationUtils
									.loadAnimation(mFragment.getActivity(),
											R.anim.bounce_out_anim);

							v.startAnimation(bounceOutAnim);
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
}
