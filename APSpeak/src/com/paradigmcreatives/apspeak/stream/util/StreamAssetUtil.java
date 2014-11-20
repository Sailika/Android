package com.paradigmcreatives.apspeak.stream.util;

import java.util.ArrayList;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.paradigmcreatives.apspeak.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.paradigmcreatives.apspeak.app.model.ASSET_TAG;
import com.paradigmcreatives.apspeak.app.model.ASSOCIATION_TYPE;
import com.paradigmcreatives.apspeak.app.model.StreamAsset;
import com.paradigmcreatives.apspeak.app.util.AppPropertiesUtil;
import com.paradigmcreatives.apspeak.app.util.Util;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.app.util.customcontrols.CustomTypefaceSpan;
import com.paradigmcreatives.apspeak.app.util.dialogs.ProgressWheel;
import com.paradigmcreatives.apspeak.app.util.images.ImageUtil;
import com.paradigmcreatives.apspeak.logging.Logger;

public class StreamAssetUtil {

	private static final String TAG = "";

	private Fragment fragment;
	private DisplayImageOptions options;

	public StreamAssetUtil(Fragment fragment, DisplayImageOptions options) {
		this.fragment = fragment;
		this.options = options;
		ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this.fragment.getActivity()));
	}

	public StreamAssetUtil(Fragment fragment) {
		this.fragment = fragment;
		ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this.fragment.getActivity()));
	}

	/**
	 * Sets asset's owner picture
	 * 
	 * @param position
	 * @param ownerImage
	 */
	public void setAssetOwnerPic(StreamAsset asset, ImageView ownerImage) {
		if (ownerImage == null) {
			return;
		}
		ownerImage.setImageResource(R.drawable.userpic);
		if (asset != null && asset.getAssetOwner() != null) {
			ImageLoader.getInstance().displayImage(asset.getAssetOwner().getProfilePicURL(), ownerImage, options,
					new ImageLoadingListener() {

						@Override
						public void onLoadingStarted(String imageUri, View view) {
							// TODO Auto-generated method stub
						}

						@Override
						public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
							// TODO Auto-generated method stub
						}

						@Override
						public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
							if (fragment != null) {
							    try{
								loadedImage = ImageUtil.getCircularBitmapResizeTo(fragment.getActivity(), loadedImage,
									Constants.BUBBLE_IMAGE_SIZE, Constants.BUBBLE_IMAGE_SIZE);
								((ImageView) view).setImageBitmap(loadedImage);
							    }catch(Exception e){
								
							    }
							}
						}

						@Override
						public void onLoadingCancelled(String imageUri, View view) {
							// TODO Auto-generated method stub
						}
					});
		} else {
			Logger.info(TAG, "Asset is null");
		}
	}

	/**
	 * Sets asset's owner name
	 * 
	 * @param position
	 * @param ownerTextView
	 */
	public void setOwnerName(StreamAsset asset, TextView ownerTextView) {
		if (ownerTextView == null) {
			return;
		}
		String defaultName = "Whatsay User";
		if (asset != null) {
			if (fragment != null) {
				defaultName = fragment.getActivity().getString(R.string.whatsay_user);
			}
			String ownerName = null;
			if (asset.getAssetOwner() != null) {
				ownerName = (TextUtils.isEmpty(asset.getAssetOwner().getName())) ? defaultName : asset.getAssetOwner()
						.getName();
			} else {
				ownerName = defaultName;
			}
			ownerTextView.setText(ownerName);
			if (fragment != null) {
				ownerTextView.setTypeface(Typeface.createFromAsset(fragment.getActivity().getAssets(),
						"Roboto-Regular.ttf"));
			}
		} else {
			Logger.info(TAG, "Asset Owner name is null");
		}
	}

	/**
	 * Sets asset owner's group name
	 * 
	 * @param position
	 * @param groupNameTextView
	 */
	public void setOwnerGroupName(StreamAsset asset, TextView ownerGroupTextView) {
		if (ownerGroupTextView == null) {
			return;
		}
		String ownerGroupName = "";
		if (asset != null) {
			if (asset.getAssetOwner() != null) {
				ArrayList<String> groups =  asset.getAssetOwner().getUserGroupNames();
				if(groups != null && groups.size() > 0){
					String groupName = groups.get(0);
					if(!TextUtils.isEmpty(groupName)){
						ownerGroupName = groupName;
					}
				}
			}
			ownerGroupTextView.setText(ownerGroupName);
			if (fragment != null) {
				ownerGroupTextView.setTypeface(Typeface.createFromAsset(fragment.getActivity().getAssets(),
						"Roboto-Regular.ttf"));
			}
		} else {
			Logger.info(TAG, "Asset Owner Group name is null");
		}
	}

	/**
	 * Sets asset's created date
	 * 
	 * @param position
	 * @param dateTextView
	 */
	public void setAssetCreateDate(StreamAsset asset, TextView dateTextView) {
		if (dateTextView == null) {
			return;
		}
		if (asset != null) {
			String createdDate = null;
			if (asset.getAssetCreatedTimestamp() != null) {
				createdDate = (TextUtils.isEmpty(asset.getAssetCreatedTimestamp())) ? "" : asset
						.getAssetCreatedTimestamp();
				// Convert createdDate from string to Date
				if (!TextUtils.isEmpty(createdDate)) {
					try {
						long milliSeconds = Long.valueOf(createdDate);
						createdDate = Util.getdate(milliSeconds);
						/*
						 * Calendar calendar = Calendar.getInstance();
						 * calendar.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
						 * calendar.setTimeInMillis(milliSeconds); int day = calendar.get(Calendar.DAY_OF_MONTH); String
						 * month = calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US).toUpperCase();
						 * createdDate = "" + day + " " + month;
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
				dateTextView.setTypeface(Typeface.createFromAsset(fragment.getActivity().getAssets(),
						"Roboto-Regular.ttf"));
			}
		} else {
			Logger.info(TAG, "Asset created date is null");
		}
	}

	/**
	 * Sets asset's tags
	 * 
	 * @param position
	 * @param tagsTextView
	 */
	public void setAssetTags(StreamAsset asset, TextView tagsTextView) {
		if (tagsTextView == null) {
			return;
		}
		String defaultTagMesage = "No tags";
		if (asset != null) {
			if (fragment != null) {
				defaultTagMesage = fragment.getActivity().getString(R.string.tags);
			}
			String tags = "";
			ASSET_TAG[] tagsArray = asset.getAssetTagsArray();
			if (tagsArray != null && tagsArray.length > 0) {
				for (int i = 0; i < tagsArray.length; i++) {
					tags += tagsArray[i].name();
				}
			} else {
				tags = defaultTagMesage;
			}
			tagsTextView.setText(tags);
		} else {
			Logger.info(TAG, "No tags for asset");
		}
	}

	/**
	 * Sets asset's loves
	 * 
	 * @param position
	 * @param lovesTextView
	 */
	public void setAssetLoves(final StreamAsset asset, TextView lovesTextView) {
		if (lovesTextView == null) {
			return;
		}
		if (asset != null) {
			int lovesCount = 0;
			HashMap<ASSOCIATION_TYPE, Integer> associations = asset.getAssetAssociations();
			if (associations != null && associations.containsKey(ASSOCIATION_TYPE.LOVE)) {
				lovesCount = associations.get(ASSOCIATION_TYPE.LOVE);
			}
			if (fragment != null) {
				String likes = "  ";
				likes += lovesCount;

				Typeface font3 = Typeface.createFromAsset(fragment.getActivity().getAssets(), "Roboto-Regular.ttf");
				SpannableStringBuilder ss = new SpannableStringBuilder(likes);
				// Set different color and font typeface for likes count
				ss.setSpan(
						new ForegroundColorSpan(fragment.getActivity().getResources()
								.getColor(R.color.black)), 0, likes.length(),
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				ss.setSpan(new CustomTypefaceSpan("", font3), 0, likes.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

				/*
				// Set different color and font typeface for remaining text
				ss.setSpan(
						new ForegroundColorSpan(fragment.getActivity().getResources()
								.getColor(R.color.black)), likes.length(), defaultLikesMessage.length(),
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				ss.setSpan(new CustomTypefaceSpan("", font3), likes.length(), defaultLikesMessage.length(),
						Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
				*/
				lovesTextView.setText(ss);
			}
			
			// Set drawable
			if(asset.getAssetIsLoved()){
			    lovesTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.likes_selected, 0, 0, 0);
			}else{
			    lovesTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.likes, 0, 0, 0);
			}

		}
	}

	/**
	 * Sets asset's reposts
	 * 
	 * @param position
	 * @param repostsTextView
	 */
	public void setAssetReposts(StreamAsset asset, TextView repostsTextView) {
		if (repostsTextView == null) {
			return;
		}
		int repostsCount = 0;
		if (asset != null) {
			repostsCount = asset.getAssetRepostsCount();
			String defaultMessage = "Reposts";
			if (repostsCount == 1) {
				defaultMessage = "Repost";
			}
			if (fragment != null) {
				if (repostsCount == 1) {
					defaultMessage = fragment.getActivity().getString(R.string.repost);
				} else {
					defaultMessage = fragment.getActivity().getString(R.string.reposts);
				}
				String reposts = "  ";
				reposts += repostsCount + " ";

				Typeface font3 = Typeface.createFromAsset(fragment.getActivity().getAssets(), "Roboto-Regular.ttf");
				SpannableStringBuilder ss = new SpannableStringBuilder(reposts + defaultMessage);
				// Set different color and font typeface for likes count
				ss.setSpan(
						new ForegroundColorSpan(fragment.getActivity().getResources()
								.getColor(R.color.blue)), 0, reposts.length(),
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				ss.setSpan(new CustomTypefaceSpan("", font3), 0, reposts.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

				// Set different color and font typeface for remaining text
				ss.setSpan(
						new ForegroundColorSpan(fragment.getActivity().getResources()
								.getColor(/*R.color.stream_asset_details*/R.color.black)), reposts.length(), defaultMessage.length(),
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				ss.setSpan(new CustomTypefaceSpan("", font3), reposts.length(), defaultMessage.length(),
						Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

				repostsTextView.setText(ss);
			}
		}
	}

	/**
	 * Sets asset's comments
	 * 
	 * @param position
	 * @param commentsTextView
	 */
	public void setAssetComments(StreamAsset asset, TextView commentsTextView) {
		if (commentsTextView == null) {
			return;
		}
		int commentsCount = 0;
		if (asset != null) {
		    commentsCount = asset.getAssetCommentsCount();
			if (fragment != null) {
				String comments = "  ";
				comments += commentsCount;

				Typeface font3 = Typeface.createFromAsset(fragment.getActivity().getAssets(), "Roboto-Regular.ttf");
				SpannableStringBuilder ss = new SpannableStringBuilder(comments);
				// Set different color and font typeface for likes count
				ss.setSpan(
						new ForegroundColorSpan(fragment.getActivity().getResources()
								.getColor(R.color.black)), 0, comments.length(),
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				ss.setSpan(new CustomTypefaceSpan("", font3), 0, comments.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

				/*
				// Set different color and font typeface for remaining text
				ss.setSpan(
						new ForegroundColorSpan(fragment.getActivity().getResources()
								.getColor(R.color.black)), comments.length(), defaultMessage.length(),
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				ss.setSpan(new CustomTypefaceSpan("", font3), comments.length(), defaultMessage.length(),
						Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
				*/

				commentsTextView.setText(ss);
			}
			
			// Set drawable
			if(asset.getAssetIsCommented()){
			    commentsTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.comments_selected, 0, 0, 0);
			}else{
			    commentsTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.comments, 0, 0, 0);
			}

		}
	}

	/**
	 * Sets asset's image
	 * 
	 * @param position
	 * @param assetImage
	 */
	public void setAssetImage(final StreamAsset asset, final ImageView assetImage, final ProgressWheel progresswheel) {
		if (assetImage == null) {
			return;
		}
		// assetImage.setImageResource(R.drawable.doodle);
		if (asset != null) {
			assetImage.setVisibility(View.INVISIBLE);
			ImageLoader.getInstance().displayImage(asset.getAssetSnapURL(), assetImage, options,
					new ImageLoadingListener() {

						@Override
						public void onLoadingStarted(String imageUri, View view) {

							if (progresswheel != null) {
								progresswheel.incrementProgress(0);
								progresswheel.setVisibility(view.VISIBLE);
							}
						}

						@Override
						public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
							// TODO Auto-generated method stub
							if (progresswheel != null)
								progresswheel.setVisibility(view.GONE);
						}

						@Override
						public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
							if (progresswheel != null)
								progresswheel.setVisibility(view.GONE);
							assetImage.setVisibility(View.VISIBLE);
						}

						@Override
						public void onLoadingCancelled(String imageUri, View view) {
							// TODO Auto-generated method stub
							if (progresswheel != null)
								progresswheel.setVisibility(view.GONE);
						}
					}, new ImageLoadingProgressListener() {

						@Override
						public void onProgressUpdate(String imageUri, View view, int current, int total) {
							if (progresswheel != null)
								progresswheel.incrementProgress((current * 360) / total);
						}
					});

		} else {
			Logger.info(TAG, "Asset is null");
		}
	}

	/**
	 * Sets asset's like icon
	 * 
	 * @param position
	 * @param likeIcon
	 */
	public void setAssetLikeIcon(StreamAsset asset, ImageView likeIcon) {
		if (likeIcon == null) {
			return;
		}
		if (asset != null) {
			if (asset.getAssetIsLoved()) {
				likeIcon.setImageResource(R.drawable.like_selected);
			} else {
				likeIcon.setImageResource(R.drawable.like);
			}
		} else {
			Logger.info(TAG, "Asset is null");
		}
	}

	/**
	 * Starts asset's animation
	 * 
	 * @param animationIcon
	 */
	public void startAnimation(StreamAsset asset, ImageView animationIcon) {
		if (animationIcon == null) {
			return;
		}
		if (asset != null) {
			// if (asset.getAssetIsLoved()) {
			startBeatAnimation(animationIcon);
			// }
		} else {
			Logger.info(TAG, "Asset is null");
		}
	}

	/**
	 * constructs and starts animation to display asset action performed
	 * 
	 */
	private void startBeatAnimation(ImageView animationIcon) {
		if (animationIcon != null && fragment != null) {
			try {
				final Animation temp = AnimationUtils.loadAnimation(fragment.getActivity(), R.anim.beat_animation);
				temp.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onAnimationEnd(Animation animation) {

					}
				});
				animationIcon.startAnimation(temp);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}// end of API constructAndStart_CountDownAnimation

	/**
	 * Sets asset's repost icon
	 * 
	 * @param position
	 * @param repostIcon
	 */
	public void setAssetRepostIcon(StreamAsset asset, ImageView repostIcon) {
		if (repostIcon == null) {
			return;
		}
		if (isCurrentUserAsset(asset)) {
			repostIcon.setVisibility(View.INVISIBLE);
		}
		if (asset != null) {
			if (asset.getAssetIsReposted()) {
				repostIcon.setImageResource(R.drawable.reposttap);
			} else {
				repostIcon.setImageResource(R.drawable.repost);
			}
		} else {
			Logger.info(TAG, "Asset is null");
		}
	}

	/**
	 * Sets asset's comment icon
	 * 
	 * @param position
	 * @param commentIcon
	 */
	public void setAssetCommentIcon(StreamAsset asset, ImageView commentIcon) {
		if (commentIcon == null) {
			return;
		}
		if (asset != null) {
			if (asset.getAssetIsCommented()) {
			    commentIcon.setImageResource(R.drawable.comment_selected);
			} else {
			    commentIcon.setImageResource(R.drawable.comment);
			}
		} else {
			Logger.info(TAG, "Asset is null");
		}
	}

	/**
	 * Checks whether given asset belongs to current user or not
	 * 
	 * @param asset
	 * @return
	 */
	public boolean isCurrentUserAsset(StreamAsset asset) {
		boolean isMyAsset = false;
		if (fragment != null && fragment.getActivity() != null && asset != null && asset.getAssetOwner() != null) {
			String assetOwnerId = asset.getAssetOwner().getUserId();
			String currentUserId = AppPropertiesUtil.getUserID(fragment.getActivity());
			if (!TextUtils.isEmpty(assetOwnerId) && !TextUtils.isEmpty(currentUserId)
					&& assetOwnerId.equals(currentUserId)) {
				isMyAsset = true;
			}
		}
		return isMyAsset;
	}

}
