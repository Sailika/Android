package com.paradigmcreatives.apspeak.registration.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.gcm.PushNotificationConfig;
import com.paradigmcreatives.apspeak.logging.Logger;
import com.paradigmcreatives.apspeak.registration.tasks.UpdateGCMIdThread;

public class GCMRegistrationReceiver extends BroadcastReceiver {

    private static final String TAG = "IncomingDoodleReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
	if (intent.getAction()
		.equals(PushNotificationConfig.CURRENT_PKG_NAME + Constants.GCM_REGISTRATION_NOTIFICATION)) {
	    try {
		(new UpdateGCMIdThread(context)).start();
	    } catch (IllegalThreadStateException itse) {
		Logger.warn(TAG, itse.getLocalizedMessage());
	    } catch (Exception e) {
		Logger.warn(TAG, e.getLocalizedMessage());
	    }
	} else {
	    // else do nothing
	    Logger.warn(TAG, "Unknown format of push notification sent from server");
	}
    }

}
