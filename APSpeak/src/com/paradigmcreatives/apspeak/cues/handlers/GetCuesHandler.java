package com.paradigmcreatives.apspeak.cues.handlers;

import java.util.ArrayList;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;

import com.paradigmcreatives.apspeak.app.model.Campaigns;
import com.paradigmcreatives.apspeak.cues.fragments.CuesFragment;

/**
 * Message handler for a thread that fetches Whatsay CUEs
 * 
 * @author Dileep | neuv
 * 
 */
public class GetCuesHandler extends Handler {

    private static final int SUCCESS = 1;
    private static final int FAILURE = 2;
    private Fragment fragment;

    public GetCuesHandler(Fragment fragment) {
	super();
	this.fragment = fragment;
    }

    @Override
    public void handleMessage(Message msg) {
	switch (msg.what) {
	case SUCCESS:
	    if (fragment != null) {
		if (fragment instanceof CuesFragment) {
		    try {
			CuesFragment cuesFragment = ((CuesFragment) fragment);
			cuesFragment.setCues((ArrayList<Campaigns>) msg.obj);
			cuesFragment.setAdapter();
		    } catch (Exception e) {
			((CuesFragment) fragment).setCuesFetchError();
		    }
		}
	    }
	    break;

	case FAILURE:
	    if (fragment != null) {
		if (fragment instanceof CuesFragment) {
		    ((CuesFragment) fragment).setCuesFetchError();
		}
	    }
	    break;

	default:
	    break;
	}
	super.handleMessage(msg);
    }

    public void didFetchComplete(ArrayList<Campaigns> cuesList) {
	Message msg = new Message();
	msg.what = SUCCESS;
	msg.obj = cuesList;
	sendMessage(msg);
    }

    public void didFail() {
	Message msg = new Message();
	msg.what = FAILURE;
	sendMessage(msg);
    }
}
