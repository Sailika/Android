package com.paradigmcreatives.apspeak.stream.handlers;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.paradigmcreatives.apspeak.app.model.ASSOCIATION_TYPE;
import com.paradigmcreatives.apspeak.app.util.constants.JSONConstants;
import com.paradigmcreatives.apspeak.logging.Logger;
import com.paradigmcreatives.apspeak.stream.fragments.AssetDetailsFragment;
import com.paradigmcreatives.apspeak.stream.fragments.AssetDetailsWithCommentsFragment;
import com.paradigmcreatives.apspeak.stream.fragments.UserStreamFragment;

/**
 * Message handler for User's Asset Relationship remove request thread
 * 
 * @author Dileep | neuv
 * 
 */
public class RemoveAssetRelationshipHandler extends Handler {
	private static final String TAG = "RemoveAssetRelationshipHandler";

	private static final int PRE_EXECUTE = 1;
	private static final int SUCCESS = 2;
	private static final int FAILURE = 3;

	public static final String LIKE_VIEW = "like_view";

	private Fragment fragment;

	public RemoveAssetRelationshipHandler(Fragment fragment) {
		super();
		this.fragment = fragment;
	}

	public void willStartTask() {
		sendEmptyMessage(PRE_EXECUTE);
	}

	public void didRemoveRelationship(String assetId, ASSOCIATION_TYPE relationship) {
		Message msg = new Message();
		msg.what = SUCCESS;
		if (!TextUtils.isEmpty(assetId) && relationship != null) {
			Bundle data = new Bundle();
			data.putString(JSONConstants.ASSET_ID, assetId);
			data.putParcelable(JSONConstants.RELATIONSHIP, relationship);
			msg.setData(data);
		}
		sendMessage(msg);
	}

	public void failed(int statusCode, int errorCode, String assetId, ASSOCIATION_TYPE relationship) {
		Message msg = new Message();
		msg.what = FAILURE;
		msg.arg1 = statusCode;
		msg.arg2 = errorCode;
		if (!TextUtils.isEmpty(assetId) && relationship != null) {
			Bundle data = new Bundle();
			data.putString(JSONConstants.ASSET_ID, assetId);
			data.putParcelable(JSONConstants.RELATIONSHIP, relationship);
			msg.setData(data);
		}
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
				/*
				 * No need to notify user now, as we are following below steps:
				 * 1) User clicks on asset LOVE/UnLOVE icon --> We change the icon to Un-LOVE, 
				 * decrement LOVE count
				 * 2) Sends user un-loved request to server
				 * 3) Gets response from server --> No need to change LOVE count as we have already did in step 1.
				 * 
				 *  We are following this approach as to give better experience to User.
				 */
				/*
				if (fragment != null) {
					Bundle data = msg.getData();
					if (fragment instanceof UserStreamFragment) {
						((UserStreamFragment) fragment).assetRelationshipRemoveSuccess(
								data.getString(JSONConstants.ASSET_ID),
								(ASSOCIATION_TYPE) data.getParcelable(JSONConstants.RELATIONSHIP));
					} else if (fragment instanceof AssetDetailsFragment) {
						((AssetDetailsFragment) fragment).assetRelationshipRemoveSuccess(
								data.getString(JSONConstants.ASSET_ID),
								(ASSOCIATION_TYPE) data.getParcelable(JSONConstants.RELATIONSHIP));
					}
				}
				*/
				break;

			case FAILURE:
				if (fragment != null) {
					Bundle data = msg.getData();
					if (fragment instanceof UserStreamFragment) {
						((UserStreamFragment) fragment).assetRelationshipRemoveFailed(
								data.getString(JSONConstants.ASSET_ID),
								(ASSOCIATION_TYPE) data.getParcelable(JSONConstants.RELATIONSHIP));
					} else if (fragment instanceof AssetDetailsFragment) {
						((AssetDetailsFragment) fragment).assetRelationshipRemoveFailed(
								data.getString(JSONConstants.ASSET_ID),
								(ASSOCIATION_TYPE) data.getParcelable(JSONConstants.RELATIONSHIP));
					}else if(fragment instanceof AssetDetailsWithCommentsFragment){
						((AssetDetailsWithCommentsFragment) fragment).assetRelationshipRemoveFailed(
							data.getString(JSONConstants.ASSET_ID),
							(ASSOCIATION_TYPE) data.getParcelable(JSONConstants.RELATIONSHIP));
					}
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

