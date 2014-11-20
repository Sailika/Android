package com.paradigmcreatives.apspeak.doodleboard.background.googleimages.tasks;

import java.util.ArrayList;

import com.paradigmcreatives.apspeak.app.util.google.image.GoogleImageSearchHelper;
import com.paradigmcreatives.apspeak.app.util.google.image.beans.ImageResultsBean;
import com.paradigmcreatives.apspeak.app.util.google.image.beans.SearchResultsBean;
import com.paradigmcreatives.apspeak.doodleboard.background.googleimages.GoogleImageActivity;
import com.paradigmcreatives.apspeak.doodleboard.background.googleimages.handlers.GoogleImageSearchHandler;
import com.paradigmcreatives.apspeak.doodleboard.fragments.BackgroundFragment;
import com.paradigmcreatives.apspeak.logging.Logger;

/**
 * Thread to search the image according to the search text;
 * 
 * @author Soumya Behera | Paradigm Creatives
 * 
 */
public class GoogleImageSearchThread extends Thread {

    private static final String TAG = "GoogleImageSearchThread";

    private GoogleImageSearchHelper googleImageSearchHelper;
    private GoogleImageSearchHandler handler;
    private boolean searchNextFlag;

    public GoogleImageSearchThread(BackgroundFragment fragment,
	    GoogleImageSearchHelper googleImageSearchHelper, boolean searchNextFlag) {
	super();
	this.googleImageSearchHelper = googleImageSearchHelper;
	this.handler = new GoogleImageSearchHandler(fragment);
	this.searchNextFlag = searchNextFlag;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Thread#run()
     */
    @Override
    public void run() {
	if (googleImageSearchHelper != null) {
	    willStartTask();
	    SearchResultsBean srb = null;
	    if (searchNextFlag) {
		srb = googleImageSearchHelper.next();
	    } else {
		srb = googleImageSearchHelper.previous();
	    }

	    if (srb != null && srb.getImageResults() != null) {
		didSucceed(srb.getImageResults());
	    } else {
		didFail();
	    }
	} else {
	    Logger.warn(TAG, "Invalid search helper");
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

    private void didSucceed(ArrayList<ImageResultsBean> searchResult) {
	if (handler != null) {
	    handler.didSucceed(searchResult);
	} else {
	    Logger.warn(TAG, "Handler is null");
	}
    }

    private void didFail() {
	if (handler != null) {
	    handler.didFail();
	} else {
	    Logger.warn(TAG, "Handler is null");
	}
    }
}
