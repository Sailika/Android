package com.paradigmcreatives.apspeak.cues.tasks;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;

import com.paradigmcreatives.apspeak.app.model.Campaigns;
import com.paradigmcreatives.apspeak.app.util.AppPropertiesUtil;
import com.paradigmcreatives.apspeak.cues.handlers.GetCuesHandler;
import com.paradigmcreatives.apspeak.cues.tasks.helpers.GetCuesHelper;

/**
 * Thread class that calls helper to fetch Cues from Whatsay server
 * 
 * @author Dileep | neuv
 * 
 */
public class GetCuesThread extends Thread {

    private Context context;
    private GetCuesHandler handler;

    public GetCuesThread(Context context, GetCuesHandler handler) {
	super();
	this.context = context;
	this.handler = handler;
    }

    @Override
    public void run() {
	Calendar calendar = Calendar.getInstance();
	calendar.setTimeInMillis(System.currentTimeMillis());
	GetCuesHelper helper = new GetCuesHelper(context, AppPropertiesUtil.getUserID(context),
		calendar.get(Calendar.HOUR_OF_DAY));
	ArrayList<Campaigns> cuesList = helper.execute();
	if (handler != null) {
	    if (cuesList != null && cuesList.size() > 0) {
		handler.didFetchComplete(cuesList);
	    } else {
		handler.didFail();
	    }
	}
	super.run();
    }
}
