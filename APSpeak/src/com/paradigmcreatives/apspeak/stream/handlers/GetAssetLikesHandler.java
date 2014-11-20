package com.paradigmcreatives.apspeak.stream.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;

import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.app.model.ASSOCIATION_TYPE;
import com.paradigmcreatives.apspeak.app.model.Friend;
import com.paradigmcreatives.apspeak.app.model.StreamAsset;
import com.paradigmcreatives.apspeak.logging.Logger;
import com.paradigmcreatives.apspeak.stream.fragments.AssetDetailsFragment;
import com.paradigmcreatives.apspeak.stream.fragments.AssetDetailsWithCommentsFragment;
import com.paradigmcreatives.apspeak.stream.fragments.UserStreamFragment;

/**
 * Message handler for a thread that fetches Asset's Likes
 * 
 * @author Dileep | neuv
 * 
 */
public class GetAssetLikesHandler extends Handler {
	private static final String TAG = "GetAssetLikesHandler";

	private static final int PRE_EXECUTE = 1;
	private static final int SUCCESS = 2;
	private static final int FAILURE = 3;
	private static final String ASSOCIATION_USERS = "association_users";
	private static final String START_INDEX = "startIndex";
	private static final String LIMIT = "limit";

	private Fragment fragment;

	public GetAssetLikesHandler(Fragment fragment) {
		super();
		this.fragment = fragment;
	}

	public void willStartTask() {
		sendEmptyMessage(PRE_EXECUTE);
	}

	public void didFetchComplete(ArrayList<Friend> associations) {
		if (associations != null && associations.size() > 0) {
			Message msg = new Message();
			msg.what = SUCCESS;
			Bundle bundle = new Bundle();
			bundle.putParcelableArrayList(ASSOCIATION_USERS, associations);
			msg.setData(bundle);
			sendMessage(msg);
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
				if (fragment != null && data != null && data.containsKey(ASSOCIATION_USERS)) {
					if (fragment instanceof AssetDetailsFragment) {
						ArrayList<Friend> assetLikedUsers = new ArrayList<Friend>();
						assetLikedUsers = data.getParcelableArrayList(ASSOCIATION_USERS);
						((AssetDetailsFragment)fragment).setAssetLikedUsers(assetLikedUsers);
					} else if(fragment instanceof AssetDetailsWithCommentsFragment){
						ArrayList<Friend> assetLikedUsers = new ArrayList<Friend>();
						assetLikedUsers = data.getParcelableArrayList(ASSOCIATION_USERS);
						((AssetDetailsWithCommentsFragment)fragment).setAssetLikedUsers(assetLikedUsers);
					}
				}
				break;

			case FAILURE:
			    /*
			    if(fragment != null && fragment instanceof UserStreamFragment){
				String errorMessage = null;
				if(msg.obj != null){
				    errorMessage = (String)msg.obj;
				}
				((UserStreamFragment)fragment).setErrorMessage(errorMessage);
			    }
			    */
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

