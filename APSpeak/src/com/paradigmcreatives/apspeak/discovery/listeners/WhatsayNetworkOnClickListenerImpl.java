package com.paradigmcreatives.apspeak.discovery.listeners;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.app.analytics.GoogleAnalyticsHelper;
import com.paradigmcreatives.apspeak.app.util.constants.GoogleAnalyticsConstants;
import com.paradigmcreatives.apspeak.discovery.fragments.UserNetworkFragment;
import com.paradigmcreatives.apspeak.discovery.fragments.UserNetworkFragment.UserNetwork;
import com.paradigmcreatives.apspeak.discovery.handlers.FollowFriendsHandler;
import com.paradigmcreatives.apspeak.discovery.tasks.FollowFriendsThread;
import com.paradigmcreatives.apspeak.discovery.tasks.helpers.FollowFriendsHelper.Type;
import com.paradigmcreatives.apspeak.home.AppNewHomeActivity;
import com.paradigmcreatives.apspeak.registration.LoginActivity;

/**
 * DoodlyDoo Network Activity components on click listener implementer
 * 
 * @author Dileep | neuv
 * 
 */
public class WhatsayNetworkOnClickListenerImpl implements OnClickListener {

	private static final String TAG = "WhatsayNetworkOnClickListenerImpl";

	private UserNetworkFragment fragment = null;
	private UserNetwork network = null;

	public WhatsayNetworkOnClickListenerImpl(UserNetworkFragment fragment,
			UserNetwork network) {
		this.fragment = fragment;
		this.network = network;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/*
		 * case R.id.network_friends_backarrow: case
		 * R.id.network_friends_doodlydootext: if (network != null && fragment
		 * != null) { sendScreenInfoToGoogleAnalytics(GoogleAnalyticsConstants.
		 * FOUNDFRIENDS_DDBACK_BUTTON,
		 * GoogleAnalyticsConstants.EVENT_ACTION_TEXTBOX); } break;
		 * 
		 * case R.id.network_friends_skiptext: case R.id.network_friends_skip:
		 * if (fragment != null && network != null) {
		 * sendScreenInfoToGoogleAnalytics
		 * (GoogleAnalyticsConstants.FOUNDFRIENDS_SKIP_BUTTON,
		 * GoogleAnalyticsConstants.EVENT_ACTION_BUTTON); Intent stream = new
		 * Intent(fragment.getActivity(), AppHomeActivity.class);
		 * fragment.startActivity(stream); fragment.getActivity().finish(); }
		 * break;
		 * 
		 * case R.id.network_friends_follow_all: if (fragment != null && network
		 * != null) {
		 * 
		 * sendScreenInfoToGoogleAnalytics(GoogleAnalyticsConstants.
		 * FOUNDFRIENDS_FOLLOWALL_BUTTON,
		 * GoogleAnalyticsConstants.EVENT_ACTION_BUTTON);
		 * 
		 * FollowFriendsHandler handler = new FollowFriendsHandler(fragment);
		 * FollowFriendsThread thread = new
		 * FollowFriendsThread(fragment.getActivity(), fragment.getUserID(),
		 * fragment.getAllFriendsIDs(), handler); Thread t = new Thread(thread);
		 * t.start(); } break;
		 */
		case R.id.network_next_text:
			// Send selected users FOLLOW request
			FollowFriendsHandler handler = new FollowFriendsHandler(fragment);
			FollowFriendsThread thread = new FollowFriendsThread(
					fragment.getActivity(), fragment.getUserID(),
					fragment.getAllSelectedFriendsIDs(), handler, Type.FOLLOW);
			Thread t = new Thread(thread);
			t.start();

			// Change UI to show Discover
			fragment.setUserNetwork(UserNetwork.SUGGESTED_FRIENDS);
			// fragment.updateUIToFriendsORDiscover();
			fragment.loadAdapter();
			break;
		case R.id.network_done_text:
			// Send selected users FOLLOW request
			FollowFriendsHandler followHandler = new FollowFriendsHandler(
					fragment);
			FollowFriendsThread followThread = new FollowFriendsThread(
					fragment.getActivity(), fragment.getUserID(),
					fragment.getAllSelectedFriendsIDs(), followHandler,
					Type.FOLLOW);
			Thread temp = new Thread(followThread);
			temp.start();

			// Record Google Analytics event
			if (fragment != null) {
				GoogleAnalyticsHelper.sendEventToGA(fragment.getActivity(),
						GoogleAnalyticsConstants.FIND_FRIENDS_SCREEN,
						GoogleAnalyticsConstants.ACTION_BUTTON,
						GoogleAnalyticsConstants.FINDFRIENDS_DONE_BUTTON);
			}

			// Launch either Stream or
			Activity activity = fragment.getActivity();
			if (activity != null) {
				if (activity instanceof LoginActivity) {
					// Launch AppHomeActivity and finish current LoginActivity
					// Intent stream = new Intent(activity,
					// AppHomeActivity.class);
					Intent stream = new Intent(activity,
							AppNewHomeActivity.class);
					fragment.startActivity(stream);
					activity.finish();
				}
			}
			break;
		case R.id.network_invite_text:
			fragment.inviteFriends();
			break;
		default:
			break;
		}
	}

	private void sendScreenInfoToGoogleAnalytics(String name, String action) {
		/*
		 * TODO: Uncomment for Gooogle Analytics switch (network) {
		 * 
		 * case FRIENDS:
		 * 
		 * if (fragment.getFromWhereString() != null &&
		 * fragment.getFromWhereString().equalsIgnoreCase(Constants.NEW_USER)) {
		 * GoogleAnalytics.sendEventTrackingInfoToGA(fragment.getActivity(),
		 * GoogleAnalyticsConstants.NETWORKFRIENDS_SCREEN_CAT_NAME, action,
		 * GoogleAnalyticsConstants.FOUNDFRIENDS_NAME + name); } else {
		 * GoogleAnalytics.sendEventTrackingInfoToGA(fragment.getActivity(),
		 * GoogleAnalyticsConstants.FINDFRIENDS_NAME, action,
		 * GoogleAnalyticsConstants.FINDFRIENDS_NAME + name);
		 * 
		 * }
		 * 
		 * break; case FOLLOWERS:
		 * GoogleAnalytics.sendEventTrackingInfoToGA(fragment.getActivity(),
		 * GoogleAnalyticsConstants.FOLLOWER_SCREEN_CAT_NAME, action,
		 * GoogleAnalyticsConstants.FOLLOWER_SCREEN_NAME + name); break; case
		 * FOLLOWING:
		 * GoogleAnalytics.sendEventTrackingInfoToGA(fragment.getActivity(),
		 * GoogleAnalyticsConstants.FOLLOWING_SCREEN_CAT_NAME, action,
		 * GoogleAnalyticsConstants.FOLLOWING_SCREEN_NAME + name); break;
		 * 
		 * default: break; }
		 */
	}

}
