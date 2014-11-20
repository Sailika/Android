package com.paradigmcreatives.apspeak.app.util.download.handlers;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;

import com.paradigmcreatives.apspeak.app.model.StreamAsset;
import com.paradigmcreatives.apspeak.logging.Logger;
import com.paradigmcreatives.apspeak.stream.fragments.AssetDetailsFragment;
import com.paradigmcreatives.apspeak.stream.fragments.AssetDetailsWithCommentsFragment;

/**
 * Message handler for Asset Details Download Thread
 * 
 * @author Dileep | neuv
 * 
 */
public class AssetDetailsDownloadHandler extends Handler {
    private static final String TAG = "AssetDetailsDownloadHandler";

    private static final int PRE_EXECUTE = 1;
    private static final int SUCCESS = 2;
    private static final int FAILURE = 3;
    private static final String ASSET_STATUS = "assetstatus";

    private Fragment fragment;

    public AssetDetailsDownloadHandler(Fragment fragment) {
	super();
	this.fragment = fragment;
    }

    public void willStartTask() {
	sendEmptyMessage(PRE_EXECUTE);
    }

    public void didDownloadComplete(StreamAsset asset) {
	Message msg = new Message();
	msg.what = SUCCESS;
	if (asset != null) {
	    Bundle bundle = new Bundle();
	    bundle.putParcelable(ASSET_STATUS, asset);
	    msg.setData(bundle);
	}
	sendMessage(msg);
    }

    public void failed(int statusCode, int errorCode, String reasonPhrase) {
	Message msg = new Message();
	msg.what = FAILURE;
	msg.arg1 = statusCode;
	msg.arg2 = errorCode;
	msg.obj = reasonPhrase;
	sendMessage(msg);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.os.Handler#handleMessage(android.os.Message)
     */
    @Override
    public void handleMessage(Message msg) {
	if (fragment != null) {
	    switch (msg.what) {
	    case PRE_EXECUTE:
		// do nothing
		break;

	    case SUCCESS:
		Bundle data = msg.getData();
		if(fragment != null){
			if (data != null && data.containsKey(ASSET_STATUS)) {
			    StreamAsset asset = data.getParcelable(ASSET_STATUS);
			    if (fragment instanceof AssetDetailsFragment) {
				((AssetDetailsFragment) fragment).setAssetDetailsFetchStatus(asset);
			    } else if (fragment instanceof AssetDetailsWithCommentsFragment) {
				((AssetDetailsWithCommentsFragment) fragment).setAssetDetailsFetchStatus(asset);
			    }
			}else if (fragment instanceof AssetDetailsWithCommentsFragment) {
			    ((AssetDetailsWithCommentsFragment) fragment).setAssetDetailsFetchStatus(null);
			}
		}
		break;

	    case FAILURE:
		if (fragment instanceof AssetDetailsWithCommentsFragment) {
		    ((AssetDetailsWithCommentsFragment) fragment).setAssetDetailsFetchStatus(null);
		}
		break;
	    default:
		break;
	    }
	} else {
	    Logger.warn(TAG, "Context is null");
	}
	super.handleMessage(msg);
    }

}
