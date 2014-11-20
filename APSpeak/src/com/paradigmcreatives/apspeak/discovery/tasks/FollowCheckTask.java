package com.paradigmcreatives.apspeak.discovery.tasks;

import android.content.Context;
import android.text.TextUtils;

import com.paradigmcreatives.apspeak.app.model.FollowCheckBean;
import com.paradigmcreatives.apspeak.discovery.handlers.FollowCheckHandler;
import com.paradigmcreatives.apspeak.discovery.tasks.helpers.FollowCheckHelper;

public class FollowCheckTask implements Runnable {

	private Context context = null;
	private String userA = null;
	private String userB = null;
	private FollowCheckHandler handler = null;

	public FollowCheckTask(Context context, String userA, String userB, FollowCheckHandler handler) {
		this.context = context;
		this.userA = userA;
		this.userB = userB;
		this.handler = handler;
	}

	@Override
	public void run() {

		if (context != null && !TextUtils.isEmpty(userA) && !TextUtils.isEmpty(userB) && handler != null) {
			handler.onStart();
			FollowCheckHelper helper = new FollowCheckHelper(context, userA, userB);
			FollowCheckBean bean = helper.execute();
			if (bean != null) {
				if (TextUtils.isEmpty(bean.geterror())) {
					handler.onSuccess(bean);
				} else {
					handler.onError(bean.geterror());
				}
			} else {
				handler.onError("Something funky happened while trying to find out whether the user follow other or not");
			}
		}

	}

}
