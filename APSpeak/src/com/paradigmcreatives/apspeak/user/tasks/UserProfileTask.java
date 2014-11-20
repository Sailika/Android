package com.paradigmcreatives.apspeak.user.tasks;

import android.content.Context;

import com.paradigmcreatives.apspeak.app.model.User;
import com.paradigmcreatives.apspeak.user.handlers.UserProfileHandler;
import com.paradigmcreatives.apspeak.user.tasks.helpers.FullProfileHelper;

public class UserProfileTask implements Runnable {

	private Context context = null;
	private UserProfileHandler handler = null;
	private String userID = null;

	public UserProfileTask(Context context, UserProfileHandler handler, String userID) {
		this.context = context;
		this.handler = handler;
		this.userID = userID;
	}

	@Override
	public void run() {
		if (context != null && handler != null) {
			handler.onStarting();
			FullProfileHelper helper = new FullProfileHelper(context, userID);
			User user = helper.execute();
			if (user != null) {
				handler.onSuccess(user);
			} else {
				handler.onError("Couldn't get the user's info. Sorry");
			}
		}

	}

}
