package com.paradigmcreatives.apspeak.doodleboard.background.assets.sync.dailysync;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.paradigmcreatives.apspeak.logging.Logger;

/**
 * This class pings the server daily and check if there are new assets available. If the device is offline when this
 * alarm goes off then this activity is not carried out
 * 
 * @author robin
 * 
 */
public class DailyAssetsSyncAlarm extends WakefulBroadcastReceiver {

    private static final String TAG = "DailyAssetsSyncAlarm";

    private static final int ALARM_TRIGGER_HOUR = 0;

    /**
     * For system's alarm service access
     */
    private AlarmManager alarmManager;

    /**
     * Pending intent that is fired when the alarm is triggered
     */
    private PendingIntent alarmIntent;

    /**
     * Sets the alarm which triggers the event of performing assets sync
     */
    public void setAlarm(Context context) {
	if (context != null) {
	    alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
	    Intent intent = new Intent(context, DailyAssetsSyncAlarm.class);
	    alarmIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

	    // Set the alarm to trigger daily at 12am
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTimeInMillis(System.currentTimeMillis());
	    calendar.set(Calendar.HOUR_OF_DAY, ALARM_TRIGGER_HOUR);
	    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
		    AlarmManager.INTERVAL_DAY, alarmIntent);
	} else {
	    Logger.warn(TAG, "Null context passed while setting alarm for daily assets sync");
	}

    }

    @Override
    public void onReceive(Context context, Intent intent) {
	if (context != null) {
	    Intent service = new Intent(context, DailyAssetsSyncService.class);

	    // Start the service, keeping the device awake while it is launching.
	    startWakefulService(context, service);
	} else {
	    Logger.warn(TAG, "Null context received in the broadcast");
	}
    }

}
