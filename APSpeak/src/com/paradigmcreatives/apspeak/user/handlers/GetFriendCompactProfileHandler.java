package com.paradigmcreatives.apspeak.user.handlers;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.paradigmcreatives.apspeak.app.model.Friend;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.logging.Logger;
import com.paradigmcreatives.apspeak.notification.WhatsayNotificationManager;
import com.paradigmcreatives.apspeak.user.fragments.ProfileFragment;
import com.paradigmcreatives.apspeak.user.tasks.ImageDownloadThread;

/**
 * Message handler for DoodlyDoo Friend's compact profile fetch thread
 * 
 * @author Dileep | neuv
 * 
 */
public class GetFriendCompactProfileHandler extends Handler {
    private static final String TAG = "GetFriendCompactProfileHandler";

    private static final int PRE_EXECUTE = 1;
    private static final int SUCCESS = 2;
    private static final int FAILURE = 3;

    private Context context;
    private String notificationType;
    private String assetId;
    private Friend friend;
    private Fragment mFragment;
    private String message;

    public GetFriendCompactProfileHandler(Context context, String notificationType) {
	super();
	this.context = context;
	this.notificationType = notificationType;
    }

    public GetFriendCompactProfileHandler(Context context, String assetId, String notificationType) {
	super();
	this.context = context;
	this.assetId = assetId;
	this.notificationType = notificationType;
    }

    public GetFriendCompactProfileHandler(Context context, String assetId, String notificationType, String message) {
	super();
	this.context = context;
	this.assetId = assetId;
	this.notificationType = notificationType;
	this.message = message;
    }

    public GetFriendCompactProfileHandler(Context context, Fragment fragment) {
	super();
	this.context = context;
	this.mFragment = fragment;
    }

    public void willStartTask() {
	sendEmptyMessage(PRE_EXECUTE);
    }

    public void didFetchComplete(Friend friend) {
	Message msg = new Message();
	msg.what = SUCCESS;
	if (friend != null) {
	    Bundle data = new Bundle();
	    data.putParcelable(Constants.FRIEND_OBJECT, friend);
	    msg.setData(data);
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
	if (context != null) {
	    switch (msg.what) {
	    case PRE_EXECUTE:
		// do nothing
		break;

	    case SUCCESS:
		if (mFragment != null) {
		    if (mFragment instanceof ProfileFragment) {
			Bundle bundle = msg.getData();
			if (bundle != null && bundle.containsKey(Constants.FRIEND_OBJECT)) {
			    friend = bundle.getParcelable(Constants.FRIEND_OBJECT);
			    ((ProfileFragment) mFragment).onSuccessfulProfileFetch(friend);
			}
		    }
		} else if (!TextUtils.isEmpty(notificationType)) {
		    Bundle data = msg.getData();
		    if (data.containsKey(Constants.FRIEND_OBJECT)) {
			friend = data.getParcelable(Constants.FRIEND_OBJECT);
			if (!TextUtils.isEmpty(friend.getProfilePicURL())) {
			    downloadFriendPic(friend.getProfilePicURL());
			} else if (!TextUtils.isEmpty(friend.getCoverImageURL())) {
			    downloadFriendPic(friend.getCoverImageURL());
			} else {
			    sendNotificationNow();
			}
		    }
		}
		break;

	    case FAILURE:
		break;
	    default:
		break;
	    }
	} else {
	    Logger.warn(TAG, "Context is null");
	}
	super.handleMessage(msg);
    }

    /**
     * Downloads image/bitmap from the give imageURL
     */
    private void downloadFriendPic(String imageURL) {
	ImageDownloadHandler handler = new ImageDownloadHandler(this);
	new ImageDownloadThread(context, imageURL, handler).start();
    }

    /**
     * Sets friend's bitmap (might be either Profile Pic or Cover Image)
     * 
     * @param bmp
     *            bitmap
     * @param imageURL
     *            image url
     */
    public void setFriendBitmap(Bitmap bmp, String imageURL) {
	if (bmp != null && !TextUtils.isEmpty(imageURL)) {
	    if (friend != null) {
		if (!TextUtils.isEmpty(friend.getProfilePicURL()) && friend.getProfilePicURL().equals(imageURL)) {
		    friend.setProfilePicBitmap(bmp);
		    sendNotificationNow();
		} else if (!TextUtils.isEmpty(friend.getCoverImageURL()) && friend.getCoverImageURL().equals(imageURL)) {
		    friend.setCoverImageBitmap(bmp);
		    sendNotificationNow();
		} else {
		    // do nothing
		}
	    }
	}
    }

    /**
     * Gets called when bitamp for the given image url is failed to download
     */
    public void friendBitmapFailed() {
	sendNotificationNow();
    }

    /**
     * Triggers NEW_FRIEND joined / USER_FOLLOWED notification
     */
    private void sendNotificationNow() {
	if (friend != null) {
	    if (notificationType.equals(Constants.NEW_FRIEND)) {
		// Show a notification saying friend has joined
		WhatsayNotificationManager.getInstance().sendNewFriendJoinedNotificationNow(context, friend, message);
	    } else if (notificationType.equals(Constants.USER_FOLLOWED)) {
		// Show a notification saying friend following
		WhatsayNotificationManager.getInstance().sendUserFollowedNotificationNow(context, friend);
	    } else if (notificationType.equals(Constants.USER)) {
		// Show a notification of a user
		WhatsayNotificationManager.getInstance().sendUserNotificationNow(context, friend, message);
	    } else if (notificationType.equals(Constants.ASSET_LOVED)) {
		// Show a notification saying friend loved your asset
		WhatsayNotificationManager.getInstance().sendAssetLovedNotificationNow(context, friend, assetId, message);
	    } else if (notificationType.equals(Constants.ASSET_COMMENTED)) {
		// Show a notification saying friend commented your asset
		WhatsayNotificationManager.getInstance().sendAssetCommentedNotificationNow(context, friend, assetId);
	    } else if (notificationType.equals(Constants.NEW_EXPRESSION)) {
		// Show a notification saying friend posted new expression
		WhatsayNotificationManager.getInstance().sendNewAssetNotificationNow(context, friend, assetId);
	    } else if (notificationType.equals(Constants.EXPRESSION)) {
		// Show a notification showing expression details 
		WhatsayNotificationManager.getInstance().sendNewAssetNotificationNow(context, friend, assetId);
	    }
	}
    }

}
