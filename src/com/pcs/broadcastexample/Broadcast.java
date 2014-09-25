package com.pcs.broadcastexample;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class Broadcast extends BroadcastReceiver {
   private int Req_Code =0;
	@Override
	public void onReceive(Context context, Intent intent) {
		Toast.makeText(context, "Implicit Call", Toast.LENGTH_LONG).show();
		
		NotificationCompat.Builder builder_notify = new NotificationCompat.Builder(context);
		
		
		NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        
		Intent call_intent = new Intent(context,Message.class);	

		
		builder_notify.setContentTitle(context.getResources().getString(R.string.app_name));
		builder_notify .setContentText(context.getResources().getString(R.string.notification_text));
		builder_notify.setSmallIcon(R.drawable.ic_launcher);
		builder_notify.setAutoCancel(true);
		

		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		
		stackBuilder.addParentStack(MainActivity.class);
		
		stackBuilder.addNextIntent(call_intent);
		
		PendingIntent resultPendingIntent = PendingIntent.getActivity(context, Req_Code , call_intent,PendingIntent.FLAG_UPDATE_CURRENT);
				
		
		builder_notify.setContentIntent(resultPendingIntent);

         manager.notify(Req_Code, builder_notify.build());
	}

}
