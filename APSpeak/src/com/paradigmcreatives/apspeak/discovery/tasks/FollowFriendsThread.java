package com.paradigmcreatives.apspeak.discovery.tasks;

import java.util.List;

import android.content.Context;

import com.paradigmcreatives.apspeak.discovery.handlers.FollowFriendsHandler;
import com.paradigmcreatives.apspeak.discovery.tasks.helpers.FollowFriendsHelper;
import com.paradigmcreatives.apspeak.discovery.tasks.helpers.FollowFriendsHelper.Type;

public class FollowFriendsThread implements Runnable {
	private Context context = null;
	private List<String> ids = null;
	private FollowFriendsHandler handler = null;
	private String userID = null;
	private String friendUserId = null;
	private Type type;

	/**
	 * @param context
	 * @param ids
	 */
	public FollowFriendsThread(Context context, String userID, List<String> ids, FollowFriendsHandler handler, Type type) {
		this.context = context;
		this.ids = ids;
		this.handler = handler;
		this.userID = userID;
		this.type = type;
	}

	/**
	 * @param context
	 * @param ids
	 */
	public FollowFriendsThread(Context context, String userID, String friendUserId, FollowFriendsHandler handler, Type type) {
		this.context = context;
		this.handler = handler;
		this.userID = userID;
		this.friendUserId = friendUserId;
		this.type = type;
	}

	@Override
	public void run() {
		if (context != null && ids != null && !ids.isEmpty()) {
			handler.onStart();
			FollowFriendsHelper helper = new FollowFriendsHelper(context, userID, ids, type);
			boolean result = helper.execute();
			if (result) {
				handler.onFollowAllSuccess();
			} else {
				handler.onError("Some freaky error occurred while trying to follow. Please retry!");
			}
		}

	}

}
