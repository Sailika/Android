package com.paradigmcreatives.apspeak.registration.tasks;

import android.content.Context;

import com.paradigmcreatives.apspeak.registration.handlers.AddUserToGroupHandler;
import com.paradigmcreatives.apspeak.registration.tasks.helpers.AddUserToGroupHelper;

/**
 * Thread that triggers adding user to selected group id
 * 
 * @author Dileep | neuv
 * 
 */
public class AddUserToGroupThread extends Thread {

    private Context mContext;
    private AddUserToGroupHandler mHandler;
    private String mGroupId;
    private String mUserId;

    public AddUserToGroupThread(Context context, AddUserToGroupHandler handler, String groupId, String userId) {
	super();
	this.mContext = context;
	this.mHandler = handler;
	this.mGroupId = groupId;
	this.mUserId = userId;
    }

    @Override
    public void run() {
	if (mHandler != null) {
	    AddUserToGroupHelper helper = new AddUserToGroupHelper(mContext, mGroupId, mUserId);
	    if (helper.execute()) {
		mHandler.didAddComplete(mGroupId);
	    } else {
		mHandler.didFail();
	    }
	}
	super.run();
    }
}
