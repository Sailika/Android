package com.paradigmcreatives.apspeak.discovery.tasks;

import java.util.Collection;
import java.util.HashMap;

import android.content.Context;

import com.paradigmcreatives.apspeak.app.model.Friend;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.discovery.fragments.UserNetworkFragment.UserNetwork;
import com.paradigmcreatives.apspeak.discovery.handlers.UserNetworkHandler;
import com.paradigmcreatives.apspeak.discovery.tasks.helpers.FindFriendsHelper;
import com.paradigmcreatives.apspeak.discovery.tasks.helpers.NetworkListHelper;

public class UserNetworkThread implements Runnable {

	private Context context = null;
	private UserNetworkHandler handler = null;
	private String userID = null;
	private UserNetwork network = UserNetwork.FRIENDS;
	private int offset = 0;
	private int limit = Constants.BATCH_FETCHLIMIT;

	public UserNetworkThread(Context context, UserNetworkHandler handler) {
		this.context = context;
		this.handler = handler;
	}

	public UserNetworkThread(Context context, UserNetworkHandler handler, String userID, UserNetwork network) {
		this(context, handler);
		this.userID = userID;
		this.network = network;
	}

	public UserNetworkThread(Context context, UserNetworkHandler handler, String userID, UserNetwork network, int offset, int limit) {
		this(context, handler, userID, network);
		this.offset = offset;
		this.limit = limit;
	}

	@Override
	public void run() {
		handler.onStarting();
		HashMap<UserNetwork, Collection<Friend>> friendsList = null;
		switch (network) {
		case FRIENDS:
			FindFriendsHelper helper = new FindFriendsHelper(context, userID);
			friendsList = helper.execute();
			break;
		case FOLLOWERS:
		case FOLLOWING:
			NetworkListHelper networkListHelper = new NetworkListHelper(context, network, userID);
			friendsList = networkListHelper.execute();
			break;
		case FACEBOOK_FRIENDS:
		    NetworkListHelper fbFriendsHelper = new NetworkListHelper(context, network, userID, offset, limit);
		    friendsList = fbFriendsHelper.execute();
		    break;
		}

		if (friendsList != null) {
			handler.onSuccess(friendsList);
		} else {
			handler.onError("Something bad happened");
		}

	}

}
