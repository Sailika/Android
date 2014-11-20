package com.paradigmcreatives.apspeak.doodleboard;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PointF;
import android.text.TextUtils;

import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.app.util.Util;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.logging.Logger;

/**
 * This class contains methods to play and resize the doodle
 * 
 * @author
 * 
 */
public class ParseDoodle {

    private Context context;
    private String currentDoodlePath;
    private final String TAG = "ParseDoodle";

    /**
     * Constructor that gets a reference of the CanvasActivity and path of the doodle to be played
     * 
     * @param instance
     *            : the canvas activity reference
     * @param doodlePath
     *            : the doodle's path in sd card
     */
    public ParseDoodle(Context instance, String doodlePath) {
	context = instance;
	currentDoodlePath = doodlePath;
    }

    /**
     * Parses the settings file to get the doodle view properties
     * 
     * @param doodleFolderPath
     * @return
     */
    public DoodleViewProperties parseProperties(String doodleFolderPath) {
	DoodleViewProperties doodleViewProperties = null;
	if (!TextUtils.isEmpty(doodleFolderPath)) {
	    FileReader fre = null;
	    BufferedReader inr = null;
	    String stri = null;
	    String[] striarray = null;
	    try {
		fre = new FileReader(doodleFolderPath + "/"
			+ context.getString(R.string.doodle_settings_file_name));
		inr = new BufferedReader(fre);

		int width = 0, height = 0;
		int color = Color.parseColor(Constants.DEFAULT_BACKGROUND_COLOR);
		Bitmap image = null;
		while ((stri = inr.readLine()) != null) {
		    String mStri = stri.substring(0, 2);
		    if (mStri.startsWith("s ")) {
			// Height width
			stri = stri.substring(2, stri.length());
			striarray = stri.split(" ");
			width = Integer.valueOf(striarray[0]);
			height = Integer.valueOf(striarray[1]);
		    } else if (mStri.startsWith("c ")) {
			// background color
			stri = stri.substring(2, stri.length());
			String colorString = "#" + stri;
			color = Color.parseColor(colorString);
		    }
		}
		image = Util.decompressImage(doodleFolderPath,
			context.getString(R.string.doodle_background_file_name));
		doodleViewProperties = new DoodleViewProperties(width, height, color, image);
	    } catch (IndexOutOfBoundsException e) {
		Logger.warn(TAG, "Error extracting the settings - " + e);
	    } catch (FileNotFoundException e) {
		Logger.warn(TAG, "Error extracting the settings - " + e);
	    } catch (NotFoundException e) {
		Logger.warn(TAG, "Error extracting the settings - " + e);
	    } catch (NumberFormatException e) {
		Logger.warn(TAG, "Error extracting the settings - " + e);
	    } catch (IOException e) {
		Logger.warn(TAG, "Error extracting the settings - " + e);
	    } catch (Exception e) {
		Logger.logStackTrace(e);
	    }
	}
	return doodleViewProperties;
    }

    /**
     * Parses the doodle points
     * 
     * @param doodleFolderPath
     * @return
     */
    public ArrayList<Object> parseDoodlePoints(String doodleFolderPath) {
	ArrayList<Object> doodle = new ArrayList<Object>();
	if (!TextUtils.isEmpty(doodleFolderPath)) {
	    FileReader fre = null;
	    BufferedReader inr = null;
	    String stri = null;
	    String[] striarray = null;
	    try {
		fre = new FileReader(doodleFolderPath + "/"
			+ context.getString(R.string.doodle_points_file_name));
		inr = new BufferedReader(fre);
		StrokeBean stroke = null;
		PointF point = null;
		while ((stri = inr.readLine()) != null) {
		    if (stri.equals(DoodleView.LINE_BREAK)) {
			doodle.add(DoodleView.LINE_BREAK);
		    } else {
			String mStri = "";
			mStri = stri.substring(0, 2);
			if (mStri.contains("c ")) {
			    stri = stri.substring(2, stri.length());
			    stroke = new StrokeBean(stri);
			    doodle.add(stroke);
			} else if (mStri.contains("w ")) {
			    stri = stri.substring(2, stri.length());
			    stroke = new StrokeBean(Integer.parseInt(stri));
			    doodle.add(stroke);
			} else if (mStri.contains("p ")) {
			    stri = stri.substring(2, stri.length());
			    striarray = stri.split(" ");
			    float xi = (float) Double.parseDouble(striarray[0]);
			    float yi = (float) Double.parseDouble(striarray[1]);
			    point = new PointF(xi, yi);
			    doodle.add(point);
			}
		    }
		}
		inr.close();
		fre.close();
	    } catch (IndexOutOfBoundsException iobe) {
		Logger.logStackTrace(iobe);
	    } catch (IOException ioe) {
		Logger.logStackTrace(ioe);
	    } catch (Exception e) {
		Logger.logStackTrace(e);
	    }
	}
	return doodle;
    }

    /**
     * Parses and plays the current doodle
     */
//    protected void play() {
//	DoodleView doodleView = context.getDoodleView();
//	if (doodleView == null) {
//	    return;
//	}
//	if (!TextUtils.isEmpty(currentDoodlePath)) {
//	    DoodleViewProperties doodleViewProperties = parseProperties(currentDoodlePath);
//	    ArrayList<Object> doodle = parseDoodlePoints(currentDoodlePath);
//	    if (doodleViewProperties != null && doodle != null) {
//		if (doodleView != null) {
//		    doodleView.play(doodle, doodleViewProperties);
//		}
//	    } else {
//		Logger.warn(TAG, "Doodle parsing failed for the given path : " + currentDoodlePath);
//	    }
//	}
//    }
//    
//    protected void resize(DoodleViewProperties doodleViewProperties, ArrayList<Object> doodle, DoodleViewBounds doodleViewBounds){
//	DoodleView doodleView = context.getDoodleView();
//	if (doodleView == null) {
//	    return;
//	}
//	if (!TextUtils.isEmpty(currentDoodlePath)) {
//	    if (doodleViewProperties != null && doodle != null) {
//		if (doodleView != null) {
//		    doodleView.resize(doodle, doodleViewProperties, doodleViewBounds);
//		}
//	    } else {
//		Logger.warn(TAG, "Doodle parsing failed for the given path : " + currentDoodlePath);
//	    }
//	}
//    }
}
