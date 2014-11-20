package com.paradigmcreatives.apspeak.doodleboard.listeners;

import com.paradigmcreatives.apspeak.doodleboard.DoodleView;
import com.paradigmcreatives.apspeak.doodleboard.gesture.RotationGestureDetector;
import com.paradigmcreatives.apspeak.doodleboard.gesture.RotationGestureDetector.OnRotationGestureListener;

public class DoodleViewRotationListener implements OnRotationGestureListener {

    @SuppressWarnings("unused")
    private static final String TAG = "DoodleViewRotationListener";
    private DoodleView doodleView;
    private static final float DAMPNESS_FACTOR = 25.0f;

    public DoodleViewRotationListener(DoodleView doodleView) {
	this.doodleView = doodleView;
    }

    @Override
    public void OnRotation(RotationGestureDetector rotationDetector) {
	doodleView.handleRotationOfLayer(rotationDetector.getAngle() / DAMPNESS_FACTOR * -1);
    }

}
