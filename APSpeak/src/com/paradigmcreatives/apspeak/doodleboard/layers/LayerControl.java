package com.paradigmcreatives.apspeak.doodleboard.layers;

import android.view.View;

/**
 * Bean for showing various controls over the layers
 * 
 * @author robin
 * 
 */
public class LayerControl {
    public enum Gravity {
	LEFT, RIGHT, CENTER, TOP, BOTTOM
    }

    private View view;
    private Gravity gravityX;
    private Gravity gravityY;

    /**
     * @param view
     * @param gravityX
     * @param gravityY
     */
    public LayerControl(View view, Gravity gravityX, Gravity gravityY) {
	this.view = view;
	this.gravityX = gravityX;
	this.gravityY = gravityY;
    }

    /**
     * @return the view
     */
    public View getView() {
	return view;
    }

    /**
     * @param view
     *            the view to set
     */
    public void setView(View view) {
	this.view = view;
    }

    /**
     * @return the gravityX
     */
    public Gravity getGravityX() {
	return gravityX;
    }

    /**
     * @param gravityX
     *            the gravityX to set
     */
    public void setGravityX(Gravity gravityX) {
	this.gravityX = gravityX;
    }

    /**
     * @return the gravityY
     */
    public Gravity getGravityY() {
	return gravityY;
    }

    /**
     * @param gravityY
     *            the gravityY to set
     */
    public void setGravityY(Gravity gravityY) {
	this.gravityY = gravityY;
    }

}
