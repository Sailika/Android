package com.paradigmcreatives.apspeak.user.fragments;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.paradigmcreatives.apspeak.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.paradigmcreatives.apspeak.app.model.GroupBean;
import com.paradigmcreatives.apspeak.app.model.StreamAsset;
import com.paradigmcreatives.apspeak.app.model.User;
import com.paradigmcreatives.apspeak.app.util.AppPropertiesUtil;
import com.paradigmcreatives.apspeak.app.util.images.ImageUtil;
import com.paradigmcreatives.apspeak.registration.handlers.GetGroupsListHandler;
import com.paradigmcreatives.apspeak.registration.tasks.GetUserGroupsListThread;
import com.paradigmcreatives.apspeak.stream.fragments.UserStreamFragment;
import com.paradigmcreatives.apspeak.user.UserProfileActivity;
import com.paradigmcreatives.apspeak.user.tasks.GetFriendCompactProfileThread;

/**
 * Fragment for showing the user profile screen and handling all the operations
 * that are possible
 * 
 * @author robin
 * 
 */
public class ProfileFragment extends Fragment {

	private static final String TAG = "ProfileFragment";
	public static final String FOLLOW_TEXT = "Follow";
	public static final String FOLLOWING_TEXT = "Following";

	private String userID = null;
	private TextView name = null;
	private TextView groupNameTextView = null;
	private ImageView picture = null;
	private DisplayImageOptions options = null;
	private ArrayList<String> titles = null;
	private User user = null;
	private Typeface robotoRegular = null;
	private Typeface robotoBold = null;
	private boolean checkFollowStatus = true;
	/*
	 * Disabled Follow/UnFollow feature private FrameLayout followLayout = null;
	 * private ProgressBar followCheck = null; private TextView followText =
	 * null;
	 */
	private String screen_name = null;

	/*
	 * Don't show tabs private TextView posts = null; private TextView
	 * followings = null; private TextView followers = null;
	 */

	private UserStreamFragment postsFragment = null;
	// private UserNetworkFragment followingsFragment = null;
	// private UserNetworkFragment followersFragment = null;
	private FragmentManager fragmentManager = null;

	private LinearLayout postsFragmentLayout = null;
	private LinearLayout followingsFragmentLayout = null;
	private LinearLayout followersFragmentLayout = null;

	public ProfileFragment() {
		super();
	}

	public ProfileFragment(String userID) {
		super();
		this.userID = userID;
		titles = new ArrayList<String>();

	}

	/**
	 * Called when the Fragment is first created.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.profile, container, false);

		this.options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).displayer(new FadeInBitmapDisplayer(250))
				.build();
		if (TextUtils.isEmpty(userID)) {
			userID = AppPropertiesUtil.getUserID(getActivity());
			checkFollowStatus = false;
		} else {
			if (TextUtils.equals(userID,
					AppPropertiesUtil.getUserID(getActivity()))) {
				checkFollowStatus = false;
			}
		}
		initializAllViews(view);
		// Fetch User's compact profile and Group details
		if (!TextUtils.isEmpty(userID)) {
			// Load user name from shared preferences if current app user and
			// profile displaying user are same
			String appUserId = AppPropertiesUtil.getUserID(getActivity());
			if (!TextUtils.isEmpty(appUserId) && !TextUtils.isEmpty(userID)
					&& appUserId.equals(userID)) {
				String appUserName = AppPropertiesUtil
						.getUserName(getActivity());
				if (!TextUtils.isEmpty(appUserName) && name != null) {
					name.setText(appUserName);
				}
			}
			GetFriendCompactProfileThread thread = new GetFriendCompactProfileThread(
					getActivity(), userID, this);
			thread.start();

			// Load user group name from shared preferences if current app user
			// and profile displaying user are same
			setGroupName();
			GetGroupsListHandler handler = new GetGroupsListHandler(this);
			GetUserGroupsListThread groupsListThread = new GetUserGroupsListThread(
					getActivity(), userID, handler);
			groupsListThread.start();
		}

		return view;
	}// end onCreateView

	/**
	 * In this method we initialize all views and setup fonts to views
	 */
	private void setGroupName() {
		String appUserGroupName = AppPropertiesUtil
				.getUserGroupName(getActivity());
		if (!TextUtils.isEmpty(appUserGroupName) && groupNameTextView != null) {
			if (userID.equals(AppPropertiesUtil.getUserID(getActivity()))) {
				groupNameTextView.setText(appUserGroupName);
			}

		}
	}

