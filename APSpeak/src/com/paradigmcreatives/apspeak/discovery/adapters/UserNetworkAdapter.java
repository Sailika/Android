package com.paradigmcreatives.apspeak.discovery.adapters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.paradigmcreatives.apspeak.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.paradigmcreatives.apspeak.app.analytics.GoogleAnalyticsHelper;
import com.paradigmcreatives.apspeak.app.model.Friend;
import com.paradigmcreatives.apspeak.app.util.AppPropertiesUtil;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.app.util.constants.GoogleAnalyticsConstants;
import com.paradigmcreatives.apspeak.app.util.images.ImageUtil;
import com.paradigmcreatives.apspeak.discovery.fragments.UserNetworkFragment.UserNetwork;
import com.paradigmcreatives.apspeak.logging.Logger;
import com.paradigmcreatives.apspeak.user.UserProfileActivity;

/**
 * Class used as adapter to hold list of Whatsay network friends
 * 
 * @author Dileep | neuv
 * 
 */
public class UserNetworkAdapter extends BaseAdapter implements Filterable {

	private static final String TAG = "UserNetworkAdapter";

	private Activity activity;
	private Collection<Friend> friends;
	private Collection<Friend> filteredFriends;
	private String userId;
	private Typeface robotoRegular = null;
	private Typeface robotoBold = null;
	private DisplayImageOptions options = null;
	private Fragment fragment = null;
	private UserNetwork network = null;
	private boolean showHeader = true;
	private String searchText = "";

	public UserNetworkAdapter(Activity activity,
			Collection<Friend> listOfFriends, boolean isFollowingList,
			Fragment fragment, String userId, UserNetwork network,
			boolean showHeaderFlag) {
		super();
		this.activity = activity;
		this.friends = listOfFriends;
		if (listOfFriends != null) {
			this.filteredFriends = new ArrayList<Friend>(listOfFriends);
		} else {
			this.filteredFriends = null;
		}

		robotoRegular = Typeface.createFromAsset(activity.getAssets(),
				"Roboto-Regular.ttf");
		robotoBold = Typeface.createFromAsset(activity.getAssets(),
				"Roboto-Bold.ttf");
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).displayer(new FadeInBitmapDisplayer(250))
				.build();
		this.fragment = fragment;
		this.userId = userId;
		this.network = network;
		this.showHeader = showHeaderFlag;

