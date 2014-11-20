package com.paradigmcreatives.apspeak.doodleboard.listeners;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Environment;

/**
 * Callback to handle pictures taken from live camera
 * 
 * @author Dileep | neuv
 *
 */
public class CameraPictureCallback implements PictureCallback{

	public static final int MEDIA_TYPE_IMAGE = 1;
	
	private CameraImageListener mListener;
	
	public CameraPictureCallback(CameraImageListener listener){
		super();
		this.mListener = listener;
	}
	
	@Override
	public void onPictureTaken(byte[] data, Camera camera) {
		 File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
	        if (pictureFile == null){
	            return;
	        }

	        try {
	            FileOutputStream fos = new FileOutputStream(pictureFile);
	            fos.write(data);
	            fos.close();
	            if(mListener != null){
	            	mListener.notifyImagePath(pictureFile.getAbsolutePath(), false);
	            }
	        } catch (FileNotFoundException e) {
	            //Log.d(TAG, "File not found: " + e.getMessage());
	        } catch (IOException e) {
	            //Log.d(TAG, "Error accessing file: " + e.getMessage());
	        } catch (Exception e) {
				// TODO: handle exception
			}
	}
	
	/** Create a File for saving an image or video */
	private static File getOutputMediaFile(int type){
	    // To be safe, you should check that the SDCard is mounted
	    // using Environment.getExternalStorageState() before doing this.

	    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
	              Environment.DIRECTORY_PICTURES), "MyCameraApp");
	    // This location works best if you want the created images to be shared
	    // between applications and persist after your app has been uninstalled.

	    // Create the storage directory if it does not exist
	    if (! mediaStorageDir.exists()){
	        if (! mediaStorageDir.mkdirs()){
	            //Log.d("MyCameraApp", "failed to create directory");
	            return null;
	        }
	    }

	    // Create a media file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    File mediaFile;
	    if (type == MEDIA_TYPE_IMAGE){
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "IMG_"+ /*timeStamp + */".jpg");
	    } else {
	        return null;
	    }

	    return mediaFile;
	}
	
	
}
