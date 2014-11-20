package com.paradigmcreatives.apspeak.user;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.paradigmcreatives.apspeak.discovery.fragments.UserNetworkFragment;
import com.paradigmcreatives.apspeak.discovery.fragments.UserNetworkFragment.UserNetwork;

public class ConnectionsPagerAdapter extends FragmentStatePagerAdapter {
	private ArrayList<String> titles = null;
	private String userID = null;

	public ConnectionsPagerAdapter(FragmentManager fm, ArrayList<String> titles) {
		super(fm);
		this.titles = titles;
	}

	public ConnectionsPagerAdapter(FragmentManager fm, ArrayList<String> titles, String userID) {
		super(fm);
		this.titles = titles;
		this.userID = userID;
	}

	@Override
	public Fragment getItem(int arg0) {
		switch (arg0) {
		case 0:
			return new UserNetworkFragment(UserNetwork.FOLLOWERS, userID, false, false);
		case 1:
			return new UserNetworkFragment(UserNetwork.FOLLOWING, userID, false, false);
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

}
