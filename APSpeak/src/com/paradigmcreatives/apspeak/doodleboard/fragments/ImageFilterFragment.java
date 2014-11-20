package com.paradigmcreatives.apspeak.doodleboard.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.gpuimage.GPUImage;
import com.android.gpuimage.GPUImageFilter;
import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.app.util.customcontrols.SquaredItemsFrameLayout;
import com.paradigmcreatives.apspeak.doodleboard.ImageFilterHelper;
import com.paradigmcreatives.apspeak.doodleboard.ImageSelectionFragmentActivity;
import com.paradigmcreatives.apspeak.doodleboard.TouchImageView;
import com.paradigmcreatives.apspeak.doodleboard.ImageFilterHelper.FilterType;
import com.paradigmcreatives.apspeak.doodleboard.handlers.UpdateFilterThumbnailHandler;
import com.paradigmcreatives.apspeak.doodleboard.tasks.GetFiltereBitmapTask;
import com.paradigmcreatives.apspeak.doodleboard.tasks.UpdateFiterThumbnailThread;

public class ImageFilterFragment extends Fragment {
	private Bitmap imageBitmap;

	private TouchImageView mTouchImageView;
	private GPUImageFilter imageFilter;
	private GPUImageFilter mFilter;
	private TextView nextButton;
	private ImageSelectionFragmentActivity activity;
	private SquaredItemsFrameLayout squaredLayout;

	private GPUImage mGPUImage;

	public ImageFilterFragment() {
		super();
	}

	public ImageFilterFragment(ImageSelectionFragmentActivity activity,
			Bitmap imageBitmap) {
		this.imageBitmap = imageBitmap;
		this.activity = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.image_filter_layout, container,
				false);

		initUi(view);
		if (activity != null) {
			activity.stopProgress();
		}
		return view;
	}

	private void initUi(View view) {
		mGPUImage = new GPUImage(this.getActivity());

		squaredLayout = (SquaredItemsFrameLayout) view
				.findViewById(R.id.image_preview_layout);
		Log.i("ImageFilterFragment", "Dimensions:" + squaredLayout.getWidth()
				+ " " + squaredLayout.getHeight());
		mTouchImageView = (TouchImageView) view.findViewById(R.id.gpuimage);

		nextButton = (TextView) view.findViewById(R.id.next_text);
		nextButton.setOnClickListener(new NextOnClickListener());
		Bitmap bitmap = null;

		if (imageBitmap != null) {
			bitmap = imageBitmap;
		}

		if (bitmap != null) {
			mGPUImage.setImage(bitmap);
			mTouchImageView.setImageBitmap(bitmap);
		}

		LinearLayout imagesLayout = (LinearLayout) view
				.findViewById(R.id.images_layout);
		final String filternames[] = ImageFilterHelper.filternames;
		Bitmap resizedThumbnail = null;
		int scaledWidth = 200;
		int scaledHeight = 200;

		if (bitmap != null) {
			resizedThumbnail = Bitmap.createScaledBitmap(bitmap, scaledWidth,
					scaledHeight, true);
		}
		ImageView imageView = new ImageView(this.getActivity());
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		params.setMargins(4, 4, 4, 4);

		imageView.setLayoutParams(params);
		imageView.setImageBitmap(resizedThumbnail);
		imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				Animation bounceInAnim = AnimationUtils.loadAnimation(
						ImageFilterFragment.this.getActivity(),
						R.anim.bounce_in);
				v.startAnimation(bounceInAnim);
				bounceInAnim.setAnimationListener(new AnimationListener() {
					public void onAnimationStart(Animation animation) {
					}

					public void onAnimationRepeat(Animation animation) {
					}

					public void onAnimationEnd(Animation animation) {
						Animation bounceOutAnim = AnimationUtils.loadAnimation(
								ImageFilterFragment.this.getActivity(),
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
										imageFilter = new GPUImageFilter();
										switchFilterTo(imageFilter);
									}
								});
					}
				});

			}

		});
		imagesLayout.addView(imageView);
		for (int x = 0; x < ImageFilterHelper.FILTERS_WITHOUT_ICONS; x++) {
			ImageView thumbnailImageView = new ImageView(getActivity());

			UpdateFilterThumbnailHandler handler = new UpdateFilterThumbnailHandler(
					this, thumbnailImageView, imagesLayout, x);
			UpdateFiterThumbnailThread updateThumbnailImageThread = new UpdateFiterThumbnailThread(
					this.getActivity(), handler, resizedThumbnail,
					filternames[x]);
			Thread t = new Thread(updateThumbnailImageThread);
			t.start();

		}
	}

	public void applyFilter(View v) {
		int position = 0;

		if (v.getTag() instanceof Integer) {
			position = (Integer) v.getTag();
		}
		final String filternames[] = ImageFilterHelper.filternames;
		FilterType filterType = FilterType.valueOf(filternames[position]);
		imageFilter = ImageFilterHelper.createFilterForType(
				ImageFilterFragment.this.getActivity(), filterType);
		switchFilterTo(imageFilter);

	}

	private void switchFilterTo(final GPUImageFilter filter) {
		if (mFilter == null
				|| (filter != null && !mFilter.getClass().equals(
						filter.getClass()))) {
			mFilter = filter;
			mGPUImage.setFilter(mFilter);
			mGPUImage.requestRender();
			(new GetFiltereBitmapTask(this, mGPUImage)).execute("");

		}
	}

	public void setFilteredBitmap(Bitmap bitmap) {
		mTouchImageView.setImageBitmap(bitmap);
	}

	private void showCanvasFragment(Bitmap bitmap) {
		((ImageSelectionFragmentActivity) this.getActivity())
				.showCanvasFragment(bitmap);

	}

	private class NextOnClickListener implements OnClickListener {
		@Override
		public void onClick(final View v) {
			Animation bounceInAnim = AnimationUtils.loadAnimation(
					ImageFilterFragment.this.getActivity(), R.anim.bounce_in);
			v.startAnimation(bounceInAnim);
			bounceInAnim.setAnimationListener(new AnimationListener() {
				public void onAnimationStart(Animation animation) {
				}

				public void onAnimationRepeat(Animation animation) {
				}

				public void onAnimationEnd(Animation animation) {
					Animation bounceOutAnim = AnimationUtils.loadAnimation(
							ImageFilterFragment.this.getActivity(),
							R.anim.bounce_out_anim);
					v.startAnimation(bounceOutAnim);
					bounceOutAnim.setAnimationListener(new AnimationListener() {
						public void onAnimationStart(Animation animation) {
						}

						public void onAnimationRepeat(Animation animation) {
						}

						public void onAnimationEnd(Animation animation) {
							mTouchImageView.setDrawingCacheEnabled(true);
							Bitmap bitmap = Bitmap.createBitmap(mTouchImageView
									.getDrawingCache());
							mTouchImageView.setDrawingCacheEnabled(false);
							showCanvasFragment(bitmap);
						}
					});
				}
			});

		}
	}

}