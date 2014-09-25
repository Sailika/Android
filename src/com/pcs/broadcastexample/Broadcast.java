package com.pcs.broadcastexample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class Broadcast extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		NotificationCompat.Builder bulider_notify = new NotificationCompat.Builder(context);
		bulider_notify.setContentTitle(context.getResources().getString(R.string.app_name))
		              .setContentText(context.getResources().getString(R.string.notification_text));
		              
		
	}

}
