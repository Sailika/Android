package com.paradigmcreatives.apspeak.registration.tasks;

import java.util.ArrayList;

import com.paradigmcreatives.apspeak.app.model.GroupBean;
import com.paradigmcreatives.apspeak.registration.handlers.GetGroupsListHandler;
import com.paradigmcreatives.apspeak.registration.tasks.helpers.GetGroupsListHelper;

/**
 * Thread that triggers fetching Groups list from Whatsay server
 * 
 * @author Dileep | neuv
 * 
 */
public class GetGroupsListThread extends Thread {

    private GetGroupsListHandler mHandler;

    public GetGroupsListThread(GetGroupsListHandler handler) {
	super();
	this.mHandler = handler;
    }

    @Override
    public void run() {
	if (mHandler != null) {
	    ArrayList<GroupBean> groupsList = null;
	    GetGroupsListHelper helper = new GetGroupsListHelper();
	    groupsList = helper.execute();
	    if (groupsList != null && groupsList.size() > 0) {
		mHandler.didFetchComplete(groupsList);
	    } else {
		mHandler.didFail();
	    }
	}
	super.run();
    }
}
