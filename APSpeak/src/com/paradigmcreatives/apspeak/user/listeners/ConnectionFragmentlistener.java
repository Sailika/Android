package com.paradigmcreatives.apspeak.user.listeners;
/*package com.paradigmcreatives.apspeak.user.listeners;

import android.support.v4.view.ViewPager.OnPageChangeListener;

import com.paradigmcreatives.apspeak.user.ConnectionsFragment;

*//**
 * Listener to handle different views swiping in Connections view in My profile screen.
 * 
 * @author Gopi
 *
 *//*

public class ConnectionFragmentlistener implements OnPageChangeListener {

	private ConnectionsFragment fragment;
	private String screename=null;

	public ConnectionFragmentlistener(ConnectionsFragment fragment) {
		this.fragment = fragment;
		if (this.fragment!=null) {
			this.screename=fragment.getScreename();
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0) {
		switch (arg0) {
		case 0:
		     TODO: Uncomment for Google Analytics
			if (fragment != null) {
				
				if (screename != null && screename.equalsIgnoreCase(GoogleAnalyticsConstants.MY_PROFILE_SCREEN)) {
					GoogleAnalytics.sendEventTrackingInfoToGA(fragment.getActivity(),
							GoogleAnalyticsConstants.MY_PROFILE_SCREEN_CAT_NAME,
							GoogleAnalyticsConstants.EVENT_ACTION_SWIPING,
							GoogleAnalyticsConstants.MY_PROFILE_CONNECTION_FOLLOWER_SWIPE);

					
				}else{
					GoogleAnalytics.sendEventTrackingInfoToGA(fragment.getActivity(),
							GoogleAnalyticsConstants.FRIEND_PROFILE_SCREEN_CAT_NAME,
							GoogleAnalyticsConstants.EVENT_ACTION_SWIPING,
							GoogleAnalyticsConstants.FRIEND_PROFILE_CONNECTION_FOLLOWER_SWIPE);
					
				}
				

			}
			
			break;
		case 1:
		     TODO: Uncomment for Google Analytics
			if (fragment != null) {
				
				if (screename != null && screename.equalsIgnoreCase(GoogleAnalyticsConstants.MY_PROFILE_SCREEN)) {
					GoogleAnalytics.sendEventTrackingInfoToGA(fragment.getActivity(),
							GoogleAnalyticsConstants.MY_PROFILE_SCREEN_CAT_NAME,
							GoogleAnalyticsConstants.EVENT_ACTION_SWIPING,
							GoogleAnalyticsConstants.MY_PROFILE_CONNECTION_FOLLOWING_SWIPE);

				
				}else{
					GoogleAnalytics.sendEventTrackingInfoToGA(fragment.getActivity(),
							GoogleAnalyticsConstants.FRIEND_PROFILE_SCREEN_CAT_NAME,
							GoogleAnalyticsConstants.EVENT_ACTION_SWIPING,
							GoogleAnalyticsConstants.FRIEND_PROFILE_CONNECTION_FOLLOWING_SWIPE);
					 
				}

				
			

			}
			
			break;

		default:
			break;
		}

	}

}
*/