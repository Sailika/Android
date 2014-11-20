package com.paradigmcreatives.apspeak.doodleboard;

import java.util.ArrayList;
import java.util.Iterator;

import android.graphics.Color;
import android.graphics.PointF;

import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.doodleboard.DoodleView.PlayState;
import com.paradigmcreatives.apspeak.logging.Logger;

class DoodlePlay extends Thread {
    private static final String TAG = "DoodlePlay";
    ArrayList<Object> doodle;
    int wait;
    DoodleViewProperties doodleViewProperties;
    DoodleView doodleView;
    boolean allowDrawAfterPlay;

    private static final int FAST_DIVIDER = 100;
    private static final int MEDIUM_DIVIDER = 400;

    private static final int DEFAULT_FAST_STEPPER = 5;
    private static final int DEFAULT_MEDIUM_STEPPER = 1;

    public DoodlePlay(DoodleView doodleView, ArrayList<Object> doodle, int wait) {
	this.doodleView = doodleView;
	this.doodle = doodle;
	this.wait = wait;
	this.allowDrawAfterPlay = false;
	this.doodleViewProperties = null;
    }

    public DoodlePlay(DoodleView doodleView, ArrayList<Object> doodle, DoodleViewProperties doodleViewProperties,
	    int wait) {
	this.doodleView = doodleView;
	this.doodle = doodle;
	this.wait = wait;
	this.doodleViewProperties = doodleViewProperties;
	this.allowDrawAfterPlay = false;
    }

    public DoodlePlay(DoodleView doodleView, ArrayList<Object> doodle, DoodleViewProperties doodleViewProperties,
	    int wait, boolean allowDrawAfterPlay) {
	this.doodleView = doodleView;
	this.doodle = doodle;
	this.wait = wait;
	this.doodleViewProperties = doodleViewProperties;
	this.allowDrawAfterPlay = allowDrawAfterPlay;
    }

