package com.paradigmcreatives.apspeak.doodleboard.handlers;

import android.os.Handler;

/**
 * Handles the message transfer during image addition to canvas
 * 
 * @author Soumya Behera | Paradigm Creatives
 * 
 */
public class CanvasAddImageHandler extends Handler {
//    private static final String TAG = "CanvasAddImageHandler";
//
//    private CanvasActivity canvasActivity;
//    private Dialog progressDialog;
//    private Intent data;
//
//    private static final int PRE_EXECUTE = 1;
//    private static final int POST_EXECUTE = 2;
//
//    public CanvasAddImageHandler(final CanvasActivity canvasActivity, Intent data) {
//	super();
//	this.canvasActivity = canvasActivity;
//	this.data = data;
//    }
//
//    public void willStartTask() {
//	sendEmptyMessage(PRE_EXECUTE);
//    }
//
//    public void didEndTask() {
//	sendEmptyMessage(POST_EXECUTE);
//    }
//
//    /*
//     * (non-Javadoc)
//     * 
//     * @see android.os.Handler#handleMessage(android.os.Message)
//     */
//    @Override
//    public void handleMessage(Message msg) {
//	if (msg != null) {
//	    switch (msg.what) {
//	    case PRE_EXECUTE:
//		if (canvasActivity != null) {
//		    DoodleView doodleView = canvasActivity.getDoodleView();
//		    if (doodleView != null) {
//			doodleView.setPlayState(PlayState.BLOCKED);
//			progressDialog = DoodleDialogsUtil.progressDialog(canvasActivity,
//				canvasActivity.getString(R.string.wait),
//				canvasActivity.getString(R.string.wait_image_loading));
//			progressDialog.show();
//		    } else {
//			Logger.warn(TAG, "DoodleView is null");
//		    }
//		} else {
//		    Logger.warn(TAG, "Canvas activity is null");
//		}
//		break;
//
//	    case POST_EXECUTE:
//		// Dismiss the progress dialog if its still there
//		if (progressDialog != null && progressDialog.isShowing()) {
//		    progressDialog.dismiss();
//		}
//		if (canvasActivity != null) {
//		    if (canvasActivity.isDrawStart()) {
//			canvasActivity.checkAndShowDrawStartAnimations();
//		    } else {
//			// canvasActivity.checkAndDoPeekaBooOfAddonOrDisappearAddOn();
//		    }
//		    DoodleView doodleView = canvasActivity.getDoodleView();
//		    if (doodleView != null) {
//
//			// If a layer is added then the play state gets changed to layers while its addition itself,
//			// otherwise, the play state should be restored to the state before initiating this action
//			if (doodleView.getPlayState() != PlayState.LAYERS) {
//			    doodleView.restorePreviousPlayState();
//			}
//			canvasActivity.initiateLoadingOfLargeImage(data);
//		    } else {
//			Logger.warn(TAG, "DoodleView is null");
//		    }
//		} else {
//		    Logger.warn(TAG, "Canvas activity is null");
//		}
//		break;
//
//	    default:
//		break;
//	    }
//	}
//	super.handleMessage(msg);
//    }
}
