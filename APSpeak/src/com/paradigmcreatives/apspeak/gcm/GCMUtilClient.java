package com.paradigmcreatives.apspeak.gcm;

import android.content.Context;
import android.util.Log;

import com.google.android.gcm.GCMRegistrar;
import com.paradigmcreatives.apspeak.logging.Logger;

/**
 * This class contains methods to register a device
 * to GCM server, making sure the device supports
 * GCM service.
 *
 * @author Vishal | Paradigm Creatives
 */
public class GCMUtilClient {
    /**
     * GCM Sender ID.
     */
    public static final String SENDER_ID = "255736116240";
    private static final String TAG = "GCMUtilClient";

    /**
     * Registers the device to the GCM server.
     */
    public static void register(Context context) {
	try {
	    GCMRegistrar.checkDevice(context);
	    GCMRegistrar.checkManifest(context);
	    final String registrationId = GCMRegistrar.getRegistrationId(context);

	    if (registrationId.equals("")) {
		GCMRegistrar.register(context, SENDER_ID);
	    } else {
		Log.d(TAG, "The device has already been registered");
	    }
	} catch (UnsupportedOperationException uoe) {
	    Logger.fatal("This device does not support GCM: " + uoe);
	} catch (Exception e) {
	    Logger.fatal("Error in registration: " + e);
	}
    }//end of register()

}//end of class