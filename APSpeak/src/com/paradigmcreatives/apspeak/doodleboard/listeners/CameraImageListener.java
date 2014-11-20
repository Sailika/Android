package com.paradigmcreatives.apspeak.doodleboard.listeners;

import android.graphics.Bitmap;

/**
 * Listener to notify captured/selected image from live camera/gallery app
 * 
 * @author Dileep | neuv
 *
 */
public interface CameraImageListener {
	
	/**
	 * To notify that the image has captured/selected and is available in the passed location
	 * 
	 * @param imagePath
	 * @param isFromGallery
	 */
	public void notifyImagePath(String imagePath, boolean isFromGallery);
	
	/**
	 * To notify that the image bitmap is availale to be used
	 * @param bitmap
	 */
	public void notifyImageBitmap(Bitmap bitmap);
}