		resetFollowStatusIfRequired();
		ImageLoader.getInstance().init(
				ImageLoaderConfiguration.createDefault(fragment.getActivity()));
	}

	/**
	 * @return the filteredFriends
	 */
	public Collection<Friend> getFilteredFriends() {
		return filteredFriends;
	}

	/**
	 * @param filteredFriends
	 *            the filteredFriends to set
	 */
	public void setFilteredFriends(ArrayList<Friend> friends) {
		this.filteredFriends = friends;
	}

	/**
	 * Sets the user network value
	 * 
	 * @param newNetwork
	 */
	public void setUserNetworkValue(UserNetwork newNetwork) {
		this.network = newNetwork;
	}

	@Override
	public int getCount() {
		if (filteredFriends != null) {
			return filteredFriends.size();
		} else {
			return 0;
		}
	}

	@Override
	public Friend getItem(int position) {
		if (filteredFriends != null) {
			return (Friend) filteredFriends.toArray()[position];
		} else {
			return null;
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(activity).inflate(
					R.layout.friends_network_list_item_v2, null);

			viewHolder.friendImage = (ImageView) convertView
					.findViewById(R.id.friend_contact_pic);
			viewHolder.friendName = (TextView) convertView
					.findViewById(R.id.friend_name);
			viewHolder.friendLocation = (TextView) convertView
					.findViewById(R.id.friend_location);
			viewHolder.followIcon = (ImageView) convertView
					.findViewById(R.id.friend_invite_pic);
			viewHolder.followIcon.setClickable(true);
			convertView.setFocusable(false);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.friendName.setTypeface(robotoBold);
		viewHolder.friendLocation.setTypeface(robotoRegular);

		if (viewHolder != null) {
			Friend friend = getItem(position);
			setFriendImage(friend, viewHolder.friendImage);
			setFriendName(friend, viewHolder.friendName);
			setFriendLocation(friend, viewHolder.friendLocation);
			setFollowIcon(friend, viewHolder.followIcon);
			setFollowIconClickListener(friend, viewHolder.followIcon);
		}
		return convertView;
	}

	/**
	 * Refreshes the particular item
	 * 
	 * @param position
	 */
	public synchronized void refreshListItemFollowStatusAtPosition(
			int position, ListView listView) {
		if (listView != null && position >= listView.getFirstVisiblePosition()
				&& position <= listView.getLastVisiblePosition()) {
			View listItem = listView.getChildAt(position
					- listView.getFirstVisiblePosition());
			ImageView inviteIcon = (ImageView) listItem
					.findViewById(R.id.friend_invite_pic);
			setFollowIcon(getItem(position), inviteIcon);
			setFollowIconClickListener(getItem(position), inviteIcon);
		}
	}

	private void setFriendName(Friend friend, TextView friendStringText) {
		if (friendStringText == null) {
			return;
		}
		Friend friendObject = friend;
		if (friendObject != null) {
			String friendName = null;
			if (friendObject.getName() != null) {
				friendName = (TextUtils.isEmpty(friendObject.getName())) ? "Anonymous"
						: friendObject.getName();
			} else {
				if (activity != null) {
					friendName = activity.getString(R.string.whatsay_user);
				}
			}
			friendStringText.setText(friendName);
		} else {
			Logger.info(TAG, "Friend object is null");
		}
	}

	private void setFriendLocation(Friend friend, TextView friendDetailsText) {
		if (friendDetailsText == null) {
			return;
		}
		Friend friendObject = friend;
		if (friendObject != null) {
			String friendLocation = null;
			if (!TextUtils.isEmpty(friendObject.getLocation())
					&& !friendObject.getLocation().equalsIgnoreCase("null")) {
				friendLocation = friendObject.getLocation();
			} else {
				if (activity != null) {
					friendLocation = "";
				}
			}

			friendDetailsText.setText(friendLocation);
		} else {
			Logger.info(TAG, "Friend object is null");
		}
	}

	private void setFriendImage(Friend friend, ImageView friendImage) {
		if (friendImage == null) {
			return;
		}

		friendImage.setImageResource(R.drawable.userpic);
		Friend friendObject = friend;
		if (friendObject != null
				&& !TextUtils.isEmpty(friendObject.getProfilePicURL())
				&& friendObject.getProfilePicURL().startsWith("http")) {
			Logger.info(TAG, friendObject.getProfilePicURL());

			ImageLoader.getInstance().displayImage(
					friendObject.getProfilePicURL(), friendImage, options,
					new ImageLoadingListener() {

						@Override
						public void onLoadingStarted(String imageUri, View view) {

						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {

						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							try {
								if (view instanceof ImageView) {
									loadedImage = ImageUtil
											.getCircularBitmapResizeTo(
													activity,
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
		}
	}

	private void setFollowIcon(Friend friend, ImageView inviteIcon) {
		// inviteIcon.setVisibility(View.INVISIBLE);
		if (inviteIcon == null) {
			return;
		}
		String currentUserId = AppPropertiesUtil.getUserID(activity);
		if ((!TextUtils.isEmpty(userId) && !TextUtils.isEmpty(currentUserId) && !userId
				.equals(currentUserId))
				|| !showHeader
				&& network != UserNetwork.FACEBOOK_FRIENDS) {
			// Do not show Invite icon for other user's friends list, ex: From
			// User Profile Screen (or)
			// When we are not showing Header
			inviteIcon.setVisibility(View.INVISIBLE);
			return;
		}

		inviteIcon.setVisibility(View.VISIBLE);
		Friend friendObject = friend;
		if ((!TextUtils.isEmpty(friend.getUserId()))) {
			// Do not show Invite icon for other user's friends list, ex: From
			// User Profile Screen (or)
			// When we are not showing Header
			if (network == UserNetwork.FACEBOOK_FRIENDS) {
				inviteIcon.setImageResource(R.drawable.whatsay_user);
				return;
			}
		} else if (friendObject != null) {
			if (friendObject.getFollowStatus()/* || isFollowingList */) {
				// inviteIcon.setImageResource(R.drawable.invitefriends);
				inviteIcon.setImageResource(R.drawable.following);
			} else {
				// inviteIcon.setImageResource(R.drawable.unfollowicon);
				inviteIcon.setImageResource(R.drawable.follow);
			}
		}
	}

	/**
	 * Sets invite icon click listener
	 * 
	 * @param position
	 * @param followIcon
	 */
	private void setFollowIconClickListener(Friend friend,
			final ImageView followIcon) {
		if (followIcon == null) {
			return;
		}
		final Friend friendObject = friend;
		if (friendObject != null) {
			if (friendObject.getUserId() != null
					|| (!TextUtils.isEmpty(friendObject.getFacebookId())
							&& network != null && network == UserNetwork.FACEBOOK_FRIENDS)) {
				followIcon.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						if (!TextUtils.isEmpty(friendObject.getUserId())) {

							Intent intent = new Intent(fragment.getActivity(),
									UserProfileActivity.class);
							intent.putExtra(UserProfileActivity.USER_ID,
									friendObject.getUserId());
							fragment.getActivity().startActivity(intent);

						} else
						// Just change the Follow/UnFollow icon to
						// UnFollow/Follow
						// The actual request will be sent only on clicking
						// either NEXT or DONE
						if (friendObject.getFollowStatus()) {
							friendObject.setFollowStatus(false);
						} else {
							friendObject.setFollowStatus(true);
						}
						setFollowIcon(friendObject, followIcon);

						if (activity != null) {
							if (friendObject.getFollowStatus()) {
								GoogleAnalyticsHelper
										.sendEventToGA(
												activity,
												GoogleAnalyticsConstants.FIND_FRIENDS_SCREEN,
												GoogleAnalyticsConstants.ACTION_BUTTON,
												GoogleAnalyticsConstants.FINDFRIENDS_UNFOLLOW_CHECK);
							} else {
								GoogleAnalyticsHelper
										.sendEventToGA(
												activity,
												GoogleAnalyticsConstants.FIND_FRIENDS_SCREEN,
												GoogleAnalyticsConstants.ACTION_BUTTON,
												GoogleAnalyticsConstants.FINDFRIENDS_FOLLOW_CHECK);
							}
						}
						/*
						 * ArrayList<String> userIds = new ArrayList<String>();
						 * userIds.add(friendObject.getUserId());
						 * FollowFriendsHelper.Type type =
						 * FollowFriendsHelper.Type.FOLLOW; if
						 * (friendObject.getFollowStatus()) { // Following; make
						 * request with server to unfollow friend type =
						 * FollowFriendsHelper.Type.UNFOLLOW; } FollowHandler
						 * handler = new FollowHandler(fragment); FollowUserTask
						 * task = new FollowUserTask(fragment.getActivity().
						 * getApplicationContext(), userIds, handler, type);
						 * Thread t = new Thread(task); t.start();
						 */
					}
				});
			} else {
				// do nothing
			}
		} else {
			Logger.info(TAG, "Friend object is null");
		}
	}

	/**
	 * Returns all the friends of whom Follow status is true
	 * 
	 * @return
	 */
	public List<String> getAllSelectedFriendsIDs() {
		List<String> selectedFriendsList = null;
		if (friends != null && friends.size() > 0) {
			selectedFriendsList = new ArrayList<String>();
			for (Friend object : friends) {
				if (object.getFollowStatus()) {
					if (network != null
							&& network == UserNetwork.FACEBOOK_FRIENDS) {
						selectedFriendsList.add(object.getFacebookId());
					} else {
						selectedFriendsList.add(object.getUserId());
					}
				}
			}
		}
		return selectedFriendsList;
	}

	/**
	 * Resets FollowStatus to true for all the friends in the current list,
	 * provided current list's UserNetwork is either FRIENDS or
	 * SUGGESTED_FRIENDS
	 */
	private void resetFollowStatusIfRequired() {
		if (friends != null
				&& friends.size() > 0
				&& network != null
				&& (network == UserNetwork.FRIENDS || network == UserNetwork.SUGGESTED_FRIENDS)) {
			for (Friend object : friends) {
				object.setFollowStatus(true);
			}
		}
	}

	public void updateAdapter(Collection<Friend> newFriendsList) {
		if (newFriendsList != null && newFriendsList.size() > 0) {
			if (friends != null) {
				friends.addAll(newFriendsList);
				getFilter().filter(searchText);
			} else {
				friends = newFriendsList;
				filteredFriends = new ArrayList<Friend>(newFriendsList);
			}
			notifyDataSetChanged();
		}
	}

	public Collection<Friend> getListOfFriends() {
		return friends;
	}

	public void setListOfFriends(ArrayList<Friend> listOfFriends) {
		this.friends = listOfFriends;
	}

	private class ViewHolder {
		private ImageView friendImage;
		private TextView friendName;
		private TextView friendLocation;
		private ImageView followIcon;
	}

	@Override
	public Filter getFilter() {
		return new FriendsFilter(this);
	}

	public void setSearchText(CharSequence searchWord) {
		if (searchWord != null && !TextUtils.isEmpty(searchWord)) {
			this.searchText = searchWord.toString();
		} else {
			this.searchText = "";
		}
	}
}
