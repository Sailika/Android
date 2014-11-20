package com.paradigmcreatives.apspeak.user;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.paradigmcreatives.apspeak.discovery.fragments.UserNetworkFragment;
import com.paradigmcreatives.apspeak.discovery.fragments.UserNetworkFragment.UserNetwork;
import com.paradigmcreatives.apspeak.stream.fragments.UserStreamFragment;
import com.paradigmcreatives.apspeak.user.fragments.ProfileFragment;

/**
 * <code>ViewPager</code> adapter for showing the user's info
 * 
 * @author robin
 * 
 */
public class UserInfoPagerAdapter extends FragmentStatePagerAdapter {

	private Fragment userStreamFragment;
	private ArrayList<String> titles = null;
	private String userID = null;
//	public static int CONNECTIONS_FRAGMENT_INDEX = 2;
	public static int USER_STREAM_FRAGMENT_INDEX = 0;
	private ProfileFragment fragment = null;

	public UserInfoPagerAdapter(FragmentManager fm, ArrayList<String> titles,
			String userID, ProfileFragment frgment) {
		super(fm);
		this.titles = titles;
		this.userID = userID;
		this.fragment = frgment;
	}

	@Override
	public Fragment getItem(int arg0) {
		switch (arg0) {
		case 0:
			// return new UserTagsFragment();
			userStreamFragment = new UserStreamFragment(userID, true);
			return userStreamFragment;
			/*
			 * case 1: return new UserTagsFragment();
			 */
		case 1:
			return new UserNetworkFragment(UserNetwork.FOLLOWING, userID,
					false, false);
		case 2:

			return new UserNetworkFragment(UserNetwork.FOLLOWERS, userID,
					false, false);
		case 3:
			return new UserTagsFragment();
		default:
			return new UserTagsFragment();
		}
	}

	@Override
	public int getCount() {
		if (titles != null) {
			return titles.size();
		}
		return 0;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		if (titles != null && titles.size() > position) {
			return titles.get(position);
		}
		return "";
	}

	public void setTitles(ArrayList<String> titles) {
		this.titles = titles;
	}

	public Fragment getUserStreamFragment() {
		return userStreamFragment;
	}
}
