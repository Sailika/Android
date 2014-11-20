package com.paradigmcreatives.apspeak.discovery.listeners;

import android.view.View;
import android.view.View.OnClickListener;

import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.app.analytics.GoogleAnalyticsHelper;
import com.paradigmcreatives.apspeak.app.util.constants.GoogleAnalyticsConstants;
import com.paradigmcreatives.apspeak.app.util.facebook.OpenGraphRequestUtil;
import com.paradigmcreatives.apspeak.discovery.fragments.InviteFriendsFragment;

/**
 * Listener that implements OnClickListener to handle user clicks from InviteFriendsFragment
 * 
 * @author Dileep | neuv
 * 
 */
public class InviteFriendsClickListener implements OnClickListener {

    private InviteFriendsFragment fragment;

    /**
     * Constructor
     * 
     * @param fragment
     */
    public InviteFriendsClickListener(InviteFriendsFragment fragment) {
	super();
	this.fragment = fragment;
    }

    @Override
    public void onClick(View v) {
	switch (v.getId()) {
	case R.id.fbinvitefriends:
	    if (fragment != null) {
		GoogleAnalyticsHelper.sendEventToGA(fragment.getActivity(), GoogleAnalyticsConstants.INVITE_FRIENDS_SCREEN,
			GoogleAnalyticsConstants.ACTION_BUTTON, GoogleAnalyticsConstants.INVITE_FBFRIENDS_BUTTON);
	    }
	    // Invite FB friends
	    OpenGraphRequestUtil util = new OpenGraphRequestUtil(fragment);
	    util.inviteFBFriends();
	    break;
	}
    }
}
