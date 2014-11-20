package com.paradigmcreatives.apspeak.doodleboard.background.googleimages.handlers;

import java.util.ArrayList;

import android.os.Handler;
import android.os.Message;

import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.app.util.Util;
import com.paradigmcreatives.apspeak.app.util.google.image.beans.ImageResultsBean;
import com.paradigmcreatives.apspeak.doodleboard.background.googleimages.GoogleImageActivity;
import com.paradigmcreatives.apspeak.doodleboard.fragments.BackgroundFragment;
import com.paradigmcreatives.apspeak.logging.Logger;

/**
 * Handles the google image search messages
 * 
 * @author Soumya Behera | Paradigm Creatives
 * 
 */
public class GoogleImageSearchHandler extends Handler {
    private static final String TAG = "GoogleImageSearchHandler";

    private static final int PRE_EXECUTE = 1;
    private static final int SUCCESS = 2;
    private static final int FAILURE = 3;

    private BackgroundFragment fragment;

    // private Dialog progressDialog;

    public GoogleImageSearchHandler(BackgroundFragment fragment) {
	super();
	this.fragment = fragment;
    }

    public void willStartTask() {
	sendEmptyMessage(PRE_EXECUTE);
    }

    public void didSucceed(ArrayList<ImageResultsBean> searchResult) {
	Message msg = new Message();
	msg.what = SUCCESS;
	msg.obj = searchResult;
	sendMessage(msg);
    }

    public void didFail() {
	sendEmptyMessage(FAILURE);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.os.Handler#handleMessage(android.os.Message)
     */
    @SuppressWarnings("unchecked")
    @Override
    public void handleMessage(Message msg) {
	if (msg != null && fragment != null) {
	    switch (msg.what) {
	    case PRE_EXECUTE:
		// progressDialog = DoodleDialogsUtil.progressDialog(activity, activity.getString(R.string.wait),
		// activity.getString(R.string.searching));
		// progressDialog.show();
		break;

	    case SUCCESS:
		// if (progressDialog != null && progressDialog.isShowing()) {
		// progressDialog.dismiss();
		// }
		if (msg.obj != null && msg.obj instanceof ArrayList<?>) {
		    fragment.updateAdapter((ArrayList<ImageResultsBean>) msg.obj);
		} else {
		    Util.displayToast(fragment.getActivity(), fragment.getString(R.string.no_image_search_result));
		}
		updateLoadState();
		break;

	    case FAILURE:
		// if (progressDialog != null && progressDialog.isShowing()) {
		// progressDialog.dismiss();
		// }
		Util.displayToast(fragment.getActivity(), fragment.getString(R.string.no_image_search_result));
		updateLoadState();
		break;

	    default:
		break;
	    }
	} else {
	    Logger.warn(TAG, "Invalid message or activity");
	}

	super.handleMessage(msg);
    }
    
    private void updateLoadState() {
	if (fragment != null) {
	    fragment.setLoadingMore(false);
	} else {
	    Logger.warn(TAG, "Activity is null");
	}
    }
}
