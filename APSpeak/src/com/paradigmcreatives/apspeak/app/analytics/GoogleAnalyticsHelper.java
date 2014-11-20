package com.paradigmcreatives.apspeak.app.analytics;

import android.app.Activity;

/**
 * This class is used for Sending user interaction data to Google Analytics.
 * 
 * @author Dileep | neuv
 * 
 */

public class GoogleAnalyticsHelper {

    /**
     * Sends screen view to Google Analytics
     * 
     * @param activity
     *            activity to use while sending analytics
     * @param path
     *            string representing the screen name
     */
    public static void sendScreenViewToGA(Activity activity, String path) {
    	/*
	if (activity != null && !TextUtils.isEmpty(path)) {
	    // Get tracker
	    Tracker t = ((WhatsayApplication) activity.getApplication()).getTracker(TrackerName.APP_TRACKER);
	    t.setScreenName(path);
	    t.send(new HitBuilders.AppViewBuilder().build());
	}
	*/
    }

    /**
     * Sends event to Google Analytics
     * 
     * @param activity
     *            activity to use while sending analytics
     * @param category
     *            category to which event belongs to
     * @param action
     *            action to which event comes under
     * @param label
     *            lable with which event can be identified
     */
    public static void sendEventToGA(Activity activity, String category, String action, String label) {
    	/*
	if (activity != null && !TextUtils.isEmpty(category) && !TextUtils.isEmpty(action) && !TextUtils.isEmpty(label)) {
	    // Get tracker
	    Tracker t = ((WhatsayApplication) activity.getApplication()).getTracker(TrackerName.APP_TRACKER);
	    // Build and send an Event.
	    t.send(new HitBuilders.EventBuilder().setCategory(category).setAction(action).setLabel(label).build());
	}
	*/
    }
}
