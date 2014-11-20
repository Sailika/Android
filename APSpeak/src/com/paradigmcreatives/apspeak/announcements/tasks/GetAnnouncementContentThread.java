package com.paradigmcreatives.apspeak.announcements.tasks;

import android.text.TextUtils;

import com.paradigmcreatives.apspeak.announcements.handlers.GetAnnouncementContentHandler;
import com.paradigmcreatives.apspeak.announcements.tasks.helpers.GetAnnouncementContentHelper;

/**
 * Thread that triggers fetching Announcement Content for the given announcement id from Whatsay server
 * 
 * @author Dileep | neuv
 * 
 */
public class GetAnnouncementContentThread extends Thread {

    private GetAnnouncementContentHandler mHandler;
    private String mAnnouncementId;

    public GetAnnouncementContentThread(GetAnnouncementContentHandler handler, String announcementId) {
	super();
	this.mHandler = handler;
	this.mAnnouncementId = announcementId;
    }

    @Override
    public void run() {
	if (mHandler != null) {
	    String announcementContent = null;
	    GetAnnouncementContentHelper helper = new GetAnnouncementContentHelper(mAnnouncementId);
	    announcementContent = helper.execute();
	    if (!TextUtils.isEmpty(announcementContent)) {
		mHandler.didFetchComplete(mAnnouncementId, announcementContent);
	    } else {
		mHandler.didFail(mAnnouncementId);
	    }
	}
	super.run();
    }
}