	private void initializAllViews(View view) {
		robotoRegular = Typeface.createFromAsset(getActivity().getAssets(),
				"Roboto-Regular.ttf");
		robotoBold = Typeface.createFromAsset(getActivity().getAssets(),
				"Roboto-Bold.ttf");

		name = (TextView) view.findViewById(R.id.user_name);
		groupNameTextView = (TextView) view.findViewById(R.id.group_name);
		name.setTypeface(robotoBold);
		groupNameTextView.setTypeface(robotoRegular);

		/*
		 * Disabled Follow/UnFollow feature followLayout = (FrameLayout)
		 * view.findViewById(R.id.follow); followCheck = (ProgressBar)
		 * view.findViewById(R.id.follow_check); followText = (TextView)
		 * view.findViewById(R.id.follow_text);
		 * followText.setTypeface(robotoBold);
		 * followLayout.setOnClickListener(new
		 * ProfileFragmentOnClickListener(this, followText, userID));
		 */

		picture = (ImageView) view.findViewById(R.id.profile_picture);

		/*
		 * Don't show tabs posts = (TextView) view.findViewById(R.id.posts);
		 * followings = (TextView) view.findViewById(R.id.followings); followers
		 * = (TextView) view.findViewById(R.id.followers);
		 */

		postsFragment = new UserStreamFragment(userID, true);
		// followingsFragment = new UserNetworkFragment(UserNetwork.FOLLOWING,
		// userID, false, false);
		// followersFragment = new UserNetworkFragment(UserNetwork.FOLLOWERS,
		// userID, false, false);

		// followingsFragment.setProgressDialogVisibilityFlag(false);
		// followersFragment.setProgressDialogVisibilityFlag(false);

		postsFragmentLayout = (LinearLayout) view
				.findViewById(R.id.postsFragment);
		followingsFragmentLayout = (LinearLayout) view
				.findViewById(R.id.followingsFragment);
		followersFragmentLayout = (LinearLayout) view
				.findViewById(R.id.followersFragment);

		fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();

		// add fragments
		// fragmentTransaction.replace(R.id.followersFragment,
		// followersFragment);
		// fragmentTransaction.replace(R.id.followingsFragment,
		// followingsFragment);
		fragmentTransaction.replace(R.id.postsFragment, postsFragment);

		fragmentTransaction.commit();

		postsFragmentLayout.setVisibility(View.VISIBLE);
		followingsFragmentLayout.setVisibility(View.INVISIBLE);
		followersFragmentLayout.setVisibility(View.INVISIBLE);

		/*
		 * Don't show tabs View.OnClickListener clickListener = new
		 * View.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { Fragment fragment = null;
		 * FragmentTransaction fragmentTransaction =
		 * fragmentManager.beginTransaction(); if(v == posts){ //fragment =
		 * postsFragment; //fragmentTransaction.replace(R.id.postsFragment,
		 * fragment); postsFragmentLayout.setVisibility(View.VISIBLE);
		 * followingsFragmentLayout.setVisibility(View.INVISIBLE);
		 * followersFragmentLayout.setVisibility(View.INVISIBLE);
		 * posts.setBackgroundColor(getResources().getColor(R.color.dark_grey));
		 * followings.setBackgroundColor(getResources().getColor(R.color.grey));
		 * followers.setBackgroundColor(getResources().getColor(R.color.grey));
		 * }else if(v == followings){ //fragment = followingsFragment;
		 * //fragmentTransaction.replace(R.id.followingsFragment, fragment);
		 * postsFragmentLayout.setVisibility(View.INVISIBLE);
		 * followingsFragmentLayout.setVisibility(View.VISIBLE);
		 * followersFragmentLayout.setVisibility(View.INVISIBLE);
		 * posts.setBackgroundColor(getResources().getColor(R.color.grey));
		 * followings
		 * .setBackgroundColor(getResources().getColor(R.color.dark_grey));
		 * followers.setBackgroundColor(getResources().getColor(R.color.grey));
		 * }else if(v == followers){ //fragment = followersFragment;
		 * //fragmentTransaction.replace(R.id.followersFragment, fragment);
		 * postsFragmentLayout.setVisibility(View.INVISIBLE);
		 * followingsFragmentLayout.setVisibility(View.INVISIBLE);
		 * followersFragmentLayout.setVisibility(View.VISIBLE);
		 * posts.setBackgroundColor(getResources().getColor(R.color.grey));
		 * followings.setBackgroundColor(getResources().getColor(R.color.grey));
		 * followers
		 * .setBackgroundColor(getResources().getColor(R.color.dark_grey)); }
		 * fragmentTransaction.commit(); } };
		 * 
		 * posts.setOnClickListener(clickListener);
		 * followings.setOnClickListener(clickListener);
		 * followers.setOnClickListener(clickListener);
		 */

	}

