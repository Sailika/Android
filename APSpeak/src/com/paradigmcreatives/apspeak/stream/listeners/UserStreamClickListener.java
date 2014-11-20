package com.paradigmcreatives.apspeak.stream.listeners;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.app.analytics.GoogleAnalyticsHelper;
import com.paradigmcreatives.apspeak.app.model.ASSOCIATION_TYPE;
import com.paradigmcreatives.apspeak.app.model.StreamAsset;
import com.paradigmcreatives.apspeak.app.util.AppPropertiesUtil;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.app.util.constants.GoogleAnalyticsConstants;
import com.paradigmcreatives.apspeak.app.util.dialogs.WhatsayDialogsUtil;
import com.paradigmcreatives.apspeak.app.util.share.ShareUtil;
import com.paradigmcreatives.apspeak.globalstream.fragments.GlobalStreamsFragment;
import com.paradigmcreatives.apspeak.stream.AppChildActivity;
import com.paradigmcreatives.apspeak.stream.fragments.AssetDetailsFragment;
import com.paradigmcreatives.apspeak.stream.fragments.AssetDetailsWithCommentsFragment;
import com.paradigmcreatives.apspeak.stream.fragments.UserStreamFragment;
import com.paradigmcreatives.apspeak.stream.handlers.AssetRepostHandler;
import com.paradigmcreatives.apspeak.stream.handlers.RemoveAssetRelationshipHandler;
import com.paradigmcreatives.apspeak.stream.handlers.UpdateAssetRelationshipHandler;
import com.paradigmcreatives.apspeak.stream.tasks.AssetRepostThread;
import com.paradigmcreatives.apspeak.stream.tasks.RemoveAssetRelationshipThread;
import com.paradigmcreatives.apspeak.stream.tasks.UpdateAssetRelationshipThread;
import com.paradigmcreatives.apspeak.stream.util.StreamAssetUtil;
import com.paradigmcreatives.apspeak.user.UserProfileActivity;

/**
 * Listener to handle different view item clicks in Stream Asset View
 * 
 * @author Dileep | neuv
 * 
 */
public class UserStreamClickListener implements OnClickListener {

	private Fragment fragment;
	private StreamAsset asset;
	private ImageView assetLoveRepostAnimationIcon;
	private String profileUserId;

