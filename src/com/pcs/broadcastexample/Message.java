package com.pcs.broadcastexample;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Message extends Activity{
	   private int Req_Code_A =1;
	   private ProgressDialog progress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message);
		Toast.makeText(Message.this, "You clicked on Notification!", Toast.LENGTH_LONG).show();
		TextView msgTxt =(TextView)findViewById(R.id.message_view);
		msgTxt.setText(getResources().getString(R.string.txt));
		Button updateBtn = (Button)findViewById(R.id.update);
		updateBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				

				final NotificationCompat.Builder builder_notify = new NotificationCompat.Builder(Message.this);
				
				//New Notification Manager
				final NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		        
				Intent call_intent = new Intent(Message.this,Message.class);	

				
				builder_notify.setContentTitle(getResources().getString(R.string.notification));
				builder_notify .setContentText(getResources().getString(R.string.notify_content));
				builder_notify.setSmallIcon(R.drawable.ic_launcher);
				builder_notify.setAutoCancel(true);
				//Thread to display progress bar
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						int i=0;
						while(i<10){
							builder_notify.setProgress(100, i, false);
							manager.notify(Req_Code_A, builder_notify.build());
							try {
								Thread.sleep(500);
							} catch (InterruptedException e) {
								e.printStackTrace();
								
							}
						i++;}
					}
				}).start();
				
				builder_notify.setContentTitle("Update Successfull!!");
				TaskStackBuilder stackBuilder = TaskStackBuilder.create(Message.this);
				
				stackBuilder.addParentStack(MainActivity.class);
				
				stackBuilder.addNextIntent(call_intent);
				
				PendingIntent resultPendingIntent = PendingIntent.getActivity(Message.this, Req_Code_A , call_intent,PendingIntent.FLAG_UPDATE_CURRENT);
						
				
				builder_notify.setContentIntent(resultPendingIntent);

		         manager.notify(Req_Code_A, builder_notify.build());
			}
		});
		
	
	}
}
