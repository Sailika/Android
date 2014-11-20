package com.paradigmcreatives.apspeak.stream.handlers;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;

import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.app.model.StreamAsset;
import com.paradigmcreatives.apspeak.featured.fragments.FeaturedFragment;
import com.paradigmcreatives.apspeak.globalstream.fragments.GlobalStreamsFragment;
import com.paradigmcreatives.apspeak.logging.Logger;
import com.paradigmcreatives.apspeak.stream.fragments.AssetDetailsWithCommentsFragment;
import com.paradigmcreatives.apspeak.stream.fragments.UserStreamFragment;
import com.paradigmcreatives.apspeak.stream.tasks.GetStreamThread.STREAM_TYPE;

/**
 * Message handler for User's Stream Fetch Thread
 * 
 * @author Dileep | neuv
 * 
 */
public class GetStreamHandler extends Handler {
    private static final String TAG = "GetUserStreamHandler";

    private static final int PRE_EXECUTE = 1;
    private static final int SUCCESS = 2;
    private static final int FAILURE = 3;
    private static final String STREAM_ASSETS = "assets";
    private static final String STREAM_TYPE = "streamType";
    private static final String START_INDEX = "startIndex";
    private static final String LIMIT = "limit";

    private Fragment fragment;

    public GetStreamHandler(Fragment fragment) {
	super();
	this.fragment = fragment;
    }

    public void willStartTask() {
	sendEmptyMessage(PRE_EXECUTE);
    }

    public void didFetchComplete(ArrayList<StreamAsset> streamAssets, STREAM_TYPE streamType) {
	if (streamAssets != null && streamAssets.size() > 0) {
	    Message msg = new Message();
	    msg.what = SUCCESS;
	    Bundle bundle = new Bundle();
	    if (streamType != null) {
		bundle.putString(STREAM_TYPE, streamType.name());
	    }
	    bundle.putParcelableArrayList(STREAM_ASSETS, streamAssets);
	    msg.setData(bundle);
	    sendMessage(msg);
	} else {
	    if (fragment != null) {
		failed(-1, -1, fragment.getActivity().getString(R.string.empty_stream));
	    }
	}
    }

    public void didBatchFetchComplete(ArrayList<StreamAsset> streamAssets, int startIndex, int limit,
	    STREAM_TYPE streamType) {
	if (streamAssets != null && streamAssets.size() > 0) {
	    Message msg = new Message();
	    msg.what = SUCCESS;
	    Bundle bundle = new Bundle();
	    if (streamType != null) {
		bundle.putString(STREAM_TYPE, streamType.name());
	    }
	    bundle.putParcelableArrayList(STREAM_ASSETS, streamAssets);
	    bundle.putInt(START_INDEX, startIndex);
	    bundle.putInt(LIMIT, limit);
	    msg.setData(bundle);
	    sendMessage(msg);
	} else {
	    if (fragment != null) {
		failed(-1, -1, fragment.getActivity().getString(R.string.empty_stream));
	    }
	}
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
		if (fragment != null && data != null && data.containsKey(STREAM_ASSETS)) {
		    ArrayList<StreamAsset> streamAssets = new ArrayList<StreamAsset>();
		    streamAssets = data.getParcelableArrayList(STREAM_ASSETS);
		    String streamType = null;
		    if (data.containsKey(STREAM_TYPE)) {
			streamType = data.getString(STREAM_TYPE);
		    }
		    int startIndex = data.getInt(START_INDEX, -1);
		    int limit = data.getInt(LIMIT, -1);
		    if (fragment instanceof UserStreamFragment) {
			if (startIndex != -1 && limit != -1) {
			    ((UserStreamFragment) fragment).setBatchedUserStream(streamAssets, startIndex, limit);
			} else {
			    ((UserStreamFragment) fragment).setUserStream(streamAssets);
			}
		    } else if (fragment instanceof GlobalStreamsFragment) {
			((GlobalStreamsFragment) fragment)
				.setBatchedStream(streamAssets, startIndex, limit, streamType);
		    } else if (fragment instanceof AssetDetailsWithCommentsFragment) {
			((AssetDetailsWithCommentsFragment) fragment).setCommentsStream(streamAssets);
		    } else if (fragment instanceof FeaturedFragment) {
			((FeaturedFragment) fragment).setBatchedStream(streamAssets, startIndex, limit, streamType);
		    }
		}
		break;

	    case FAILURE:

		String errorMessage = null;
		if (msg.obj != null) {
		    errorMessage = (String) msg.obj;
		}
		if (fragment != null && fragment instanceof UserStreamFragment) {
		    ((UserStreamFragment) fragment).setErrorMessage(errorMessage);
		} else if (fragment instanceof GlobalStreamsFragment) {
		    ((GlobalStreamsFragment) fragment).setErrorMessage(errorMessage);
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