	public UserStreamClickListener(final Fragment fragment, StreamAsset asset) {
		this.fragment = fragment;
		this.asset = asset;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.asset_owner_pic:
		case R.id.asset_owner_name:
			if (fragment != null) {
				if (fragment instanceof AssetDetailsWithCommentsFragment
						&& v.getId() == R.id.asset_owner_name) {
					GoogleAnalyticsHelper
							.sendEventToGA(
									fragment.getActivity(),
									GoogleAnalyticsConstants.STREAM_DETAIL_SCREEN,
									GoogleAnalyticsConstants.ACTION_BUTTON,
									GoogleAnalyticsConstants.STREAMDETAIL_USERNAME_BUTTON);
				}
			}
			if (asset != null && asset.getAssetOwner() != null
					&& !TextUtils.isEmpty(asset.getAssetOwner().getUserId())) {
				launchUserProfileFragment(asset.getAssetOwner().getUserId());
			}
			break;

		case R.id.asset_image:

			// launchAssetDetailsFragment();
			shareViaAppsDialog(asset.getAssetShareURL());
			/*
			 * TODO: Uncomment for Google Analytics if (fragment != null &&
			 * fragment instanceof UserStreamFragment) {
			 * GoogleAnalytics.sendEventTrackingInfoToGA(fragment.getActivity(),
			 * GoogleAnalyticsConstants.STREAM_SCREEN_CAT_NAME,
			 * GoogleAnalyticsConstants.EVENT_ACTION_IMAGE,
			 * GoogleAnalyticsConstants.STREAM_DOODLE_IMAGE); } else if
			 * (fragment != null && fragment instanceof AssetDetailsFragment) {
			 * GoogleAnalytics.sendEventTrackingInfoToGA(fragment.getActivity(),
			 * GoogleAnalyticsConstants.DOODLE_STREAM_DETAILS_CAT_NAME,
			 * GoogleAnalyticsConstants.EVENT_ACTION_IMAGE,
			 * GoogleAnalyticsConstants.DSD_DOODLE_IMAGE); }
			 */
			break;

		case R.id.asset_action_share:
			if (asset != null && fragment != null) {
				/*
				 * TODO: Uncomment for Google Analytics if (fragment instanceof
				 * UserStreamFragment) {
				 * GoogleAnalytics.sendEventTrackingInfoToGA
				 * (fragment.getActivity(),
				 * GoogleAnalyticsConstants.STREAM_SCREEN_CAT_NAME,
				 * GoogleAnalyticsConstants.EVENT_ACTION_BUTTON,
				 * GoogleAnalyticsConstants.STREAM_SHARE_BUTTON);
				 * 
				 * } else if (fragment instanceof AssetDetailsFragment) {
				 * GoogleAnalytics
				 * .sendEventTrackingInfoToGA(fragment.getActivity(),
				 * GoogleAnalyticsConstants.DOODLE_STREAM_DETAILS_CAT_NAME,
				 * GoogleAnalyticsConstants.EVENT_ACTION_BUTTON,
				 * GoogleAnalyticsConstants.DSD_SHARE_BUTTON);
				 * 
				 * }
				 */
				shareViaAppsDialog(asset.getAssetShareURL());
			}
			break;

		case R.id.assetbottom_action_love:

		case R.id.asset_action_love:
			performAssetLoveOrUnLove();
			break;
		case R.id.share_image:
			if (fragment != null) {
				if (fragment instanceof UserStreamFragment) {

				} else if (fragment instanceof AssetDetailsWithCommentsFragment) {
					((AssetDetailsWithCommentsFragment) fragment).shareAsset();
				}
			}
			break;
		case R.id.asset_action_repost:
			if (asset != null) {
				if (!asset.getAssetIsReposted()) {
					// Perform asset REPOST action
					// Start REPOST animation
					if (assetLoveRepostAnimationIcon != null) {
						assetLoveRepostAnimationIcon
								.setImageResource(R.drawable.repostani);
						StreamAssetUtil util = new StreamAssetUtil(fragment);
						util.startAnimation(asset, assetLoveRepostAnimationIcon);
					}
					// Update asset REPOST icon and reposts count
					if (fragment != null) {
						if (fragment instanceof UserStreamFragment) {
							((UserStreamFragment) fragment)
									.assetRepostSuccess(asset.getAssetId());
						} else if (fragment instanceof AssetDetailsFragment) {
							((AssetDetailsFragment) fragment)
									.assetRepostSuccess(asset.getAssetId());
						}
					}
					performAssetRepost(asset);
				} else {
					// do nothing, as asset already reposted by current user
				}
			}
			/*
			 * TODO: Uncomment for Google Analytics if (fragment != null &&
			 * fragment instanceof UserStreamFragment) {
			 * GoogleAnalytics.sendEventTrackingInfoToGA(fragment.getActivity(),
			 * GoogleAnalyticsConstants.STREAM_SCREEN_CAT_NAME,
			 * GoogleAnalyticsConstants.EVENT_ACTION_BUTTON,
			 * GoogleAnalyticsConstants.STREAM_REPOST_BUTTON);
			 * 
			 * } else if (fragment != null && fragment instanceof
			 * AssetDetailsFragment) {
			 * GoogleAnalytics.sendEventTrackingInfoToGA(fragment.getActivity(),
			 * GoogleAnalyticsConstants.DOODLE_STREAM_DETAILS_CAT_NAME,
			 * GoogleAnalyticsConstants.EVENT_ACTION_BUTTON,
			 * GoogleAnalyticsConstants.DSD_REPOST_BUTTON);
			 * 
			 * }
			 */
			break;

		case R.id.asset_loves:
			performAssetLoveOrUnLove();
			// launchAssetLikedPeoplesList();
			/*
			 * TODO: Uncomment for Google Analytics if (fragment != null &&
			 * fragment instanceof UserStreamFragment) {
			 * GoogleAnalytics.sendEventTrackingInfoToGA(fragment.getActivity(),
			 * GoogleAnalyticsConstants.STREAM_SCREEN_CAT_NAME,
			 * GoogleAnalyticsConstants.EVENT_ACTION_BUTTON,
			 * GoogleAnalyticsConstants.STREAM_LOVES_BUTTON);
			 * 
			 * } else if (fragment != null && fragment instanceof
			 * AssetDetailsFragment) {
			 * GoogleAnalytics.sendEventTrackingInfoToGA(fragment.getActivity(),
			 * GoogleAnalyticsConstants.DOODLE_STREAM_DETAILS_CAT_NAME,
			 * GoogleAnalyticsConstants.EVENT_ACTION_BUTTON,
			 * GoogleAnalyticsConstants.DSD_LOVES_BUTTON);
			 * 
			 * }
			 */
			break;

		case R.id.asset_reposts:
			/*
			 * TODO: Uncomment for Google Analytics if (fragment != null &&
			 * fragment instanceof UserStreamFragment) {
			 * GoogleAnalytics.sendEventTrackingInfoToGA(fragment.getActivity(),
			 * GoogleAnalyticsConstants.STREAM_SCREEN_CAT_NAME,
			 * GoogleAnalyticsConstants.EVENT_ACTION_BUTTON,
			 * GoogleAnalyticsConstants.STREAM_REPOSTS_BUTTON);
			 * 
			 * } else if (fragment != null && fragment instanceof
			 * AssetDetailsFragment) {
			 * GoogleAnalytics.sendEventTrackingInfoToGA(fragment.getActivity(),
			 * GoogleAnalyticsConstants.DOODLE_STREAM_DETAILS_CAT_NAME,
			 * GoogleAnalyticsConstants.EVENT_ACTION_BUTTON,
			 * GoogleAnalyticsConstants.DSD_REPOSTS_BUTTON);
			 * 
			 * }
			 */
			launchAssetRepostedPeoplesList();
			break;
		case R.id.asset_options:
			if (fragment != null) {
				if (fragment instanceof UserStreamFragment) {
					if (asset != null) {
						((UserStreamFragment) fragment)
								.showUserOptionsDialog(asset.getAssetId());
					}
				} else if (fragment instanceof AssetDetailsWithCommentsFragment) {
					if (asset != null) {
						((AssetDetailsWithCommentsFragment) fragment)
								.showUserOptionsDialog();
					}
				} else if (fragment instanceof GlobalStreamsFragment) {
					if (asset != null) {
						String assetId = asset.getAssetId();
						String userId = null;
						if(asset.getAssetOwner() != null){
							userId = asset.getAssetOwner().getUserId();
						}
						((GlobalStreamsFragment) fragment).showUserOptionsDialog(asset.getAssetId(), userId);
					}
				}
			}
			break;
		case R.id.asset_see_all:
			launchAssetLikedPeoplesList();
			break;

		case R.id.assetbottom_action_comment:
			if (fragment != null) {
				/*
				 * if(fragment instanceof AssetDetailsFragment){
				 * ((AssetDetailsFragment)fragment).launchCanvas(); }else
				 * if(fragment instanceof AssetDetailsWithCommentsFragment){
				 * ((AssetDetailsWithCommentsFragment)fragment).launchCanvas();
				 * }
				 */
			}

		default:
			break;
		}
	}

	/**
	 * Sets the userId of the person of whom Profile Fragment needs to be
	 * launched
	 * 
	 * @param userId
	 */
	public void setProfileToLaunch(String userId) {
		this.profileUserId = userId;
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
			fragment.getActivity().startActivity(childIntent);
		}
	}

	/**
	 * Launches user profile fragment
	 */
	private void launchUserProfileFragment(String userId) {
		if (fragment != null && !TextUtils.isEmpty(userId)) {
			Intent intent = new Intent(fragment.getActivity(),
					UserProfileActivity.class);
			intent.putExtra(UserProfileActivity.USER_ID, userId);
			fragment.getActivity().startActivity(intent);
		}
	}

	/**
	 * Launches the Share Via Apps dialog from which asset url can be shared
	 * 
	 * @param shareURL
	 */
	public void shareViaAppsDialog(String shareURL) {
		if (!TextUtils.isEmpty(shareURL) && fragment != null) {
			WhatsayDialogsUtil.shareDoodleDialog(fragment.getActivity(),
					shareURL,
					ShareUtil.getLinkShareAppsList(fragment.getActivity()),
					true).show();
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

	/**
	 * Makes server request to perform Asset Repost action
	 */
	public void performAssetRepost(StreamAsset asset) {
		if (asset != null && fragment != null) {
			AssetRepostHandler handler = new AssetRepostHandler(fragment);
			AssetRepostThread thread = new AssetRepostThread(
					fragment.getActivity(),
					AppPropertiesUtil.getUserID(fragment.getActivity()),
					asset.getAssetId(), handler);
			thread.start();
		}
	}

	/**
	 * Launches list of people who liked the asset
	 */
	public void launchAssetLikedPeoplesList() {
		if (fragment != null && asset != null
				&& !TextUtils.isEmpty(asset.getAssetAsJSON())) {
			Intent childIntent = new Intent(fragment.getActivity(),
					AppChildActivity.class);
			childIntent.putExtra(Constants.LAUNCH_ASSETLIKED_USERSLIST, true);
			if (fragment instanceof AssetDetailsWithCommentsFragment) {
				childIntent.putExtra(Constants.LIKED_USERS,
						((AssetDetailsWithCommentsFragment) fragment)
								.getAssetLikedUsers());
			}
			fragment.getActivity().startActivity(childIntent);
		}
	}

	/**
	 * Launches list of people who reposted the asset
	 */
	public void launchAssetRepostedPeoplesList() {
		if (fragment != null && asset != null
				&& !TextUtils.isEmpty(asset.getAssetAsJSON())) {
			Intent childIntent = new Intent(fragment.getActivity(),
					AppChildActivity.class);
			childIntent
					.putExtra(Constants.LAUNCH_ASSETREPOSTED_USERSLIST, true);
			childIntent.putExtra(Constants.ASSET_AS_JSON,
					asset.getAssetAsJSON());
			fragment.getActivity().startActivity(childIntent);
		}
	}

	/**
	 * Sets asset love/repost animation icon holder
	 * 
	 * @param animationIconHolder
	 */
	public void setAssetAnimationIconHolder(ImageView animationIconHolder) {
		this.assetLoveRepostAnimationIcon = animationIconHolder;
	}

	private void performAssetLoveOrUnLove() {
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
						GoogleAnalyticsHelper.sendEventToGA(
								fragment.getActivity(),
								GoogleAnalyticsConstants.STREAM_DETAIL_SCREEN,
								GoogleAnalyticsConstants.ACTION_BUTTON,
								GoogleAnalyticsConstants.STREAM_LOVE_BUTTON);
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
}
