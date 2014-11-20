package com.paradigmcreatives.apspeak.feed.handlers;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;

import com.paradigmcreatives.apspeak.app.model.MyFeedBean;
import com.paradigmcreatives.apspeak.feed.fragments.MyFeedFragment;
import com.paradigmcreatives.apspeak.logging.Logger;

public class MyFeedHandler extends Handler {

	private static final String TAG = "MyFeedHandler";

	private static final int PRE_EXECUTE = 1;
	private static final int SUCCESS = 2;
	private static final int FAILURE = 3;
	private static final String FEED_STATUS = "feedstatus";

	private Fragment fragment;

	public MyFeedHandler(Fragment fragment) {
		super();
		this.fragment = fragment;
	}

	public void willStartTask() {
		sendEmptyMessage(PRE_EXECUTE);
	}

	public void didFetchFeedComplete(ArrayList<MyFeedBean> myFeed) {
		Message msg = new Message();
		msg.what = SUCCESS;
		if (myFeed != null) {
			Bundle bundle = new Bundle();
			bundle.putParcelableArrayList(FEED_STATUS, myFeed);
			msg.setData(bundle);
		}
		sendMessage(msg);
	}

	public void failed(int statusCode, int errorCode, String reasonPhrase) {
		Message msg = new Message();
		msg.what = FAILURE;
		msg.arg1 = statusCode;
		msg.arg2 = errorCode;
		msg.obj = reasonPhrase;
		sendMessage(msg);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.Handler#handleMessage(android.os.Message)
	 */
	@Override
	public void handleMessage(Message msg) {
		if (fragment != null) {
			switch (msg.what) {
			case PRE_EXECUTE:
				// do nothing
				break;

			case SUCCESS:
				Bundle data = msg.getData();
				if (fragment != null && data != null
						&& data.containsKey(FEED_STATUS)) {
					ArrayList<MyFeedBean> myFeedBean = data
							.getParcelableArrayList(FEED_STATUS);
					if (fragment instanceof MyFeedFragment) {
						((MyFeedFragment) fragment)
								.setMyFeedFetchStatus(myFeedBean);
					}
				}
				break;

			case FAILURE:
				if (fragment instanceof MyFeedFragment) {
					((MyFeedFragment) fragment)
							.setMyFeedFetchError();
				}
				break;
			default:
				break;
			}
		} else {
			Logger.warn(TAG, "Context is null");
		}
		super.handleMessage(msg);
	}

}
