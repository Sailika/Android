package com.paradigmcreatives.apspeak.discovery.fragments;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.app.model.Friend;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.discovery.adapters.UserNetworkAdapter;
import com.paradigmcreatives.apspeak.discovery.handlers.FollowFriendsHandler;
import com.paradigmcreatives.apspeak.discovery.handlers.UserNetworkHandler;
import com.paradigmcreatives.apspeak.discovery.listeners.UserNetworkListClickListener;
import com.paradigmcreatives.apspeak.discovery.listeners.WhatsayNetworkOnClickListenerImpl;
import com.paradigmcreatives.apspeak.discovery.tasks.FollowFriendsThread;
import com.paradigmcreatives.apspeak.discovery.tasks.UserNetworkThread;
import com.paradigmcreatives.apspeak.discovery.tasks.helpers.FollowFriendsHelper.Type;
import com.paradigmcreatives.apspeak.discovery.tasks.parsers.StreamAssetFriendsParser;
import com.paradigmcreatives.apspeak.discovery.tasks.parsers.UserNetworkParser;
import com.paradigmcreatives.apspeak.registration.listeners.SearchTextWatcherImpl;
import com.paradigmcreatives.apspeak.registration.listeners.SearchTextWatcherImpl.SEARCH_TYPE;
import com.paradigmcreatives.apspeak.stream.listeners.ListOnScrollListenerImpl;
import com.paradigmcreatives.apspeak.stream.listeners.NextBatchFetchListener;
import com.paradigmcreatives.apspeak.user.UserProfileActivity;

/**
 * This fragment contains all the UI components related to showing the list of
 * friends who are there in the Doodly Doo network and are also friends with the
 * user. A user would be able to follow his/her friends from this screen.
 * 
 * @author robin
 * 
 */
