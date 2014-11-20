package com.paradigmcreatives.apspeak.user.tasks;

import java.util.List;

import android.content.Context;

import com.paradigmcreatives.apspeak.discovery.tasks.helpers.FollowFriendsHelper;
import com.paradigmcreatives.apspeak.logging.Logger;
import com.paradigmcreatives.apspeak.user.handlers.FollowHandler;

/**
 * Task for following/unfollowing friends
 * 
 * @author robin
 * 
 */
public class FollowUserTask implements Runnable {

	private static final String TAG = "FollowUserTask";
	private Context context = null;
	private List<String> ids = null;
	private FollowHandler handler = null;
	private FollowFriendsHelper.Type type = null;

	/**
	 * @param context
	 * @param userID
	 * @param ids
	 * @param handler
	 */
	public FollowUserTask(Context context, List<String> ids, FollowHandler handler, FollowFriendsHelper.Type type) {
		this.context = context;
		this.ids = ids;
		this.handler = handler;
		this.type = type;
	}

	@Override
	public void run() {
		if (context != null && handler != null) {
			handler.onStarting();
			FollowFriendsHelper helper = new FollowFriendsHelper(context, ids, type);
			if (helper.execute()) {
				handler.onSuccess(ids);
			} else {
				handler.onError("Something freakishly bad happened");
			}

		} else {
			Logger.warn(TAG, "Required values is/are null. Context: " + context + ", Handler: " + handler);
		}

	}

}
