package com.paradigmcreatives.apspeak.stream.listeners;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.app.analytics.GoogleAnalyticsHelper;
import com.paradigmcreatives.apspeak.app.model.ASSOCIATION_TYPE;
import com.paradigmcreatives.apspeak.app.model.StreamAsset;
import com.paradigmcreatives.apspeak.app.util.AppPropertiesUtil;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.app.util.constants.GoogleAnalyticsConstants;
import com.paradigmcreatives.apspeak.globalstream.fragments.GlobalStreamsFragment;
import com.paradigmcreatives.apspeak.stream.AppChildActivity;
import com.paradigmcreatives.apspeak.stream.fragments.AssetDetailsFragment;
import com.paradigmcreatives.apspeak.stream.fragments.AssetDetailsWithCommentsFragment;
import com.paradigmcreatives.apspeak.stream.fragments.UserStreamFragment;
import com.paradigmcreatives.apspeak.stream.handlers.RemoveAssetRelationshipHandler;
import com.paradigmcreatives.apspeak.stream.handlers.UpdateAssetRelationshipHandler;
import com.paradigmcreatives.apspeak.stream.tasks.RemoveAssetRelationshipThread;
import com.paradigmcreatives.apspeak.stream.tasks.UpdateAssetRelationshipThread;
import com.paradigmcreatives.apspeak.stream.util.StreamAssetUtil;

/**
 * Listener to detect gestures on User stream
 * 
 * @author Dileep | neuv
 * 
 */
public class UserStreamGestureListener extends
		GestureDetector.SimpleOnGestureListener {

	private Fragment fragment;
	private StreamAsset asset;
	private ImageView assetLoveRepostAnimationIcon;

	public UserStreamGestureListener(final Fragment fragment, StreamAsset asset) {
		super();
		this.fragment = fragment;
		this.asset = asset;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return true;
	}

	@Override
	public boolean onDoubleTap(MotionEvent e) {
		if (fragment != null) {
			if (fragment instanceof UserStreamFragment) {
				GoogleAnalyticsHelper.sendEventToGA(fragment.getActivity(),
						GoogleAnalyticsConstants.USER_STREAM_SCREEN,
						GoogleAnalyticsConstants.ACTION_TAP,
						GoogleAnalyticsConstants.STREAM_LOVE_TAP);
			} else if (fragment instanceof AssetDetailsWithCommentsFragment) {
				GoogleAnalyticsHelper.sendEventToGA(fragment.getActivity(),
						GoogleAnalyticsConstants.STREAM_DETAIL_SCREEN,
						GoogleAnalyticsConstants.ACTION_TAP,
						GoogleAnalyticsConstants.STREAM_LOVE_TAP);
			}
		}
		performLoveOrUnlove();
		return true;
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		if (fragment != null) {
			if (fragment instanceof UserStreamFragment) {
				GoogleAnalyticsHelper.sendEventToGA(fragment.getActivity(),
						GoogleAnalyticsConstants.USER_STREAM_SCREEN,
						GoogleAnalyticsConstants.ACTION_TAP,
						GoogleAnalyticsConstants.STREAM_SELECT_EXPRESSION);
			}
			if (fragment instanceof AssetDetailsWithCommentsFragment) {
				// do nothing
			} else {
				launchAssetDetailsFragment();
			}
		}
		return true;
	}

	/**
	 * Sets asset love/repost animation icon holder
	 * 
	 * @param animationIconHolder
	 */
	public void setAssetAnimationIconHolder(ImageView animationIconHolder) {
		this.assetLoveRepostAnimationIcon = animationIconHolder;
	}

	/**
	 * Launches asset details fragment
	 */
	private void launchAssetDetailsFragment() {
		if (fragment != null) {
			Intent childIntent = new Intent(fragment.getActivity(),
					AppChildActivity.class);
			childIntent.putExtra(Constants.LAUNCH_ASSET_DETAILS, true);
			childIntent.putExtra(Constants.ASSET_OBJECT, asset);
			fragment.getActivity().startActivityForResult(childIntent,
					Constants.ASSET_DETAILS_RESULT_CODE);
		}
	}

	private void performLoveOrUnlove() {
		if (asset != null) {
			if (!asset.getAssetIsLoved()) {
				// Perform asset LOVE action
				// Start LOVE animation
				if (assetLoveRepostAnimationIcon != null) {
					assetLoveRepostAnimationIcon
							.setImageResource(R.drawable.loveani);
					StreamAssetUtil util = new StreamAssetUtil(fragment);
					util.startAnimation(asset, assetLoveRepostAnimationIcon);
				}
				// Update asset LOVE icon and love count
				if (fragment != null) {
					if (fragment instanceof UserStreamFragment) {
						((UserStreamFragment) fragment)
								.assetRelationshipUpdated(asset.getAssetId(),
										ASSOCIATION_TYPE.LOVE);
					} else if (fragment instanceof AssetDetailsFragment) {
						((AssetDetailsFragment) fragment)
								.assetRelationshipUpdated(asset.getAssetId(),
										ASSOCIATION_TYPE.LOVE);
					} else if (fragment instanceof AssetDetailsWithCommentsFragment) {
						((AssetDetailsWithCommentsFragment) fragment)
								.assetRelationshipUpdated(asset.getAssetId(),
										ASSOCIATION_TYPE.LOVE);
					}
				}
				performAssetLove(asset);
			} else {
				// Perform asset UnLOVE action
				// Update asset LOVE icon and love count
				if (fragment != null) {
					if (fragment instanceof UserStreamFragment) {
						((UserStreamFragment) fragment)
								.assetRelationshipRemoveSuccess(
										asset.getAssetId(),
										ASSOCIATION_TYPE.LOVE);
					} else if (fragment instanceof AssetDetailsFragment) {
						((AssetDetailsFragment) fragment)
								.assetRelationshipRemoveSuccess(
										asset.getAssetId(),
										ASSOCIATION_TYPE.LOVE);
					} else if (fragment instanceof AssetDetailsWithCommentsFragment) {
						((AssetDetailsWithCommentsFragment) fragment)
								.assetRelationshipRemoveSuccess(
										asset.getAssetId(),
										ASSOCIATION_TYPE.LOVE);
					}
				}
				performAssetUnLove(asset);
			}
		}
	}

	/**
	 * Makes server request to update Asset Love action
	 */
	public void performAssetLove(StreamAsset asset) {
		if (asset != null && fragment != null) {
			UpdateAssetRelationshipHandler handler = new UpdateAssetRelationshipHandler(
					fragment);
			UpdateAssetRelationshipThread thread = new UpdateAssetRelationshipThread(
					fragment.getActivity(),
					AppPropertiesUtil.getUserID(fragment.getActivity()),
					asset.getAssetId(), ASSOCIATION_TYPE.LOVE, handler);
			thread.start();
		}
	}

	/**
	 * Makes server request to update Asset UnLove action
	 */
	public void performAssetUnLove(StreamAsset asset) {
		if (asset != null && fragment != null) {
			RemoveAssetRelationshipHandler handler = new RemoveAssetRelationshipHandler(
					fragment);
			RemoveAssetRelationshipThread thread = new RemoveAssetRelationshipThread(
					fragment.getActivity(),
					AppPropertiesUtil.getUserID(fragment.getActivity()),
					asset.getAssetId(), ASSOCIATION_TYPE.LOVE, handler);
			thread.start();
		}
	}

	@Override
	public void onLongPress(MotionEvent e) {
		if (fragment instanceof GlobalStreamsFragment) {
		//	((GlobalStreamsFragment) fragment).performLongPress();
		}
		super.onShowPress(e);
	}



}