public class UserNetworkFragment extends Fragment implements
NextBatchFetchListener {

	public enum UserNetwork {
		FRIENDS, FOLLOWERS, FOLLOWING, GENERAL, LOVED_USERS, REPOSTED_USERS, SUGGESTED_FRIENDS, FACEBOOK_FRIENDS
	}

	// private Collection<Friend> friendsList;
	private HashMap<UserNetwork, Collection<Friend>> friendsList;
	private ListView friendsNetworkListView;
	private UserNetworkAdapter networkAdapter;
	private String userID = null;
	private UserNetwork network = UserNetwork.GENERAL;
	private boolean fetchListFromServer = true;
	private boolean showHeader = true;
	private boolean showLoadingDialog = true;
	private ListOnScrollListenerImpl mScrollListener;

	private TextView friendsHeader;
	private TextView next;
	private TextView done;
	private TextView invite;

	private RelativeLayout mSearchLayout;
	private EditText mSearchField;

	// Bundle State Keys
	private static final String NETWORK = "network";
	private static final String FETCH_LIST_FROM_SERVER = "fetch_list_from_server";
	private static final String SHOW_COUNT_TEXT = "show_count_text";
	private long loadingtime = 0;

	public UserNetworkFragment() {
		super();
	}

	public UserNetworkFragment(UserNetwork network) {
		this.network = network;
		this.friendsList = new HashMap<UserNetwork, Collection<Friend>>();
	}

	public UserNetworkFragment(UserNetwork network, String userID,
			boolean showFollowAll, boolean showHeader) {
		this.network = network;
		this.userID = userID;
		this.showHeader = showHeader;
		this.friendsList = new HashMap<UserNetwork, Collection<Friend>>();
	}

	/**
	 * Constructor that accepts asset as json, parses list of users who liked
	 * the asset
	 * 
	 * @param assetJSON
	 * @param network
	 */
	public UserNetworkFragment(JSONObject assetJSON, UserNetwork network,
			boolean showFollowAll, boolean showHeader) {
		super();
		this.friendsList = new HashMap<UserNetwork, Collection<Friend>>();
		this.network = network;
		this.fetchListFromServer = false;
		this.showHeader = showHeader;
		// Parse list of friends who liked/reposted the asset
		if (network != null) {
			StreamAssetFriendsParser parser = new StreamAssetFriendsParser(
					assetJSON, network);
			this.friendsList.put(this.network, parser.parse());
		}
	}

	/**
	 * Constructor that accepts UserNetwork and list of Friends of given
	 * UserNetwork type
	 * 
	 * @param assetJSON
	 * @param network
	 * @param showHeader
	 */
	public UserNetworkFragment(UserNetwork network, ArrayList<Friend> list,
			boolean showHeader) {
		super();
		this.network = network;
		this.friendsList = new HashMap<UserNetwork, Collection<Friend>>();
		if (list != null && list.size() > 0) {
			friendsList.put(network, list);
		}
		this.fetchListFromServer = false;
		this.showHeader = showHeader;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.user_network, container, false);
		if (savedInstanceState != null) {
			if (savedInstanceState.getBoolean(FETCH_LIST_FROM_SERVER, true)) {
				fetchListFromServer = true;
			} else {
				fetchListFromServer = false;
			}

			if (savedInstanceState.getBoolean(SHOW_COUNT_TEXT, true)) {
				showHeader = true;
			} else {
				showHeader = false;
			}

			int ordinal = savedInstanceState.getInt(NETWORK, 3);
			network = UserNetwork.values()[ordinal];

		}
		initView(view);
		this.getActivity()
		.getWindow()
		.setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		return view;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		if (outState != null) {
			outState.putBoolean(FETCH_LIST_FROM_SERVER, fetchListFromServer);
			outState.putBoolean(SHOW_COUNT_TEXT, showHeader);
			outState.putInt(NETWORK, network.ordinal());
		}
		super.onSaveInstanceState(outState);
	}

	/**
	 * Initializes various view components of this fragment
	 * 
	 * @param view
	 *            View to initialize
	 */
	private void initView(View view) {
		friendsNetworkListView = (ListView) view
				.findViewById(R.id.network_friends_list);
		next = (TextView) view.findViewById(R.id.network_next_text);
		done = (TextView) view.findViewById(R.id.network_done_text);
		invite = (TextView) view.findViewById(R.id.network_invite_text);
		friendsHeader = (TextView) view.findViewById(R.id.network_friends_text);

		WhatsayNetworkOnClickListenerImpl listener = new WhatsayNetworkOnClickListenerImpl(
				this, network);
		next.setOnClickListener(listener);
		done.setOnClickListener(listener);
		invite.setOnClickListener(listener);

		Activity activity = getActivity();
		if (activity instanceof UserProfileActivity || !showHeader) {
			friendsHeader.setVisibility(View.GONE);
			next.setVisibility(View.GONE);
			done.setVisibility(View.GONE);
			invite.setVisibility(View.GONE);
		}

		mScrollListener = new ListOnScrollListenerImpl(this, false);
		networkAdapter = new UserNetworkAdapter(getActivity(), null, false,
				this, userID, this.network, this.showHeader);
		friendsNetworkListView.setAdapter(networkAdapter);
		friendsNetworkListView.setOnScrollListener(mScrollListener);
		friendsNetworkListView
		.setOnItemClickListener(new UserNetworkListClickListener(
				getActivity(), this, this.network));

		initSearchLayout(view);
		String friendsListString = getJsonStringFromFile(Constants.FRIENDS_LIST_FILENAME);
		if (!TextUtils.isEmpty(friendsListString)) {
			UserNetworkParser parser = new UserNetworkParser(friendsListString,
					network);
			friendsList.put(this.network, parser.parse());
			refreshListView(friendsList);
		}
		if (UserNetworkFragment.this.network != null
				&& UserNetworkFragment.this.network == UserNetwork.FACEBOOK_FRIENDS) {
			fetchNextBatch(0, Constants.FRIENDS_FETCHLIMIT, false);
		} else {
			performServerSyncAndRender();
		}

	}

	/**
	 * Initializes search layout
	 * 
	 * @param view
	 */
	private void initSearchLayout(View view) {
		if (view != null) {
			mSearchLayout = (RelativeLayout) view
					.findViewById(R.id.search_layout);
			mSearchField = (EditText) view.findViewById(R.id.search_field);

			if (this.network == UserNetwork.FACEBOOK_FRIENDS) {
				mSearchLayout.setVisibility(View.VISIBLE);
				mSearchField.addTextChangedListener(new SearchTextWatcherImpl(
						this, friendsNetworkListView,
						SEARCH_TYPE.FACEBOOK_FRIENDS));
			} else {
				mSearchLayout.setVisibility(View.GONE);
			}
		}
	}

	private void performServerSyncAndRender() {

		UserNetworkHandler handler = new UserNetworkHandler(this,
				showLoadingDialog);
		UserNetworkThread thread = new UserNetworkThread(getActivity()
				.getApplicationContext(), handler, userID, network);
		Thread t = new Thread(thread);
		t.start();
	}

	public synchronized void refreshListView(
			HashMap<UserNetwork, Collection<Friend>> friendsNetworkList) {

		this.friendsList = friendsNetworkList;
		if (getActivity() != null) {
			updateUIToFriendsORDiscover();
			loadAdapter();
			loadingtime = System.currentTimeMillis() - loadingtime;
		}
	}

	public synchronized void refreshBatchedListView(
			HashMap<UserNetwork, Collection<Friend>> friendsNetworkList,
			UserNetwork network, int offset) {
		if (!isAdded()) {
			return;
		}

		if (offset <= 0) {
			friendsList = friendsNetworkList;
		} else {
			if (friendsList != null
					&& friendsList.containsKey(UserNetwork.FACEBOOK_FRIENDS)
					&& friendsNetworkList != null
					&& friendsNetworkList
					.containsKey(UserNetwork.FACEBOOK_FRIENDS)) {
				friendsList.get(UserNetwork.FACEBOOK_FRIENDS).addAll(
						friendsNetworkList.get(UserNetwork.FACEBOOK_FRIENDS));
			}
		}
		if (getActivity() != null) {
			updateUIToFriendsORDiscover();
			if (networkAdapter != null
					&& friendsNetworkList != null
					&& friendsNetworkList
					.containsKey(UserNetwork.FACEBOOK_FRIENDS)) {
				if (mScrollListener != null) {
					mScrollListener.setShouldLoadMoreFlag(true);
				}
				networkAdapter.updateAdapter(friendsNetworkList
						.get(UserNetwork.FACEBOOK_FRIENDS));
			} else {
				loadAdapter();
			}
			loadingtime = System.currentTimeMillis() - loadingtime;
		}
	}

	/**
	 * Handles the successful follow all
	 */
	public void processSuccessfulFollowAll() {
		if (network != null) {
			if (network == UserNetwork.FACEBOOK_FRIENDS) {
				Toast.makeText(getActivity(),
						getResources().getString(R.string.invite_success),
						Toast.LENGTH_SHORT).show();
				getActivity().finish();
			}
		}

		// FollowAll happens only when user clicks on NEXT from Friends Screen
		// Here, no need to worry whether FOLLOW ALL request is success or not,
		// blindly take him to Discovery/Stream
		/*
		 * // Update each friend follow status with FOLLOWing icon if
		 * (friendsList != null && friendsList.size() > 0) { ArrayList<Friend>
		 * friends = new ArrayList<Friend>(friendsList.get(this.network)); if
		 * (friends != null) { for (int i = 0; i < friends.size(); i++) { Friend
		 * friend = friends.get(i); if (friend != null) {
		 * friend.setFollowStatus(true); } } refreshListView(friends); } }
		 */
	}

	public void processFailureFollowAll() {
		if (network != null) {
			if (network == UserNetwork.FACEBOOK_FRIENDS) {
				Toast.makeText(getActivity(),
						getResources().getString(R.string.invite_failure),
						Toast.LENGTH_SHORT).show();
				getActivity().finish();
			}
		}
	}

	/**
	 * Gets called when the request to follow/unfollow a user has initiated
	 */
	public void onFollowUnFollowStarted() {

	}

	/**
	 * Gets called when the request to follow/unfollow a friend is success
	 * 
	 * @param friendUserId
	 */
	public void onFollowUnFollowSuccessful(String friendUserId) {
		// With current requirements, user can follow/unfollow a single friend
		// from either Friend's Profile screen or
		// from clicking NEXT(with one friend selected)
		// Hence, control will never come to this point/API
		/*
		 * 1. Update respective Friend object with follow status as true 2.
		 * Update respective Friend list item 3. Show/Hide FOLLOW ALL icon 4. If
		 * login/registration usecase, show AppHomeActivity
		 */
		/*
		 * if (friendsList != null && friendsList.size() > 0) { // Update Friend
		 * object with follow status as true int unFollowFriendsCount = 0; int
		 * position = -1; int i = -1; for (Friend friend : friendsList) { if
		 * (friend != null) { i++; if (friend.getUserId().equals(friendUserId))
		 * { if (friend.getFollowStatus()) { friend.setFollowStatus(false); }
		 * else { friend.setFollowStatus(true); } position = i; } if
		 * (!friend.getFollowStatus()) { // Not following
		 * unFollowFriendsCount++; } } }
		 * 
		 * // Update respective Friend list item if (friendsNetworkListView !=
		 * null && networkAdapter != null) {
		 * networkAdapter.refreshListItemFollowStatusAtPosition(position,
		 * friendsNetworkListView); } }
		 */
	}

	/**
	 * Gets called when the request to follow/unfollow has failed
	 * 
	 * @param failMessage
	 */
	public void onFollowUnFollowFailed(String failMessage) {

	}

	/**
	 * Get user IDs of all the selected friends from the current list
	 * 
	 * @return
	 */
	public List<String> getAllSelectedFriendsIDs() {
		if (networkAdapter != null) {
			return networkAdapter.getAllSelectedFriendsIDs();
		}
		return null;
	}

	public void showError(String error) {
		Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	/**
	 * Updates UI that fits for Discover functionality (If there is atleast one
	 * suggested friend to show)
	 */
	public void updateUIToFriendsORDiscover() {
		// Show Discover friends list if exists, else depends on activity,
		// launch either Stream or InviteFriends
		// fragment
		if (!isAdded()) {
			return;
		}
		if (friendsList != null) {
			if (network == UserNetwork.FRIENDS) {
				if (friendsList.containsKey(UserNetwork.FRIENDS)
						&& friendsList.get(UserNetwork.FRIENDS).size() > 0) {
					friendsHeader.setText(getResources().getString(
							R.string.friends));
					next.setVisibility(View.VISIBLE);
					done.setVisibility(View.GONE);
					invite.setVisibility(View.GONE);
				} else {
					next.performClick();
				}
			} else if (network == UserNetwork.SUGGESTED_FRIENDS) {
				if (friendsList.containsKey(UserNetwork.SUGGESTED_FRIENDS)
						&& friendsList.get(UserNetwork.SUGGESTED_FRIENDS)
						.size() > 0) {
					friendsHeader.setText(getResources().getString(
							R.string.discover));
					next.setVisibility(View.GONE);
					done.setVisibility(View.VISIBLE);
					invite.setVisibility(View.GONE);
				} else {
					done.performClick();
				}
			} else if (network == UserNetwork.FACEBOOK_FRIENDS) {
				if (friendsList.containsKey(UserNetwork.FACEBOOK_FRIENDS)
						&& friendsList.get(UserNetwork.FACEBOOK_FRIENDS).size() > 0) {
					friendsHeader.setVisibility(View.GONE);
					next.setVisibility(View.GONE);
					done.setVisibility(View.GONE);
					invite.setVisibility(View.VISIBLE);
				}
			}
		}
	}

	/**
	 * Loads adapter with respect to UserNetwork type
	 */
	public void loadAdapter() {
		if (!isAdded()) {
			return;
		}
		mScrollListener = new ListOnScrollListenerImpl(this, false);
		networkAdapter = new UserNetworkAdapter(getActivity(),
				friendsList.get(this.network), false, this, userID,
				this.network, this.showHeader);
		if (friendsNetworkListView != null) {
			friendsNetworkListView.setAdapter(networkAdapter);
			friendsNetworkListView.setOnScrollListener(mScrollListener);
		}
		networkAdapter.notifyDataSetChanged();
	}

	public void setUserNetwork(UserNetwork newNetwork) {
		this.network = newNetwork;
	}

	/**
	 * Returns friends list of given UserNetwork type
	 * 
	 * @param networkValue
	 * @return
	 */
	public Collection<Friend> getListForUserNetwork(UserNetwork networkValue) {
		if (friendsList != null && friendsList.size() > 0) {
			return friendsList.get(this.network);
		}
		return null;
	}

	public void setProgressDialogVisibilityFlag(boolean showDialog) {
		this.showLoadingDialog = showDialog;
	}

	public void inviteFriends() {
		if (getAllSelectedFriendsIDs() != null
				&& getAllSelectedFriendsIDs().size() > 0) {
			FollowFriendsHandler inviteHandler = new FollowFriendsHandler(this);
			FollowFriendsThread inviteThread = new FollowFriendsThread(
					this.getActivity(), getUserID(),
					getAllSelectedFriendsIDs(), inviteHandler, Type.INVITE);
			Thread tempInvite = new Thread(inviteThread);
			tempInvite.start();
		} else {
			Toast.makeText(getActivity(),
					getResources().getString(R.string.select_friend_invite),
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void fetchNextBatch(int startIndex, int limit,
			boolean isPullToRefresh) {
		UserNetworkHandler handler = new UserNetworkHandler(this,
				showLoadingDialog, this.network, startIndex);
		UserNetworkThread thread = new UserNetworkThread(getActivity(),
				handler, userID, network, startIndex, limit);
		Thread t = new Thread(thread);
		t.start();
	}

	private String getJsonStringFromFile(String destFileName) {
		StringBuilder text = new StringBuilder();
		try {
			String appPrivateFilesDirectory = this.getActivity().getFilesDir()
					.getAbsolutePath();
			File filesDir = new File(appPrivateFilesDirectory);
			if (!filesDir.exists()) {
				filesDir.mkdirs();
			}

			File destFile = new File(filesDir, destFileName);

			BufferedReader br = new BufferedReader(new FileReader(destFile));
			
			String line;
			while ((line = br.readLine()) != null) {
				text.append(line);
				System.out.println("text : " + text + " : end");
				text.append('\n');
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("hello");

		}
		return text.toString();
	}
}