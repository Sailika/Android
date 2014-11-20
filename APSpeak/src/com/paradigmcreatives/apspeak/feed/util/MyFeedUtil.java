package com.paradigmcreatives.apspeak.feed.util;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.paradigmcreatives.apspeak.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.paradigmcreatives.apspeak.app.model.MyFeedBean;
import com.paradigmcreatives.apspeak.app.util.Util;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.app.util.images.ImageUtil;
import com.paradigmcreatives.apspeak.logging.Logger;

public class MyFeedUtil {

	private static final String TAG = "MyFeedUtil";

	private Fragment fragment;
	private DisplayImageOptions options;

	public MyFeedUtil(Fragment fragment, DisplayImageOptions options) {
		this.fragment = fragment;
		this.options = options;
		ImageLoader.getInstance().init(
				ImageLoaderConfiguration.createDefault(this.fragment
						.getActivity()));
	}

	public MyFeedUtil(Fragment fragment) {
		this.fragment = fragment;
		ImageLoader.getInstance().init(
				ImageLoaderConfiguration.createDefault(this.fragment
						.getActivity()));
	}

	/**
	 * Sets my feed owner picture
	 * 
	 * @param myFeedBean
	 * @param ownerImage
	 */
	public void setMyFeedPic(final MyFeedBean myFeedBean, final ImageView imageView, final String imageURL, final boolean isCircularImage) {
		if (imageView == null) {
			return;
		}
		//imageView.setImageResource(R.drawable.userpic);
		if (myFeedBean != null && !TextUtils.isEmpty(imageURL)) {
			ImageLoader.getInstance().displayImage(
					imageURL, imageView, options,
					new ImageLoadingListener() {

						@Override
						public void onLoadingStarted(String imageUri, View view) {
							// TODO Auto-generated method stub
						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
							// TODO Auto-generated method stub
						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
						    if(isCircularImage){
							    try{
								if (fragment != null) {
									loadedImage = ImageUtil
											.getCircularBitmapResizeTo(
													fragment.getActivity(),
													loadedImage,
													Constants.BUBBLE_IMAGE_SIZE,
													Constants.BUBBLE_IMAGE_SIZE);
									((ImageView) view).setImageBitmap(loadedImage);
								}
							    }catch(Exception e){
								
							    }
						    }
						    /*
						    String type = myFeedBean.getType();
						    if(!TextUtils.isEmpty(type)){
							if(type.equals(Constants.EXPRESSION) || type.equals(Constants.EMOTE)){
								imageView.setVisibility(View.VISIBLE);
							}
						    }
						    */

						}

						@Override
						public void onLoadingCancelled(String imageUri,
								View view) {
							// TODO Auto-generated method stub
						}
					});
		} else {
			Logger.info(TAG, "My Feed Profile pic is null");
		}
	}

	/**
	 * Sets my feed's created date
	 * 
	 * @param myFeedBean
	 * @param dateTextView
	 */
	public void setMyFeedCreateDate(MyFeedBean myFeedBean, TextView dateTextView) {
		if (dateTextView == null) {
			return;
		}
		if (myFeedBean != null) {
			String createdDate = null;
			if (myFeedBean.getTs() != null) {
				createdDate = (TextUtils.isEmpty(myFeedBean.getTs())) ? ""
						: myFeedBean.getTs();
				// Convert createdDate from string to Date
				if (!TextUtils.isEmpty(createdDate)) {
					try {
						long milliSeconds = Long.valueOf(createdDate);
						createdDate = Util.getdate(milliSeconds);
						/*
						 * Calendar calendar = Calendar.getInstance();
						 * calendar.setTimeZone
						 * (TimeZone.getTimeZone("Asia/Calcutta"));
						 * calendar.setTimeInMillis(milliSeconds); int day =
						 * calendar.get(Calendar.DAY_OF_MONTH); String month =
						 * calendar.getDisplayName(Calendar.MONTH,
						 * Calendar.SHORT, Locale.US).toUpperCase(); createdDate
						 * = "" + day + " " + month;
						 */
					} catch (NumberFormatException e) {
						createdDate = "";
					} catch (Exception e) {
						createdDate = "";
					}
				}
			} else {
				createdDate = "";
			}
			dateTextView.setText(createdDate);
			if (fragment != null) {
				dateTextView.setTypeface(Typeface.createFromAsset(fragment
						.getActivity().getAssets(), "Roboto-Regular.ttf"));
			}
		} else {
			Logger.info(TAG, "My Feed created date is null");
		}
	}

}
