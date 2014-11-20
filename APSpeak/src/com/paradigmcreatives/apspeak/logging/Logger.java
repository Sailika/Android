package com.paradigmcreatives.apspeak.logging;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.util.Log;

import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.app.util.AppPropertiesUtil;
import com.paradigmcreatives.apspeak.app.util.DeviceInfoUtil;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;

/**
 * Logger for this APP. Contains methods which logs data and creates files on the device. The logs are created daily.<br/>
 * 
 * @author Robin | Paradigm Creatives
 */
public class Logger {
    private static final boolean DEBUG = Constants.DEBUG;
    private static Context context = null;
    private static final String TAG = "Logger";

    /**
     * Severity of the logs while writing the file.
     * 
     * @author Robin | Paradigm Creatives
     */
    enum Severity {
	INFO, WARNING, FATAL
    };

    /**
     * Perform operations to initialize the Logger and writes the device info as the first log.
     * 
     * @param context
     *            <code>Context</code> to be used for all the system operations. NOTE: Should not be <code>null</code><br>
     *            . The operations are as follows:<br/>
     *            <ul>
     *            <li>Initialized the context</li>
     *            <li>Create the log directory if it does not exists</li>
     *            <li>Write Device Info into the logs</li>
     *            </ul>
     * 
     * @return <code>true</code> if init is successful, else <code>false</code>.
     */
    public static boolean init(Context context) {
	// Operation 1: Its very important to have a valid context here
	if (context == null) {
	    if (DEBUG) {
		Log.e(TAG, "null Context supplied to Logger");
	    }

	    return false;
	}
	Logger.context = context;

	// Operation 2: Create the log and an empty file (if they don't exist
	// already)

	// Check if writing to external memory is allowed or not
	if (DeviceInfoUtil.mediaWritable()) {
	    if (checkAndCreateLogDir() && initLogFile()) {
		if (DEBUG) {
		    Log.i(TAG, "Log directory and file initialization... done");

		    // Initialize periodic log uploader (if required)
		    /*
		    String periodicLogInitMessage = "";
		    if (PeriodicLogUpload.init(context)) {
			periodicLogInitMessage = "Periodic log uploader init done";
		    } else {
			periodicLogInitMessage = "Periodic log uploader init failed";
		    }

		    if (DEBUG) {
			Log.i(TAG, periodicLogInitMessage);
		    }
		    */
		}
	    } else {
		if (DEBUG) {
		    Log.e(TAG, "Log directory and file creation... failed");
		}
		return false;
	    }

	} else {
	    // External media not available for writing
	    if (DEBUG) {
		Log.e(TAG, "External media not available for writing");
	    }
	    return false;
	}

	return true;
    }// end of init()

    /**
     * Create the log directory in the SD card it does not exists.
     * 
     * @return <code>true</code> if directory is created successfully or it already exists, else <code>false</code>.
     */
    private static boolean checkAndCreateLogDir() {
	if (context == null) {
	    return false;
	}

	boolean created = false;

	File logDir = new File(AppPropertiesUtil.getAppDirectory(context)
		+ context.getResources().getString(R.string.log_folder));

	// Either the directories exists or creation of directories happen
	// without any problems
	created = logDir.exists() || logDir.mkdirs();

	return created;
    }// end of checkAndCreateLogDir()

    /**
     * Creates the file to store all the logs being sent to the logger. The files are created based on today's date.
     * Nothing is done is file already exists. For every new file being created, the device info is added first.
     * 
     * @return <code>true</code> if file gets created or file already exists, else <code>false</code>.
     */
    private static boolean initLogFile() {
	if (context == null) {
	    return false;
	}

	try {
	    File logFile = new File(AppPropertiesUtil.getAppDirectory(context)
		    + context.getResources().getString(R.string.log_folder), generateTheFileName());

	    // A new file is created every time.
	    if (logFile.exists()) {
		return true;
	    } else {
		if (logFile.createNewFile()) {
		    // Attempt to write the device info to the log file
		    logDeviceInfo();

		    // Irrespective of whether the device info got saved or not,
		    // return true, as the file got created
		    Log.i(TAG, "Log file created");
		    return true;
		} else {
		    // Attempt to create the file failed
		    Log.e(TAG, "Could not create the log file");
		    return false;
		}
	    }

	} catch (IOException e) {
	    if (DEBUG) {
		Log.e(TAG, e + "");
	    }
	    // Something seriously went wrong, check your logcat
	    return false;
	}
    }// end of initLogFile()

    /**
     * Generates the log file name based on today's date.
     * 
     * @return File name.
     */
    private static String generateTheFileName() {
	String fileName = "";

	String now = DateFormat.getDateInstance(DateFormat.SHORT).format(new Date());
	now = now.replaceAll("/", "");

	fileName = context.getResources().getString(R.string.log_file) + "_" + now + "."
		+ context.getResources().getString(R.string.log_extension);
	// fileName = context.getResources().getString(R.string.log_file) + "."
	// + context.getResources().getString(R.string.log_extension);

	return fileName;

    }// end of generatedFileName()