    public void run() {
	if (doodleView != null) {

	    doodleView.waitForSurfaceInit();
	    if (doodleView.getPlayState() == PlayState.REPLAY) {
		doodleView.clearSurface();
	    } else {
		doodleView.clear();
	    }

	    // Update the DoodleViewProperties
	    if (doodleViewProperties != null) {
		Logger.info(TAG, "Doodle Play doodle properties : " + doodleViewProperties.toString());
		doodleView.setDoodleViewProperties(doodleViewProperties);
		doodleView.calculateDoodleBounds();

		if (doodleViewProperties.getBackgroundBitmap() != null) {
		    doodleView.setBackgroundImage(doodleViewProperties.getBackgroundBitmap());
		    doodleView.drawBackgroundImage(this.doodleViewProperties.getBackgroundBitmap());
		}
		
		if(doodleViewProperties.getBitmapLayers() != null && doodleViewProperties.getBitmapLayers().size() > 0) {
		    doodleView.drawLayers(false);
		}
	    }

	    if (doodle == null) {
		// If the supplied doodle is null then say buh-bye here
		return;
	    }

	    // Update the doodle model of doodle view
	    doodleView.setDoodle(doodle);

	    // Update the play state
	    doodleView.setPlayState(PlayState.PLAYING);

	    try {
		Iterator<Object> iterator = doodle.iterator();
		PointF touched = null;
		PointF lastTouched = null;
		int touchedPointsCount = 0;
		StrokeBean stroke = null;
		String lineBreak = null;
		int count = 0;
		int drawJumpSteps = 0;
		if (wait == Constants.DOODLE_SPEED_MEDIUM) {
		    drawJumpSteps = (doodle.size() > 0) ? (doodle.size() / MEDIUM_DIVIDER) : DEFAULT_MEDIUM_STEPPER;
		    if (drawJumpSteps < DEFAULT_MEDIUM_STEPPER) {
			drawJumpSteps = DEFAULT_MEDIUM_STEPPER;
		    }
		} else if (wait == Constants.DOODLE_SPEED_HIGH) {
		    drawJumpSteps = (doodle.size() > 0) ? (doodle.size() / FAST_DIVIDER) : DEFAULT_FAST_STEPPER;
		    if (drawJumpSteps < DEFAULT_FAST_STEPPER) {
			drawJumpSteps = DEFAULT_FAST_STEPPER;
		    }
		}
		while (iterator.hasNext() && doodleView.getPlayState() != PlayState.KILLING) {
		    switch (doodleView.getPlayState()) {
		    case BLOCKED:
		    case PAUSED:
			break;
		    case STOPPED:
			// update the wait value so that the entire doodle gets drawn immediately without delay
			wait = 0;

		    case PLAYING:
		    case REPLAY:
		    default:
			Object object = iterator.next();
			if (object instanceof StrokeBean) { // Item has stroke data
			    stroke = (StrokeBean) object;

			    // If the stroke has color data change the brush color
			    if (stroke.getColor() != null) {
				doodleView.getBrush().setColor(Color.parseColor("#" + stroke.getColor()));
			    }

			    // If the stroke has size data change the brush size
			    if (stroke.getSize() > 0) {
				float scaleFactor = doodleView.getScaleFactor();
				int strokeSize = (int) (scaleFactor * stroke.getSize());
				doodleView.getBrush().setStrokeWidth(strokeSize);
			    }
			} else if (object instanceof PointF) { // Item has point data
			    touched = (PointF) object;
			    touchedPointsCount++;
			    /*
			     * Logic involved in drawing DOT is as follows:
			     * Track the count of PointF objects since the last LINE_BREAK object.
			     * IF the count exceeds 1(one) then trigger the DOT/POINT draw in the immediate iteration where LINE_BREAK comes in
			     * ELSE perform the line draw immediately
			     */
			    if(touchedPointsCount > 1){
				    // Draw the line by passing FROM Point and TO Point
				    doodleView.doDraw(touched, lastTouched); // This triggers LINE draw as we are passing valid points for touched and lastTouched objects
			    }else{
			    	// Trigger the DOT/POINT draw in the next iteration where LINE_BREAK comes in.
			    }

			    // Update last touched
			    lastTouched = touched;
			} else if (object instanceof String) { // Item has LINE_BREAK
			    // Its a line break. Update last touched so that a point is drawn
			    lineBreak = (String) object;
			    if (lineBreak.equals(DoodleView.LINE_BREAK)) {
			    	if(touchedPointsCount == 1){
					    // Trigger the DOT/POINT draw by passing lastTouched as null
					    doodleView.doDraw(touched, null);
			    	}
			    touchedPointsCount = 0;
				lastTouched = null;
			    }
			}

		    }
		    if (wait > 0) {
			if (wait == Constants.DOODLE_SPEED_MEDIUM) {
			    if (count >= drawJumpSteps) {
				count = 0;
				Thread.sleep(wait);
				doodleView.updateCanvas();
			    } else {
				count++;
			    }
			} else {
			    Thread.sleep(wait);
			    doodleView.updateCanvas();
			}
		    } else {
			if (!(doodleView.getPlayState() == PlayState.STOPPED)) {
			    if (count >= drawJumpSteps) {
				count = 0;
				doodleView.updateCanvas();
			    } else {
				count++;
			    }
			}
		    }
		}
		doodleView.updateCanvas();
	    } catch (InterruptedException e) {
		Logger.warn(TAG, "Issue while trying to wait for doodle to draw" + e);
	    } catch (Exception e) {
		Logger.logStackTrace(e);
	    }

	    if (doodleView.getPlayState() == PlayState.KILLING) {
		doodleView.setPlayState(PlayState.KILLED);
	    } else {
		if (allowDrawAfterPlay) {
		    doodleView.setPlayState(PlayState.DRAW);
		    doodleView.onReplayStop();
		} else {
		    doodleView.setPlayState(PlayState.STOPPED);
		    doodleView.onPlayStop();
		}

	    }
	}
    }

    public void pauseDoodle() {
	if (doodleView != null && doodleView.getPlayState() == PlayState.PLAYING) {
	    doodleView.setPlayState(PlayState.PAUSED);
	}
    }

    public void resumeDoodle() {
	if (doodleView != null && doodleView.getPlayState() == PlayState.PAUSED) {
	    doodleView.setPlayState(PlayState.PLAYING);
	}
    }

    public void stopDoodle() {
	if (doodleView != null) {
	    doodleView.setPlayState(PlayState.STOPPED);
	}
    }

    public void kill() {
	if (doodleView != null) {
	    doodleView.setPlayState(PlayState.KILLING);
	}
    }

}
