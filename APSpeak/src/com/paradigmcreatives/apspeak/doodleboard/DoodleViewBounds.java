package com.paradigmcreatives.apspeak.doodleboard;

import android.graphics.RectF;

/**
 * Stores the doodle bounds for the device
 * 
 * @author Soumya Behera | Paradigm Creatives
 * 
 */
public class DoodleViewBounds {

    private RectF boundRect;
    private float scaleFactor;

    DoodleViewBounds(RectF boundRect, float scaleFactor) {
	this.setBoundRect(boundRect);
	this.setScaleFactor(scaleFactor);
    }

    public RectF getBoundRect() {
	return boundRect;
    }

    public void setBoundRect(RectF boundRect) {
	this.boundRect = boundRect;
    }

    public float getScaleFactor() {
	return scaleFactor;
    }

    public void setScaleFactor(float scaleFactor) {
	this.scaleFactor = scaleFactor;
    }

}
