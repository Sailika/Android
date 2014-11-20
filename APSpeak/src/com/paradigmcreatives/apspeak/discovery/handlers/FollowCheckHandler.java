package com.paradigmcreatives.apspeak.discovery.handlers;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.paradigmcreatives.apspeak.app.model.FollowCheckBean;

/**
 * Handler for follow check
 * 
 * @author robin
 * 
 */
public class FollowCheckHandler extends Handler {
	private static final int STARTING = 0;
	private static final int SUCCESS = 1;
	private static final int ERROR = 2;
	
	private static final String ISFOLLOWING = "isFollowing";
	private static final String STATUS = "status";

	private FollowCheckListener listener = null;

	public FollowCheckHandler(FollowCheckListener listener) {
		this.listener = listener;
	}

	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		if (msg != null && listener != null) {
			switch (msg.what) {
			case STARTING:
				listener.onStart();
				break;
			case SUCCESS:
			    Bundle data = (Bundle) msg.obj;
			    if(data != null){
				listener.onSuccess(data.getBoolean(ISFOLLOWING), data.getString(STATUS));
			    }
				break;
			case ERROR:
				listener.onError((String) msg.obj);
				break;
			}
		}

	}

	public void onStart() {
		sendEmptyMessage(STARTING);
	}

	public void onSuccess(FollowCheckBean followCheck) {
		Message msg = new Message();
		if (followCheck != null) {
			msg.what = SUCCESS;
			//msg.obj = followCheck.isFollows();
			Bundle data = new Bundle();
			data.putBoolean(ISFOLLOWING, followCheck.isFollows());
			data.putString(STATUS, followCheck.getStatus());
			msg.obj = data;
			sendMessage(msg);
		} else {
			msg.what = ERROR;
			msg.obj = "Empty Response Received";
			sendMessage(msg);
		}
	}

	public void onError(String error) {
		Message msg = new Message();
		msg.what = ERROR;
		msg.obj = error;
		sendMessage(msg);
	}

	/**
	 * The Fragments/Activities handling the follow check should implement this
	 * listener to handle the success and error scenarios. The methods in this
	 * interface would be triggered on UI Thread
	 * 
	 * @author robin
	 * 
	 */
	public interface FollowCheckListener {
		/**
		 * Called on successful execution of the web-service
		 * 
		 * @param follows
		 *            <code>true</code> if userA follows userB,
		 *            <code>false</code> otherwise
		 *            
		 * @param status
		 * 		follow status message
		 */
		public void onSuccess(boolean follows, String status);

		/**
		 * Called when an error occurs
		 * 
		 * @param msg
		 *            Reason of the error
		 */
		public void onError(String error);

		/**
		 * Called when the web-service call is about to start
		 */
		public void onStart();
	}

}
