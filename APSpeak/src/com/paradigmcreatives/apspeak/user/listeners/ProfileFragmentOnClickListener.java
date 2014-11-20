package com.paradigmcreatives.apspeak.user.listeners;

import java.util.ArrayList;

import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.discovery.tasks.helpers.FollowFriendsHelper;
import com.paradigmcreatives.apspeak.user.fragments.ProfileFragment;
import com.paradigmcreatives.apspeak.user.handlers.FollowHandler;
import com.paradigmcreatives.apspeak.user.tasks.FollowUserTask;

public class ProfileFragmentOnClickListener implements OnClickListener, OnPageChangeListener {

	private ProfileFragment fragment = null;
	private TextView followText = null;
	private String userID = null;
	private String currentscreen = null;

	public ProfileFragmentOnClickListener(ProfileFragment fragment, TextView followText, String userID) {
		this.fragment = fragment;
		this.followText = followText;
		this.userID = userID;
		if (fragment != null) {
			currentscreen = fragment.getScreenName();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.follow:

			if (!TextUtils.isEmpty(userID) && followText != null) {
				FollowFriendsHelper.Type type = FollowFriendsHelper.Type.FOLLOW;
				if (TextUtils.equals(followText.getText(), ProfileFragment.FOLLOWING_TEXT)) {
					type = FollowFriendsHelper.Type.UNFOLLOW;
				}

				ArrayList<String> ids = new ArrayList<String>();
				ids.add(userID);

				FollowHandler handler = new FollowHandler(fragment);
				FollowUserTask task = new FollowUserTask(fragment.getActivity().getApplicationContext(), ids, handler,
						type);
				Thread t = new Thread(task);
				t.start();
			}
			break;
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int arg0) {

		switch (arg0) {
		case 0:
		    /* TODO: Uncomment for Google Analytics
			if (fragment != null) {
				if (currentscreen != null && currentscreen.equalsIgnoreCase(GoogleAnalyticsConstants.MY_PROFILE_SCREEN)) {
				
					GoogleAnalytics.sendEventTrackingInfoToGA(fragment.getActivity(),
							GoogleAnalyticsConstants.MY_PROFILE_SCREEN_CAT_NAME,
							GoogleAnalyticsConstants.EVENT_ACTION_SWIPING,
							GoogleAnalyticsConstants.MY_PROFILE_POSTS_SWIPE);
					
				} else {
					GoogleAnalytics.sendEventTrackingInfoToGA(fragment.getActivity(),
							GoogleAnalyticsConstants.FRIEND_PROFILE_SCREEN_CAT_NAME,
							GoogleAnalyticsConstants.EVENT_ACTION_SWIPING,
							GoogleAnalyticsConstants.FRIEND_PROFILE_POSTS_SWIPE);
			
				}

			}
			*/
			break;
		/*
		case 1:
			if (fragment != null) {
				if (currentscreen != null && currentscreen.equalsIgnoreCase(GoogleAnalyticsConstants.MY_PROFILE_SCREEN)) {
					GoogleAnalytics.sendEventTrackingInfoToGA(fragment.getActivity(),
							GoogleAnalyticsConstants.MY_PROFILE_SCREEN_CAT_NAME,
							GoogleAnalyticsConstants.EVENT_ACTION_SWIPING,
							GoogleAnalyticsConstants.MY_PROFILE_TAGS_SWIPE);

				
				} else {
					GoogleAnalytics.sendEventTrackingInfoToGA(fragment.getActivity(),
							GoogleAnalyticsConstants.FRIEND_PROFILE_SCREEN_CAT_NAME,
							GoogleAnalyticsConstants.EVENT_ACTION_SWIPING,
							GoogleAnalyticsConstants.FRIEND_PROFILE_TAGS_SWIPE);
				
				}

			}
			break;
		*/
		case 1:
		    /* TODO: Uncomment for Google Analytics
			if (fragment != null) {
				if (currentscreen != null && currentscreen.equalsIgnoreCase(GoogleAnalyticsConstants.MY_PROFILE_SCREEN)) {
					GoogleAnalytics.sendEventTrackingInfoToGA(fragment.getActivity(),
							GoogleAnalyticsConstants.MY_PROFILE_SCREEN_CAT_NAME,
							GoogleAnalyticsConstants.EVENT_ACTION_SWIPING,
							GoogleAnalyticsConstants.MY_PROFILE_CONNECTION_SWIPE);

					
				} else {
					GoogleAnalytics.sendEventTrackingInfoToGA(fragment.getActivity(),
							GoogleAnalyticsConstants.FRIEND_PROFILE_SCREEN_CAT_NAME,
							GoogleAnalyticsConstants.EVENT_ACTION_SWIPING,
							GoogleAnalyticsConstants.FRIEND_PROFILE_CONNECTION_SWIPE);
				

				}
			}
			*/
			break;

		default:
			break;
		}

	}
}
