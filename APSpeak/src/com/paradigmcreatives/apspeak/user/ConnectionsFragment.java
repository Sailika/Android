package com.paradigmcreatives.apspeak.user;
/*package com.paradigmcreatives.apspeak.user;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.app.model.User;
import com.paradigmcreatives.apspeak.navdrawer.AppHomeActivity;
import com.paradigmcreatives.apspeak.user.fragments.ProfileFragment;
import com.paradigmcreatives.apspeak.user.listeners.ConnectionFragmentlistener;
import com.viewpagerindicator.TitlePageIndicator;
import com.viewpagerindicator.TitlePageIndicator.IndicatorStyle;

public class ConnectionsFragment extends Fragment {

	private ConnectionsPagerAdapter adapter = null;
	private User user = null;
	private ProfileFragment parentfragment=null;
	
	public ConnectionsFragment(){
		
	}
	
	public ConnectionsFragment(ProfileFragment fragment){
		this.parentfragment=fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.user_connections, container, false);
		initializAllViews(view);
		return view;

	}

	private void initializAllViews(View view) {
		String userID = null;
		if (getActivity() instanceof AppHomeActivity) {
			this.user = ((AppHomeActivity) getActivity()).getCurrentUser();
		} else if (getActivity() instanceof UserProfileActivity) {
			this.user = ((UserProfileActivity) getActivity()).getCurrentUser();
		}

		if (user != null) {
			userID = user.getUserId();
		}

		ViewPager pager = (ViewPager) view.findViewById(R.id.pager);
		adapter = new ConnectionsPagerAdapter(getChildFragmentManager(), null, userID);
		pager.setAdapter(adapter);
		updateTitles(this.user);

		TitlePageIndicator indicator = (TitlePageIndicator) view.findViewById(R.id.indicator);
		indicator.setViewPager(pager);
        indicator.setOnPageChangeListener(new ConnectionFragmentlistener(this));
		final float density = getResources().getDisplayMetrics().density;
		indicator.setBackgroundColor(0xFFFFFFFF);
		indicator.setFooterColor(0xFF666666);
		indicator.setFooterLineHeight(1 * density); // 1dp
		indicator.setFooterIndicatorHeight(1 * density); // 3dp
		indicator.setFooterIndicatorStyle(IndicatorStyle.Underline);
		indicator.setTextColor(0xFF666666);
		indicator.setSelectedColor(0xFF666666);
		indicator.setSelectedBold(true);
		indicator.setTextSize(10 * density);

	}

	*//**
	 * Updates the titles of this <code>ViewPager</code>
	 * 
	 * @param titles
	 *//*
	public void onTitlesUpdates(ArrayList<String> titles) {
		if (titles != null && adapter != null) {
			adapter.setTitles(titles);
			adapter.notifyDataSetChanged();
		}
	}

	private void updateTitles(User user) {

		if (user != null && adapter != null) {
			ArrayList<String> titles = new ArrayList<String>();
			String followers = "Followed by " + user.getFollowers();
			String following = "Following " + user.getFollowing();
			titles.add(followers);
			titles.add(following);
			onTitlesUpdates(titles);
		}
	}
	
	public String getScreename(){
		if(parentfragment!=null){
			return parentfragment.getScreenName();  
		}else{
			return "";
		}
		
	}

}
*/