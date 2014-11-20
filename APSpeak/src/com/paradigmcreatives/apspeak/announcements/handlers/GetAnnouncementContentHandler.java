package com.paradigmcreatives.apspeak.announcements.handlers;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;

import com.paradigmcreatives.apspeak.feed.fragments.MyFeedFragment;

/**
 * Message handler for a thread that fetches Announcement's content
 * 
 * @author Dileep | neuv
 * 
 */
public class GetAnnouncementContentHandler extends Handler {

    private static final int SUCCESS = 1;
    private static final int FAILURE = 2;

    private static final String ANNOUNCEMENT_ID = "announcementId";
    private static final String ANNOUNCEMENT_CONTENT = "announcementContent";

    private Fragment fragment;

    public GetAnnouncementContentHandler(Fragment fragment) {
	super();
	this.fragment = fragment;
    }

    @Override
    public void handleMessage(Message msg) {
	String announcementId = null;
	String announcementContent = null;
	Bundle data = msg.getData();
	if (data != null) {
	    if (data.containsKey(ANNOUNCEMENT_ID)) {
		announcementId = data.getString(ANNOUNCEMENT_ID);
	    }
	    if (data.containsKey(ANNOUNCEMENT_CONTENT)) {
		announcementContent = data.getString(ANNOUNCEMENT_CONTENT);
	    }
	}
	switch (msg.what) {
	case SUCCESS:
	    if (fragment != null) {
		if (fragment instanceof MyFeedFragment) {
		    ((MyFeedFragment) fragment).setAnnouncementContentSuccess(announcementId, announcementContent);
		}
	    }
	    break;

	case FAILURE:
	    if (fragment != null) {
		if (fragment instanceof MyFeedFragment) {
		    ((MyFeedFragment) fragment).setAnnouncementContentFailed(announcementId);
		}
	    }
	    break;

	default:
	    break;
	}
	super.handleMessage(msg);
    }

    public void didFetchComplete(String announcementId, String announcementContent) {
	Message msg = new Message();
	msg.what = SUCCESS;
	Bundle data = new Bundle();
	data.putString(ANNOUNCEMENT_ID, announcementId);
	data.putString(ANNOUNCEMENT_CONTENT, announcementContent);
	msg.setData(data);
	sendMessage(msg);
    }

    public void didFail(String announcementId) {
	Message msg = new Message();
	msg.what = FAILURE;
	Bundle data = new Bundle();
	data.putString(ANNOUNCEMENT_ID, announcementId);
	msg.setData(data);
	sendMessage(msg);
    }
}
