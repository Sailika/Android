package com.paradigmcreatives.apspeak.doodleboard;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.doodleboard.layers.Layer;

/**
 * Contains settings of a doodle view and helper method to persist the settings in the SDCard
 * 
 * @author robin
 * 
 */
public class DoodleViewProperties {

    private int screenWidth;
    private int screenHeight;
    private int backgroundColor;
    private Bitmap backgroundBitmap;
    private ArrayList<Layer> bitmapLayers;

    /**
     * Constructor without background image
     * 
     * @param screenWidth
     *            : the screen width
     * @param screenHeight
     *            : The screen height
     * @param backgroundColor
     *            : The background color
     */
    public DoodleViewProperties(int screenWidth, int screenHeight, int backgroundColor) {
	this(screenWidth, screenHeight, backgroundColor, null);
    }

    /**
     * Constructor with all fields
     * 
     * @param screenWidth
     *            : the screen width
     * @param screenHeight
     *            : The screen height
     * @param backgroundColor
     *            : The background color
     * @param backgroundBitmap
     *            : The background image
     */
    public DoodleViewProperties(int screenWidth, int screenHeight, int backgroundColor, Bitmap backgroundBitmap) {
	super();
	this.screenWidth = screenWidth;
	this.screenHeight = screenHeight;
	this.backgroundColor = backgroundColor;
	this.backgroundBitmap = backgroundBitmap;
    }
    
    public DoodleViewProperties(int screenWidth, int screenHeight, int backgroundColor, Bitmap backgroundBitmap, ArrayList<Layer> bitmapLayers) {
	super();
	this.screenWidth = screenWidth;
	this.screenHeight = screenHeight;
	this.backgroundColor = backgroundColor;
	this.backgroundBitmap = backgroundBitmap;
	this.bitmapLayers = bitmapLayers;
    }


    /**
     * Constructor with all fields
     * 
     * @param screenWidth
     *            : the screen width
     * @param screenHeight
     *            : The screen height
     * @param backgroundBitmap
     *            : The background image
     */
    public DoodleViewProperties(int screenWidth, int screenHeight, Bitmap backgroundBitmap) {
	this(screenWidth, screenHeight, Color.parseColor(Constants.DEFAULT_BACKGROUND_COLOR), backgroundBitmap);
    }

    /**
     * Constructor with all fields
     * 
     * @param screenWidth
     *            : the screen width
     * @param screenHeight
     *            : The screen height
     */
    public DoodleViewProperties(int screenWidth, int screenHeight) {
	this(screenWidth, screenHeight, Color.parseColor(Constants.DEFAULT_BACKGROUND_COLOR), null);
    }

    /**
     * @return the screenWidth
     */
    public int getScreenWidth() {
	return screenWidth;
    }

    /**
     * @param screenWidth
     *            the screenWidth to set
     */
    public void setScreenWidth(int screenWidth) {
	this.screenWidth = screenWidth;
    }

    /**
     * @return the screenHeight
     */
    public int getScreenHeight() {
	return screenHeight;
    }

    /**
     * @param screenHeight
     *            : the screenHeight to set
     */
    public void setScreenHeight(int screenHeight) {
	this.screenHeight = screenHeight;
    }

    /**
     * @return the backgroundColor
     */
    public int getBackgroundColor() {
	return backgroundColor;
    }

    /**
     * @param backgroundColor
     *            the backgroundColor to set
     */
    public void setBackgroundColor(int backgroundColor) {
	this.backgroundColor = backgroundColor;
    }

    public Bitmap getBackgroundBitmap() {
	return backgroundBitmap;
    }

    public void setBackgroundBitmap(Bitmap backgroundBitmap) {
	if (backgroundBitmap != null) {
	    this.backgroundBitmap = backgroundBitmap;
	} else {
	    this.backgroundBitmap = null;
	}
    }
    
    

    /**
     * @return the bitmapLayers
     */
    public ArrayList<Layer> getBitmapLayers() {
        return bitmapLayers;
    }

    /**
     * @param bitmapLayers the bitmapLayers to set
     */
    public void setBitmapLayers(ArrayList<Layer> bitmapLayers) {
        this.bitmapLayers = bitmapLayers;
    }

    @Override
    public String toString() {
	return "screen width : " + screenWidth + "\t screen height : " + screenHeight + "\t image exists : "
		+ (backgroundBitmap != null) + "\t background color : " + backgroundColor;
    }

}