	/**
	 * Handle the UI when the profile fetch of the user is successful
	 * 
	 * @param user
	 */
	public void onSuccessfulProfileFetch(User user) {
		if (user != null && isAdded()) {
			this.user = user;
			/*
			 * Don't show tabs constructTitle(user); if(titles != null &&
			 * titles.size() > 0){ try{ if(posts != null){
			 * posts.setText(titles.get(0)); } if(followings != null){
			 * followings.setText(titles.get(1)); } if(followers != null){
			 * followers.setText(titles.get(2)); } }catch(Exception e){
			 * 
			 * } }
			 */
			if (getActivity() instanceof UserProfileActivity) {
				((UserProfileActivity) getActivity()).setCurrentUser(user);
			}

			if (!TextUtils.isEmpty(user.getName())) {
				name.setText(user.getName());
			}

			if (!TextUtils.isEmpty(user.getProfilePicURL())) {

				ImageLoader.getInstance().init(
						ImageLoaderConfiguration.createDefault(this
								.getActivity()));
				ImageLoader.getInstance().displayImage(user.getProfilePicURL(),
						picture, options, new ImageLoadingListener() {

							@Override
							public void onLoadingStarted(String imageUri,
									View view) {
							}

							@Override
							public void onLoadingFailed(String imageUri,
									View view, FailReason failReason) {
							}

							@Override
							public void onLoadingComplete(String imageUri,
									View view, Bitmap loadedImage) {
								if (view != null && view instanceof ImageView
										&& getActivity() != null) {
									try {
										int radius = ImageUtil
												.getProfilePicBubbleRadius(getActivity()
														.getApplicationContext());
										loadedImage = ImageUtil
												.getCircularBitmapResizeTo(
														getActivity()
																.getApplicationContext(),
														loadedImage, radius,
														radius);
										((ImageView) view)
												.setImageBitmap(loadedImage);
									} catch (Exception e) {

									}
								}
							}

							@Override
							public void onLoadingCancelled(String imageUri,
									View view) {
							}
						}, new ImageLoadingProgressListener() {

							@Override
							public void onProgressUpdate(String imageUri,
									View view, int current, int total) {
								// TODO Auto-generated method stub
							}
						});
			}

			// Store current logged in user details in SharedPreferences
			String appUserId = AppPropertiesUtil.getUserID(getActivity());
			if (!TextUtils.isEmpty(appUserId) && !TextUtils.isEmpty(userID)
					&& appUserId.equals(userID)) {
				if (this.user != null) {
					AppPropertiesUtil
							.setUserName(getActivity(), user.getName());
				}
			}
		}
	}

