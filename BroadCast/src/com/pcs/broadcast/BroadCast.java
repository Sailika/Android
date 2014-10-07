package com.pcs.broadcast;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.support.v4.app.NotificationCompat;

public class BroadCast extends BroadcastReceiver{
	
	
	public final int Req_Code = 1;
	
	
	@Override
	public void onReceive(Context context, Intent intent) {

		//On Receive of Broadcast Noyification is created using NotificationCompact.Builder
		NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
		
		
		//Notification MAnager is Inittialized
		NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

		Intent call_intent = new Intent(context,MainActivity.class);	

		//Setting the notification content title and icon 
		builder.setContentTitle(context.getResources().getString(R.string.app_name));

		builder .setContentText(context.getResources().getString(R.string.notification_text));
		builder.setSmallIcon(R.drawable.ic_launcher);
		builder.setAutoCancel(true);
		builder.build();


		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

		stackBuilder.addParentStack(MainActivity.class);

		stackBuilder.addNextIntent(call_intent);

		PendingIntent resultPendingIntent = PendingIntent.getActivity(context, Req_Code , call_intent,PendingIntent.FLAG_UPDATE_CURRENT);


		builder.setContentIntent(resultPendingIntent);
        //Notification Manager handles the notification created.
		manager.notify(Req_Code, builder.build());

	}

}
