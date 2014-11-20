package com.paradigmcreatives.apspeak.registration.tasks;

import java.util.ArrayList;

import android.content.Context;
import android.text.TextUtils;

import com.paradigmcreatives.apspeak.app.model.GroupBean;
import com.paradigmcreatives.apspeak.app.util.AppPropertiesUtil;
import com.paradigmcreatives.apspeak.registration.handlers.GetGroupsListHandler;
import com.paradigmcreatives.apspeak.registration.tasks.helpers.GetUserGroupsListHelper;

/**
 * Thread that triggers fetching current user's Groups list from Whatsay server
 * 
 * @author Dileep | neuv
 * 
 */
public class GetUserGroupsListThread extends Thread {

    private Context mContext;
    private String mUserId;
    private GetGroupsListHandler mHandler;

    public GetUserGroupsListThread(Context context, String userId) {
	super();
	this.mContext = context;
	this.mUserId = userId;
    }

    public GetUserGroupsListThread(Context context, String userId, GetGroupsListHandler handler) {
	this(context, userId);
	this.mHandler = handler;
    }

    @Override
    public void run() {
	ArrayList<GroupBean> groupsList = null;
	GetUserGroupsListHelper helper = new GetUserGroupsListHelper(mContext, mUserId);
	groupsList = helper.execute();
	if (groupsList != null && groupsList.size() > 0) {
	    if (!TextUtils.isEmpty(mUserId) && mUserId.equals(AppPropertiesUtil.getUserID(mContext))) {
		AppPropertiesUtil.setGroupID(mContext, groupsList.get(0).getGroupId());
	    }
	    if (mHandler != null) {
		mHandler.didFetchComplete(groupsList);
	    }
	}
	super.run();
    }
}
