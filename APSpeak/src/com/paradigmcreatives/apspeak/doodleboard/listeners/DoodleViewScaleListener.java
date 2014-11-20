package com.paradigmcreatives.apspeak.doodleboard.listeners;

import android.graphics.PointF;
import android.view.ScaleGestureDetector;

import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.doodleboard.DoodleView;

/**
 * Detects the scaling gestures
 * 
 * @author robin
 * 
 */
public class DoodleViewScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

    private float mScaleFactor = 1.f;
    @SuppressWarnings("unused")
    private static final String TAG = "DoodleViewScaleListener";
    private DoodleView doodleView;

    public DoodleViewScaleListener(DoodleView doodleView) {
	this.doodleView = doodleView;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
	mScaleFactor = detector.getScaleFactor();

	// Don't let the object get too small or too large.
	mScaleFactor = Math.max(Constants.MIN_SCALE, Math.min(mScaleFactor, Constants.MAX_SCALE));

	// Get the focus point
	PointF focus = new PointF(detector.getFocusX(), detector.getFocusY());

	doodleView.handleScalingOfLayer(mScaleFactor, focus);

	return true;
    }

}
