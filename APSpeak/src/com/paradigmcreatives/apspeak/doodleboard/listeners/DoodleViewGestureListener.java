package com.paradigmcreatives.apspeak.doodleboard.listeners;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.paradigmcreatives.apspeak.doodleboard.DoodleView;

/**
 * Identifies the gestures being made on the doodle view
 * 
 * @author robin
 * 
 */
public class DoodleViewGestureListener extends GestureDetector.SimpleOnGestureListener {
    private static String TAG = "DoodleViewGestureListener";
    private DoodleView doodleView;
    private Context context;

    public DoodleViewGestureListener(Context context, DoodleView doodleView) {
	this.context = context;
	this.doodleView = doodleView;
    }

    @Override
    public boolean onDown(MotionEvent event) {
	return true;
    }

	// @Override
	// public void onLongPress(MotionEvent e) {
	// if (context != null) {
	// if (doodleView.getSelectedLayerID() >= 0) {
	// LayerDialogUtil.launchBackgroundChangeDialog(doodleView, context,
	// doodleView.getSelectedLayerID())
	// .show();
	// }
	// } else {
	// Logger.warn(TAG, "Null context passed in the doodle gesture listener");
	// }
	// Logger.info(TAG, "Longpress detected");
	// }
}
