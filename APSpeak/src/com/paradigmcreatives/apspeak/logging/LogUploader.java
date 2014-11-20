package com.paradigmcreatives.apspeak.logging;

import java.io.File;
import java.util.Calendar;
import java.util.UUID;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

import com.paradigmcreatives.apspeak.app.util.DeviceInfoUtil;

/**
 * Log utility class to upload the daily log files to the server
 * 
 * @author robin
 * 
 */
public class LogUploader {

    private static final String TAG = "LogUploader";
    private Context context = null;
    private String logDirectoryPath = null;

    /**
     * Constructor with the application context
     * 
     * @param context
     *            Application's context
     */
    public LogUploader(Context context, String logDirectoryPath) {
	this.context = context;
	this.logDirectoryPath = logDirectoryPath;
    }

    /**
     * Attempts to upload the local log files to S3. If there are any pending log files which were not uploaded in any
     * of the previous tries then they are also attempted for upload. Once uploaded successfully the local file is
     * removed from local storage
     */
    public void uploadLogsToS3() {
	// Try for upload only if its a valid context and the external media is
	// mounted

	if (context != null && DeviceInfoUtil.mediaReadable() && logDirectoryPath != null
		&& logDirectoryPath.length() > 0) {
	    // Get the log directory
	    File logDirectory = new File(logDirectoryPath);
	    if (logDirectory.exists() && logDirectory.isDirectory()) {
		// Get all the files in log directory
		File[] files = logDirectory.listFiles();

		// Attempt to upload each of the file
		for (File f : files) {

		    // Upload log files only if it has content
		    if (f.length() > 0) {
			if (upload(f)) {
			    // Upload successful, delete from local
			    if (f.delete()) {
				Log.i(TAG, "Log file uploaded and cleaned.. yay!!!");
			    } else {
				Log.w(TAG, "Log file uploaded but file not deleted");
			    }
			} else {
			    Log.w(TAG, "Log file not uploaded. Will retry later");
			}
		    }
		}
	    } else {
		Log.e(TAG, "Attempt to upload logs failed. Log-dir NOT found - " + logDirectoryPath);
	    }
	} else {
	    Log.w(TAG, "Not a valid context or media is not available or logDirectory path is empty");
	}
    }

    /**
     * Upload a single file to S3
     * 
     * @param file
     *            Path of the file to be uploaded
     * @return <code>true</code> if file uploaded successfully, otherwise <code>false</code>
     */
    private boolean upload(File file) {
	boolean uploaded = false;

	// Upload to S3 if required
	
	return uploaded;
    }

    /**
     * Creates a unique key to be used while uploading log to S3
     * 
     * @return Unique key for log upload
     */
    private String generateKey() {
	// Generate current date in YYYY-MM-DD format
	Calendar currentDate = Calendar.getInstance();

	// Calendar.MONTH starts with 0
	int month = currentDate.get(Calendar.MONTH) + 1;
	int date = currentDate.get(Calendar.DATE);
	String now = "" + currentDate.get(Calendar.YEAR) + "-" + (month < 10 ? ("0" + month) : month) + "-"
		+ (date < 10 ? ("0" + date) : date);

	// To generate Universal Unique ID
	String randomID = UUID.randomUUID().toString();

	String phoneModel = android.os.Build.MODEL;

	PackageInfo pi;
	String versionName;
	try {
	    pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
	    versionName = pi.versionName;
	} catch (NameNotFoundException e) {
	    Log.w(TAG, "Could get the version name while loading the log" + e);
	    versionName = "UnknownVersion";
	}

	String key = "logs/android/" + now + "/" + phoneModel + "_V" + versionName + "_" + randomID + ".log";

	return key;

    }
}
