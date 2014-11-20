package com.paradigmcreatives.apspeak.feed.tasks;

import java.util.ArrayList;

import android.content.Context;
import android.text.TextUtils;

import com.paradigmcreatives.apspeak.app.model.MyFeedBean;
import com.paradigmcreatives.apspeak.feed.handlers.MyFeedHandler;
import com.paradigmcreatives.apspeak.feed.tasks.helpers.MyFeedHelper;
import com.paradigmcreatives.apspeak.logging.Logger;

public class MyFeedThread extends Thread {

	private static final String TAG = "MyFeedThread";

	private Context context;
	private String offset;
	private String count;
	private MyFeedHandler handler;
	private String userId;

	public MyFeedThread(Context context, String userId, String offset,
			String count, MyFeedHandler handler) {
		super();
		this.offset = offset;
		this.count = count;
		this.context = context;
		this.handler = handler;
		this.userId = userId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		if (context != null) {
			if (!TextUtils.isEmpty(userId) && !TextUtils.isEmpty(offset)
					&& !TextUtils.isEmpty(count)) {
				willStartTask();

				MyFeedHelper helper = new MyFeedHelper(context, handler);
				ArrayList<MyFeedBean> myFeed = helper.fetch(userId, offset,
						count);
				didFetchFeedComplete(myFeed);
			} else {
				didFetchError();
				Logger.warn(TAG, "UserId or Offset or Count is null");
			}
		} else {
			didFetchError();
			Logger.warn(TAG, "Context is null");
		}

		super.run();
	}

	private void willStartTask() {
		if (handler != null) {
			handler.willStartTask();
		} else {
			Logger.warn(TAG, "Handler is null");
		}
	}

	private void didFetchFeedComplete(ArrayList<MyFeedBean> myFeed) {
		if (handler != null) {
			handler.didFetchFeedComplete(myFeed);
		} else {
			Logger.warn(TAG, "Handler is null");
		}
	}
	
	private void didFetchError(){
		if(handler != null){
			handler.failed(-1, -1, "");
		}
	}

}
