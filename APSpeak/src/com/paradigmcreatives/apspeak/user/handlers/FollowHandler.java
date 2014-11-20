package com.paradigmcreatives.apspeak.user.handlers;

import java.util.List;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;

import com.paradigmcreatives.apspeak.discovery.fragments.UserNetworkFragment;
import com.paradigmcreatives.apspeak.user.fragments.ProfileFragment;

/**
 * Handler for follow/unfollow from user profile
 * 
 * @author robin
 * 
 */
public class FollowHandler extends Handler {
    private static final int STARTING = 0;
    private static final int SUCCESS = 1;
    private static final int ERROR = 2;

    private String USERIDS = "userIds";

    private Fragment fragment = null;

    public FollowHandler(Fragment fragment) {
	this.fragment = fragment;
    }

    @Override
    public void handleMessage(Message msg) {
	super.handleMessage(msg);
	if (msg != null && fragment != null) {
	    switch (msg.what) {
	    case STARTING:
		if (fragment instanceof UserNetworkFragment) {
		    ((UserNetworkFragment) fragment).onFollowUnFollowStarted();
		} else if (fragment instanceof ProfileFragment) {
		    ((ProfileFragment) fragment).onFollowStarted();
		}
		break;
	    case SUCCESS:
		if (fragment instanceof UserNetworkFragment) {
		    String friendUserId = (String) msg.obj;
		    ((UserNetworkFragment) fragment).onFollowUnFollowSuccessful(friendUserId);
		} else if (fragment instanceof ProfileFragment) {
		    ((ProfileFragment) fragment).onFollowSuccessful();
		}
		break;
	    case ERROR:
		if (fragment instanceof UserNetworkFragment) {
		    ((UserNetworkFragment) fragment).onFollowUnFollowFailed("Something freakishly bad happened");
		} else if (fragment instanceof ProfileFragment) {
		    ((ProfileFragment) fragment).onFollowFailed("Something freakishly bad happened");
		}
		break;
	    }
	}
    }

    public void onSuccess(List<String> ids) {
	Message msg = new Message();
	msg.what = SUCCESS;
	if (fragment != null && fragment instanceof UserNetworkFragment && ids != null && ids.size() > 0) {
	    msg.obj = ids.get(0);
	}
	sendMessage(msg);
    }

    public void onError(String error) {
	Message msg = new Message();
	msg.what = ERROR;
	msg.obj = error;
	sendMessage(msg);
    }

    public void onStarting() {
	sendEmptyMessage(STARTING);
    }

}
