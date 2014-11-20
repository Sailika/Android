package com.paradigmcreatives.apspeak.doodleboard.gesture;

import android.view.MotionEvent;

import com.paradigmcreatives.apspeak.logging.Logger;

/**
 * Taken from http://stackoverflow.com/questions/10682019/android-two-finger-rotation
 * 
 * @author robin
 * 
 */
public class RotationGestureDetector {
    private static final int INVALID_POINTER_ID = -1;
    @SuppressWarnings("unused")
    private static final String TAG = "RotationGestureDetector";
    private float fX, fY, sX, sY;
    private int ptrID1, ptrID2;
    private float mAngle;
    private static final float MAX_ANGLE_PER_GESTURE = 100.f;
    private static final float MIN_ANGLE_PER_GESTURE = 0.3f;

    private OnRotationGestureListener mListener;

    public float getAngle() {
	return mAngle;
    }

    public RotationGestureDetector(OnRotationGestureListener listener) {
	mListener = listener;
	ptrID1 = INVALID_POINTER_ID;
	ptrID2 = INVALID_POINTER_ID;
    }

    public boolean onTouchEvent(MotionEvent event) {
	switch (event.getActionMasked()) {
	case MotionEvent.ACTION_DOWN:
	    ptrID1 = event.getPointerId(event.getActionIndex());
	    break;
	case MotionEvent.ACTION_POINTER_DOWN:
	    ptrID2 = event.getPointerId(event.getActionIndex());
	    sX = event.getX(event.findPointerIndex(ptrID1));
	    sY = event.getY(event.findPointerIndex(ptrID1));
	    fX = event.getX(event.findPointerIndex(ptrID2));
	    fY = event.getY(event.findPointerIndex(ptrID2));
	    break;
	case MotionEvent.ACTION_MOVE:
	    if (ptrID1 != INVALID_POINTER_ID && ptrID2 != INVALID_POINTER_ID) {
		float nfX, nfY, nsX, nsY;
		nsX = event.getX(event.findPointerIndex(ptrID1));
		nsY = event.getY(event.findPointerIndex(ptrID1));
		nfX = event.getX(event.findPointerIndex(ptrID2));
		nfY = event.getY(event.findPointerIndex(ptrID2));

		mAngle = angleBetweenLines(fX, fY, sX, sY, nfX, nfY, nsX, nsY);
		Logger.info(TAG, "Angle: " + mAngle);
		if (mListener != null && Math.abs(mAngle) < MAX_ANGLE_PER_GESTURE && Math.abs(mAngle) > MIN_ANGLE_PER_GESTURE) {
		    mListener.OnRotation(this);
		} else { // Kill the event
		    ptrID2 = INVALID_POINTER_ID;
		}
	    }
	    break;
	case MotionEvent.ACTION_UP:
	    ptrID1 = INVALID_POINTER_ID;
	    break;
	case MotionEvent.ACTION_POINTER_UP:
	    ptrID2 = INVALID_POINTER_ID;
	    break;
	}
	return true;
    }

    private float angleBetweenLines(float fX, float fY, float sX, float sY, float nfX, float nfY, float nsX, float nsY) {
	float angle1 = (float) Math.atan2((fY - sY), (fX - sX));
	float angle2 = (float) Math.atan2((nfY - nsY), (nfX - nsX));

	float angle = ((float) Math.toDegrees(angle1 - angle2)) % 360;
	if (angle < -180.f)
	    angle += 360.0f;
	if (angle > 180.f)
	    angle -= 360.0f;
	return angle;
    }

    public static interface OnRotationGestureListener {
	public void OnRotation(RotationGestureDetector rotationDetector);
    }
}