package com.paradigmcreatives.apspeak.doodleboard.handlers;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.doodleboard.fragments.ImageFilterFragment;

public class UpdateFilterThumbnailHandler extends Handler {
	private static final int SUCCESS = 2;
	private static final int FAILURE = 3;
	private ImageView thumbnailImageView;
	private LinearLayout imagesLayout;
	private int tag;
	private ImageFilterFragment fragment;

	public UpdateFilterThumbnailHandler(ImageFilterFragment fragment,
			ImageView thumbnailImageView, LinearLayout imagesLayout, int tag) {
		this.thumbnailImageView = thumbnailImageView;
		this.fragment = fragment;
		this.imagesLayout = imagesLayout;
		this.tag = tag;
	}

	@Override
	public void handleMessage(Message msg) {
		if (msg.what == SUCCESS) {
			if (msg.obj != null) {
				updateImage((Bitmap) msg.obj);
			}
		} else if (msg.what == FAILURE) {

		}
		super.handleMessage(msg);
	}

	public void success(Bitmap bitmap) {
		Message msg = new Message();
		msg.what = SUCCESS;
		msg.obj = bitmap;
		sendMessage(msg);
	}

	public void failure() {
		Message msg = new Message();
		msg.what = FAILURE;
		sendMessage(msg);
	}

	private void updateImage(Bitmap bitmap) {
		if (thumbnailImageView != null) {
			thumbnailImageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(final View v) {
					Animation bounceInAnim = AnimationUtils.loadAnimation(
							fragment.getActivity(), R.anim.bounce_in);
					v.startAnimation(bounceInAnim);
					bounceInAnim.setAnimationListener(new AnimationListener() {
						public void onAnimationStart(Animation animation) {
						}

						public void onAnimationRepeat(Animation animation) {
						}

						public void onAnimationEnd(Animation animation) {
							Animation bounceOutAnim = AnimationUtils
									.loadAnimation(fragment.getActivity(),
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
											fragment.applyFilter(v);
										}
									});
						}
					});

				}
			});
			thumbnailImageView.setImageBitmap(bitmap);

			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT);
			lp.setMargins(4, 4, 4, 4);
			thumbnailImageView.setLayoutParams(lp);
			thumbnailImageView.setTag(tag);
			imagesLayout.addView(thumbnailImageView);
		}

	}

}