	/**
	 * Sets current User's group name
	 * 
	 * @param group
	 */
	public void onSuccessfulUserGroupFetch(ArrayList<GroupBean> groupsList) {
		if (groupsList != null && groupsList.size() > 0) {
			GroupBean group = groupsList.get(0);
			if (groupNameTextView != null && group != null
					&& !TextUtils.isEmpty(group.getGroupName())) {
				groupNameTextView.setText(group.getGroupName());

				// Store current logged in user's group name in
				// SharedPreferences
				String appUserId = AppPropertiesUtil.getUserID(getActivity());
				if (!TextUtils.isEmpty(appUserId) && !TextUtils.isEmpty(userID)
						&& appUserId.equals(userID)) {
					AppPropertiesUtil.setUserGroupName(getActivity(),
							group.getGroupName());
				}
			}
		}
	}

	/**
	 * Display the error when profile fetch is unsuccessful
	 * 
	 * @param str
	 */
	public void onError(String str) {

	}

	private void constructTitle(User user) {
		if (user != null) {
			String posts = (user.getPosts() + user.getReposts()) + " "
					+ "Posts";
			titles.add(posts);

			String followers = user.getFollowers() + " Followers";
			String following = user.getFollowing() + " Following";
			titles.add(following);
			titles.add(followers);

		}
	}

	public User getUser() {
		return user;
	}

	/*
	 * Disabled Follow/UnFollow feature private void checkFollowStatus() {
	 * FollowCheckHandler handler = new FollowCheckHandler(new
	 * FollowCheckListener() {
	 * 
	 * @Override public void onSuccess(boolean follows, String status) {
	 * followLayout.setClickable(true); followText.setVisibility(View.VISIBLE);
	 * followCheck.setVisibility(View.GONE);
	 * 
	 * if (follows) { followText.setText(FOLLOWING_TEXT);
	 * followLayout.setBackgroundResource(R.color.green); } else { //
	 * followText.setText(FOLLOW_TEXT); if (!TextUtils.isEmpty(status) &&
	 * status.equalsIgnoreCase("REQUESTED")) { followLayout.setClickable(false);
	 * followText.setText(status); } else { followText.setText(FOLLOW_TEXT); }
	 * followLayout.setBackgroundResource(R.color.black); } }
	 * 
	 * @Override public void onStart() { followLayout.setClickable(false);
	 * followText.setVisibility(View.GONE);
	 * followCheck.setVisibility(View.VISIBLE); }
	 * 
	 * @Override public void onError(String error) { } }); FollowCheckTask task
	 * = new FollowCheckTask(getActivity(),
	 * AppPropertiesUtil.getUserID(getActivity()), userID, handler); Thread t =
	 * new Thread(task); t.start(); }
	 */

	public void onFollowSuccessful() {
		/*
		 * Disabled Follow/UnFollow feature if (isAdded()) {
		 * followLayout.setClickable(true);
		 * followText.setVisibility(View.VISIBLE);
		 * followCheck.setVisibility(View.GONE);
		 * 
		 * if (TextUtils.equals(followText.getText(), "Follow")) {
		 * followText.setText(FOLLOWING_TEXT);
		 * followLayout.setBackgroundResource(R.color.green); } else {
		 * followText.setText(FOLLOW_TEXT);
		 * followLayout.setBackgroundResource(R.color.black); } }
		 */
	}

	public void onFollowStarted() {
		/*
		 * Disabled Follow/UnFollow feature if (isAdded()) {
		 * followLayout.setClickable(false);
		 * followText.setVisibility(View.GONE);
		 * followCheck.setVisibility(View.VISIBLE);
		 * followLayout.setClickable(false); }
		 */
	}

	public void onFollowFailed(String error) {
		if (!TextUtils.isEmpty(error) && isAdded()) {
			Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Sets user stream
	 * 
	 * @param streamAssets
	 */
	public void setUserStream(ArrayList<StreamAsset> streamAssets) {
		if (streamAssets != null && streamAssets.size() > 0 && isAdded()) {
			Fragment fragment = postsFragment;
			if (fragment != null) {
				if (fragment instanceof UserStreamFragment) {
					((UserStreamFragment) fragment).setUserStream(streamAssets);
				}
			}
		}
	}

	public String getScreenName() {
		return screen_name;
	}

	@Override
	public void onResume() {
		super.onResume();
		setGroupName();
	}

	public void refreshStream() {
		postsFragment.refreshStream();
	}

}