    /**
     * Logs a set of standard info about the device
     */
    private static void logDeviceInfo() {
	DeviceInfoUtil deviceInfoUtil = new DeviceInfoUtil(context);
	ArrayList<String> deviceInfoList = deviceInfoUtil.collectDeviceInfo();

	info("---------------------------------------------------------------");
	info("LOGGING DEVICE INFORMATION");
	info("---------------------------------------------------------------");
	for (String deviceInfo : deviceInfoList) {
	    info(deviceInfo);
	}
	info("---------------------------------------------------------------");
    }// end of logDeviceInfo()

    /**
     * Logs the warning message with default TAG.
     * 
     * @param message
     *            Message to log.
     */
    public static void warn(String message) {
	warn(TAG, message);
    }// end of warn()

    /**
     * Logs the warning message with the given TAG.
     * 
     * @param tag
     *            Tag to use while logging the message.
     * @param message
     *            Message to log.
     */
    public static void warn(String tag, String message) {
	if (DEBUG && message != null) {
	    Log.w(tag, message);
	    writeLog(Severity.WARNING, message);
	}
    }// end of warn()

    /**
     * Logs the info message with the default TAG
     * 
     * @param message
     *            Message to log.
     */
    public static void info(String message) {
	info(TAG, message);
    }// end of info()

    /**
     * Logs the info message with the given tag
     * 
     * @param tag
     *            Tag to use while logging the message.
     * 
     * @param message
     *            Message to log.
     */
    public static void info(String tag, String message) {
	// Call writeLog(Severity.INFO, message)
	if (DEBUG && message != null) {
	    Log.i(tag, message);
	    writeLog(Severity.INFO, message);
	}
    }// end of info()

    /**
     * Logs the fatal message with default TAG
     * 
     * @param message
     *            Message to log
     */
    public static void fatal(String message) {
	fatal(TAG, message);
    }// end of fatal()

    /**
     * Logs the fatal message with the given tag
     * 
     * @param tag
     *            Tag to use while logging the message
     * @param message
     *            Message to log.
     */
    public static void fatal(String tag, String message) {
	// Call writeLog(Severity.FATAL, message)
	if (DEBUG && message != null) {
	    Log.e(tag, message);
	    writeLog(Severity.INFO, message);
	}
    }// end of fatal()

    /**
     * Write the log information into the log file. The log format is:
     * 
     * Time-stamp Severity Log message
     * 
     * If the log file does not exists then it retries once to create the directory + file structure.
     * 
     * @param severity
     *            Severity of the message
     * @param logString
     *            The message to be logged
     * 
     * @return <code>true</code> if written successfully, else <code>false</code>.
     */
    synchronized private static boolean writeLog(Severity severity, String logString) {
	boolean written = false;

	// Check for valid context
	if (context != null) {

	    // If media is writable then proceed
	    if (DeviceInfoUtil.mediaWritable()) {
		BufferedWriter out = null;

		File logFile = new File(AppPropertiesUtil.getAppDirectory(context)
			+ context.getResources().getString(R.string.log_folder), generateTheFileName());

		// Attempt to write if the log file exists. If it does not then
		// try re-initialization once
		if (logFile.exists() || (checkAndCreateLogDir() && initLogFile())) {
		    try {
			// Open the log file in append mode
			FileWriter writer = new FileWriter(logFile, true);
			out = new BufferedWriter(writer);

			// Write the log
			out.write("\n" + DateFormat.getDateTimeInstance().format(new Date()) + "\t\t" + severity.name()
				+ "\t\t" + logString);
		    } catch (IOException e) {
			Log.e(TAG, e + "");
		    } catch (Exception e) {
			Log.e(TAG, e + "");
		    } finally {
			try {
			    if (out != null) {
				out.close();
			    }
			} catch (IOException e) {
			    Log.e(TAG, "Logger's BufferedReader not closed" + e);
			}

		    }

		} else {
		    Log.e(TAG, "No Log file present and failed to create one");
		}

	    } else {
		Log.e(TAG, "No logs written, external media not writable");
	    }

	} else {
	    if (DEBUG) {
		Log.e(TAG, "No logs written, supplied Context is no more valid");
	    }
	}

	return written;
    }// end of writeLog()

    // TODO Still need to find its proper use and clean it
    public static void logStackTrace(Exception e) {
	e.printStackTrace();
	String exception = "";
	exception = e.toString() + "\n";
	StackTraceElement[] temp = e.getStackTrace();
	for (int i = 0; i < temp.length; i++) {
	    exception = exception + "Doodlydoo Exception at " + temp[i].toString() + "\n";
	}

	// Log.w(TAG, exception);
	writeLog(Severity.FATAL, exception);
	// return exception;
    }// end of logStackTrace()

}// end of class
