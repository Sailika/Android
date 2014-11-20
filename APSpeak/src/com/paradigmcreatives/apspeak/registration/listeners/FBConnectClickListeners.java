package com.paradigmcreatives.apspeak.registration.listeners;

import java.util.ArrayList;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.app.analytics.GoogleAnalyticsHelper;
import com.paradigmcreatives.apspeak.app.model.GroupBean;
import com.paradigmcreatives.apspeak.app.util.Util;
import com.paradigmcreatives.apspeak.app.util.constants.GoogleAnalyticsConstants;
import com.paradigmcreatives.apspeak.app.util.facebook.OpenGraphRequestUtil;
import com.paradigmcreatives.apspeak.registration.LoginActivity;
import com.paradigmcreatives.apspeak.registration.fragments.FacebookConnectAnimationFragment;
import com.paradigmcreatives.apspeak.registration.fragments.FacebookConnectFragment;

/**
 * Handles the click event of all the UI component of the Home Screen
 * 
 * @author robin
 * 
 */
public class FBConnectClickListeners implements OnClickListener {
    private Activity activity;
    private Fragment fragment;

    public FBConnectClickListeners(Fragment fragment, Activity activity) {
	this.fragment = fragment;
	this.activity = activity;
    }

    @Override
    public void onClick(View v) {
	if (activity != null) {
	    if (activity instanceof LoginActivity) {
		switch (v.getId()) {
		case R.id.fbconnect:
		    if (Util.isOnline(activity)) {
			GoogleAnalyticsHelper.sendEventToGA(activity, GoogleAnalyticsConstants.FACEBOOK_LOGIN_SCREEN,
				GoogleAnalyticsConstants.ACTION_BUTTON, GoogleAnalyticsConstants.FBCONNECT_BUTTON);
			OpenGraphRequestUtil graphUtil = new OpenGraphRequestUtil(fragment);
			graphUtil.fetchBasicProfile();
		    } else {
			Toast.makeText(activity, activity.getString(R.string.NoNetworkmsg), Toast.LENGTH_SHORT).show();
		    }
		    break;
		case R.id.chooseCollege:
		case R.id.grouppicker_layout:
		    if (fragment != null) {
			if (fragment instanceof FacebookConnectFragment) {
			    ((FacebookConnectFragment) fragment).extractAndProvideCollegeListView();
			} else if (fragment instanceof FacebookConnectAnimationFragment) {
			    ((FacebookConnectAnimationFragment) fragment)
				    .startGroupSelectionButtonAnimationCenterToTop();
			}
		    }
		    break;

		case R.id.next_button:
		    // do nothing
		    break;
		}
	    }

	}
    }

}
