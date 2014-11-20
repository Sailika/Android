package com.paradigmcreatives.apspeak.app.util.connectivity;

import com.paradigmcreatives.apspeak.app.util.Util;
import com.paradigmcreatives.apspeak.autosend.AutoSendManager;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Receives a broadcast whenever the network state changes. Since its a <code>WakefulBroadcastReceiver</code> therefore
 * it will keep the CPU active unless the process if not over
 * 
 * @author robin
 * 
 */
public class ConnectivityChangeReceiver extends WakefulBroadcastReceiver {

    @SuppressWarnings("unused")
    private static final String TAG = "NetworkStateReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
	if (context != null && intent.getAction() == ConnectivityManager.CONNECTIVITY_ACTION && Util.isOnline(context)) {
		AutoSendManager.getInstance(context).startSending();
	}

    }

}
