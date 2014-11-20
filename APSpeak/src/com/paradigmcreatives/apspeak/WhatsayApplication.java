package com.paradigmcreatives.apspeak;

import android.app.Application;

public class WhatsayApplication extends Application{

    // The following line represents Whatsay GA app property id.
    // Remember to switch between Development and Production property ids depends on app releases
    private static final String PROPERTY_ID = "UA-50558474-3"; // Development GA
    //private static final String PROPERTY_ID = "UA-50558474-1"; // Production GA

    public static int GENERAL_TRACKER = 0;

    /*
    public enum TrackerName {
        APP_TRACKER, // Tracker used only in this app.
        GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg: roll-up tracking.
        ECOMMERCE_TRACKER, // Tracker used by all ecommerce transactions from a company.
    }
    */

    //HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

    public WhatsayApplication() {
        super();
    }

    /*
    public synchronized Tracker getTracker(TrackerName trackerId) {
        if (!mTrackers.containsKey(trackerId)) {

            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            if(trackerId == TrackerName.APP_TRACKER){
        	Tracker t = analytics.newTracker(PROPERTY_ID);
                mTrackers.put(trackerId, t);
            }
        }
        return mTrackers.get(trackerId);
    }
    */
    }
